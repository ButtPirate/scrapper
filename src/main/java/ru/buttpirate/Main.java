package ru.buttpirate;

public class Main {
    public static void main(String[] args) throws Exception {
        String rawCookie = FileService.readFileContent("./cookies.txt");

        String rawLinks = FileService.readFileContent("./links.txt");

        String[] links = FileService.parseLinkLines(rawLinks);

        for (String link : links) {
            System.out.println("INFO: parsing link <"+link+">...");

            if (link.startsWith("https://vk.com/video")) {
                System.out.println("INFO: link <" + link + "> was detected as VK video!");
                VKScrapper.saveVKVideo(link, rawCookie);
            } else if (link.startsWith("https://itch.io/bundle/download")) {
                System.out.println("INFO: link <" + link + "> was detected as Itch.io bundle download!");

                if (link.contains("?")) {
                    link = link.substring(0, link.lastIndexOf("?"));
                }

                if (link.endsWith("/")) {
                    link = link.substring(0, link.lastIndexOf("/"));
                }

                ItchIoScrapper.claimAllGamesFromBundle(link, rawCookie);
            } else {
                System.out.println("ERROR: Unknown link format: <"+link+">!");
            }

            // TODO: additional scrappers here
        }
    }

}
