package com.example.pointmanagement.job.expire.message;

import com.example.pointmanagement.job.listener.InputExpiredPointAlarmCriteriaDateStepListener;
import com.example.pointmanagement.message.Message;
import com.example.pointmanagement.point.ExpiredPointSummary;
import com.example.pointmanagement.point.PointRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Map;

@Configuration
public class MessageExpiredPointStepConfiguration {
    @Bean
    @JobScope
    public Step messageExpiredPointStep(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager,
            InputExpiredPointAlarmCriteriaDateStepListener listener,
            RepositoryItemReader<ExpiredPointSummary> messageExpiredPointItemReader,
            ItemProcessor<ExpiredPointSummary, Message> messageExpiredPointItemProcessor,
            JpaItemWriter<Message> messageExpiredPointItemWriter
    ) {
        return new StepBuilder("messageExpiredPointStep", jobRepository)
                .allowStartIfComplete(true)
                .listener(listener)
                .<ExpiredPointSummary, Message>chunk(1000, platformTransactionManager)
                .reader(messageExpiredPointItemReader)
                .processor(messageExpiredPointItemProcessor)
                .writer(messageExpiredPointItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<ExpiredPointSummary> messageExpiredPointItemReader(
            PointRepository pointRepository,
            @Value("#{T(java.time.LocalDate).parse(stepExecutionContext[alarmCriteriaDate])}")
            LocalDate alarmCriteriaDate
    ) {
        return new RepositoryItemReaderBuilder<ExpiredPointSummary>()
                .name("messageExpiredPointItemReader")
                .repository(pointRepository)
                .methodName("sumByExpiredDate")
                .pageSize(1000)
                .arguments(alarmCriteriaDate)
                .sorts(Map.of("pointWallet", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<ExpiredPointSummary, Message> messageExpiredPointItemProcessor(
            @Value("#{T(java.time.LocalDate).parse(jobParameters[today])}")
            LocalDate today
    ) {
        return summary -> Message.expiredPointMessageInstance(
                summary.getUserId(), today, summary.getAmount()
        );
    }

    @Bean
    @StepScope
    public JpaItemWriter<Message> messageExpiredPointItemWriter(
            EntityManagerFactory entityManagerFactory
    ) {
       JpaItemWriter<Message> jpaItemWriter = new JpaItemWriter<>();
       jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
       return jpaItemWriter;
    }
}
