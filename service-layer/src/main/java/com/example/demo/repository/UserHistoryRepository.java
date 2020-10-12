package com.example.demo.repository;

import com.example.demo.model.UserHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHistoryRepository extends JpaRepository<UserHistoryEntity, Long> {

}
