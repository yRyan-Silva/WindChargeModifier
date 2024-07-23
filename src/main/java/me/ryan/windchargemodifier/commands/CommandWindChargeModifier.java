package me.ryan.windchargemodifier.commands;

import me.ryan.windchargemodifier.config.Config;
import me.ryan.windchargemodifier.utils.Services;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CommandWindChargeModifier implements CommandExecutor {

    private final Config config;

    public CommandWindChargeModifier() {
        config = Services.loadService(Config.class);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] a) {
        if (!s.hasPermission("enxplugin.admin")) {
            s.sendMessage("�cVoc� n�o possui permiss�o para executar este comando.");
            return false;
        }

        if (a.length == 0) {
            s.sendMessage("�cSintaxe errada utilize, /" + label + " (reload/give) [player] [amount]");
            return false;
        }

        String subArg = a[0];
        if (subArg.equalsIgnoreCase("reload")) {
            config.reloadConfig();
            s.sendMessage("�aConfigura��o recarregada com sucesso.");
        } else if (subArg.equalsIgnoreCase("give")) {
            if (a.length <= 2) {
                s.sendMessage("�cSintaxe errada utilize, /" + label + " give (player) (amount)");
                return false;
            }

            Player target = Bukkit.getPlayer(a[1]);
            if (target == null || !target.isOnline()) {
                s.sendMessage("�cEste jogador est� off-line ou n�o existe.");
                return false;
            }

            int amount;
            try {
                amount = Integer.parseInt(a[2]);
            } catch (Exception e) {
                s.sendMessage("�cA quantidade deve ser apenas n�meros.");
                return false;
            }

            // Dando o wind charge para o jogador
            ItemStack windChargeItem = config.getWindChargeItem();
            windChargeItem.setAmount(amount);
            target.getInventory().addItem(windChargeItem);
            s.sendMessage("�aVoc� enviou �f" + amount + " �awind charges para o �f" + target.getName() + "�a.");
        }


        return true;
    }

}
