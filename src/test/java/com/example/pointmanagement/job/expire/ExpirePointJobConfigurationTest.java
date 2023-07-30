package com.example.pointmanagement.job.expire;

import com.example.pointmanagement.BatchTestSupport;
import com.example.pointmanagement.point.Point;
import com.example.pointmanagement.point.PointRepository;
import com.example.pointmanagement.point.wallet.PointWallet;
import com.example.pointmanagement.point.wallet.PointWalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;


class ExpirePointJobConfigurationTest extends BatchTestSupport {
    @Autowired
    Job expirePointJob;
    @Autowired
    PointWalletRepository pointWalletRepository;
    @Autowired
    PointRepository pointRepository;

    @Test
    void expirePointJob() throws Exception {
        // given
        LocalDate earnedDate = LocalDate.of(2021, 1, 1);
        LocalDate expireDate = LocalDate.of(2021, 1, 5);
        PointWallet pointWallet = pointWalletRepository.save(
                new PointWallet(
                        "user123",
                        BigInteger.valueOf(6000)
                )
        );
        pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnedDate, expireDate));
        pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnedDate, expireDate));
        pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnedDate, expireDate));
        // when
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2021-01-06")
                .toJobParameters();
        JobExecution jobExecution = launchJob(expirePointJob, jobParameters);
        // then
        then(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<Point> points = pointRepository.findAll();
        then(points.stream().filter(Point::isExpired)).hasSize(3);
        PointWallet changedPointWallet = pointWalletRepository.findById(pointWallet.getId()).orElseGet(null);
        then(changedPointWallet).isNotNull();
        then(changedPointWallet.getAmount()).isEqualByComparingTo(BigInteger.valueOf(3000));
    }
}