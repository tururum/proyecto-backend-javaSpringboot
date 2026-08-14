package com.karmantial.studentservice.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic studentCreatedTopic(){
        return new NewTopic("student-created", 1, (short) 1);
    }

    @Bean
    public NewTopic studentUpdatedTopic(){
        return new NewTopic("student-updated", 1, (short)1);
    }

    @Bean
    public NewTopic studentDeletedTopic(){
        return new NewTopic("student-deleted",1, (short)1);
    }
}
