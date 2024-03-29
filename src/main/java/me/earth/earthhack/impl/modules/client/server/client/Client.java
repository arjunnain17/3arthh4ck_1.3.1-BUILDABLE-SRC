package me.earth.earthhack.impl.modules.client.server.client;

import java.io.DataInputStream;
import java.io.IOException;
import me.earth.earthhack.impl.modules.client.server.api.AbstractConnection;
import me.earth.earthhack.impl.modules.client.server.api.IClient;
import me.earth.earthhack.impl.modules.client.server.api.IPacket;
import me.earth.earthhack.impl.modules.client.server.api.IPacketManager;
import me.earth.earthhack.impl.modules.client.server.api.IServerList;
import me.earth.earthhack.impl.modules.client.server.protocol.ProtocolUtil;
import me.earth.earthhack.impl.util.thread.SafeRunnable;

public final class Client
extends AbstractConnection
implements SafeRunnable,
IClient {
    private final IPacketManager manager;
    private final IServerList serverList;

    public Client(IPacketManager manager, IServerList serverList, String ip, int port) throws IOException {
        super(ip, port);
        this.manager = manager;
        this.serverList = serverList;
    }

    @Override
    public void runSafely() throws Throwable {
        try (DataInputStream in = new DataInputStream(this.getInputStream());){
            while (this.isOpen()) {
                IPacket packet = ProtocolUtil.readPacket(in);
                this.manager.handle(this, packet.getId(), packet.getBuffer());
            }
        }
    }

    @Override
    public void handle(Throwable t) {
        t.printStackTrace();
        this.close();
    }

    @Override
    public IServerList getServerList() {
        return this.serverList;
    }
}
