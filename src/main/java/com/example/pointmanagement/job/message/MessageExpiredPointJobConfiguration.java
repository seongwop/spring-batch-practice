package com.example.pointmanagement.job.message;

import com.example.pointmanagement.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageExpiredPointJobConfiguration {
    @Bean
    public Job MessageExpiredPointJob(
            JobRepository jobRepository,
            TodayJobParameterValidator validator,
            Step messageExpiredPointStep
    ) {
        return new JobBuilder("MessageExpiredPointStep", jobRepository)
                .validator(validator)
                .incrementer(new RunIdIncrementer())
                .start(messageExpiredPointStep)
                .build();
    }
}
