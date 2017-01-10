//package com.me.core.service.features.tag;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.support.GenericXmlApplicationContext;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class TagExtractService {
//    private List<String> targetCategories;
//    private WebsiteDAO websiteDAO;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    private TagUtility tagUtility;
//
//    public TagUtility getTagUtility() {
//        return tagUtility;
//    }
//
//    public void setTagUtility(TagUtility tagUtility) {
//        this.tagUtility = tagUtility;
//    }
//
//    public List<String> getTargetCategories() {
//        return targetCategories;
//    }
//
//    public void setTargetCategories(List<String> targetCategories) {
//        this.targetCategories = targetCategories;
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
//    public void extractTagData(){
//        List<Category> categories = websiteDAO.findDesiredCategories(targetCategories);
//        logger.info(">>> starting for categories {}", categories);
//
//        categories.forEach(category -> {
//            logger.info("starting for category {}", category);
//            List<WebsiteTagCount> counts = new LinkedList<>();
//            // retrieve htmls in category
//            List<HTML> htmls = websiteDAO.findHTMLsByCategory(category);
//            processHTMLsInCategory(counts, htmls);
//            websiteDAO.batchSaveCounts(counts);
//            counts.clear();
//        });
//        logger.info(">>> all categories processed!");
//    }
//
//    private void processHTMLsInCategory(List<WebsiteTagCount> counts,
//                                        List<HTML> htmls) {
//        // foreach html in category
//        htmls.forEach(html -> {
//            // count all tags in page
//            int count = tagUtility.countAllTags(html.getHtml());
//            // create respective object
//            WebsiteTagCount websiteTagCount = new WebsiteTagCount();
//            websiteTagCount.setWebsite(html.getWebsite());
//            websiteTagCount.setCount(count);
//            counts.add(websiteTagCount);
//            List<TagsInPage> toInsert = processSingleHTML(html);
//            // now save list to Db
//            websiteDAO.batchSaveTagsInPage(toInsert);
//            toInsert.clear();
//        });
//    }
//
//    @NotNull
//    private List<TagsInPage> processSingleHTML(HTML html) {
//        // list all tags from html
//        List<String> tags = tagUtility.getAllTagsInPage(html.getHtml());
//        // now list only unique tags
//        List<String> unique = tagUtility.getUniqueTagsInPage(html.getHtml());
//
//        List<TagsInPage> toInsert = new LinkedList<>();
//        // foreach unique tag in html
//        unique.forEach(tagName -> {
//            int occurNumber = tagUtility.countTagOccur(tags, tagName);
//            // create objects
//            Tag tag = new Tag();
//            tag.setTagName(tagName);
//            TagsInPage tagInPage = new TagsInPage();
//            tagInPage.setWebsite(html.getWebsite());
//            tagInPage.setTag(tag);
//            tagInPage.setOccurrencesNumber(occurNumber);
//            toInsert.add(tagInPage);
//        });
//        return toInsert;
//    }
//
//    public static void main(String[] args) {
//        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
//        ctx.load("META-INF/spring/spring-root.xml");
//        ctx.refresh();
//
//        TagExtractService tagExtractService =
//                (TagExtractService) ctx.getBean("tagExtractService");
//        tagExtractService.extractTagData();
//    }
//}
