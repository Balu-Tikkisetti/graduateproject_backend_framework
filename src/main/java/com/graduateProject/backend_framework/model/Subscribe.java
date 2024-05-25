package com.graduateProject.backend_framework.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CollectionId;

@Entity
@IdClass(SubscribeId.class)
public class Subscribe {

    @Id
    @Column(name="subscribed_user_id")
    private Long subscribed_user_id;

    @Id
    @Column(name="subscriber_user_id")
    private Long subscriber_user_id;


    public Long getSubscribed_user_id() {
        return subscribed_user_id;
    }

    public void setSubscribed_user_id(Long subscribed_user_id) {
        this.subscribed_user_id = subscribed_user_id;
    }

    public Long getSubscriber_user_id() {
        return subscriber_user_id;
    }

    public void setSubscriber_user_id(Long subscriber_user_id) {
        this.subscriber_user_id = subscriber_user_id;
    }



}
