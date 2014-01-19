/**
 * 
 */
package net.nyvaria.utils;

import org.bukkit.ChatColor;

/**
 * @author Paul Thompson, Drathus
 *
 */
public class StringUtils {

	private static enum ColorMap {
		// Colours
		BLACK       ("&0", ChatColor.BLACK        ),
		DARK_BLUE   ("&1", ChatColor.DARK_BLUE    ),
		DARK_GREEN  ("&2", ChatColor.DARK_GREEN   ),
		DARK_AQUA   ("&3", ChatColor.DARK_AQUA    ),
		DARK_RED    ("&4", ChatColor.DARK_RED     ),
		DARK_PURPLE ("&5", ChatColor.DARK_PURPLE  ),
		GOLD        ("&6", ChatColor.GOLD         ),
		GRAY        ("&7", ChatColor.GRAY         ),
		DARK_GRAY   ("&8", ChatColor.DARK_GRAY    ),
		BLUE        ("&9", ChatColor.BLUE         ),
		GREEN       ("&A", ChatColor.GREEN        ),
		AQUA        ("&B", ChatColor.AQUA         ),
		RED         ("&C", ChatColor.RED          ),
		LIGHT_PURPLE("&D", ChatColor.LIGHT_PURPLE ),
		YELLOW      ("&E", ChatColor.YELLOW       ),
		WHITE       ("&F", ChatColor.WHITE        ),
		
		// Formatting
		MAGIC       ("&K", ChatColor.MAGIC        ),
		BOLD        ("&L", ChatColor.BOLD         ),
		STRIKE      ("&M", ChatColor.STRIKETHROUGH),
		UNDERLINED  ("&N", ChatColor.UNDERLINE    ),
		ITALIC      ("&O", ChatColor.ITALIC       ),
		RESET       ("&R", ChatColor.RESET        );
		
		private final String    chatCode;
		private final ChatColor gameCode;
		
		ColorMap(String chatCode, ChatColor gameCode) {
			this.chatCode = chatCode;
			this.gameCode = gameCode;
		}
	}

	// Parse chat & codes into game ChatColor codes
	//
	// Makes two StringBuffers from message, one as passed in, the other uppercase
	// to match against the game colour codes in the enum.
	//
	// This bit of code largely based on something very very similar from Drathus
	
	public static String mapToChatColors(String text) {
		StringBuilder out      = new StringBuilder(text);
		StringBuilder outUpper = new StringBuilder(text.toUpperCase());
		
		for (ColorMap c : ColorMap.values()) {
		    int index = outUpper.indexOf(c.chatCode);
		    while (index != -1) {
		    	out     .replace(index, index + c.chatCode.length(), c.gameCode.toString());
		    	outUpper.replace(index, index + c.chatCode.length(), c.gameCode.toString());
		    	
		        index += c.gameCode.toString().length();
		        index = outUpper.indexOf(c.chatCode, index);
		    }
		}
		
		return out.toString();
	}

	public static String splitCamelCase(String text) {
	   return text.replaceAll(
	      String.format("%s|%s|%s",
	         "(?<=[A-Z])(?=[A-Z][a-z])",
	         "(?<=[^A-Z])(?=[A-Z])",
	         "(?<=[A-Za-z])(?=[^A-Za-z])"
	      ),
	      " "
	   );
	}
}
