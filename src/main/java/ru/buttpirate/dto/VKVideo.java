package ru.buttpirate.dto;

public class VKVideo {
    private String title;
    private String directLink;

    public VKVideo(String title, String directLink) {
        this.title = title;
        this.directLink = directLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirectLink() {
        return directLink;
    }

    public void setDirectLink(String directLink) {
        this.directLink = directLink;
    }
}
