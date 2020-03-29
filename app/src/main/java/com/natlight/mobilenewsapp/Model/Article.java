package com.natlight.mobilenewsapp.Model;

public class Article {
    private String title;
    private String description;
    private String url;
    private String autor;

    public Article(String title, String description, String url, String autor, String imageUrl, String published) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.autor = autor;
        this.imageUrl = imageUrl;
        this.published = published;
    }


    public Article() {
    }

    private String imageUrl;
    private String published;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }


}
