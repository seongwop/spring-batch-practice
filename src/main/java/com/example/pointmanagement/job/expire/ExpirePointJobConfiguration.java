package com.example.pointmanagement.job.expire;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpirePointJobConfiguration {
    @Bean
    public Job expirePointJob(
            JobRepository jobRepository
    ) {
        return new JobBuilder("expirePointJob", jobRepository)
                .start(step)
                .build();
    }
}
