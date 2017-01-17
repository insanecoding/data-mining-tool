package com.me.common.initializer;

import com.me.common.MyExecutable;
import com.me.core.domain.dto.Modes;
import com.me.core.domain.dto.Types;
import com.me.core.domain.entities.Experiment;
import com.me.core.domain.entities.ExperimentParam;
import com.me.core.service.experiment.ExperimentCreator;
import com.me.core.service.experiment.text.aml.PrepareAMLDatService;
import com.me.core.service.experiment.text.dictionary.TextDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DictionaryServiceInitializer implements Initializer {

    private final TextDictionaryService dictionaryService;
    private final ExperimentCreator experimentCreator;
    private final PrepareAMLDatService amlDatService;

    @Autowired
    public DictionaryServiceInitializer(TextDictionaryService dictionaryService,
                                        ExperimentCreator experimentCreator,
                                        PrepareAMLDatService amlDatService) {
        this.dictionaryService = dictionaryService;
        this.experimentCreator = experimentCreator;
        this.amlDatService = amlDatService;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void initialize(Map<String, Object> dto, List<MyExecutable> executables) {
        Map<String, Object> settings = (Map<String, Object>) dto.get("run");

        if ((boolean) settings.get("isOn")) {
            List<Map<String, Object>> experiments =
                    (List<Map<String, Object>>) settings.get("experiments");

            Map<Experiment, String> experimentDataSetName = new LinkedHashMap<>();
            experiments.forEach(elem -> processElem(elem, experimentDataSetName));

            List<String> expNames = experimentDataSetName.keySet().stream()
                    .map(Experiment::getExpName).collect(Collectors.toList());

            experimentCreator.setExperimentDataSetName(new LinkedHashMap<>(experimentDataSetName));
            dictionaryService.setExpNames(new ArrayList<>(expNames));
            setFullPath(dto, settings);
            amlDatService.setExpNames(new ArrayList<>(expNames));

            executables.add(experimentCreator);
            executables.add(dictionaryService);
            executables.add(amlDatService);
        }
    }

    private void processElem(Map<String, Object> settings,
                                   Map<Experiment, String> experimentDataSetNameMap) {
        String dataSetName = (String) settings.get("dataSetName");
        String name = (String) settings.get("name");
        String description = (String) settings.get("description");

        String strMode = (String) settings.get("mode");
        String strType = (String) settings.get("type");
        Modes mode = Modes.valueOf(strMode.toUpperCase());
        Types type = Types.valueOf(strType.toUpperCase());

        double IDF_Threshold = (double) settings.get("IDF_Treshold");
        String IDF_Type = (String) settings.get("IDF_Type");
        String TF_Type = (String) settings.get("TF_Type");
        int featuresByCategory = (int) settings.get("featuresByCategory");

        ExperimentParam experimentParam =
                new ExperimentParam(IDF_Threshold, IDF_Type, TF_Type, featuresByCategory);

        if (mode.equals(Modes.NGRAMS)) {
            int nGramSize = (int) settings.get("nGramSize");
            experimentParam.setNGramSize(nGramSize);
        }

        if (mode.equals(Modes.TEXT_FROM_TAGS)) {
            String tagName = (String) settings.get("tagName");
            experimentParam.setTagName(tagName);
        }

        Experiment experiment =
                new Experiment(name, description, mode, type, experimentParam);

        experimentDataSetNameMap.put(experiment, dataSetName);
    }

    @SuppressWarnings("unchecked")
    private void setFullPath(Map<String, Object> dto, Map<String, Object> settings) {
        Map<String, Object> formImport = (Map<String, Object>) dto.get("import");
        String cwd = (String) formImport.get("cwd");
        String stopWordsSubPath = (String) settings.get("stopWordsPath");
        String fullPath = cwd + "\\" + stopWordsSubPath;
        dictionaryService.setStopWordsPath(fullPath);
    }
}
