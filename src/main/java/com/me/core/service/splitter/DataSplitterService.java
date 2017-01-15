package com.me.core.service.splitter;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.ChosenCategory;
import com.me.core.domain.entities.DataSet;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataSplitterService extends StoppableObservable implements MyExecutable {

    @Getter @Setter
    private List<DataSplitterParam> params;
    @Getter @Setter
    private DataSplitterUtil dataSplitterUtil;
    @Getter @Setter
    private MyDao dao;

    @Autowired
    public DataSplitterService(DataSplitterUtil dataSplitterUtil, MyDao dao,
                               @Qualifier("progressWatcher") ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.dataSplitterUtil = dataSplitterUtil;
        this.dao = dao;
    }

    @Override
    public void execute() throws Exception {
        for (DataSplitterParam param : params) {
            DataSet dataSet = param.getDataSet();
            super.updateMessageCheck("creating dataset: " + dataSet.getName());
            dao.saveEntity(dataSet);

            List<ChosenCategory> categories =
                    dao.findCategoriesByNames(param.getCategories())
                            .stream().map(category -> new ChosenCategory(dataSet, category))
                            .collect(Collectors.toList());
            dao.batchSave(categories, false);

            dataSplitterUtil.createDivideSave(param, categories);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Map<String, Object> param) {
        List<DataSplitterParam> dsp = (List<DataSplitterParam>) param.get("splitter");
        this.params = new ArrayList<>(dsp);
    }

    @Override
    public String getName() {
        return "Data Splitting Service";
    }
}
