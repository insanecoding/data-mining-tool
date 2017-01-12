//package com.me.core.service.features.nGrams;
//
//import com.me.core.domain.entities.Category;
//import com.me.core.domain.entities.NGrams;
//import com.me.core.domain.entities.Website;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.IntStream;
//
//public class NGramExtractorService {
//    private NGramCreator nGramCreator;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    private WebsiteDAO websiteDAO;
//    private Integer maxNGramSize;
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
//    public Integer getMaxNGramSize() {
//        return maxNGramSize;
//    }
//
//    public void setMaxNGramSize(Integer maxNGramSize) {
//        this.maxNGramSize = maxNGramSize;
//    }
//
//    public WebsiteDAO getWebsiteDAO() {
//        return websiteDAO;
//    }
//
//    public void setWebsiteDAO(WebsiteDAO websiteDAO) {
//        this.websiteDAO = websiteDAO;
//    }
//
//    public NGramCreator getnGramCreator() {
//        return nGramCreator;
//    }
//
//    public void setnGramCreator(NGramCreator nGramCreator) {
//        this.nGramCreator = nGramCreator;
//    }
//
//    private void extractNGramsInCategory(Category category, List<Website> websites) {
//        IntStream.range(2, maxNGramSize + 1).forEach(size -> {
//            List<NGrams> nGrams = new LinkedList<>();
//            logger.info("creating NGrams with N={} for category {}", size, category );
//            websites.forEach(website -> {
//                Optional<NGrams> res =
//                        nGramCreator.generateNGrams(website, size);
//                res.ifPresent(nGrams::add);
//            });
//            websiteDAO.batchSaveNGrams(nGrams);
//            nGrams.clear();
//            logger.info("finished with N={} for category {}", size, category );
//        });
//    }
//
//    public void generateNGrams(){
//        List<Category> categories =
//                websiteDAO.findDesiredCategories(targetCategories);
//        categories.forEach(category -> {
//            logger.info(">> processing category {}", category);
//            List<Website> urls = websiteDAO.findURLsByCategory(category);
//            extractNGramsInCategory(category, urls);
//            logger.info("finished with category {}", category);
//        });
//    }
//
//}
