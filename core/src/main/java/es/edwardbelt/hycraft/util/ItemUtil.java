package es.edwardbelt.hycraft.util;

import com.hypixel.hytale.server.core.asset.type.item.config.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    public static List<String> hytaleItemDescToList(String description) {
        List<String> result = new ArrayList<>();

        if (description == null || description.isEmpty()) {
            return result;
        }

        String[] lines = description.split("\n");

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                result.add("");
                continue;
            }

            StringBuilder processedLine = new StringBuilder("§8");

            line = line.replace("•", "-");

            int i = 0;
            boolean isItalic = false;

            while (i < line.length()) {
                if (line.charAt(i) == '<') {
                    if (line.startsWith("<color is=\"#", i)) {
                        int endOfColorValue = line.indexOf("\">", i);
                        if (endOfColorValue != -1) {
                            String hexColor = line.substring(i + 12, endOfColorValue);
                            processedLine.append("§x");
                            for (char c : hexColor.substring(1).toCharArray()) {
                                processedLine.append("§").append(Character.toLowerCase(c));
                            }
                            if (isItalic) {
                                processedLine.append("§o");
                            }
                            i = endOfColorValue + 2;
                            continue;
                        }
                    }
                    else if (line.startsWith("</color>", i)) {
                        processedLine.append("§8");
                        if (isItalic) {
                            processedLine.append("§o");
                        }
                        i += 8;
                        continue;
                    }
                    else if (line.startsWith("<i>", i)) {
                        processedLine.append("§o");
                        isItalic = true;
                        i += 3;
                        continue;
                    }
                    else if (line.startsWith("</i>", i)) {
                        processedLine.append("§r§8");
                        isItalic = false;
                        i += 4;
                        continue;
                    }
                    else if (line.startsWith("<item is=\"", i)) {
                        int endOfTag = line.indexOf("\"", i + 10);
                        if (endOfTag != -1) {
                            String itemId = line.substring(i + 10, endOfTag);
                            Item itemConfig = getItemConfig(itemId);
                            if (itemConfig != null) {
                                String itemName = LanguageUtil.getMessage(itemConfig.getTranslationProperties().getName());
                                processedLine.append("§b").append(itemName);
                                if (isItalic) {
                                    processedLine.append("§8§o");
                                } else {
                                    processedLine.append("§8");
                                }
                            } else {
                                processedLine.append("§b[").append(itemId).append("]§8");
                            }
                            i = line.indexOf("/>", endOfTag) + 2;
                            continue;
                        }
                    }
                }

                processedLine.append(line.charAt(i));
                i++;
            }

            result.addAll(wrapLine(processedLine.toString(), 50));
        }

        return result;
    }

    public static Item getItemConfig(String itemId) {
        return Item.getAssetMap().getAsset(itemId);
    }

    private static List<String> wrapLine(String line, int maxLength) {
        List<String> wrappedLines = new ArrayList<>();

        String currentLine = line;

        while (getVisibleLength(currentLine) > maxLength) {
            int splitIndex = findSplitIndex(currentLine, maxLength);

            if (splitIndex <= 0) {
                wrappedLines.add(currentLine);
                break;
            }

            String firstPart = currentLine.substring(0, splitIndex).trim();
            wrappedLines.add(firstPart);

            String lastColor = getLastColorCode(firstPart);
            String remainder = currentLine.substring(splitIndex).trim();
            currentLine = lastColor + remainder;
        }

        if (getVisibleLength(currentLine) > 0) {
            wrappedLines.add(currentLine);
        }

        return wrappedLines;
    }

    private static int findSplitIndex(String line, int maxLength) {
        int visibleCount = 0;
        int lastSpaceIndex = -1;
        int lastSpaceVisibleCount = -1;

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '§' && i + 1 < line.length()) {
                i++;
                continue;
            }

            if (line.charAt(i) == ' ') {
                lastSpaceIndex = i;
                lastSpaceVisibleCount = visibleCount;
            }

            visibleCount++;

            if (visibleCount >= maxLength) {
                if (lastSpaceIndex > 0 && lastSpaceVisibleCount <= maxLength) {
                    return lastSpaceIndex;
                }
                return i;
            }
        }

        return -1;
    }

    private static int getVisibleLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '§' && i + 1 < text.length()) {
                i++;
                continue;
            }
            length++;
        }
        return length;
    }

    private static String getLastColorCode(String text) {
        StringBuilder colorCode = new StringBuilder("§8");
        boolean isItalic = false;

        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) == '§') {
                char code = text.charAt(i + 1);
                if (code == 'r') {
                    colorCode = new StringBuilder("§8");
                    isItalic = false;
                } else if (code == 'o') {
                    isItalic = true;
                } else if (code == 'x') {
                    colorCode = new StringBuilder("§x");
                    for (int j = 0; j < 12 && i + 2 + j < text.length(); j += 2) {
                        if (text.charAt(i + 2 + j) == '§') {
                            colorCode.append("§").append(text.charAt(i + 3 + j));
                        }
                    }
                } else if ("0123456789abcdef".indexOf(code) >= 0) {
                    colorCode = new StringBuilder("§").append(code);
                    isItalic = false;
                }
            }
        }

        if (isItalic) {
            colorCode.append("§o");
        }

        return colorCode.toString();
    }
}