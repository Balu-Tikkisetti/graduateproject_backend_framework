package com.graduateProject.backend_framework.repository;

import com.graduateProject.backend_framework.model.Subscribe;
import com.graduateProject.backend_framework.model.SubscribeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, SubscribeId> {

    Subscribe save(Subscribe subscribe);

    List<Subscribe> findAll();

    void deleteById(SubscribeId id); // corrected parameter name
}
