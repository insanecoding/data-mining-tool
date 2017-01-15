package com.me.core.service.dao;

import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.DataSet;
import com.me.core.domain.entities.Website;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("myDaoImpl")
@Transactional
public class ExperimentDaoImpl implements ExperimentDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public ExperimentDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Category> findCategoriesByDataSet(DataSet dataSet) {
        return sessionFactory.getCurrentSession()
                .createQuery("select ds.chosenCategories " +
                        " from DataSet ds where ds = :dataSet")
                .setParameter("dataSet", dataSet).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Website> findWebsitesByDataSet(DataSet dataSet, boolean isLearn) {

        String query = (isLearn) ? "select ds.websitesLearn from DataSet ds where ds = :dataSet" :
                "select ds.websitesTest from DataSet ds where ds = :dataSet";
        return sessionFactory.getCurrentSession()
                .createQuery(query)
                .setParameter("dataSet", dataSet)
                .list();
    }
}
