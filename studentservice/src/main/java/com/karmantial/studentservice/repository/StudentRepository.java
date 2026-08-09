package com.karmantial.studentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.karmantial.studentservice.model.Student;



@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
