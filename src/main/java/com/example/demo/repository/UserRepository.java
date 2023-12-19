package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.User;

public interface UserRepository extends CrudRepository<User, String> {

    List<User> findByUserNm(String name);

    @Override
    Optional<User> findById(String id);

}
