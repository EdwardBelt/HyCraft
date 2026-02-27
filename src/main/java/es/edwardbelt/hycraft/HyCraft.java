package es.edwardbelt.hycraft;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.MinecraftServerBootstrap;

import javax.annotation.Nonnull;

public class HyCraft extends JavaPlugin {
    private MinecraftServerBootstrap minecraftServerBootstrap;

    public HyCraft(@Nonnull JavaPluginInit init) {
        super(init);
        this.minecraftServerBootstrap = new MinecraftServerBootstrap();
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
}