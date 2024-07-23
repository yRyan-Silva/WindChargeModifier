package me.ryan.windchargemodifier.events;

import me.ryan.windchargemodifier.config.Config;
import me.ryan.windchargemodifier.utils.Services;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityKnockbackByEntityListener implements Listener {

    private final Config config;

    public EntityKnockbackByEntityListener() {
        config = Services.loadService(Config.class);
    }

    @EventHandler
    void onProjectileLaunch(EntityKnockbackByEntityEvent event) {
        Entity sourceEntity = event.getSourceEntity();

        if (sourceEntity.getType() != EntityType.WIND_CHARGE) return;

        // Se nem todas as wind charge estão ativas na 'config.yml' e nem a wind charge conter dados persistentes 'wind_charge_modifier_item' retornam
        PersistentDataContainer persistentDataContainer = sourceEntity.getPersistentDataContainer();
        if (!config.isEveryoneWindCharge() && !persistentDataContainer.getOrDefault(NamespacedKey.minecraft("wind_charge_modifier_item"),
                PersistentDataType.BOOLEAN, false))
            return;

        if (config.isParticlesActivated()) {
            World world = sourceEntity.getWorld();
            world.spawnParticle(config.getParticleType(), sourceEntity.getLocation(), config.getParticleAmount());
        }

        // Alterar a força do wind charge
        event.setFinalKnockback(event.getFinalKnockback().multiply(config.getWindChargeExplosionPower()));
    }

}
