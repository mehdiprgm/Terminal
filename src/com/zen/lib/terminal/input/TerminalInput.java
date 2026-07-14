package com.zen.lib.terminal.input;

import com.zen.lib.terminal.driver.TerminalCursorCoordinate;
import com.zen.lib.terminal.driver.TerminalDriver;

/**
 * Handles advanced terminal input processing.
 *
 * <p>This class provides features such as password masking, input echoing,
 * length constraints, and support for terminal escape sequences (arrow keys,
 * home, end, delete).</p>
 *
 * @author Mehdi Lavasani (zenDEv2)
 * @version 3.0
 * @since 21
 */
public class TerminalInput {
    private boolean password;
    private boolean echo;

    public static final int KEY_TAB = 9;
    public static final int KEY_ENTER = 10;
    public static final int KEY_BACKSPACE = 127;
    public static final int KEY_ESC = 27;

    public static final char KEY_ARROW_UP = 'A';
    public static final char KEY_ARROW_DOWN = 'B';
    public static final char KEY_ARROW_RIGHT = 'C';
    public static final char KEY_ARROW_LEFT = 'D';
    public static final char KEY_DELETE = '3';

    private void redrawLine(int startX, int startY, StringBuilder result, int cursor) {
        if (!echo) {
            return;
        }

        TerminalDriver.gotoxy(startX, startY);

        if (password) {
            for (int i = 0; i < result.length(); i++) {
                System.out.print('*');
            }
        } else {
            System.out.print(result);
        }

        /* Clear from cursor to end of line */
        System.out.print("\033[K");

        /* Move cursor to logical position */
        TerminalDriver.gotoxy(startX + cursor, startY);
    }

    /**
     * Constructs a new TerminalInput with default settings.
     *
     * <p>Default: password mode disabled, echo enabled.</p>
     */
    public TerminalInput() {
        this(false, true);
    }

    /**
     * Constructs a new TerminalInput with specified password mode.
     *
     * @param password {@code true} to mask input with asterisks, {@code false} for plain text
     */
    public TerminalInput(boolean password) {
        this(password, true);
    }

    /**
     * Constructs a new TerminalInput with specified password and echo modes.
     *
     * @param password {@code true} to mask input, {@code false} for plain text
     * @param echo {@code true} to display input characters, {@code false} to suppress output
     */
    public TerminalInput(boolean password, boolean echo) {
        this.password = password;
        this.echo = echo;
    }

    /**
     * Checks if password masking is enabled.
     *
     * @return {@code true} if password mode is active
     */
    public boolean isPassword() {
        return password;
    }

    /**
     * Sets whether the input should be masked as a password.
     *
     * @param password {@code true} to enable masking
     */
    public void setPassword(boolean password) {
        this.password = password;
    }

    /**
     * Checks if character echoing is enabled.
     *
     * @return {@code true} if echo is active
     */
    public boolean isEcho() {
        return echo;
    }

    /**
     * Sets whether the input should be echoed to the terminal.
     *
     * @param echo {@code true} to enable echo
     */
    public void setEcho(boolean echo) {
        this.echo = echo;
    }

    /**
     * Reads input from the terminal with default parameters.
     *
     * @return the input string
     */
    public String read() {
        return read("", Integer.MAX_VALUE, false);
    }

    /**
     * Reads input from the terminal after displaying a message.
     *
     * @param message the prompt message to display
     * @return the input string
     */
    public String read(String message) {
        return read(message, Integer.MAX_VALUE, false);
    }

    /**
     * Reads input with a prompt message and a maximum length constraint.
     *
     * @param message the prompt message to display
     * @param maxLength the maximum number of characters allowed
     * @return the input string
     */
    public String read(String message, int maxLength) {
        return read(message, maxLength, false);
    }

