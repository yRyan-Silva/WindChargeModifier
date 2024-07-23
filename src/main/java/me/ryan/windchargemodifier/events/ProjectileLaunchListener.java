package me.ryan.windchargemodifier.events;

import me.ryan.windchargemodifier.config.Config;
import me.ryan.windchargemodifier.utils.Services;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

public class ProjectileLaunchListener implements Listener {

    private final Config config;

    public ProjectileLaunchListener() {
        config = Services.loadService(Config.class);
    }

    @EventHandler
    void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();

        if (!(projectile instanceof WindCharge windCharge)) return;

        ProjectileSource shooter = projectile.getShooter();
        if (!(shooter instanceof Player player)) return;

        ItemStack item = getItemInUse(player);
        PersistentDataContainer persistentDataContainer = item.getItemMeta().getPersistentDataContainer();

        // Se nem todas as wind charge estão ativas na 'config.yml' e nem a wind charge conter dados persistentes 'wind_charge_modifier_item' retornam
        NamespacedKey namespacedKey = NamespacedKey.minecraft("wind_charge_modifier_item");
        boolean itemWindCharge = persistentDataContainer.getOrDefault(namespacedKey, PersistentDataType.BOOLEAN, false);
        if (!config.isEveryoneWindCharge() && !itemWindCharge)
            return;

        // Se o item wind charge for fornecido pelo comando, defina dados persistentes para wind charge para modificar a força mais tarde
        if (itemWindCharge)
            windCharge.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BOOLEAN, true);

        // Altera a velocidade do wind charge
        windCharge.setAcceleration(windCharge.getAcceleration().multiply(config.getWindChargeSpeed()));
    }

    private ItemStack getItemInUse(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemInMainHand = playerInventory.getItemInMainHand(),
                itemInOffHand = playerInventory.getItemInOffHand();
        return itemInMainHand.getType() == Material.WIND_CHARGE ? itemInMainHand : itemInOffHand;
    }

}
