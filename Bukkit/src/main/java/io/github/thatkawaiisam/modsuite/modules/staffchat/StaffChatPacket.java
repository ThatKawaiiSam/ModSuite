package io.github.thatkawaiisam.modsuite.modules.staffchat;

import io.github.thatkawaiisam.pyrite.packet.Packet;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StaffChatPacket extends Packet {

    private String username;
    private String message;
    private String server;

}
