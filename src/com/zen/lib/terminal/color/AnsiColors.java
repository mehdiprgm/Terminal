package com.zen.lib.terminal.color;

/**
 * Provides a collection of ANSI escape code constants for coloring and styling
 * text output in terminal environments that support ANSI sequences.
 *
 * <p>This class exposes foreground and background color codes in normal,
 * bright, bold and underlined variants, along with a reset code to restore
 * the default terminal style.</p>
 *
 * @author Mehdi Lavasani (zenDEv2)
 * @since 21
 * @version 1.0
 */
public class AnsiColors {
    public static final String BG_BLACK = "\u001b[40m";
    public static final String BG_RED = "\u001b[41m";
    public static final String BG_GREEN = "\u001b[42m";
    public static final String BG_YELLOW = "\u001b[43m";
    public static final String BG_BLUE = "\u001b[44m";
    public static final String BG_PURPLE = "\u001b[45m";
    public static final String BG_CYAN = "\u001b[46m";
    public static final String BG_WHITE = "\u001b[47m";
    public static final String BG_BLACK_BRIGHT = "\u001b[0;100m";
    public static final String BG_RED_BRIGHT = "\u001b[0;101m";
    public static final String BG_GREEN_BRIGHT = "\u001b[0;102m";
    public static final String BG_YELLOW_BRIGHT = "\u001b[0;103m";
    public static final String BG_BLUE_BRIGHT = "\u001b[0;104m";
    public static final String BG_PURPLE_BRIGHT = "\u001b[0;105m";
    public static final String BG_CYAN_BRIGHT = "\u001b[0;106m";
    public static final String BG_WHITE_BRIGHT = "\u001b[0;107m";

    public static final String FR_RESET = "\u001b[0m";

    public static final String FR_BLACK = "\u001b[0;30m";
    public static final String FR_RED = "\u001b[0;31m";
    public static final String FR_GREEN = "\u001b[0;32m";
    public static final String FR_YELLOW = "\u001b[0;33m";
    public static final String FR_BLUE = "\u001b[0;34m";
    public static final String FR_PURPLE = "\u001b[0;35m";
    public static final String FR_CYAN = "\u001b[0;36m";
    public static final String FR_WHITE = "\u001b[0;37m";
    public static final String FR_BLACK_BOLD = "\u001b[1;30m";
    public static final String FR_RED_BOLD = "\u001b[1;31m";
    public static final String FR_GREEN_BOLD = "\u001b[1;32m";
    public static final String FR_YELLOW_BOLD = "\u001b[1;33m";
    public static final String FR_BLUE_BOLD = "\u001b[1;34m";
    public static final String FR_PURPLE_BOLD = "\u001b[1;35m";
    public static final String FR_CYAN_BOLD = "\u001b[1;36m";
    public static final String FR_WHITE_BOLD = "\u001b[1;37m";

    public static final String FR_BLACK_UNDERLINED = "\u001b[4;30m";
    public static final String FR_RED_UNDERLINED = "\u001b[4;31m";
    public static final String FR_GREEN_UNDERLINED = "\u001b[4;32m";
    public static final String FR_YELLOW_UNDERLINED = "\u001b[4;33m";
    public static final String FR_BLUE_UNDERLINED = "\u001b[4;34m";
    public static final String FR_PURPLE_UNDERLINED = "\u001b[4;35m";
    public static final String FR_CYAN_UNDERLINED = "\u001b[4;36m";
    public static final String FR_WHITE_UNDERLINED = "\u001b[4;37m";

    public static final String FR_BLACK_BRIGHT = "\u001b[0;90m";
    public static final String FR_RED_BRIGHT = "\u001b[0;91m";
    public static final String FR_GREEN_BRIGHT = "\u001b[0;92m";
    public static final String FR_YELLOW_BRIGHT = "\u001b[0;93m";
    public static final String FR_BLUE_BRIGHT = "\u001b[0;94m";
    public static final String FR_PURPLE_BRIGHT = "\u001b[0;95m";
    public static final String FR_CYAN_BRIGHT = "\u001b[0;96m";
    public static final String FR_WHITE_BRIGHT = "\u001b[0;97m";

    public static final String FR_BLACK_BOLD_BRIGHT = "\u001b[1;90m";
    public static final String FR_RED_BOLD_BRIGHT = "\u001b[1;91m";
    public static final String FR_GREEN_BOLD_BRIGHT = "\u001b[1;92m";
    public static final String FR_YELLOW_BOLD_BRIGHT = "\u001b[1;93m";
    public static final String FR_BLUE_BOLD_BRIGHT = "\u001b[1;94m";
    public static final String FR_PURPLE_BOLD_BRIGHT = "\u001b[1;95m";
    public static final String FR_CYAN_BOLD_BRIGHT = "\u001b[1;96m";
    public static final String FR_WHITE_BOLD_BRIGHT = "\u001b[1;97m";
}
