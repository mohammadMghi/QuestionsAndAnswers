package com.example.questionandanswer.model;

public class Question {
    private int id;
    private String title;
    private String content;
    private String UrlImageQuestion;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrlImageQuestion() {
        return UrlImageQuestion;
    }

    public void setUrlImageQuestion(String urlImageQuestion) {
        UrlImageQuestion = urlImageQuestion;
    }
}
