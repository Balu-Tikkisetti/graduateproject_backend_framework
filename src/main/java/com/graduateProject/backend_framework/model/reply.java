package com.graduateProject.backend_framework.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long reply_id;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "comment_id")
    private comments comments;

    public Long getReply_text() {
        return reply_text;
    }

    public void setReply_text(Long reply_text) {
        this.reply_text = reply_text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public com.graduateProject.backend_framework.model.comments getComments() {
        return comments;
    }

    public void setComments(com.graduateProject.backend_framework.model.comments comments) {
        this.comments = comments;
    }

    public Long getReply_id() {
        return reply_id;
    }

    public void setReply_id(Long reply_id) {
        this.reply_id = reply_id;
    }

    @Column(name="reply_text")
    private Long reply_text;

    @Column(name="timestamp")
    private String timestamp;


}
