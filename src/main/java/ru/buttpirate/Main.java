package ru.buttpirate;

import org.apache.commons.text.StringEscapeUtils;
import ru.buttpirate.dto.VKVideo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        String rawCookie = FileService.readFileContent("./cookies.txt");

        String rawLinks = FileService.readFileContent("./links.txt");

        String[] links = FileService.parseLinkLines(rawLinks);

        for (String link : links) {
            System.out.println("INFO: parsing link <"+link+">...");

            if (link.startsWith("https://vk.com/video")) {
                System.out.println("INFO: link <"+link+"> was detected as VK video!");
                VKScrapper.saveVKVideo(link, rawCookie);
            } else {
                System.out.println("ERROR: Unknown link format: <"+link+">!");
            }

            // TODO: additional scrappers here
        }

    }

}
