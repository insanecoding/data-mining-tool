package com.me.core.service.experiment;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.DataSet;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.dao.MyDao;
import com.me.core.service.experiment.text.dictionary.DictionaryParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExperimentCreator extends StoppableObservable implements MyExecutable {

    @Getter @Setter
    private List<DictionaryParam> dictionaryParams;
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
        dictionaryParams.forEach(elem -> {
            Experiment experiment = elem.getExperiment();
            String dataSetName = elem.getDataSetName();
            DataSet dataSet = dao.findDataSetByName(dataSetName);
            experiment.setDataSet(dataSet);
            dao.trySaveExperiment(experiment);
        });
        super.updateMessage("experiments created");
    }

    @Override
    public String getName() {
        return "Experiment creator";
    }
}