    /**
     * Reads input with a prompt message, maximum length, and optional trimming.
     *
     * <p>This method processes terminal escape sequences to support navigation
     * and editing during input.</p>
     *
     * @param message the prompt message to display
     * @param maxLength the maximum number of characters allowed
     * @param trim {@code true} to trim leading and trailing whitespace from the result
     * @return the processed input string
     * @throws IllegalArgumentException if maxLength is negative
     */
    public String read(String message, int maxLength, boolean trim) {
        StringBuilder result = new StringBuilder();
        int cursor = 0;

        if (maxLength < 0) {
            throw new IllegalArgumentException("maxLength cannot be negative");
        }

        System.out.print(message);
        TerminalCursorCoordinate start = TerminalDriver.getTerminalCursorCoordinate();

        int startX = start.x();
        int startY = start.y();

        while (true) {
            char ch = TerminalDriver.getch(false);

            if (ch == KEY_ENTER) {
                break;
            }

            if (ch == KEY_TAB) {
                /* Check current entered text length and calculate remain spaces to prevent overflow */
                int count = Math.min(4, maxLength - result.length());

                for (int i = 0; i < count; i++) {
                    result.insert(cursor, ' ');
                    cursor++;
                }

                redrawLine(startX, startY, result, cursor);
                continue;
            }

            if (ch == KEY_BACKSPACE) {
                if (cursor > 0) {
                    result.deleteCharAt(cursor - 1);
                    cursor--;

                    redrawLine(startX, startY, result, cursor);
                }

                continue;
            }

            if (ch == KEY_ESC) {
                char c1 = TerminalDriver.getch(false);

                /*
                 * CSI sequence: ESC [
                 * SS3 sequence: ESC O   (sometimes Home/End)
                 */
                if (c1 == '[') {
                    char c2 = TerminalDriver.getch(false);

                    switch (c2) {
                        case KEY_ARROW_UP, KEY_ARROW_DOWN:
                            /* Do nothing */
                            break;

                        case KEY_ARROW_RIGHT:
                            /* If we are not at the end of string move cursor to right */
                            if (cursor < result.length()) {
                                cursor++;

                                if (echo) {
                                    TerminalDriver.gotoxy(startX + cursor, startY);
                                }
                            }
                            break;

                        case KEY_ARROW_LEFT:
                            /* If we are not at the beginning of string move cursor to left */
                            if (cursor > 0) {
                                cursor--;

                                if (echo) {
                                    TerminalDriver.gotoxy(startX + cursor, startY);
                                }
                            }

                            break;

                        case 'H':
                            /* Home */
                            cursor = 0;

                            if (echo) {
                                TerminalDriver.gotoxy(startX + cursor, startY);
                            }

                            break;

                        case 'F':
                            /* End */
                            cursor = result.length();

                            if (echo) {
                                TerminalDriver.gotoxy(startX + cursor, startY);
                            }

                            break;

                        case KEY_DELETE:
                            /* Usually Delete comes as ESC [ 3 ~ */
                            char c3 = TerminalDriver.getch(false);

                            /* Delete key pressed */
                            if (c3 == '~') {
                                /* Check to see if we are not at the end */
                                if (cursor < result.length()) {
                                    result.deleteCharAt(cursor);
                                    redrawLine(startX, startY, result, cursor);
                                }
                            }

                            break;

                        case '1':
                        case '7':
                            /* This is home key */
                            char c3home = TerminalDriver.getch(false);
                            if (c3home == '~') {
                                cursor = 0;

                                if (echo) {
                                    TerminalDriver.gotoxy(startX + cursor, startY);
                                }
                            }

                            break;

                        case '4':
                        case '8':
                            /* This is end key */
                            char c3end = TerminalDriver.getch(false);
                            if (c3end == '~') {
                                cursor = result.length();

                                if (echo) {
                                    TerminalDriver.gotoxy(startX + cursor, startY);
                                }
                            }

                            break;

                        default:
                            break;
                    }
                } else if (c1 == 'O') {
                    char c2 = TerminalDriver.getch(false);

                    /* Again check for home key and end key */
                    switch (c2) {
                        case 'H':
                            cursor = 0;

                            if (echo) {
                                TerminalDriver.gotoxy(startX + cursor, startY);
                            }

                            break;

                        case 'F':
                            cursor = result.length();

                            if (echo) {
                                TerminalDriver.gotoxy(startX + cursor, startY);
                            }
                            break;

                        default:
                            break;
                    }
                }

                continue;
            }

            /* Check to see if we are reached the max allowed length of input text */
            if (result.length() >= maxLength) {
                continue;
            }

            /* Only normal and printable characters are allowed to put in to the text */
            if (!Character.isISOControl(ch)) {
                result.insert(cursor, ch);
                cursor++;

                redrawLine(startX, startY, result, cursor);
            }
        }

        System.out.println();
        return trim ? result.toString().trim() : result.toString();
    }
}
