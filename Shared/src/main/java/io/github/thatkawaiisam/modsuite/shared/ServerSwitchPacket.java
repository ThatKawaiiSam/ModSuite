package io.github.thatkawaiisam.modsuite.shared;

import io.github.thatkawaiisam.pyrite.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerSwitchPacket extends Packet {

    private String username;
    private String currentServer;
    private String previousServer;

}
