package es.edwardbelt.hycraft.api;

import es.edwardbelt.hycraft.api.connection.HyCraftConnection;

import java.util.Map;
import java.util.UUID;

public interface HyCraftApi {
    HyCraftConnection connectionByUUID(UUID uuid);
    Map<UUID, HyCraftConnection> onlineConnections();

    static void setInstance(HyCraftApi instance) {
        InstanceHolder.INSTANCE = instance;
    }

    static HyCraftApi get() {
        return InstanceHolder.INSTANCE;
    }

    class InstanceHolder {
        public static HyCraftApi INSTANCE;
    }
}
