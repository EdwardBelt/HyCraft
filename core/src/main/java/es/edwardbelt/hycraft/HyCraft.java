package es.edwardbelt.hycraft;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import es.edwardbelt.hycraft.api.HyCraftApi;
import es.edwardbelt.hycraft.api.connection.HyCraftConnection;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.MinecraftServerBootstrap;
import es.edwardbelt.hycraft.network.player.ClientConnection;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class HyCraft extends JavaPlugin implements HyCraftApi {
    private final MinecraftServerBootstrap minecraftServerBootstrap;

    public HyCraft(@Nonnull JavaPluginInit init) {
        super(init);
        this.minecraftServerBootstrap = new MinecraftServerBootstrap();
    }

    @Override
    protected void setup() {
        HyCraftApi.setInstance(this);
    }

    @Override
    protected void start() {
        minecraftServerBootstrap.init();
        MappingRegistry.init();
    }

    @Override
    protected void shutdown() {
        minecraftServerBootstrap.shutdown();
    }

    @Override
    public HyCraftConnection connectionByUUID(UUID uuid) {
        return minecraftServerBootstrap.getConnection(uuid);
    }

    @Override
    public Map<UUID, HyCraftConnection> onlineConnections() {
        return Collections.unmodifiableMap(minecraftServerBootstrap.getConnectionsByUUID());
    }
}