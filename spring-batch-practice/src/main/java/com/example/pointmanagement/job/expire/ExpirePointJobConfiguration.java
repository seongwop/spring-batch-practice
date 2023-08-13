package com.example.pointmanagement.job.expire;

import com.example.pointmanagement.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpirePointJobConfiguration {
    @Bean
    public Job expirePointJob(
            JobRepository jobRepository,
            TodayJobParameterValidator validator,
            Step expirePointStep
    ) {
        return new JobBuilder("expirePointJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .validator(validator)
                .start(expirePointStep)
                .build();
    }
}
