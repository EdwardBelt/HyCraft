package es.edwardbelt.hycraft.protocol.packet.play;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import es.edwardbelt.hycraft.protocol.io.PacketBuffer;
import es.edwardbelt.hycraft.protocol.packet.Packet;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CommandsPacket implements Packet {
    private Map<String, AbstractCommand> commandRegistration;

    private static final byte TYPE_ROOT = 0x00;
    private static final byte TYPE_LITERAL = 0x01;
    private static final byte FLAG_EXECUTABLE = 0x04;
    private static final byte FLAG_HAS_REDIRECT = 0x08;

    private static class Node {
        byte flags;
        final List<Integer> children = new ArrayList<>();
        int redirectTo = -1;
        String name;
    }

    @Override
    public void write(PacketBuffer buffer) {
        List<Node> nodes = new ArrayList<>();
        Map<AbstractCommand, Integer> cmdIndex = new IdentityHashMap<>();

        Node root = new Node();
        root.flags = TYPE_ROOT;
        nodes.add(root);

        for (AbstractCommand command : commandRegistration.values()) {
            if (command.getName() == null) continue;
            int idx = buildCommandNode(command, nodes, cmdIndex);
            root.children.add(idx);
            for (String alias : command.getAliases()) {
                root.children.add(buildAliasNode(alias, idx, nodes));
            }
        }

        buffer.writeVarInt(nodes.size());

        for (Node node : nodes) {
            buffer.writeByte(node.flags);
            buffer.writeVarInt(node.children.size());
            for (int child : node.children) {
                buffer.writeVarInt(child);
            }
            if ((node.flags & FLAG_HAS_REDIRECT) != 0) {
                buffer.writeVarInt(node.redirectTo);
            }
            if (node.name != null) {
                buffer.writeString(node.name);
            }
        }

        buffer.writeVarInt(0);
    }

    private int buildCommandNode(AbstractCommand command, List<Node> nodes, Map<AbstractCommand, Integer> cmdIndex) {
        Node node = new Node();
        node.name = command.getName();
        node.flags = TYPE_LITERAL;

        int index = nodes.size();
        nodes.add(node);
        cmdIndex.put(command, index);

        for (AbstractCommand sub : command.getSubCommands().values()) {
            if (sub.getName() == null) continue;
            int subIdx = buildCommandNode(sub, nodes, cmdIndex);
            node.children.add(subIdx);
            for (String alias : sub.getAliases()) {
                node.children.add(buildAliasNode(alias, subIdx, nodes));
            }
        }

        if (node.children.isEmpty()) {
            node.flags |= FLAG_EXECUTABLE;
        }

        return index;
    }

    private int buildAliasNode(String alias, int redirectTo, List<Node> nodes) {
        Node node = new Node();
        node.name = alias;
        node.flags = (byte) (TYPE_LITERAL | FLAG_HAS_REDIRECT);
        node.redirectTo = redirectTo;
        int index = nodes.size();
        nodes.add(node);
        return index;
    }
}