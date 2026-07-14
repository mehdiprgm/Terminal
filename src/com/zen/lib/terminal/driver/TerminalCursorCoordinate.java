package com.zen.lib.terminal.driver;

/**
 * Represents the coordinate position of the cursor within a terminal window.
 *
 * <p>This record encapsulates the horizontal (x) and vertical (y) positions,
 * typically used for cursor movement and screen rendering operations.</p>
 *
 * @author Mehdi Lavasani (zenDEv2)
 * @version 1.0
 * @since 21
 */
public record TerminalCursorCoordinate(int x, int y) {
}