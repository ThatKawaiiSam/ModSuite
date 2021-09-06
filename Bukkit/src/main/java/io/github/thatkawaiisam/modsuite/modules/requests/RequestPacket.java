package io.github.thatkawaiisam.modsuite.modules.requests;

import io.github.thatkawaiisam.pyrite.packet.Packet;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestPacket extends Packet {

    private String username;
    private String message;
    private String server;

}
