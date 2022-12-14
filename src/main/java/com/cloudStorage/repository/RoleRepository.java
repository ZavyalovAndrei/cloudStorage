package com.cloudStorage.repository;

import com.cloudStorage.model.EntityRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface RoleRepository extends JpaRepository<EntityRole, Long> {

}
