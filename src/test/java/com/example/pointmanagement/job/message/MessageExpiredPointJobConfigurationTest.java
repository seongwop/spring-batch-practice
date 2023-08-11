package com.example.pointmanagement.job.message;

import com.example.pointmanagement.BatchTestSupport;
import com.example.pointmanagement.message.Message;
import com.example.pointmanagement.message.MessageRepository;
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

class MessageExpiredPointJobConfigurationTest extends BatchTestSupport {
    @Autowired
    Job messageExpiredPointJob;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    PointWalletRepository pointWalletRepository;
    @Autowired
    PointRepository pointRepository;

    @Test
    void messageExpirePointJob() throws Exception {
        // given
        LocalDate earnDate = LocalDate.of(2023, 1, 1);
        LocalDate expireDate = LocalDate.of(2023, 8, 9);
        LocalDate notExpireDate = LocalDate.of(2023, 12, 31);
        PointWallet pointWallet1 = pointWalletRepository.save(
                new PointWallet("user1", BigInteger.valueOf(3000))
        );
        PointWallet pointWallet2 = pointWalletRepository.save(
                new PointWallet("user2", BigInteger.ZERO)
        );
        pointRepository.save(new Point(pointWallet2, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet2, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));
        // when
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2023-08-10")
                .toJobParameters();
        JobExecution execution = launchJob(messageExpiredPointJob, jobParameters);
        // then
        // user 1 : 3000pt expire message
        // user 2 : 2000pt expire message
        then(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<Message> messages = messageRepository.findAll();
        then(messages).hasSize(2);
        Message message1 = messages.stream().filter(item -> item.getUserId().equals("user1")).findFirst().orElseGet(null);
        then(message1).isNotNull();
        then(message1.getTitle()).isEqualTo("3000 포인트 만료");
        then(message1.getContent()).isEqualTo("2023-08-10 기준 3000 포인트가 만료되었습니다.");
        Message message2 = messages.stream().filter(item -> item.getUserId().equals("user2")).findFirst().orElseGet(null);
        then(message2).isNotNull();
        then(message2.getTitle()).isEqualTo("2000 포인트 만료");
        then(message2.getContent()).isEqualTo("2023-08-10 기준 2000 포인트가 만료되었습니다.");
    }
}