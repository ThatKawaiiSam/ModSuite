package io.github.thatkawaiisam.modsuite.modules.reports;

import io.github.thatkawaiisam.pyrite.packet.Packet;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportPacket extends Packet {

    private String reporter;
    private String reported;
    private String reason;
    private String server;

}
