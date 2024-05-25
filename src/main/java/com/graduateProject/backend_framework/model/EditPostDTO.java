package com.graduateProject.backend_framework.model;

public class EditPostDTO {
    Long post_id;
    Long user_id;
    String post_text;
    String timestamp;
    String hashtags;
    String username;
    int likes_count;
    String profile_pic;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getPost_base64() {
        return post_base64;
    }

    public void setPost_base64(String post_base64) {
        this.post_base64 = post_base64;
    }

    String post_base64;

    String changed_on;

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }





    public String getChanged_on() {
        return changed_on;
    }

    public void setChanged_on(String changed_on) {
        this.changed_on = changed_on;
    }




}
