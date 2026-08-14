package com.karmantial.studentservice.Kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.karmantial.studentservice.model.Event.StudentCreatedEvent;
import com.karmantial.studentservice.model.Event.StudentUpdatedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentKafkaProducer {

    private static final String CREATED = "student-created";

    private static final String UPDATED = "student-updated";

    private final KafkaTemplate<String, StudentCreatedEvent> createdKafkaTemplate;

    private final KafkaTemplate<String, StudentUpdatedEvent> updatedKafkaTemplate;

    public void sendStudentCreatedEvent(StudentCreatedEvent event){
        createdKafkaTemplate.send(CREATED, event.id().toString(), event);
    }

    public void sendStudentUpdatedEvent(StudentUpdatedEvent event){
        updatedKafkaTemplate.send(UPDATED, event.id().toString(), event);
    }

}
