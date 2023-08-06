package com.example.pointmanagement.job.expire;

import com.example.pointmanagement.point.Point;
import com.example.pointmanagement.point.PointRepository;
import com.example.pointmanagement.point.wallet.PointWallet;
import com.example.pointmanagement.point.wallet.PointWalletRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Map;

@Configuration
public class ExpirePointStepConfiguration {
    @Bean
    @JobScope
    public Step expirePointStep(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager,
            JpaPagingItemReader<Point> expirePointItemReader,
            ItemProcessor<Point, Point> expirePointItemProcessor,
            ItemWriter<Point> expirePointItemWriter
    ) {
        return new StepBuilder("expirePointStep", jobRepository)
                .allowStartIfComplete(true)
                .<Point, Point>chunk(1000, platformTransactionManager)
                .reader(expirePointItemReader)
                .processor(expirePointItemProcessor)
                .writer(expirePointItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Point> expirePointItemReader(
            EntityManagerFactory entityManagerFactory,
            @Value("#{T(java.time.LocalDate).parse(jobParameters[today])}")
            LocalDate today
    ) {
        return new JpaPagingItemReaderBuilder<Point>()
                .name("expirePointItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select p from Point p where p.expireDate < :today and used = false and expired = false")
                .parameterValues(Map.of("today", today))
                .pageSize(1000)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Point, Point> expirePointItemProcessor() {
        return point -> {
            point.setExpired(true);
            PointWallet wallet = point.getPointWallet();
            wallet.setAmount(wallet.getAmount().subtract(point.getAmount()));
            return point;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Point> expirePointItemWriter(
            PointRepository pointRepository,
            PointWalletRepository pointWalletRepository
    ) {
        return points -> {
            for (Point point : points) {
                if (point.isExpired()) {
                    pointRepository.save(point);
                    pointWalletRepository.save(point.getPointWallet());
                }
            }
        };
    }
}
