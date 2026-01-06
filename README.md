# EpicMobsReborn

[简体中文](./README_CN.md) | **English**

> **A Tribute to the Classics.** A comprehensive remake of the legendary *EpicMobs* plugin, tailored for Minecraft 1.16.5.

![Java](https://img.shields.io/badge/Java-16%2B-orange) ![Platform](https://img.shields.io/badge/Platform-Paper%201.16.5-blue) ![License](https://img.shields.io/badge/License-All%20Rights%20Reserved-red)

## 📖 The Story & Acknowledgements

**History is not just about the past; it's about what we carry forward.**

The inspiration for this project comes from a plugin named **EpicMobs 1.3 Bate** (yes, "Bate", not Beta), originally completed around **August 11, 2017**.

* **Original Concept:** Jin Chen (Owner of Nanke Server)
* **Original Modifier:** Kai Wen (QQ: 2846339392)
* **Original Server:** `mc.toin.cc`

My journey began on **ChinaMC** (a Slimefun server featuring a map of China) on **August 27, 2019**. This plugin accompanied me through countless adventures. Years later, in **2023**, during the *Lom Alliance Season 3* (1.16.5 Slimefun era), this memory resurfaced.

In **2025**, when the server was revived by **cnwanchen**, I realized that while the original plugin had ceased updates nearly a decade ago, its spirit didn't have to die. With a deeper understanding of Computer Science, I decided to decompile, analyze, and completely rewrite this plugin from scratch, bringing it to the modern 1.16.5 era with clean, optimized code.

**This is not just a plugin; it's a testament to our history.**

## ✨ Features

**EpicMobsReborn** transforms your survival experience into a scaling RPG adventure. The further you travel, the harder it gets.

* **DISTANCE-BASED SCALING**: Monsters gain health based on their distance from the world spawn.
    * *Example:* A Zombie 100 blocks away is weak. A Zombie 10,000 blocks away is a raid boss.
* **RPG TITLES**: Mobs are assigned prefixes based on their level (e.g., `[Elite]`, `[Legendary]`, `[Godly]`).
* **GEAR PROGRESSION**: High-level mobs spawn with enchanted weapons (Diamond Swords, Power Bows, etc.).
* **UNIQUE SKILLS**: Elite mobs (Level 20+) have a chance to **teleport** to you when attacked, making kiting strategies much harder.
* **VISUAL FEEDBACK**: An Action Bar health indicator shows exactly how much damage you are dealing.
* **FULL CUSTOMIZATION**: 
    * Configurable base health for almost all 1.16.5 mobs (including Piglins, Hoglins, Ravagers).
    * **Multi-language Support** (English & Simplified Chinese).

## 🛠️ Installation & Compatibility

### Requirements
* **Java:** Java 16 or higher (Required for 1.16.5 Paper).
* **Server Core:** **Paper 1.16.5** is highly recommended.
    * *Compatible:* Spigot, Purpur.
    * *Incompatible:* Fabric, Forge (unless using hybrid cores like Mohist/Cardboard, but stability is not guaranteed).

### Setup
1.  Download the `.jar` file.
2.  Place it into your server's `plugins` folder.
3.  Restart the server.
4.  (Optional) Edit `plugins/EpicMobsReborn/config.yml` to adjust difficulty or switch languages.

## ⚙️ Configuration

You can switch the language in `config.yml`:
```yaml
# Language Setting
# Options: zh_CN (Simplified Chinese), en_US (English)
Language: en_US

```

To reload configuration without restarting:

```bash
/epicmobs reload
```

## 🤝 Contributing (Issues & PRs)

Although this project is **All Rights Reserved**, I welcome feedback to make it better.

* **Issues:** Please provide your server core version, Java version, and a clear description of the bug. "It doesn't work" is not a valid issue description.
* **Pull Requests:** * Code must be clean and follow Object-Oriented principles.
* No "Spaghetti Code" (nested if-else chains).
* Please explain *why* your change is necessary.

## 📄 License

**All Rights Reserved.**
You are free to use this plugin on your private or public server. However, you may **not** redistribute, sell, or claim this code as your own without explicit permission.