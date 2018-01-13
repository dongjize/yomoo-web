package com.yomoo.yomooweb.entity;

/**
 * Description:
 *
 * @Author: dong
 * @Date: 2017-12-25
 * @Time: 00:04
 */
public class BreedingInfo extends BaseModel {
    private Long id;
    private String title;
    private String content;
    private User publisher;

    public BreedingInfo() {
    }

    public BreedingInfo(String title, String content, User publisher) {
        this.title = title;
        this.content = content;
        this.publisher = publisher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }
}
