package com.bravos2k5.bravosshop.utils;

public interface SnowFlakeId {

    Long getId();

    default Long getCreatedAt() {
        Long id = getId();
        if(id == null) {
            throw new NullPointerException();
        }
        return IdentifyGenerator.extractTimestamp(getId());
    }

    default Long getMachineId() {
        Long id = getId();
        if(id == null) {
            throw new NullPointerException();
        }
        return IdentifyGenerator.extractMachineId(getId());
    }

}
