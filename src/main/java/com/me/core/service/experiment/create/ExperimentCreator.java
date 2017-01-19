package com.me.core.service.experiment.create;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.dto.Modes;
import com.me.core.domain.entities.DataSet;
import com.me.core.domain.entities.DependentExperiment;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExperimentCreator extends StoppableObservable implements MyExecutable {

    @Getter @Setter
    private List<Experiment> experiments;
    @Getter @Setter
    private MyDao dao;

    @Autowired
    public ExperimentCreator(MyDao dao, ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.dao = dao;
    }

    @Override
    public void execute() throws Exception {
        super.updateMessage("creating experiments");

        experiments.forEach(experiment -> {
            String dataSetName = experiment.getExperimentParam().getDataSetName();
            DataSet dataSet = dao.findDataSetByName(dataSetName);
            experiment.setDataSet(dataSet);

            String fixedExpName = experiment.getExpName().replaceAll(
                    "[^A-Z0-9a-z_\\s]", " "
            );
            experiment.setExpName(fixedExpName);

            dao.trySaveExperiment(experiment);
            processJoinedExperiment(experiment);
        });
        super.updateMessage("experiments created");
    }

    private void processJoinedExperiment(Experiment experiment) {
        if (experiment.getMode().equals(Modes.JOIN)) {
            List<String> dependencies =
                    experiment.getExperimentParam().getDependeciesNames();
            List<Experiment> experiments =
                    dao.findExperimentsByNames(dependencies);
            experiments.forEach(exp -> dao.trySaveExperiment(exp));

            experiments.stream()
                    .map(elem -> new DependentExperiment(experiment, elem))
                    .forEach(dep -> dao.saveEntity(dep));
        }
    }

    @Override
    public String getName() {
        return "Experiment creator";
    }
}
