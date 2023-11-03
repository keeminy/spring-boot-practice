package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.example.demo.entity.User;


public interface UserRepository extends CrudRepository<User, String>{
 
    List<User> findByName(String name);

    Optional<User> findById(String id);
}
