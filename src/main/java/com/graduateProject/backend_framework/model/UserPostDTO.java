package com.graduateProject.backend_framework.model;

public class UserPostDTO {
    private Long postId;
    private String postText;
    private String filebyte;
    private  String hastags;
    private String type;
    private String timestamp;
    private Integer likes_count;

    public Integer getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(Integer likes_count) {
        this.likes_count = likes_count;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHastags() {
        return hastags;
    }

    public void setHastags(String hastags) {
        this.hastags = hastags;
    }



    // Constructor
    public UserPostDTO() {
    }

    // Getters and setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }



    public void setFilebytes(String filebyte) {
        this.filebyte = filebyte;
    }
    public String getFilebyte(){
        return filebyte;
    }

    public String get_type(){
        return type;
    }

    public void set_type(String type){
        this.type=type;
    }
}
