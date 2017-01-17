package com.me.common.initializer;

import com.me.common.MyExecutable;
import com.me.core.domain.dto.Modes;
import com.me.core.domain.dto.Types;
import com.me.core.domain.entities.Experiment;
import com.me.core.domain.entities.ExperimentParam;
import com.me.core.service.experiment.ExperimentCreator;
import com.me.core.service.experiment.tag.TagDictionaryCreator;
import com.me.core.service.experiment.text.aml.PrepareAMLDatService;
import com.me.core.service.experiment.text.dictionary.TextDictionaryService;
import com.me.core.service.experiment.text.output.AMLDATWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DictionaryServiceInitializer implements Initializer {

    @Lazy
    private final TextDictionaryService textDictionaryService;
    @Lazy
    private final ExperimentCreator experimentCreator;
    @Lazy
    private final PrepareAMLDatService amlDatPrepareService;
    @Lazy
    private final AMLDATWriter amldatWriter;
    @Lazy
    private final TagDictionaryCreator tagDictionaryCreator;

    @Autowired
    public DictionaryServiceInitializer(TextDictionaryService textDictionaryService,
                                        ExperimentCreator experimentCreator,
                                        PrepareAMLDatService amlDatPrepareService,
                                        AMLDATWriter amldatWriter,
                                        TagDictionaryCreator tagDictionaryCreator) {
        this.textDictionaryService = textDictionaryService;
        this.experimentCreator = experimentCreator;
        this.amlDatPrepareService = amlDatPrepareService;
        this.amldatWriter = amldatWriter;
        this.tagDictionaryCreator = tagDictionaryCreator;
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

            List<String> textExperimentsNames = experimentDataSetName.keySet().stream()
                    .filter(experiment -> !experiment.getMode().equals(Modes.TAG_STAT))
                            .map(Experiment::getExpName).collect(Collectors.toList());

            List<String> tagExperimentNames = experimentDataSetName.keySet().stream()
                    .filter(experiment -> experiment.getMode().equals(Modes.TAG_STAT))
                    .map(Experiment::getExpName).collect(Collectors.toList());

            experimentCreator.setExperimentDataSetName(new LinkedHashMap<>(experimentDataSetName));
            textDictionaryService.setExpNames(new ArrayList<>(textExperimentsNames));
            setFullPaths(dto, settings);
            amlDatPrepareService.setExpNames(new ArrayList<>(textExperimentsNames));
            amldatWriter.setExpNames(new ArrayList<>(textExperimentsNames));
            tagDictionaryCreator.setExpNames(tagExperimentNames);

//            executables.add(experimentCreator);
//            executables.add(textDictionaryService);
//            executables.add(amlDatPrepareService);
//            executables.add(amldatWriter);
            executables.add(tagDictionaryCreator);
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

        ExperimentParam experimentParam = new ExperimentParam();
        if (!mode.equals(Modes.TAG_STAT)) {
            double IDF_Threshold = (double) settings.get("IDF_Treshold");
            experimentParam.setIDF_Threshold(IDF_Threshold);
            String IDF_Type = (String) settings.get("IDF_Type");
            experimentParam.setIDF_Type(IDF_Type);
            String TF_Type = (String) settings.get("TF_Type");
            experimentParam.setTF_Type(TF_Type);
        }

        int featuresByCategory = (int) settings.get("featuresByCategory");
        experimentParam.setFeaturesByCategory(featuresByCategory);

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
    private void setFullPaths(Map<String, Object> dto, Map<String, Object> settings) {
        Map<String, Object> formImport = (Map<String, Object>) dto.get("import");
        String cwd = (String) formImport.get("cwd");

        String stopWordsSubPath = (String) settings.get("stopWordsPath");
        String fullStopWordsPath = cwd + "\\" + stopWordsSubPath;

        String amlPath = (String) settings.get("amlPath");
        String fullAmlPath = cwd + "\\" + amlPath;

        textDictionaryService.setStopWordsPath(fullStopWordsPath);
        amldatWriter.setOutputFolder(fullAmlPath);
    }
}
