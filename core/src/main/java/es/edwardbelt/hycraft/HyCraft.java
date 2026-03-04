package es.edwardbelt.hycraft;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import es.edwardbelt.hycraft.api.HyCraftApi;
import es.edwardbelt.hycraft.api.connection.HyCraftConnection;
import es.edwardbelt.hycraft.api.gui.HyCraftGui;
import es.edwardbelt.hycraft.config.ConfigManager;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.MinecraftServerBootstrap;
import es.edwardbelt.hycraft.network.handler.minecraft.manager.gui.GuiManager;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class HyCraft extends JavaPlugin implements HyCraftApi {
    public static final String PATH = "mods/HyCraft";
    private static HyCraft INSTANCE;
    public static HyCraft get() { return INSTANCE; }

    private final MinecraftServerBootstrap minecraftServerBootstrap;
    @Getter
    private final ConfigManager configManager;

    public HyCraft(@Nonnull JavaPluginInit init) {
        super(init);
        INSTANCE = this;

        this.minecraftServerBootstrap = new MinecraftServerBootstrap();
        this.configManager = new ConfigManager();
    }

    @Override
    protected void setup() {
        HyCraftApi.setInstance(this);
        configManager.reload();
    }

    @Override
    protected void start() {
        MappingRegistry.init();
        minecraftServerBootstrap.init();
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

    @Override
    public void openGui(UUID uuid, HyCraftGui gui) {
        ClientConnection connection = minecraftServerBootstrap.getConnection(uuid);
        if (connection == null) return;
        GuiManager.get().openGui(connection, gui);
    }

    @Override
    public void closeGui(UUID uuid) {
        ClientConnection connection = minecraftServerBootstrap.getConnection(uuid);
        if (connection == null) return;
        GuiManager.get().closeGui(connection);
    }

    @Override
    public HyCraftGui getOpenedGui(UUID uuid) {
        ClientConnection connection = minecraftServerBootstrap.getConnection(uuid);
        if (connection == null) return null;
        return connection.getOpenedGui();
    }
}