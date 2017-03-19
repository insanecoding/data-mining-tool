package com.me.common.initializer;

import com.me.common.MyExecutable;
import com.me.core.domain.dto.Modes;
import com.me.core.domain.dto.Types;
import com.me.core.domain.entities.Experiment;
import com.me.core.domain.entities.ExperimentParam;
import com.me.core.service.experiment.create.ExperimentCreator;
import com.me.core.service.experiment.output.AMLDATWriter;
import com.me.core.service.experiment.tag.dictionary.TagDictionaryCreator;
import com.me.core.service.experiment.tag.prepare_aml.PrepareTagAMLDATService;
import com.me.core.service.experiment.text.aml.PrepareAMLDatService;
import com.me.core.service.experiment.text.dictionary.TextDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DictionaryServiceInitializer implements Initializer {

    @Lazy
    private final TextDictionaryService textDictionaryCreator;
    @Lazy
    private final ExperimentCreator experimentCreator;
    @Lazy
    private final PrepareAMLDatService textAmlDatPrepareService;
    @Lazy
    private final AMLDATWriter amldatWriter;
    @Lazy
    private final TagDictionaryCreator tagDictionaryCreator;
    @Lazy
    private final PrepareTagAMLDATService tagAmlDatPrepareService;
    private final SchemeGeneratorInitializer next;


    @Autowired
    public DictionaryServiceInitializer(TextDictionaryService textDictionaryCreator,
                                        ExperimentCreator experimentCreator,
                                        PrepareAMLDatService textAmlDatPrepareService,
                                        AMLDATWriter amldatWriter,
                                        TagDictionaryCreator tagDictionaryCreator,
                                        PrepareTagAMLDATService tagAmlDatPrepareService,
                                        SchemeGeneratorInitializer next) {
        this.textDictionaryCreator = textDictionaryCreator;
        this.experimentCreator = experimentCreator;
        this.textAmlDatPrepareService = textAmlDatPrepareService;
        this.amldatWriter = amldatWriter;
        this.tagDictionaryCreator = tagDictionaryCreator;
        this.tagAmlDatPrepareService = tagAmlDatPrepareService;
        this.next = next;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void initialize(Map<String, Object> dto, List<MyExecutable> executables) {
        Map<String, Object> settings = (Map<String, Object>) dto.get("prepare");

        if ((boolean) settings.get("isOn")) {
            List<Map<String, Object>> experiments =
                    (List<Map<String, Object>>) settings.get("experiments");

            List<Experiment> experimentDataSetName = new ArrayList<>();
            List<Modes> modesUsed = experiments.stream()
                    .map(elem -> processElem(elem, experimentDataSetName))
                    .collect(Collectors.toList());

            List<MyExecutable> myExecutables =
                    initializeServices(dto, settings, experimentDataSetName, modesUsed);
            executables.addAll(myExecutables);
        }
        next.initialize(dto, executables);
    }

    private List<MyExecutable> initializeServices(Map<String, Object> dto,
                                                  Map<String, Object> settings,
                                                  List<Experiment> experimentDataSetName, List<Modes> modes) {
        List<String> textExperimentsNames = createTextExperimentsNames(experimentDataSetName);
        List<String> tagExperimentNames = createTagExperimentsNames(experimentDataSetName);
        List<String> allExperimentNames = createAllExperimentsNames(experimentDataSetName);

        List<MyExecutable> result = new ArrayList<>();

        experimentCreator.setExperiments(new ArrayList<>(experimentDataSetName));
        result.add(experimentCreator);
        setFullPaths(dto, settings);

        if (modes.contains(Modes.TEXT_MAIN) || modes.contains(Modes.TEXT_FROM_TAGS)
                || modes.contains(Modes.NGRAMS)) {
            textDictionaryCreator.setExpNames(new ArrayList<>(textExperimentsNames));
            textAmlDatPrepareService.setExpNames(new ArrayList<>(textExperimentsNames));
            result.addAll(Arrays.asList(textDictionaryCreator, textAmlDatPrepareService));
        }

        if (modes.contains(Modes.TAG_STAT)) {
            tagDictionaryCreator.setExpNames(tagExperimentNames);
            tagAmlDatPrepareService.setExpNames(tagExperimentNames);
            result.addAll(Arrays.asList(tagDictionaryCreator, tagAmlDatPrepareService));
        }

        amldatWriter.setExpNames(new ArrayList<>(allExperimentNames));
        result.add(amldatWriter);

        return result;
    }

    private List<String> createAllExperimentsNames(List<Experiment> experiments) {
        return experiments.stream()
                .map(Experiment::getExpName).collect(Collectors.toList());
    }

    private List<String> createTagExperimentsNames(List<Experiment> experiments) {
        return experiments.stream()
                .filter(experiment ->
                        experiment.getMode().equals(Modes.TAG_STAT))
                .map(Experiment::getExpName).collect(Collectors.toList());
    }

    private List<String> createTextExperimentsNames(List<Experiment> experiments) {
        return experiments.stream()
                .filter(experiment ->
                        !experiment.getMode().equals(Modes.TAG_STAT) &&
                                !experiment.getMode().equals(Modes.JOIN)
                ).map(Experiment::getExpName).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Modes processElem(Map<String, Object> settings,
                                   List<Experiment> experiments) {
        // these are common for all experiments
        String name = (String) settings.get("name");
        String description = (String) settings.get("description");
        String strMode = (String) settings.get("mode");
        Modes mode = Modes.valueOf(strMode.toUpperCase());
        Types type = null;

        ExperimentParam experimentParam = new ExperimentParam();
        // text experiments only
        if (!mode.equals(Modes.TAG_STAT) && !mode.equals(Modes.JOIN)) {
            double IDF_Threshold = Utils.stringToDouble(settings.get("IDF_Treshold"));
            experimentParam.setIDF_Threshold(IDF_Threshold);
            String IDF_Type = (String) settings.get("IDF_Type");
            experimentParam.setIDF_Type(IDF_Type);
            String TF_Type = (String) settings.get("TF_Type");
            experimentParam.setTF_Type(TF_Type);
        }

        if (!mode.equals(Modes.JOIN)) {
            // all experiments except joined have type
            String strType = (String) settings.get("type");
            type = Types.valueOf(strType.toUpperCase());

            int featuresByCategory = Utils.stringToInt(settings.get("featuresByCategory"));
            String dataSetName = (String) settings.get("dataSetName");
            experimentParam.setFeaturesByCategory(featuresByCategory);
            experimentParam.setDataSetName(dataSetName);
        } else {
            List<String> dependenciesNames = (List<String>) settings.get("experiments");
            experimentParam.setDependeciesNames(dependenciesNames);
        }

        if (mode.equals(Modes.NGRAMS)) {
            int nGramSize = Utils.stringToInt(settings.get("nGramSize"));
            experimentParam.setNGramSize(nGramSize);
        }

        if (mode.equals(Modes.TEXT_FROM_TAGS)) {
            String tagName = (String) settings.get("tagName");
            experimentParam.setTagName(tagName);
        }

        if (mode.equals(Modes.TAG_STAT)) {
            double normalizeRatio = Utils.stringToDouble(settings.get("normalizeRatio"));
            experimentParam.setNormalizeRatio(normalizeRatio);
            int roundToDecimalPlaces = Utils.stringToInt(settings.get("roundToDecimalPlaces"));
            experimentParam.setRoundToDecimalPlaces(roundToDecimalPlaces);
        }

        Experiment experiment =
                new Experiment(name, description, mode);
        experiment.setType(type);
        experiment.setExperimentParam(experimentParam);

        experiments.add(experiment);
        return mode;
    }

    @SuppressWarnings("unchecked")
    private void setFullPaths(Map<String, Object> dto, Map<String, Object> settings) {
        Map<String, Object> formImport = (Map<String, Object>) dto.get("import");
        String cwd = (String) formImport.get("cwd");

        String stopWordsSubPath = (String) settings.get("stopWordsPath");
        String fullStopWordsPath = cwd + "\\" + stopWordsSubPath;

        String fullAmlPath = cwd + "\\amls\\";

        textDictionaryCreator.setStopWordsPath(fullStopWordsPath);
        amldatWriter.setOutputFolder(fullAmlPath);
    }
}
