package es.edwardbelt.hycraft.api;

import es.edwardbelt.hycraft.api.connection.HyCraftConnection;
import es.edwardbelt.hycraft.api.gui.HyCraftGui;

import java.util.Map;
import java.util.UUID;

public interface HyCraftApi {
    HyCraftConnection connectionByUUID(UUID uuid);
    Map<UUID, HyCraftConnection> onlineConnections();

    void openGui(UUID uuid, HyCraftGui gui);
    void closeGui(UUID uuid);
    HyCraftGui getOpenedGui(UUID uuid);

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
