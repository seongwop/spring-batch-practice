package com.example.pointmanagement.job.reservation;


import com.example.pointmanagement.point.reservation.PointReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ExecutePointReservationStepPartitioner implements Partitioner {
    private final PointReservationRepository pointReservationRepository;
    private final LocalDate today;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        long min = pointReservationRepository.findMinId(today);
        long max = pointReservationRepository.findMaxId(today);
        long targetSize = (max - min) / gridSize + 1;

        Map<String, ExecutionContext> result = new HashMap<>();
        long number = 0;
        long start = min;
        long end = start + targetSize - 1;

        while (start <= max) {
            ExecutionContext value = new ExecutionContext();
            if (end >= max) {
                end = max;
            }
            value.putLong("minId", start);
            value.putLong("maxId", end);
            result.put("partition" + number, value);
            start += targetSize;
            end += targetSize;
            number++;
        }
        return result;
    }
}
