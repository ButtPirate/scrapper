package ru.buttpirate;

import org.apache.commons.text.StringEscapeUtils;
import ru.buttpirate.dto.VKVideo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        String cookie = "Paste cookie here";

        String[] links = {

        };

        for (String link : links) {
            System.out.println("INFO: parsing link <"+link+">...");

            if (link.startsWith("https://vk.com/video")) {
                System.out.println("INFO: link <"+link+"> was detected as VK video!");
                VKScrapper.saveVKVideo(link, cookie);
            } else {
                System.out.println("ERROR: Unknown link format: <"+link+">!");
            }

            // TODO: additional scrappers here
        }

    }

}
