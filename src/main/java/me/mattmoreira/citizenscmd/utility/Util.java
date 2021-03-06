/**
 * CitizensCMD - Add-on for Citizens
 * Copyright (C) 2018 Mateus Moreira
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.mattmoreira.citizenscmd.utility;

import me.mattmoreira.citizenscmd.CitizensCMD;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Util {

    /**
     * String with CitizensCMD default header and tag
     */
    public static final String HEADER = "&c&m-&6&m-&e&m-&a&m-&b&m-&3&l CitizensCMD &b&m-&a&m-&e&m-&6&m-&c&m-";
    public static final String TAG = "&f[&3Citizens&cCMD&f]&r ";

    /**
     * @param str String to check if it is a number or not
     * @return Returns true if it is a number false if it is a string or contains any non numeric character
     */
    public static boolean notInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException | NullPointerException e) {
            return true;
        }
        return false;
    }

    /**
     * @param str String to check if it is a double number or not
     * @return Returns true if it is a number false if it is a string or contains any non numeric character
     */
    public static boolean notDouble(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException | NullPointerException e) {
            return true;
        }
        return false;
    }

    /**
     * @param str String to check if it is a float number or not
     * @return Returns true if it is a number false if it is a string or contains any non numeric character
     */
    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if player has or not selected an NPC
     *
     * @param player The player to check if it has any NPC selected or not
     * @return Returns true if has an NPC selected and false if not
     */
    public static boolean npcNotSelected(CitizensCMD plugin, Player player) {
        if (CitizensAPI.getDefaultNPCSelector().getSelected(player) != null) return false;

        player.sendMessage(color(HEADER));
        player.sendMessage(plugin.getLang().getMessage(Path.NO_NPC));
        return true;
    }

    /**
     * Checks if player has or not selected an NPC
     *
     * @param player The player to check if it has any NPC selected or not
     * @return Returns true if has an NPC selected and false if not
     */
    public static boolean npcNotSelectedNM(Player player) {
        return CitizensAPI.getDefaultNPCSelector().getSelected(player) == null;
    }

    /**
     * Gets the NPC id
     *
     * @param player To get the id of the NPC the player has selected
     * @return Returns the id of the NPC
     */
    public static int getSelectedNpcId(Player player) {
        return CitizensAPI.getDefaultNPCSelector().getSelected(player).getId();
    }

    /**
     * Checks whether or not it should check for updates
     *
     * @return Returns true if CheckUpdates is true on the config and false if not
     */
    public static boolean upCheck(CitizensCMD plugin) {
        return plugin.getConfig().getBoolean("check-updates");
    }

    /**
     * Utility to use color codes easierly
     *
     * @param msg The message String
     * @return returns the string with color
     */
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * Simplified way for sending console messages
     *
     * @param msg the message to be sent to the console
     */
    public static void info(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(msg);
    }

    /**
     * Gets default cooldown set on the config
     *
     * @return returns the seconds from the config
     */
    public static int getDefaultCooldown(CitizensCMD plugin) {
        return plugin.getConfig().getInt("default-cooldown");
    }

    /**
     * Gets arguments from each command for the tab completion
     *
     * @return Returns 2d string array with arguments for tab completion
     */
    public static String[][] getTabCompleteArgs(CitizensCMD plugin, String subCMD, Player player) {
        String[][] argComplete = new String[5][];

        switch (subCMD.toLowerCase()) {
            case "add":
                argComplete[0] = new String[]{"console", "none", "permission", "server", "message"};
                break;
            case "remove":
                argComplete[0] = new String[]{"left", "right"};
                argComplete[1] = plugin.getDataHandler().getCompleteCommandsNumbers(getSelectedNpcId(player), EnumTypes.ClickType.LEFT);
                argComplete[2] = plugin.getDataHandler().getCompleteCommandsNumbers(getSelectedNpcId(player), EnumTypes.ClickType.RIGHT);
                break;
            case "edit":
                argComplete[0] = new String[]{"perm", "cmd"};
                argComplete[1] = new String[]{"left", "right"};
                argComplete[2] = plugin.getDataHandler().getCompleteCommandsNumbers(getSelectedNpcId(player), EnumTypes.ClickType.LEFT);
                argComplete[3] = plugin.getDataHandler().getCompleteCommandsNumbers(getSelectedNpcId(player), EnumTypes.ClickType.RIGHT);
                argComplete[4] = new String[]{"console", "none", "permission", "server", "message"};
                break;

            case "sound":
                argComplete[0] = getSoundsList();
                break;
        }
        return argComplete;
    }

    private static String[] getSoundsList() {
        Sound[] sounds = Sound.values();
        String[] soundString = new String[sounds.length];

        for (int i = 0; i < sounds.length; i++) {
            soundString[i] = sounds[i].name();
        }

        return soundString;
    }

    /**
     * Gets the difference in seconds between times
     *
     * @param storedTime the stored time to compare
     * @return returns the difference in seconds
     */
    public static long getSecondsDifference(long storedTime) {
        return TimeUnit.SECONDS.convert((System.currentTimeMillis() - storedTime), TimeUnit.MILLISECONDS);
    }

    /**
     * Checks for old config and renames it
     */
    public static void checkOldConfig(CitizensCMD plugin) {
        File configFile;
        File configFileNew;
        FileConfiguration configConf;

        boolean isNew = true;

        boolean contains[] = new boolean[5];
        for (int i = 0; i < contains.length; i++) {
            contains[i] = false;
        }

        try {
            configFile = new File(plugin.getDataFolder(), "config.yml");
            configFileNew = new File(plugin.getDataFolder(), "config_old.yml");
            configConf = new YamlConfiguration();

            if (configFile.exists()) {
                configConf.load(configFile);
                if (configConf.contains("check-updates")) contains[0] = true;
                if (configConf.contains("lang")) contains[1] = true;
                if (configConf.contains("default-cooldown")) contains[2] = true;
                if (configConf.contains("shift-confirm")) contains[3] = true;
                if (configConf.contains("cooldown-time-display")) contains[4] = true;
            }

            for (boolean bool : contains) {
                if (!bool) {
                    isNew = false;
                }
            }

            if (!isNew) {
                configFile.renameTo(configFileNew);
            }

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void disablePlugin(CitizensCMD plugin) {
        info(color(TAG + "&cCitizens &7is needed for this plugin to work!"));
        info(color(TAG + "&cCitizens.jar &7is not installed on the server!"));
        info(color(TAG + "&cDisabling CitizensCMD..."));
        Bukkit.getServer().getPluginManager().disablePlugin(plugin);
    }

    public static boolean doesCitizensExist() {
        return Bukkit.getPluginManager().isPluginEnabled("Citizens");
    }
}
