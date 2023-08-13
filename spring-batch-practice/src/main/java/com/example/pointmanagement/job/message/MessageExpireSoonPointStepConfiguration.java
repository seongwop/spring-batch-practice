package com.example.pointmanagement.job.message;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MessageExpireSoonPointStepConfiguration {
    @Bean
    @JobScope
    public Step messageExpireSoonPointStep(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager
    ) {
        
    }
}
