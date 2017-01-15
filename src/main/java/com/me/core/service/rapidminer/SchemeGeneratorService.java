//package com.me.core.service.rapidminer;
//
//import com.me.data.entities.Experiment;
//
//import java.util.List;
//
//public class SchemeGeneratorService {
//    private String templatesPath;
//    private String workingDir;
//    private List<String> targetCategories;
//
//    public List<String> getTargetCategories() {
//        return targetCategories;
//    }
//
//    public void setTargetCategories(List<String> targetCategories) {
//        this.targetCategories = targetCategories;
//    }
//
//    public String getTemplatesPath() {
//        return templatesPath;
//    }
//
//    public void setTemplatesPath(String templatesPath) {
//        this.templatesPath = templatesPath;
//    }
//
//    public String getWorkingDir() {
//        return workingDir;
//    }
//
//    public void setWorkingDir(String workingDir) {
//        this.workingDir = workingDir;
//    }
//
//    public void generateSchemes(Experiment experiment, List<String> categories){
//        SchemeGenerator sg = new SchemeGenerator();
//        sg.setTemplatesPath(templatesPath);
//        sg.setWorkingDir(workingDir);
//
//        categories.forEach(category ->
//                sg.generateBaseLearnersScheme(experiment,
//                        categories.indexOf(category) + 1, category));
//
//        sg.generateStackingScheme(experiment, categories.size());
//        sg.generateApplyModelScheme(experiment);
//    }
//}
