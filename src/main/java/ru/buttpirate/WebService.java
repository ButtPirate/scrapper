package ru.buttpirate;

import org.jsoup.Jsoup;

import java.io.IOException;

public class WebService {
    public static String getPageAsString(String href, String cookie) throws IOException {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        String html;
        if (cookie != null) {
            html = Jsoup.connect(href).cookie("Cookie", cookie).get().html();
        } else {
            html = Jsoup.connect(href).get().html();
        }

        return html;
    }

}
