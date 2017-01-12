package com.me.core.service.features.nGrams;

import com.me.core.domain.entities.NGrams;
import com.me.core.domain.entities.Website;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class NGramCreator {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String cleanURL(String url) {
        String cleanResult = "";
        // remove "http://" prefix
        String[] tokens = url.split("//");
        // now tokens[0] contains http:// and tokens[1] contains all the necessary data
        // let's find website extension (.com / .de / .eu etc)
        // but first make sure that tokens[1] has adequate size:
        try {
            int trailingPointIdx = tokens[1].lastIndexOf(".");
            // remove website extension, leave only website body
            String urlBody = tokens[1].substring(0, trailingPointIdx);
            // applying regexp to clean data: leave only letters and digits
            cleanResult = urlBody.replaceAll("[^A-Za-z0-9]", "");
        } catch (StringIndexOutOfBoundsException e) {
            logger.error("Exception: {} with utl {}", e, url);
        }

        return cleanResult;
    }

    public Optional<NGrams> generateNGrams (Website inputURL, int nGramSize) {
        String url = inputURL.getUrl();
        String res =  generateNGram(url, nGramSize);
        NGrams nGrams = null;
        if (!res.equals("")) {
            nGrams = new NGrams(inputURL, nGramSize, res);
        }
        return Optional.ofNullable(nGrams);
    }

    private String generateNGram(String word, int nGramSize) {
        // do some clean-up
        word = cleanURL(word);
        String result = "";

        if (!word.equals("")){
            for (int i = 0; i < word.length(); i++) {
                if ( (i + nGramSize - 1) < word.length() ) {
                    String nGram = word.substring(i, i + nGramSize);
                    // add to list if the string is not only digits
                    if (!StringUtils.isNumeric(nGram)) {
                        result += nGram + " ";
                    }
                }
            }
        }

        return result;
    }
}
