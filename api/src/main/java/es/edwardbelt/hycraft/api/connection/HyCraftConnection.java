package es.edwardbelt.hycraft.api.connection;

import es.edwardbelt.hycraft.api.entity.HyCraftEntity;

import java.util.Map;

public interface HyCraftConnection {
    int getNetworkId();
    Map<Integer, HyCraftEntity> getEntities();
    float getHealth();
}
