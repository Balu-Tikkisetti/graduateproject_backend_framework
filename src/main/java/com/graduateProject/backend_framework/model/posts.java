package com.graduateProject.backend_framework.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.sql.Blob;

@Entity
public class posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long post_id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Users user;

    @Column(name = "post_video_url", unique = true, length = 1000)
    private String post_video_url;

    @Column(name = "post_pic_url",unique = true,length = 1000)
    private String post_pic_url;

    @Column(name="post_image_blob")
    private Blob post_image_blob;

    @Column(name = "post_text")
    private String post_text;

    @Column(name = "likes_count")
    private Integer likes_count;

    @Column(name = "comments_count")
    private Integer comments_count;

    public Blob getPost_image_blob() {
        return post_image_blob;
    }

    public void setPost_image_blob(Blob post_image_blob) {
        this.post_image_blob = post_image_blob;
    }

    public Integer getComments_count() {
        return comments_count!=null?comments_count:0;
    }

    public void setComments_count(Integer comments_count) {
        this.comments_count = comments_count;
    }

    public int getLikes_count() {
        return likes_count != null ? likes_count : 0;
    }

    public void setLikes_count(Integer likes_count) {
        this.likes_count = likes_count;
    }

    @Column(name = "changed_on")
    private String changed_on;


    public String getChanged_on() {
        return changed_on;
    }

    public void setChanged_on(String changed_on) {
        this.changed_on = changed_on;
    }

    @Column(name="timestamp")
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getPost_video_url() {
        return post_video_url;
    }

    public void setPost_video_url(String post_video_url) {
        this.post_video_url = post_video_url;
    }

    public String getPost_pic_url() {
        return post_pic_url;
    }

    public void setPost_pic_url(String post_pic_url) {
        this.post_pic_url = post_pic_url;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }
}
