package com.me.core.service.uncompress;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class UncompressService extends StoppableObservable implements MyExecutable  {

    @Autowired
    public UncompressService(@Qualifier("progressWatcher") ProgressWatcher watcher) {
        super.addSubscriber(watcher);
    }

    @Getter @Setter
    private List<String> compressed = new LinkedList<>();

    /**
     * Uncompress all the archives inside the <code>compressed</code> folder
     * allows nested subdirectories
     * @param compressed starting folder (with all archives)
     * @param output where to store output
     */
    private void uncompress(String compressed, String output) {
        File file = new File(compressed);
        File[] subFolders = file.listFiles();

        assert subFolders != null;
        for (File subFolder: subFolders) {
            String subFolderPath = subFolder.getAbsolutePath();
            if (subFolder.isFile()) {
                String folderName = subFolder.getParentFile().getName();
                String saveResultsTo = output + "//" + folderName;
                UncompressBlacklist.uncompress(subFolderPath, saveResultsTo);
            } else {
                uncompress(subFolderPath, output);
            }
        }
    }

    @Override
    public void execute() throws Exception {
        if (compressed.size() == 0)
            throw new IllegalArgumentException("incorrect size of list with archives path");

        for (String pathToCompressed : compressed) {
            super.updateMessageCheck("uncompressing: " + pathToCompressed);
            uncompress(pathToCompressed, pathToCompressed);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Map<String, Object> param) {
        this.compressed.addAll((List<String>) param.get("uncompress"));
    }

    @Override
    public void cleanUp(){
        compressed.clear();
    }

    @Override
    public String getName() {
        return "uncompress blacklist";
    }
}
