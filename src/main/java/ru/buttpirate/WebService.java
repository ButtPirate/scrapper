package ru.buttpirate;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static int sendPost(String url, String cookie, Map<String, String> params, Map<String, String> headers) throws IOException {
        HttpPost post = new HttpPost(url);

        // Post body
        List<NameValuePair> urlParameters = new ArrayList<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            urlParameters.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        // Headers
        post.setHeader("Cookie", cookie);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            post.setHeader(header.getKey(), header.getValue());
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            return response.getStatusLine().getStatusCode();
        }

    }

}
