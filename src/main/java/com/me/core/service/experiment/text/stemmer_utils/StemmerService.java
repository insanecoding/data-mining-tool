package com.me.core.service.experiment.text.stemmer_utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StemmerService {
    public static String stemmString(String str) throws Exception {
        Stemmer s = new Stemmer();

        String res = "";
        char[] w = new char[501];
        try (InputStream in = new ByteArrayInputStream(str.toLowerCase().getBytes())) {
            while (true) {
                int ch = in.read();
                if (Character.isLetter((char) ch)) {
                    int j = 0;
                    while (true) {
                        ch = Character.toLowerCase((char) ch);
                        w[j] = (char) ch;
                        if (j < 500)
                            j++;
                        ch = in.read();
                        if (!Character.isLetter((char) ch)) {
            /* to test add(char ch) */
                            for (int c = 0; c < j; c++)
                                s.add(w[c]);

            /* or, to test add(char[] w, int j) */
            /* s.add(w, j); */

                            s.stem();
                            {
                                String u;

              /* and now, to test toString() : */
                                u = s.toString();

              /* to test getResultBuffer(), getResultLength() : */
              /* u = new String(s.getResultBuffer(), 0, s.getResultLength()); */

                                res += u;
                            }
                            break;
                        }
                    }
                }
                if (ch < 0)
                    break;
                res += (char) ch;
            }
        }
        return res;
    }
}
