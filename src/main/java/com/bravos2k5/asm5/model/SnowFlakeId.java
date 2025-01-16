package com.bravos2k5.asm5.model;

import com.bravos2k5.asm5.utils.IdentifyGenerator;

public interface SnowFlakeId {

    Long getId();

    default Long getCreatedAt() {
        return IdentifyGenerator.extractTimestamp(getId());
    }

    default Long getMachineId() {
        return IdentifyGenerator.extractMachineId(getId());
    }

}
