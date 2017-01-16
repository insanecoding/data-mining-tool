package com.me.common.initializer;

import com.me.common.MyExecutable;
import com.me.core.domain.dto.DictionaryParam;
import com.me.core.domain.dto.Modes;
import com.me.core.domain.dto.Types;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.experiment.ExperimentCreator;
import com.me.core.service.experiment.text.dictionary.TextDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DictionaryServiceInitializer implements Initializer {

    private final TextDictionaryService dictionaryService;
    private final ExperimentCreator experimentCreator;

    @Autowired
    public DictionaryServiceInitializer(TextDictionaryService dictionaryService,
                                        ExperimentCreator experimentCreator) {
        this.dictionaryService = dictionaryService;
        this.experimentCreator = experimentCreator;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void initialize(Map<String, Object> dto, List<MyExecutable> executables) {
        Map<String, Object> settings = (Map<String, Object>) dto.get("run");

        if ((boolean) settings.get("isOn")) {
            List<Map<String, Object>> experiments =
                    (List<Map<String, Object>>) settings.get("experiments");

            List<DictionaryParam> dictionaryParams = experiments.stream()
                    .map(this::createDictionaryParams)
                    .collect(Collectors.toList());

            experimentCreator.setDictionaryParams(new ArrayList<>(dictionaryParams));
            dictionaryService.setDictionaryParams(new ArrayList<>(dictionaryParams));
            setFullPath(dto, settings);
            executables.add(experimentCreator);
            executables.add(dictionaryService);
        }
    }

    private DictionaryParam createDictionaryParams(Map<String, Object> experiment) {
        String dataSetName = (String) experiment.get("dataSetName");
        Experiment experimentObj = createExperiment(experiment);

        DictionaryParam dictionaryParam = new DictionaryParam();
        dictionaryParam.setExperiment(experimentObj);
        dictionaryParam.setDataSetName(dataSetName);

        initializeByMode(experiment, experimentObj, dictionaryParam);
        return dictionaryParam;
    }

    private void initializeByMode(Map<String, Object> experiment, Experiment experimentObj,
                                  DictionaryParam dictionaryParam) {
        if (experimentObj.getMode().equals(Modes.TEXT_MAIN)) {
            initializeTextMainMode(experiment, dictionaryParam);
        } else if (experimentObj.getMode().equals(Modes.TEXT_FROM_TAGS)) {
            initializeTextFromTagMode(experiment, dictionaryParam);
        } else if (experimentObj.getMode().equals(Modes.NGRAMS)) {
            initializeNGramMode(experiment, dictionaryParam);
        }
    }

    private void initializeTextMainMode(Map<String, Object> experiment,
                                        DictionaryParam dictionaryParam) {
        initializeCommonParameters(experiment, dictionaryParam);
    }

    private void initializeTextFromTagMode(Map<String, Object> experiment,
                                           DictionaryParam dictionaryParam) {
        initializeCommonParameters(experiment, dictionaryParam);
        String tagName = (String) experiment.get("tagName");
        dictionaryParam.setTagName(tagName);
    }

    private void initializeNGramMode(Map<String, Object> experiment,
                                     DictionaryParam dictionaryParam) {
        initializeCommonParameters(experiment, dictionaryParam);
        int nGramSize = (int) experiment.get("nGramSize");
        dictionaryParam.setNGramSize(nGramSize);
    }

    private void initializeCommonParameters(Map<String, Object> experiment, DictionaryParam dictionaryParam) {
        double IDF_Treshold = (double) experiment.get("IDF_Treshold");
        String IDF_Type = (String) experiment.get("IDF_Type");
        String TF_Type = (String) experiment.get("TF_Type");
        int featuresByCategory = (int) experiment.get("featuresByCategory");

        dictionaryParam.setIDF_Treshold(IDF_Treshold);
        dictionaryParam.setIDF_Type(IDF_Type);
        dictionaryParam.setTF_Type(TF_Type);
        dictionaryParam.setFeaturesByCategory(featuresByCategory);
    }

    @SuppressWarnings("unchecked")
    private void setFullPath(Map<String, Object> dto, Map<String, Object> settings) {
        Map<String, Object> formImport = (Map<String, Object>) dto.get("import");
        String cwd = (String) formImport.get("cwd");
        String stopWordsSubPath = (String) settings.get("stopWordsPath");
        String fullPath = cwd + "\\" + stopWordsSubPath;
        dictionaryService.setStopWordsPath(fullPath);
    }

    private Experiment createExperiment(Map<String, Object> settings) {
        String name = (String) settings.get("name");
        String description = (String) settings.get("description");

        String strMode = (String) settings.get("mode");
        String strType = (String) settings.get("type");
        Modes mode = Modes.valueOf(strMode.toUpperCase());
        Types type = Types.valueOf(strType.toUpperCase());

        return createExperimentObject(name, description, mode, type);
    }

    private Experiment createExperimentObject(String name, String description, Modes mode, Types type) {
        Experiment experimentObj = new Experiment();
        experimentObj.setDescription(description);
        experimentObj.setExpName(name);
        experimentObj.setMode(mode);
        experimentObj.setType(type);
        return experimentObj;
    }
}
