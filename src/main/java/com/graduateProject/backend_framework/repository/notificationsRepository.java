package com.graduateProject.backend_framework.repository;

import com.graduateProject.backend_framework.model.notifications;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface notificationsRepository extends CrudRepository<notifications,Long> {

    notifications save(notifications notification);
    List<notifications> findAll();
}
