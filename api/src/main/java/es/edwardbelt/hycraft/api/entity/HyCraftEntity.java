package es.edwardbelt.hycraft.api.entity;

import java.util.UUID;

public interface HyCraftEntity {
    int getId();
    UUID getUuid();

    double getX();
    double getY();
    double getZ();
}
