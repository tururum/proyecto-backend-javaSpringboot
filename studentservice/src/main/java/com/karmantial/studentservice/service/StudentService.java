package com.karmantial.studentservice.service;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;
import com.karmantial.studentservice.repository.StudentRepository;
import com.karmantial.studentservice.Kafka.StudentKafkaProducer;
import com.karmantial.studentservice.model.Student;
import com.karmantial.studentservice.model.Event.StudentCreatedEvent;
import com.karmantial.studentservice.model.Event.StudentUpdatedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentKafkaProducer studentKafkaProducer;

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
        String telefono = student.getTelefono();
        if (verifyStudent(telefono)) {
           Student savedStudent =  studentRepository.save(student);
            StudentCreatedEvent event = new StudentCreatedEvent(savedStudent.getId(), 
            savedStudent.getFirstName(), 
            savedStudent.getLastName(), 
            savedStudent.getTelefono());

            studentKafkaProducer.sendStudentCreatedEvent(event);
            return savedStudent;
        }

        throw new Error("Estudiante existene");
    }

    public Student editStudent(Student student, Long id){
        Optional<Student> existingStudent = studentRepository.findById(id);

        if (!existingStudent.isEmpty()) {
            Student foundStudent =  existingStudent.get();
            foundStudent.setFirstName(student.getFirstName());
            foundStudent.setLastName(student.getLastName());
            foundStudent.setTelefono(student.getTelefono());
            studentRepository.save(foundStudent);


            StudentUpdatedEvent event = new StudentUpdatedEvent(foundStudent.getId(),
                foundStudent.getFirstName(),
                foundStudent.getLastName(),
                foundStudent.getTelefono());
            
            studentKafkaProducer.sendStudentUpdatedEvent(event);
            return foundStudent;
            
        }

        throw new Error("Usuario no encontrado");

    }

    public boolean deleteStudent(Long id){
        Optional<Student> existingStudent = studentRepository.findById(id);
        
        if (!existingStudent.isEmpty()) {
            studentRepository.deleteById(id);
            return true;
        }

        throw new Error("usuario no encontrado");
    }


    public Boolean verifyStudent(String telefono){
        Optional<Student> existingStudent = studentRepository.findByTelefono(telefono);
        if (existingStudent.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
