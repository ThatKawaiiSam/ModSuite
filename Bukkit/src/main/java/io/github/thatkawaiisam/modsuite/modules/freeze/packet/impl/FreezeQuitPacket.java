package io.github.thatkawaiisam.modsuite.modules.freeze.packet.impl;

import io.github.thatkawaiisam.pyrite.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FreezeQuitPacket extends Packet {

    private String player;

}
