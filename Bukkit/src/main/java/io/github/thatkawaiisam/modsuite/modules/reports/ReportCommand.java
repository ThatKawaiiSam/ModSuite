package io.github.thatkawaiisam.modsuite.modules.reports;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Syntax;
import io.github.thatkawaiisam.artus.bukkit.BukkitCommand;
import io.github.thatkawaiisam.utils.MessageUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReportCommand extends BukkitCommand<ReportsModule> {

    /**
     * Report Command.
     *
     * @param module instance.
     */
    public ReportCommand(ReportsModule module) {
        super(module);
    }

    @Syntax("<player> <message>")
    @CommandAlias("report")
    public void onReport(Player reporter, String reported, String reason) {
        // Check for permission.
        if (!reporter.hasPermission(this.getModule().getSendPermission())) {
            reporter.sendMessage(MessageUtility.formatMessage("&cNo permission!"));
            return;
        }
        // TODO: See whether we actually want this...
        if (Bukkit.getPlayer(reported) == null) {
            reporter.sendMessage(MessageUtility.formatMessage("&cThat player is currently not online."));
            return;
        }
        this.getModule().sendMessage(reporter, Bukkit.getPlayer(reported), reason);
    }

}
