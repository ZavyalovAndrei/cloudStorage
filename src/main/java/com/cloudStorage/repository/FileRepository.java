package com.cloudStorage.repository;

import com.cloudStorage.entity.FileToCloud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileToCloud, Long> {

    @Query(value = "select * from netology.depository s where s.user_id = ?1 order by s.id desc limit ?2", nativeQuery = true)
    List<FileToCloud> findAllByUserIdWithLimit(long userId, int limit);
    Optional<FileToCloud> findByUserIdAndName(long userId, String name);
    void removeByUserIdAndName(long userId, String name);
}

