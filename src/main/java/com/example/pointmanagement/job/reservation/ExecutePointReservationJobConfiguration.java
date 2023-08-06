package com.example.pointmanagement.job.reservation;

import com.example.pointmanagement.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;

public class ExecutePointReservationJobConfiguration {

    @Bean
    public Job executePointReservationJob(
            JobRepository jobRepository,
            TodayJobParameterValidator validator,
            Step executePointReservationStep
    ) {
        return new JobBuilder("executePointReservation", jobRepository)
                .incrementer(new RunIdIncrementer())
                .validator(validator)
                .start(executePointReservationStep)
                .build();
    }
}
