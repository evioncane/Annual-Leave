package com.example.demo.repository;

import com.example.demo.model.ApplicationEntity;
import com.example.demo.model.ApplicationType;
import com.example.demo.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    Optional<ApplicationEntity> findById(long id);

    @Query("SELECT app FROM ApplicationEntity app WHERE (app.type = :type OR :type IS NULL)" +
            " AND (app.status = :status OR :status IS NULL) AND (app.user.username = :username OR :username IS NULL)" +
            " AND (app.date >= :dateBefore OR :dateBefore IS NULL) AND (app.date <= :dateAfter OR :dateAfter IS NULL)")
    List<ApplicationEntity> filterAll(@Param("type") ApplicationType type, @Param("status") Status status,
                                      @Param("username") String username, @Param("dateBefore") Date dateBefore,
                                      @Param("dateAfter") Date dateAfter);

}
