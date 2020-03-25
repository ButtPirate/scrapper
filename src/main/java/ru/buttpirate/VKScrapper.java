package ru.buttpirate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import ru.buttpirate.dto.VKVideo;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class VKScrapper {
    public static void saveVKVideo(String pageLink, String cookie) throws Exception {
        System.out.println("INFO: Saving VK video...");
        String page = WebService.getPageAsString(pageLink, cookie);

        if (page.contains("src=\\\"https:\\/\\/www.youtube.com\\/embed\\/")) {
            String youtubeLink = VKScrapper.parseYoutubeLink(page);
            System.out.println("INFO: Embedded youtube video found! Link: <"+youtubeLink+">");
            YoutubeScrapper.saveYoutubeVideo(youtubeLink);
            return;
        }

        // Parse page to find direct link with biggest resolution and video title
        VKVideo video = VKScrapper.parseVideo(page);

        // Escape characters, append format
        String filename = VKScrapper.generateFilename(pageLink, video);

        System.out.println("INFO: Saving <"+pageLink+"> as <"+filename+"> to disk...");

        FileService.saveFile(video.getDirectLink(), filename);

        System.out.println("INFO: VK video <"+pageLink+"> saved as <"+filename+">!");

    }


    public static VKVideo parseVideo(String page) {
        List<String> allResolutionsLinks = parseDirectLinks(page);

        String biggestResolutionLink = findBiggestResolution(allResolutionsLinks);

        String title = parseVideoTitle(page);

        return new VKVideo(title, biggestResolutionLink);
    }

    private static List<String> parseDirectLinks(String page) {
        String[] rawURLparts = page.split("\"url");

        List<String> allResolutionLinks = new ArrayList();

        for (String URLpart : rawURLparts) {
            if (URLpart.contains(".mp4") && URLpart.contains("\":\"http")) {
                int urlStartIndex = URLpart.indexOf("\":\"");
                int urlEndIndex = URLpart.indexOf(".mp4");
                String escapedLink = URLpart.substring(urlStartIndex + 3, urlEndIndex + 4);
                String link = StringEscapeUtils.unescapeJava(escapedLink);

                allResolutionLinks.add(link);
            }

        }

        return allResolutionLinks;

    }

    public static String parseYoutubeLink(String page) {
        String[] parts = page.split("embed");
        String rightPart = "";

        for (String part : parts) {
            if (part.contains("enablejsapi")) {
                rightPart = part;
                break;
            }
        }
        Integer index = rightPart.indexOf("?");
        String youtubeId = rightPart.substring(2, index);
        return "https://www.youtube.com/watch?v=" + youtubeId;
    }

    private static String findBiggestResolution(List<String> links) {
        return links.stream().max((o1, o2) -> {
            String[] o1DotParts = o1.split("\\.");
            String o1Resolution = o1DotParts[o1DotParts.length - 2];

            String[] o2DotParts = o2.split("\\.");
            String o2Resolution = o2DotParts[o2DotParts.length - 2];

            if (Integer.parseInt(o1Resolution) > Integer.parseInt(o2Resolution)) {
                return 1;
            } else {
                return -1;
            }


        }).get();

    }

    private static String parseVideoTitle(String page) {
        int start = page.indexOf("id=\\\"mv_title\\\">");
        String withTail = page.substring(start+16);
        int end = withTail.indexOf("<\\/div>\\n");

        return withTail.substring(0, end);

    }

    public static String generateFilename(String pageLink, VKVideo video) {
        String[] idParts = pageLink.split("_");
        String videoId = idParts[idParts.length-1];

        String escapedTitle = video.getTitle().replaceAll("[^а-яА-Яa-zA-Z0-9-_\\.]", " ");

        String[] formatParts = video.getDirectLink().split("\\.");
        String format = formatParts[formatParts.length - 1];

        return videoId + "_" + escapedTitle + "." + format;

    }

}
