package com.me.core.service.reporting;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.dto.ConfusionTable;
import com.me.core.service.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExcelReporterService extends StoppableObservable implements MyExecutable {

    @Getter @Setter
    private List<String> expNames;
    @Getter @Setter
    private String cwd;

    @Autowired
    public ExcelReporterService(ProgressWatcher watcher) {
        super.addSubscriber(watcher);
    }

    @Override
    public void execute() throws Exception {

        for (String expName : expNames) {
            String fixedExpName = expName.replaceAll("[^A-Z0-9a-z_]", "");
            String excelOutput = cwd + "/reports/" + fixedExpName + "/report.xlsx";
            String performanceFile = cwd + "/per/" + fixedExpName + "/apply.per";
            Utils.createFilePath(excelOutput);

            super.updateMessage("creating Excel report for: " + expName);
            ConfusionTable confusionTable = PerformanceFileParser.parsePerformanceXML(performanceFile);
            ReportTool.readPerformance(excelOutput, confusionTable);
        }
    }

    @Override
    public String getName() {
        return "Excel Reporter service";
    }
}
