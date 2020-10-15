package com.example.demo.repository;

import com.example.demo.model.RoleEntity;
import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Set<RoleEntity> findByNameIn(Set<String> names);

    @Query("SELECT role.users FROM RoleEntity role WHERE role.name = :roleName")
    List<UserEntity> findEmailsByRole(@Param("roleName") String roleName);
}
