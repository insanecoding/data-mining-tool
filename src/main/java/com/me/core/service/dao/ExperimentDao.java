package com.me.core.service.dao;

import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.DataSet;
import com.me.core.domain.entities.Website;

import java.util.List;

public interface ExperimentDao {
    List<Category> findCategoriesByDataSet(DataSet dataSet);

    List<Website> findWebsitesByDataSet(DataSet dataSet, boolean isLearn);
}