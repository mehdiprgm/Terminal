package com.zen.lib.terminal.driver;

/**
 * Provides native access to low-level terminal operations.
 *
 * <p>This class exposes native methods to control raw input mode, clear screens,
 * position the cursor, and query the current dimensions and cursor state of the terminal.</p>
 *
 * @author Mehdi Lavasani (zenDEv2)
 * @version 1.0
 * @since 21
 */
public class TerminalDriver {

    /**
     * Enables raw mode for terminal input.
     *
     * <p>In raw mode, input characters are passed directly to the application
     * without line buffering or local echo.</p>
     */
    public static native void enableRawMode();

    /**
     * Disables raw mode and restores the terminal to its standard behavior.
     *
     * <p>Restores line-buffered input, echo, and system processing of control signals.</p>
     */
    public static native void disableRawMode();

    /**
     * Clears the entire terminal screen.
     *
     * <p>Resets the display and usually moves the cursor position to the top-left corner.</p>
     */
    public static native void clearScreen();

    /**
     * Clears the current line from the current cursor position.
     *
     * <p>Removes characters on the line containing the cursor.</p>
     */
    public static native void clearLine();

    /**
     * Moves the terminal cursor to the specified coordinates.
     *
     * @param x the target column coordinate (horizontal position)
     * @param y the target row coordinate (vertical position)
     */
    public static native void gotoxy(int x, int y);

    /**
     * Reads a single character from the terminal input buffer.
     *
     * @param echo {@code true} to print the read character to the screen,
     *             {@code false} to read the input silently
     * @return the character read from the terminal input
     */
    public static native char getch(boolean echo);

    /**
     * Retrieves the current coordinate position of the terminal cursor.
     *
     * @return the coordinates of the cursor
     */
    public static native TerminalCursorCoordinate getTerminalCursorCoordinate();

    /**
     * Retrieves the current dimensions of the terminal window.
     *
     * @return the current width and height of the terminal
     */
    public static native TerminalWindowSize getTerminalWindowSize();
}