//package com.me.core.service.rapidminer;
//
//import com.me.data.entities.Experiment;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class RapidMinerExecService {
//    private String workingDir;
//    private RapidMinerExecutor rapidMinerExecutor;
//
//    public String getWorkingDir() {
//        return workingDir;
//    }
//
//    public void setWorkingDir(String workingDir) {
//        this.workingDir = workingDir;
//    }
//
//    public RapidMinerExecutor getRapidMinerExecutor() {
//        return rapidMinerExecutor;
//    }
//
//    public void setRapidMinerExecutor(RapidMinerExecutor rapidMinerExecutor) {
//        this.rapidMinerExecutor = rapidMinerExecutor;
//    }
//
//    private List<String> findSchemes(Experiment experiment){
//        String fixedExpName = experiment.getExpName().replaceAll("[^A-Z0-9a-z]", "");
//        List<String> strs = new LinkedList<>();
//        try {
//            strs = Files.list(Paths.get(workingDir + "/schemes/" + fixedExpName))
//                    .filter(Files::isRegularFile).map(Path::toFile)
//                    .map(File::getAbsolutePath)
//                    .collect(Collectors.toList());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return strs;
//    }
//
//    public void execInRM(Experiment experiment){
//        List<String> schemes = findSchemes(experiment);
//        schemes.forEach(scheme -> rapidMinerExecutor.executeProcessInRM(scheme));
//    }
//}
