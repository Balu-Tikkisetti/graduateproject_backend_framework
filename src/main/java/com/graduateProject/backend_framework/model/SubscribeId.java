package com.graduateProject.backend_framework.model;

import java.io.Serializable;

public class SubscribeId implements Serializable {
    private Long subscribed_user_id;
    private Long subscriber_user_id;
    public SubscribeId() {
    }

    public SubscribeId(Long subscribed_user_id, Long subscriber_user_id) {
        this.subscribed_user_id = subscribed_user_id;
        this.subscriber_user_id = subscriber_user_id;
    }
}
