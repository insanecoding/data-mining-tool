package com.me.core.service.rapidminer;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RapidMinerExecService extends StoppableObservable implements MyExecutable {
    @Getter @Setter
    private String workingDir;
    @Getter @Setter
    private List<String> expNames;
    @Getter @Setter
    private String pathToRM;

    private final RapidMinerExecutor rapidMinerExecutor;
    private final MyDao dao;

    @Autowired
    public RapidMinerExecService(RapidMinerExecutor rapidMinerExecutor, MyDao dao,
                                 ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.rapidMinerExecutor = rapidMinerExecutor;
        this.dao = dao;
    }

    private List<String> findSchemes(Experiment experiment) throws IOException {
        return Files.list(Paths.get(workingDir + "/schemes/" + experiment.getExpName()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
    }

    private void execInRM(Experiment experiment) throws IOException {
        List<String> schemes = findSchemes(experiment);

        for (String scheme : schemes) {
            rapidMinerExecutor.executeProcessInRM(pathToRM, scheme);
        }
    }

    @Override
    public void execute() throws Exception {
        List<Experiment> experiments = dao.findExperimentsByNames(expNames);
        experiments.sort(Comparator.comparing(Experiment::getExperimentNumber));

        for (Experiment experiment : experiments) {
            super.updateMessage("Executing schemes for experiment: " + experiment.getExpName());
            execInRM(experiment);
        }
    }

    @Override
    public String getName() {
        return "Rapid Miner Executor Service";
    }
}
