package com.graduateProject.backend_framework.repository;

import com.graduateProject.backend_framework.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByGmail(String gmail);



    Optional<Users>  findById(Long user_id);
}
