package com.example.demo.repository;

import com.example.demo.model.ApplicationEntity;
import com.example.demo.model.ApplicationType;
import com.example.demo.model.Status;
import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    Optional<ApplicationEntity> findByIdAndDeleted(long id, boolean deleted);
    Optional<ApplicationEntity> findByIdAndDeletedAndUser(long id, boolean deleted, UserEntity userEntity);

    @Query("SELECT app FROM ApplicationEntity app WHERE (app.type = :type OR :type IS NULL)" +
            " AND (app.status = :status OR :status IS NULL) AND (app.user.username = :username OR :username IS NULL)" +
            " AND (app.date >= :dateBefore OR :dateBefore IS NULL) AND (app.date <= :dateAfter OR :dateAfter IS NULL)" +
            " AND app.deleted = :deleted")
    List<ApplicationEntity> filterAll(@Param("type") ApplicationType type, @Param("status") Status status,
                                      @Param("username") String username, @Param("dateBefore") Date dateBefore,
                                      @Param("dateAfter") Date dateAfter, @Param("deleted") boolean deleted);

    @Query("DELETE FROM ApplicationEntity entity WHERE entity.id = :id AND entity.deleted = :deleted AND entity.user.username = :username")
    long deleteById(@Param("id") long id, @Param("deleted") boolean deleted, @Param("username") String username);

}
