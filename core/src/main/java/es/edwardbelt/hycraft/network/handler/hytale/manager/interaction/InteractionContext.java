package es.edwardbelt.hycraft.network.handler.hytale.manager.interaction;

import es.edwardbelt.hycraft.network.player.ClientConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class InteractionContext {
    private ClientConnection connection;
    private Map<String, String> vars;
}
