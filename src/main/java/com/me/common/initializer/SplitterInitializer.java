package com.me.common.initializer;

import com.me.common.MyExecutable;
import com.me.core.domain.entities.DataSet;
import com.me.core.service.splitter.DataSplitterParam;
import com.me.core.service.splitter.DataSplitterService;
import com.me.core.service.splitter.MainDataSplitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SplitterInitializer implements Initializer {


    private final DataSplitterService splitter;
    private final DictionaryServiceInitializer next;

    @Autowired
    public SplitterInitializer(DataSplitterService splitter, DictionaryServiceInitializer next) {
        this.splitter = splitter;
        this.next = next;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Map<String, Object> dto, List<MyExecutable> executables) {
        Map<String, Object> settings = (Map<String, Object>) dto.get("dataSplit");
        if ((boolean) settings.get("isOn")) {
            List<Map<String, Object>> param =
                    (List<Map<String, Object>>) settings.get("param");

            List<DataSplitterParam> dsp = param.stream()
                    .map(this::createDataSplitterParam)
                    .collect(Collectors.toList());
            splitter.setParams(new ArrayList<>(dsp));
            executables.add(splitter);
        }
        next.initialize(dto, executables);
    }

    @SuppressWarnings("unchecked")
    private DataSplitterParam createDataSplitterParam(Map<String, Object> settings) {

        List<String> categories = (List<String>) settings.get("categories");
        MainDataSplitParams mdp = createMainParams(settings);
        DataSet dataSet = createDataSet(settings);
        return new DataSplitterParam(mdp, dataSet, categories);
    }

    private DataSet createDataSet(Map<String, Object> settings) {
        String dataSetName = (String) settings.get("dataSetName");
        String description = (String) settings.get("description");
        double learn = (double) settings.get("partitionLearn");
        DataSet dataSet = new DataSet();
        dataSet.setName(dataSetName);
        dataSet.setDescription(description);
        dataSet.setPartitionLearn(learn);
        return dataSet;
    }

    private MainDataSplitParams createMainParams(Map<String, Object> settings) {
        String lang = (String) settings.get("lang");
        int minLength = (int) settings.get("minTextLength");
        int maxLength = (int) settings.get("maxTextLength");
        int websitesPerCategory = (int) settings.get("websitesPerCategory");

        return new MainDataSplitParams(lang, minLength, maxLength, websitesPerCategory);
    }
}
