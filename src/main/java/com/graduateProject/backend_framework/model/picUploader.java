package com.graduateProject.backend_framework.model;

import jakarta.persistence.*;

import java.sql.Blob;

@Entity
public class picUploader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pic_id")
    private Long pic_id;

    @Column(name="image_data")
    private Blob image_data;

    public Long getPic_id() {
        return pic_id;
    }

    public void setPic_id(Long pic_id) {
        this.pic_id = pic_id;
    }

    public Blob getImage_data() {
        return image_data;
    }

    public void setImage_data(Blob image_data) {
        this.image_data = image_data;
    }
}
