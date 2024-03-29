package me.earth.earthhack.impl.modules.client.server.protocol.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import me.earth.earthhack.impl.modules.client.server.api.IConnection;
import me.earth.earthhack.impl.modules.client.server.api.IConnectionManager;
import me.earth.earthhack.impl.modules.client.server.api.IPacketHandler;
import me.earth.earthhack.impl.modules.client.server.protocol.ProtocolUtil;

public class PrivateMessageServer
implements IPacketHandler {
    private final IConnectionManager manager;

    public PrivateMessageServer(IConnectionManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(IConnection connection, byte[] bytes) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int id = buffer.getInt();
        IConnection target = this.manager.getConnections().stream().filter(c -> c != null && c.getId() == id).findFirst().orElse(null);
        if (target != null) {
            byte[] packet = new byte[bytes.length + 12];
            ProtocolUtil.addInt(5, packet);
            ProtocolUtil.addInt(bytes.length + 4, packet, 4);
            ProtocolUtil.addInt(connection.getId(), bytes, 8);
            ProtocolUtil.addAllBytes(bytes, packet, 12);
            target.send(packet);
        }
    }
}
