package me.ryan.windchargemodifier.config;

import lombok.Getter;
import me.ryan.windchargemodifier.WindChargeModifier;
import me.ryan.windchargemodifier.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

@Getter
public class Config {

    private final JavaPlugin plugin;

    private float windChargeExplosionPower, windChargeSpeed;

    private boolean particlesActivated, everyoneWindCharge;

    private Particle particleType;

    private int particleAmount;

    private ItemStack windChargeItem;

    public Config() {
        this.plugin = WindChargeModifier.getInstance();
        loadConfig();
    }

    // Pegando a configuração de 'config.yml' e colocando-a em variáveis, para que você não precise acessar o arquivo 'config.yml' diretamente
    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();

        everyoneWindCharge = config.getBoolean("Wind Charge.Everyone");
        windChargeExplosionPower = (float) config.getDouble("Wind Charge.Power Multiply");
        windChargeSpeed = (float) config.getDouble("Wind Charge.Speed Multiply");

        particlesActivated = config.getBoolean("Wind Charge.Particles.Activated");
        particleType = Particle.valueOf(config.getString("Wind Charge.Particles.Type"));
        particleAmount = config.getInt("Wind Charge.Particles.Amount");

        windChargeItem = new ItemBuilder(Material.WIND_CHARGE)
                .setName(config.getString("Wind Charge.Item Stack.Name").replace('&', '§'))
                .setLore(config.getStringList("Wind Charge.Item Stack.Lore").stream().map($ -> $.replace('&', '§')).collect(Collectors.toList()))
                .setGlow(config.getBoolean("Wind Charge.Item Stack.Glow"))
                .build();
    }

    public ItemStack getWindChargeItem() {
        ItemStack item = windChargeItem.clone();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("wind_charge_modifier_item"), PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }

}
