package org.fejzu.golem.helpers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatHelper {
    public static String fix(String text) {
        if (text == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', text).replace(">>", "»").replace("<<", "«").replace("<3", "");
    }

    public static List<String> fix(List<String> raw) {
        List<String> stringList = new ArrayList<>();
        for (String string : raw) {
            stringList.add(fix(string));
        }
        return stringList;
    }

    public static void message(CommandSender sender, String type, String message) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            switch (type.toUpperCase()) {
                case "TITLE": {
                    p.sendTitle(fix(message), "");
                    break;
                }
                case "TITLE_SUBTITLE": {
                    p.sendTitle(fix(message), fix(message));
                    break;
                }
                case "SUBTITLE": {
                    p.sendTitle("", fix(message));
                    break;
                }
                case "ACTIONBAR": {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(fix(message)));
                    break;
                }
                case "CHAT": {
                    p.sendMessage(fix(message));
                    break;
                }
            }
        } else {
            sender.sendMessage(fix(message));
        }
    }

    public static String getProgressBar(int current, int max, int totalBars, String symbol, String completedColor, String notCompletedColor) {
        float percent = current / (float) max;
        int progressBars = (int) (totalBars * percent);
        int leftOver = totalBars - progressBars;
        StringBuilder sb = new StringBuilder();
        if (current > max) {
            sb.append(fix(completedColor));
            for (int i = 0; i < totalBars; ++i) {
                sb.append(fix(symbol));
            }
            return sb.toString();
        }
        sb.append(fix(completedColor));
        for (int i = 0; i < progressBars; ++i) {
            sb.append(fix(symbol));
        }
        sb.append(fix(notCompletedColor));
        for (int i = 0; i < leftOver; ++i) {
            sb.append(fix(symbol));
        }
        return sb.toString();
    }
}