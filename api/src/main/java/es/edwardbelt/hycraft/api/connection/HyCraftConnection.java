package es.edwardbelt.hycraft.api.connection;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import es.edwardbelt.hycraft.api.entity.HyCraftEntity;

import java.util.Map;

public interface HyCraftConnection {
    int getNetworkId();
    PlayerRef getPlayerRef();
    Map<Integer, HyCraftEntity> getEntities();
    float getHealth();
}
