package com.courseproject.api.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Configuration
public class HibernateSearchIndexBuild implements ApplicationListener<ApplicationReadyEvent> {

    private Logger logger = LogManager.getLogger();

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("Started Initializing Indexes");
        SearchSession searchSession = Search.session(entityManager);
        MassIndexer indexer = searchSession.massIndexer().idFetchSize(150).batchSizeToLoadObjects(25)
                .threadsToLoadObjects(12);
        try {
            indexer.startAndWait();
        } catch (InterruptedException e) {
            logger.warn("Failed to load data from database");
            Thread.currentThread().interrupt();
        }
        logger.info("Completed Indexing");

    }

}
