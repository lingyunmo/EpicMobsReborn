package cn.bzlom.epicmobs;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class EpicMobsReborn extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private FileConfiguration messages; // 语言配置
    private final Random random = new Random();

    // 核心注册表
    private static final Map<EntityType, MobDefinition> MOB_REGISTRY = new HashMap<>();

    private enum WeaponType { NONE, SWORD, BOW, GOLD_SWORD, AXE, CROSSBOW, STONE_SWORD }

    @Override
    public void onEnable() {
        // 1. 加载 Config.yml
        saveDefaultConfig();
        config = getConfig();

        // 2. 加载多语言系统
        loadLanguage();

        // 3. 初始化及注册
        initializeRegistry();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("EpicMobsReborn Loaded. Language: " + config.getString("Language"));
    }

    @Override
    public void onDisable() {
        getLogger().info("EpicMobsReborn Unloaded.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // 检查命令是否是 epicmobs
        if (command.getName().equalsIgnoreCase("epicmobs")) {

            // 检查参数是否为空
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /epicmobs reload");
                return true;
            }

            // 检查 reload 参数
            if ("reload".equalsIgnoreCase(args[0])) {
                // 检查权限
                if (!sender.hasPermission("epicmobs.reload")) {
                    sender.sendMessage(ChatColor.RED + "你没有权限执行此命令 (You don't have permission).");
                    return true;
                }

                // 执行重载
                reloadConfig();
                config = getConfig();
                loadLanguage(); // 重新加载语言
                sender.sendMessage(ChatColor.GREEN + "[EpicMobs] Configuration & Language reloaded successfully!");
                return true;
            }
        }
        return false;
    }

    // 单独封装了语言加载逻辑
    private void loadLanguage() {
        String lang = config.getString("Language", "zh_CN"); // 默认读取 zh_CN
        String fileName = "messages_" + lang + ".yml";

        File msgFile = new File(getDataFolder(), fileName);

        // 如果数据文件夹里没有这个语言文件，尝试从 jar 包里释放它
        if (!msgFile.exists()) {
            try {
                saveResource(fileName, false);
            } catch (IllegalArgumentException e) {
                // 如果 jar 包里也没有，说明用户填了个不存在的语言，回退到 zh_CN
                getLogger().warning("Language file " + fileName + " not found! Fallback to zh_CN.");
                saveResource("messages_zh_CN.yml", false);
                msgFile = new File(getDataFolder(), "messages_zh_CN.yml");
            }
        }
        messages = YamlConfiguration.loadConfiguration(msgFile);
    }

    private void initializeRegistry() {
        MOB_REGISTRY.clear();
        register(EntityType.ZOMBIE, "ZombieHealth", "zombie", WeaponType.SWORD);
        register(EntityType.SKELETON, "SkeletonHealth", "skeleton", WeaponType.BOW);
        register(EntityType.CREEPER, "CreeperHealth", "creeper", WeaponType.NONE);
        register(EntityType.SPIDER, "SpiderHealth", "spider", WeaponType.NONE);
        register(EntityType.CAVE_SPIDER, "CavespiderHealth", "cavespider", WeaponType.NONE);
        register(EntityType.WITCH, "WitchHealth", "witch", WeaponType.NONE);
        register(EntityType.SLIME, "SlimeHealth", "slime", WeaponType.NONE);
        register(EntityType.SILVERFISH, "SilverfishHealth", "silverfish", WeaponType.NONE);
        register(EntityType.HUSK, "HuskHealth", "husk", WeaponType.SWORD);
        register(EntityType.STRAY, "StrayHealth", "stray", WeaponType.BOW);
        register(EntityType.PHANTOM, "PhantomHealth", "phantom", WeaponType.NONE);
        register(EntityType.DROWNED, "DrownedHealth", "drowned", WeaponType.NONE);
        register(EntityType.GUARDIAN, "GuardianHealth", "guardian", WeaponType.NONE);
        register(EntityType.ELDER_GUARDIAN, "ElderGuardianHealth", "elder_guardian", WeaponType.NONE);
        register(EntityType.PILLAGER, "PillagerHealth", "pillager", WeaponType.CROSSBOW);
        register(EntityType.VINDICATOR, "VindicatorHealth", "vindicator", WeaponType.AXE);
        register(EntityType.EVOKER, "EvokerHealth", "evoker", WeaponType.NONE);
        register(EntityType.RAVAGER, "RavagerHealth", "ravager", WeaponType.NONE);
        register(EntityType.VEX, "VexHealth", "vex", WeaponType.SWORD);
        register(EntityType.ZOMBIFIED_PIGLIN, "ZombiePigmanHealth", "zombie_pigman", WeaponType.GOLD_SWORD);
        register(EntityType.PIGLIN, "PiglinHealth", "piglin", WeaponType.GOLD_SWORD);
        register(EntityType.PIGLIN_BRUTE, "PiglinBruteHealth", "piglin_brute", WeaponType.AXE);
        register(EntityType.HOGLIN, "HoglinHealth", "hoglin", WeaponType.NONE);
        register(EntityType.ZOGLIN, "ZoglinHealth", "zoglin", WeaponType.NONE);
        register(EntityType.WITHER_SKELETON, "WitherSkeletonHealth", "wither_skeleton", WeaponType.STONE_SWORD);
        register(EntityType.BLAZE, "BlazeHealth", "blaze", WeaponType.NONE);
        register(EntityType.GHAST, "GhastHealth", "ghast", WeaponType.NONE);
        register(EntityType.MAGMA_CUBE, "MagmaCubeHealth", "magma_cube", WeaponType.NONE);
        register(EntityType.ENDERMAN, "EndermanHealth", "enderman", WeaponType.NONE);
        register(EntityType.SHULKER, "ShulkerHealth", "shulker", WeaponType.NONE);
        register(EntityType.ENDERMITE, "EndermiteHealth", "endermite", WeaponType.NONE);
        register(EntityType.POLAR_BEAR, "PolarBearHealth", "polar_bear", WeaponType.NONE);
        register(EntityType.WOLF, "WolfHealth", "wolf", WeaponType.NONE);
        register(EntityType.BEE, "BeeHealth", "bee", WeaponType.NONE);
        register(EntityType.CHICKEN, "ChickenHealth", "chicken", WeaponType.NONE);
        register(EntityType.COW, "CowHealth", "cow", WeaponType.NONE);
        register(EntityType.SHEEP, "SheepHealth", "sheep", WeaponType.NONE);
        register(EntityType.PIG, "PigHealth", "pig", WeaponType.NONE);
        register(EntityType.HORSE, "HorseHealth", "horse", WeaponType.NONE);
    }

    private void register(EntityType type, String configKey, String msgKey, WeaponType weapon) {
        MOB_REGISTRY.put(type, new MobDefinition(configKey, msgKey, weapon));
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if (e.isCancelled()) return;

        LivingEntity entity = e.getEntity();
        MobDefinition def = MOB_REGISTRY.get(entity.getType());

        if (def == null) return;
        if (!config.getStringList("WorldName").contains(entity.getWorld().getName())) return;
        if (!config.contains(def.configKey)) return;

        int level = calculateLevel(entity.getLocation());
        double baseHealth = config.getDouble(def.configKey);

        // 血量计算公式 base + (level * multiplier)
        double multiplier = config.getDouble("HealthMultiplier", 2.0); // 默认每级加2血
        double finalHealth = baseHealth + (level * multiplier);

        applyAttributes(entity, finalHealth);
        applyName(entity, def.msgKey, level);

        if (level > 6) {
            applyEquipment(entity, def.weaponType, level);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity mob) || !(event.getDamager() instanceof Player player)) return;

        if (!MOB_REGISTRY.containsKey(mob.getType()) || !mob.isCustomNameVisible()) return;

        showHealthBar(player, mob, event.getFinalDamage());

        // 瞬移限制逻辑
        int level = calculateLevel(mob.getLocation());
        if (level > 20 && random.nextInt(100) < 30) {
            Bukkit.getScheduler().runTask(this, () -> performTeleportSkill(mob));
        }
    }

    private int calculateLevel(Location loc) {
        if (loc.getWorld() == null) return 1;
        int level = (int) (loc.distance(loc.getWorld().getSpawnLocation()) / 100.0);
        return Math.max(1, level);
    }

    private void applyAttributes(LivingEntity entity, double health) {
        AttributeInstance maxHealthAttr = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.setBaseValue(health);
            entity.setHealth(maxHealthAttr.getValue());
        }
    }

    private void showHealthBar(Player player, LivingEntity mob, double damage) {
        double maxHealth = 0;
        AttributeInstance attr = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attr != null) maxHealth = attr.getValue();
        double currentHealth = Math.max(0, mob.getHealth() - damage);

        String format = getMsg("actionbar_health")
                .replace("%current%", String.format("%.0f", currentHealth))
                .replace("%max%", String.format("%.0f", maxHealth));

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(format));
    }

    private void applyName(LivingEntity entity, String msgKey, int level) {
        String prefix = getLevelPrefix(level);
        String mobName = getMsg("mobs." + msgKey);
        // 名称格式也可以在语言文件里配置
        String format = getMsg("name_format")
                .replace("%prefix%", prefix)
                .replace("%name%", mobName);

        entity.setCustomNameVisible(true);
        entity.setCustomName(format);
    }

    private void applyEquipment(LivingEntity entity, WeaponType weaponType, int level) {
        if (entity.getEquipment() == null || weaponType == WeaponType.NONE) return;

        ItemStack weapon = null;
        int enchantLevel = Math.max(1, level - 6);

        switch (weaponType) {
            case SWORD: weapon = new ItemStack(Material.DIAMOND_SWORD); break;
            case STONE_SWORD: weapon = new ItemStack(Material.STONE_SWORD); break;
            case GOLD_SWORD: weapon = new ItemStack(Material.GOLDEN_SWORD); break;
            case AXE: weapon = new ItemStack(Material.IRON_AXE); break;
            case BOW:
                weapon = new ItemStack(Material.BOW);
                weapon.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
                break;
            case CROSSBOW:
                weapon = new ItemStack(Material.CROSSBOW);
                weapon.addUnsafeEnchantment(Enchantment.PIERCING, enchantLevel);
                return;
        }

        if (weapon != null) {
            Enchantment dmgEnchant = (weaponType == WeaponType.BOW) ? Enchantment.ARROW_DAMAGE : Enchantment.DAMAGE_ALL;
            weapon.addUnsafeEnchantment(dmgEnchant, enchantLevel);
            entity.getEquipment().setItemInMainHand(weapon);
            entity.getEquipment().setItemInMainHandDropChance(0f);
        }
    }

    private void performTeleportSkill(LivingEntity mob) {
        if (mob.isDead()) return;
        Location loc = mob.getLocation();
        Location target = loc.clone().add(2, 0, 0);
        if (target.getBlock().getType().isSolid()) target.add(0, 1, 0);

        mob.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.5, 1, 0.5, 0.1);
        mob.getWorld().spawnParticle(Particle.PORTAL, target, 15, 0.5, 1, 0.5, 0.1);
        mob.getWorld().playSound(target, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
        mob.teleport(target);
    }

    private String getLevelPrefix(int level) {
        String key;
        if (level < 10) key = "1";
        else if (level < 20) key = "10";
        else if (level < 30) key = "20";
        else if (level < 40) key = "30";
        else if (level < 50) key = "40";
        else if (level < 60) key = "50";
        else if (level < 70) key = "60";
        else if (level < 80) key = "70";
        else if (level < 90) key = "80";
        else key = "100";

        return getMsg("prefixes." + key) + level;
    }

    private String getMsg(String path) {
        if (messages.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(messages.getString(path)));
        }
        return path;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private record MobDefinition(String configKey, String msgKey, WeaponType weaponType) {}
}