package com.example.pointmanagement.job.expire.message;

import com.example.pointmanagement.job.listener.InputExpiredSoonPointAlarmCriteriaDateStepListener;
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
public class MessageExpireSoonPointStepConfiguration {
    @Bean
    @JobScope
    public Step messageExpireSoonPointStep(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager,
            InputExpiredSoonPointAlarmCriteriaDateStepListener listener,
            RepositoryItemReader<ExpiredPointSummary> messageExpireSoonPointItemReader,
            ItemProcessor<ExpiredPointSummary, Message> messageExpireSoonPointItemProcessor,
            JpaItemWriter<Message> messageExpireSoonPointItemWriter
    ) {
        return new StepBuilder("messageExpireSoonPointStep", jobRepository)
                .allowStartIfComplete(true)
                .listener(listener)
                .<ExpiredPointSummary, Message>chunk(1000, platformTransactionManager)
                .reader(messageExpireSoonPointItemReader)
                .processor(messageExpireSoonPointItemProcessor)
                .writer(messageExpireSoonPointItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<ExpiredPointSummary> messageExpireSoonPointItemReader(
            PointRepository pointRepository,
            @Value("#{T(java.time.LocalDate).parse(stepExecutionContext[alarmCriteriaDate])}")
            LocalDate alarmCriteriaDate
    ) {
        return new RepositoryItemReaderBuilder<ExpiredPointSummary>()
                .name("messageExpiredSoonPointItemReader")
                .repository(pointRepository)
                .methodName("sumBeforeCriteriaDate")
                .pageSize(1000)
                .arguments(alarmCriteriaDate)
                .sorts(Map.of("pointWallet", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<ExpiredPointSummary, Message> messageExpireSoonPointItemProcessor(
            @Value("#{T(java.time.LocalDate).parse(stepExecutionContext[alarmCriteriaDate])}")
            LocalDate alarmCriteriaDate
    ) {
        return summary -> Message.expiredSoonPointMessageInstance(
                summary.getUserId(), alarmCriteriaDate, summary.getAmount()
        );
    }

    @Bean
    @StepScope
    public JpaItemWriter<Message> messageExpireSoonPointItemWriter(
            EntityManagerFactory entityManagerFactory
    ) {
        JpaItemWriter<Message> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
