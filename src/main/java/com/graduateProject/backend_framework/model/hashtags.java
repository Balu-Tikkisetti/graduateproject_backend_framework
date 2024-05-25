package com.graduateProject.backend_framework.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class hashtags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long hashtag_id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private posts posts;

    @Column(name = "hashtag_text")
    private String hashtag_text;

    @Column(name = "timestamp")
    private String timestamp;

    public Long getHashtag_id() {
        return hashtag_id;
    }



    public void setHashtag_id(Long hashtag_id) {
        this.hashtag_id = hashtag_id;
    }

    public com.graduateProject.backend_framework.model.posts getPosts() {
        return posts;
    }

    public void setPosts(com.graduateProject.backend_framework.model.posts posts) {
        this.posts = posts;
    }

    public String getHashtag_text() {
        return hashtag_text;
    }

    public void setHashtag_text(String hashtag_text) {
        this.hashtag_text = hashtag_text;
    }




    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }








}
