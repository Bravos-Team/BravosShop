package com.bravos2k5.asm5.utils;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
public class IdentifyGenerator {

    @Setter
    private long machineId;

    private static final long machineIdBits = 10;
    private static final long sequenceBits = 12;
    private static final long timeStampBits = 41;
    private static final long epoch = LocalDateTime.of(2025,1,1,0,0).toInstant(ZoneOffset.UTC).toEpochMilli();

    private static final long machineIdShift = sequenceBits + timeStampBits;
    private static final long timestampShift = sequenceBits;

    private static final long sequenceMask = (1L << sequenceBits) - 1;

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public IdentifyGenerator(long machineId) {
        if(machineId < 0 || ((machineId > (1L << machineIdBits) - 1))) {
            throw new IllegalArgumentException("Machine ID must be between 0 and " + ((1L << machineIdBits) - 1));
        }
        this.machineId = machineId;
    }

    private long waitForNextMillis() {
        long currentTimeMillis = System.currentTimeMillis();
        while (currentTimeMillis <= lastTimestamp) {
            currentTimeMillis = System.currentTimeMillis();
        }
        return currentTimeMillis;
    }

    public synchronized long generateId() {
        long currentTimestamp = System.currentTimeMillis();
        long timestamp = currentTimestamp - epoch;
        if(currentTimestamp != lastTimestamp) {
            sequence = 0L;
            lastTimestamp = currentTimestamp;
        }
        else if(sequence >= sequenceMask) {
            long nextMillis = waitForNextMillis();
            timestamp = nextMillis - epoch;
            sequence = 0L;
            lastTimestamp = nextMillis;
        } else {
            sequence++;
        }
        long timestampShifted = timestamp << timestampShift;
        long machineIdShifted = machineId << machineIdShift;
        return timestampShifted | machineIdShifted | sequence;
    }

    public static long extractTimestamp(long id) {
        return ((id >> timestampShift) &
                ((1L << timeStampBits) - 1)) + epoch;
    }

    public static long extractMachineId(long id) {
        return (id >> machineIdShift) &
                ((1L << machineIdBits) - 1);
    }


}
