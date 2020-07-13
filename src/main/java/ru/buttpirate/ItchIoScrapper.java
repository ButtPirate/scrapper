package ru.buttpirate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.buttpirate.dto.ItchUnclaimedGame;

import java.io.IOException;
import java.net.ConnectException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ItchIoScrapper {
    public static void claimAllGamesFromBundle(String startPageLink, String rawCookie) throws IOException, InterruptedException {
        // Check cookie format
        if (!rawCookie.contains("itchio_token") || !rawCookie.contains("__cfduid")) {
            throw new InvalidParameterException("Invalid cookie <"+rawCookie+"> for itch.io!");
        }

        // Parse first bundle page
        String startPageAsString = null;
        try {
            startPageAsString = WebService.getPageAsString(startPageLink, rawCookie);
        } catch (ConnectException e) {
            System.out.println("ERROR: Unable to load page by initial link! Check connection");
            return;
        }

        Document startPageDocument = Jsoup.parse(startPageAsString);

        // Get total number of pages
        Element pager = startPageDocument.getElementsByClass("pager").first();
        Element ofLabel = pager.getElementsByClass("pager_label").first();
        Element ofLink = ofLabel.getElementsByTag("a").first();
        int totalPages = Integer.parseInt(ofLink.ownText());

        // For each page
        for (int i = 1; i <= totalPages; i++) {
            // Initial request for game page
            String gamePageLink = startPageLink + "?page="+i;
            String initialGamePageAsString = null;
            try {
                initialGamePageAsString = WebService.getPageAsString(gamePageLink, rawCookie);
            } catch (ConnectException e) {
                System.out.println("ERROR: Unable to load game page <"+i+">! Check connection");
                return;
            }

            Document initialGamePageDocument = Jsoup.parse(initialGamePageAsString);

            // Find all unclaimed games - their "Download" button is actually POST form submit
            Elements gameRows = initialGamePageDocument.getElementsByClass("game_row");
            Elements downloadButtons = gameRows.select(".button_row");
            Elements claimForms = downloadButtons.select("form");

            // All games on page are claimed, proceed to next page
            if (claimForms.isEmpty()) {
                continue;
            }

            // Parse all not claimed games to objects
            List<ItchUnclaimedGame> unclaimedGames = new ArrayList<>();
            for (Element claimForm : claimForms) {
                ItchUnclaimedGame gameObject = new ItchUnclaimedGame();

                // Get id form one of the inputs in the form
                gameObject.setId(claimForm.getElementsByAttributeValue("name", "game_id").first().val());

                // Find parent div with class "game_title", get child <a> and parse href & title from it
                Element gameTitleTag = claimForm.parent().parent().getElementsByClass("game_title").first().getElementsByTag("a").first();
                gameObject.setLink(gameTitleTag.attr("href"));
                gameObject.setTitle(gameTitleTag.ownText());

                unclaimedGames.add(gameObject);

            }

            for (ItchUnclaimedGame unclaimedGame : unclaimedGames) {
                // Sleep for random ms to avoid getting ip blocked
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000 + 1));

                System.out.println("INFO: Claiming game <"+unclaimedGame.getTitle()+"> with id <"+unclaimedGame.getId()+">...");

                // Request game page again to generate new csrf token
                String gamePageAsString = null;
                try {
                    gamePageAsString = WebService.getPageAsString(gamePageLink, rawCookie);
                } catch (ConnectException e) {
                    System.out.println("ERROR: Unable to reload game page due to connection error!");
                    continue;
                }

                Document gamePageDocument = Jsoup.parse(gamePageAsString);

                // Find form for this particular game by it's game id
                Element gameForm = gamePageDocument.getElementsByAttributeValue("value", unclaimedGame.getId()).first().parent();

                // Get generated csrf token from corresponding input in the form
                String csrfToken = gameForm.getElementsByAttributeValue("name", "csrf_token").val();

                // Form request params
                Map<String, String> requestParams = new HashMap();
                requestParams.put("csrf_token", csrfToken);
                requestParams.put("game_id", unclaimedGame.getId());
                requestParams.put("action", "claim");

                // Custom itch.io headers
                Map<String, String> headers = new HashMap();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Host", "itch.io");

                // Send post
                int statusCode;
                try {
                    statusCode = WebService.sendPost(gamePageLink, rawCookie, requestParams, headers);
                } catch (Exception e) {
                    System.out.println("ERROR: Error on claiming game <"+unclaimedGame.getTitle()+"> with id <"+unclaimedGame.getId()+">!" + e);
                    continue;
                }

                // Game claimed
                if (statusCode == 302) {
                    System.out.println("INFO: Successfully claimed game <"+unclaimedGame.getTitle()+"> with id <"+unclaimedGame.getId()+">!");
                } else {
                    System.out.println("ERROR: Invalid status code on POST request: <"+statusCode+">! Game might not be claimed!");
                }

            }


        }

    }

}
