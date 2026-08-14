package com.karmantial.groupservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.karmantial.groupservice.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

}
