package com.zen.lib.terminal;

import com.zen.lib.terminal.driver.TerminalDriver;

import java.util.List;
import java.util.Scanner;

/**
 * Provides utility methods for terminal input and output.
 *
 * <p>This class includes helpers for printing values, showing colored messages,
 * clearing the terminal, waiting for user input, and asking simple interactive
 * questions.</p>
 *
 * @author Mehdi Lavasani (zenDEv2)
 * @version 10
 * @since 21
 */
public class Terminal {

    static {
        System.loadLibrary("zenterminal");
    }

    /**
     * Defines supported terminal message types.
     *
     * @author Mehdi Lavasani (zenDEv2)
     * @since 21
     */
    public enum MessageType {
        ERROR("\u001b[0;91m"),
        WARNING("\u001b[0;93m"),
        SUCCESS("\u001b[0;92m"),
        INFORMATION("\u001b[0;96m");

        private final String color;

        MessageType(String color) {
            this.color = color;
        }

        /**
         * Returns the ANSI color code of this message type.
         *
         * @return the ANSI color code
         */
        public String color() {
            return color;
        }
    }

    /**
     * Prints the given character repeatedly.
     *
     * @param ch the character to print
     * @param length the number of times to print the character
     * @param nextLine {@code true} to print a newline after the characters
     */
    public static void printCharacters(char ch, int length, boolean nextLine) {
        for (int i = 0; i < length; i++) {
            System.out.print(ch);
        }

        if (nextLine) {
            System.out.println();
        }
    }

    /**
     * Prints the given objects without appending a newline.
     *
     * @param objects the values to print
     */
    public static void print(Object... objects) {
        for (Object object : objects) {
            System.out.print(object);
        }
    }

    /**
     * Prints the given objects and appends a newline.
     *
     * @param objects the values to print
     */
    public static void println(Object... objects) {
        for (Object object : objects) {
            System.out.print(object);
        }

        System.out.println();
    }

    /**
     * Prints formatted text to the terminal.
     *
     * @param format the format string
     * @param args the values referenced by the format specifiers
     */
    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    /**
     * Prints the items of a list.
     *
     * @param list the list to print
     * @param nextLine {@code true} to print each item on a new line
     * @param showNumbers {@code true} to print item numbers before each value
     * @param <T> the item type
     */
    public static <T> void printList(List<T> list, boolean nextLine, boolean showNumbers) {
        for (int i = 0; i < list.size(); i++) {
            if (showNumbers) {
                System.out.printf("[%d] ", i + 1);
            }

            System.out.print(list.get(i));
            if (nextLine) {
                System.out.println();
            }
        }
    }

    /**
     * Prints the given objects using the color of the specified message type.
     *
     * @param type the message type that defines the output color
     * @param objects the values to print
     */
    public static void printMessage(MessageType type, Object... objects) {
        for (Object object : objects) {
            System.out.printf("%s%s%s", type.color(), object, "\u001b[0m");
        }
    }

    /**
     * Waits until the user presses Enter using the default message.
     */
    public static void pressEnter() {
        pressEnter("Press enter to continue!...");
    }

    /**
     * Waits until the user presses Enter.
     *
     * @param message the message shown before waiting for input
     */
    public static void pressEnter(String message) {
        System.out.print(message);
        new Scanner(System.in).nextLine();
    }

    /**
     * Waits until the user presses a key using the default message.
     *
     * @return the pressed character
     */
    public static char pressAnyKey() {
        return pressAnyKey("Press any key to continue!... ");
    }

    /**
     * Waits until the user presses a key.
     *
     * @param message the message shown before waiting for input
     * @return the pressed character
     */
    public static char pressAnyKey(String message) {
        char ch;

        System.out.print(message);
        ch = TerminalDriver.getch(false);

        System.out.println();
        return ch;
    }

    /**
     * Clears the terminal screen.
     */
    public static void clearScreen() {
        TerminalDriver.clearScreen();
    }

    /**
     * Clears the current terminal line.
     */
    public static void clearLine() {
        TerminalDriver.clearLine();
    }

    /**
     * Asks the user for a yes or no answer.
     *
     * <p>The method reads a single character and returns {@code true} for
     * {@code y} and {@code false} for {@code n}. If repetition is enabled,
     * the prompt is shown again until a valid answer is entered.</p>
     *
     * @param message the prompt message
     * @param repeat {@code true} to repeat until a valid answer is entered
     * @return {@code true} if the user answers yes; otherwise {@code false}
     */
    public static boolean sure(String message, boolean repeat) {
        char ch;

        do {
            if (!message.isEmpty() && !message.isBlank()) {
                if (!message.toUpperCase().endsWith("[Y/N]: ")) {
                    System.out.printf("%s [Y/N]: ", message.trim());
                } else {
                    System.out.printf("%s: ", message.trim());
                }
            }

            ch = Character.toLowerCase(TerminalDriver.getch(true));
            System.out.println();

            if (ch == 'y') {
                return true;
            } else if (ch == 'n') {
                return false;
            }
        } while (repeat);

        return false;
    }

    /**
     * Asks the user to choose one of the given values.
     *
     * <p>The method prints the available choices and returns the index of the
     * matched value. If repetition is enabled, the prompt is shown again until
     * a valid answer is entered.</p>
     *
     * @param message the prompt message
     * @param repeat {@code true} to repeat until a valid answer is entered
     * @param objects the allowed answer values
     * @return the index of the selected value, or {@code -1} if no valid answer is matched
     */
    public static int ask(String message, boolean repeat, Object... objects) {
        StringBuilder sb = new StringBuilder("[");
        String answer;

        for (int i = 0; i < objects.length; i++) {
            sb.append(objects[i]);

            if (i + 1 < objects.length) {
                sb.append(", ");
            }
        }

        sb.append("]: ");

        do {
            System.out.printf("%s %s", message, sb);
            answer = new Scanner(System.in).nextLine();

            for (int i = 0; i < objects.length; i++) {
                if (answer.equals(objects[i])) {
                    return i;
                }
            }
        } while (repeat);

        return -1;
    }
}