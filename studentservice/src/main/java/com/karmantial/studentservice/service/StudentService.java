package com.karmantial.studentservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.karmantial.studentservice.repository.StudentRepository;
import com.karmantial.studentservice.model.Student;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getStudent(){
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id){
        Optional<Student> existingStudent = studentRepository.findById(id);

        if (!existingStudent.isEmpty()) {
            Student foundStudent = existingStudent.get();
            return foundStudent;
        }

        throw new Error("Usuario no encontrado");
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student editStudent(Student student, Long id){
        Optional<Student> existingStudent = studentRepository.findById(id);

        if (!existingStudent.isEmpty()) {
            Student foundStudent =  existingStudent.get();
            foundStudent.setFirstName(student.getFirstName());
            foundStudent.setLastName(student.getLastName());

            return studentRepository.save(foundStudent);
        }

        throw new Error("Usuario no encontrado");

    }

    public void deleteStudent(Long id){
        Optional<Student> existingStudent = studentRepository.findById(id);
        
        if (!existingStudent.isEmpty()) {
            studentRepository.deleteById(id);
        }

        throw new Error("usuario no encontrado");
    }


}
