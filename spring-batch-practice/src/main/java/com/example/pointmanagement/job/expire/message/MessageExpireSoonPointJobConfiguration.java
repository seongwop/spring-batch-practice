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
public class MessageExpireSoonPointJobConfiguration {
    @Bean
    public Job messageExpireSoonPointJob(
            JobRepository jobRepository,
            TodayJobParameterValidator validator,
            Step messageExpireSoonPointStep
    ) {
        return new JobBuilder("MessageExpireSoonPointJob", jobRepository)
                .validator(validator)
                .incrementer(new RunIdIncrementer())
                .start(messageExpireSoonPointStep)
                .build();
    }
}
