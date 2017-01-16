package com.me.core.service.splitter;

import com.me.core.domain.dto.DataSplitterParam;
import com.me.core.domain.entities.ChosenCategory;
import com.me.core.domain.entities.ChosenWebsite;
import com.me.core.domain.entities.Website;
import com.me.core.service.dao.MyDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataSplitterUtil {

    private final MyDao dao;

    @Data
    @AllArgsConstructor
    private class LearnAndTest {
        private List<List<ChosenWebsite>> learn;
        private List<List<ChosenWebsite>> test;
    }

    @Autowired
    public DataSplitterUtil(MyDao dao) {
        this.dao = dao;
    }

    void createDivideSave(DataSplitterParam param,
                          List<ChosenCategory> categories) throws InterruptedException {
        // divide into learn and test
        LearnAndTest data = divideByLearnAndTest(param, categories);
        // save learn part
        save(data.getLearn(), true);
        // save test part
        save(data.getTest(), false);

        data.getLearn().clear();
        data.getTest().clear();
    }

    private LearnAndTest divideByLearnAndTest(DataSplitterParam param,
                                              List<ChosenCategory> categories) {

        int websitesPerCategory = param.getMainParams().getWebsitesPerCategory();
        double partitionLearn = param.getDataSet().getPartitionLearn();

        List<List<ChosenWebsite>> learn = new LinkedList<>();
        List<List<ChosenWebsite>> test = new LinkedList<>();

        // select websites in each category that satisfy predefined condition
        List<List<ChosenWebsite>> data = createDataSet(param, categories);

        data.forEach(dataInCategory -> {

            int learningWebsitesNum;
            int maxSize;
            // calculate number
            if (dataInCategory.size() > websitesPerCategory) {
                learningWebsitesNum = (int) (websitesPerCategory * partitionLearn);
                maxSize = websitesPerCategory;
            } else {
                learningWebsitesNum = (int) (dataInCategory.size() * partitionLearn);
                maxSize = dataInCategory.size();
            }

            // dataInCategory is List!
            Collections.shuffle(dataInCategory);
            List<ChosenWebsite> learnInCategory =
                    dataInCategory.subList(0, learningWebsitesNum);
            learn.add(learnInCategory);

            List<ChosenWebsite> testInCategory =
                    dataInCategory.subList(learningWebsitesNum, maxSize);
            test.add(testInCategory);
        });
        data.clear();
        return new LearnAndTest(learn, test);
    }

    private List<List<ChosenWebsite>> createDataSet(DataSplitterParam param,
                                                    List<ChosenCategory> categories) {
        // their websites will be stored here
        List<List<ChosenWebsite>> data = new LinkedList<>();

        for (ChosenCategory category : categories) {
            List<Website> websites =
                    dao.findWebsitesWithCondition(
                            category.getCategory(),
                            param.getMainParams()
                    );

            // now create from Website objects ChosenWebsiteObjects
            List<ChosenWebsite> chosen = websites.stream()
                    .map(website -> {
                        ChosenWebsite chosenWebsite = new ChosenWebsite();
                        chosenWebsite.setWebsite(website);
                        chosenWebsite.setDataSet(param.getDataSet());
                        return chosenWebsite;
                    })
                    .collect(Collectors.toList());
            data.add(chosen);
        }
        return data;
    }

    private void save(List<List<ChosenWebsite>> listList,
                      boolean isForLearn) throws InterruptedException {
        for (List<ChosenWebsite> list : listList) {
            list.forEach(chosenWebsite -> chosenWebsite.setForLearning(isForLearn));
            dao.batchSave(list, false);
        }
    }
}
