package com.me.core.service.importbl;


import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.Blacklist;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.Website;
import com.me.core.service.dao.MyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * needs path to blacklist directory (<b>root</b>)
 * allows any level of nested subdirectories
 * able to process one blacklist at time
 * requires <i>domains</i> files inside each category subfolder of <b>root</b>
 * <i>domains</i> file contains urls to websites
 * final line in <i>domains</i> can be empty or the last url
 * two empty lines will result in incorrect processing
 */

@Component("fileSystemBehaviour")
public class FileSystemAddBehaviour extends StoppableObservable implements AddBehaviour {

    private final MyDao dao;

    @Autowired
    public FileSystemAddBehaviour(@Qualifier("progressWatcher") ProgressWatcher watcher,
                                  @Qualifier("myDaoImpl") MyDao dao) {
        super.addSubscriber(watcher);
        this.dao = dao;
    }

    @Override
    public void importBlacklist(Blacklist blacklist1, String path) throws Exception {
        Blacklist blacklist = dao.trySaveBlacklist(blacklist1);
        try (Stream<Path> domainFilesStream = getAllDomainFiles(path)) {
            // transform stream into list
            List<Path> domainFiles = domainFilesStream.collect(Collectors.toList());
            // for each domains file
            for (Path domainFile : domainFiles) {
                processDomainFile(blacklist, domainFiles, domainFile);
            }
        }
    }

    private void processDomainFile(Blacklist blacklist, List<Path> domainFiles,
                                   Path domainFile) throws Exception {
        // create associated Category object
        Category category = createCategory(domainFile);
        super.updateWorkingCheck("processing category: " + category.getCategoryName(),
                domainFiles.indexOf(domainFile), domainFiles.size());

        List<Website> websites = extractWebsites(blacklist, domainFile, category);
        dao.batchSaveWebsites(websites);
        super.updateWorking("done with category: " + category.getCategoryName(),
                domainFiles.indexOf(domainFile) + 1, domainFiles.size());
    }

    private Stream<Path> getAllDomainFiles(String root) throws IOException {
        return Files
                .walk(Paths.get(root))
                .filter(path -> (path.getFileName().toString().equals("domains")));
    }

    private List<Website> extractWebsites(Blacklist blacklist,
                                          Path domainFile, Category category) throws IOException {
        // for each line in domains file
        List<Website> websites = new ArrayList<>();
        try (Stream<String> fileContents = getFileContents(domainFile)) {
            fileContents.forEach(line -> {
                String url = "http://" + line;
                Website website = new Website(url, category, blacklist);
                websites.add(website);
            });
        }
        return websites;
    }

    private Category createCategory(Path directory) {
        // remove all non-letters from string to avoid problems in the future
        String categoryName = directory.getParent().getFileName()
                .toString().replaceAll("[\\p{Punct}]", "");
        Category category = new Category(categoryName);
        // check if category is unique and save if it's true
        return dao.trySaveCategory(category);
    }

    /**
     * @return Stream of lines inside <code>domains</code> file
     */
    private Stream<String> getFileContents(Path file) throws IOException {
        return Files.lines(file);
    }
}