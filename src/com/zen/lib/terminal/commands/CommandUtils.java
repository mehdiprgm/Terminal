package com.zen.lib.terminal.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for working with command-line style arguments.
 *
 * <p>This class provides helpers for locating and consuming specific arguments,
 * parsing a raw input line into tokens and validating argument lists against
 * size and allowed values.</p>
 *
 * @author Mehdi Lavasani (zenDEv2)
 * @version 3.0
 * @since 21
 */
public class CommandUtils {

    /**
     * Finds the value of a specific argument key and removes it from the list.
     *
     * <p>This method assumes that the argument key requires a value and that both
     * the key and its value should be removed from the provided list if found.</p>
     *
     * @param arguments the mutable list of arguments to search and modify
     * @param key       the argument key to look for (for example {@code "--name"})
     * @return the value associated with the key, or {@code null} if the key is not present
     */
    public static String findArgument(List<String> arguments, String key) {
        return findArgument(arguments, key, true, true);
    }

    /**
     * Finds the value of a specific argument key and optionally removes it from the list.
     *
     * <p>If {@code isValueRequired} is {@code true}, the method expects the key to be
     * followed by a value and returns that value when present. If no value is found,
     * {@code null} is returned.</p>
     *
     * @param arguments       the mutable list of arguments to search and modify
     * @param key             the argument key to look for
     * @param isValueRequired {@code true} if the key must be followed by a value,
     *                        {@code false} if the presence of the key alone is sufficient
     * @return the value associated with the key, an empty string if the key is present
     * and {@code isValueRequired} is {@code false}, or {@code null} if the key
     * is not found or no value is available
     */
    public static String findArgument(List<String> arguments, String key, boolean isValueRequired) {
        return findArgument(arguments, key, isValueRequired, true);
    }

    /**
     * Finds the value of a specific argument key with full control over behavior.
     *
     * <p>The method scans the argument list for the first occurrence of {@code key}.
     * When found, it may treat the next element as the value (if required) and can
     * optionally remove the key and its value from the list.</p>
     *
     * @param arguments       the mutable list of arguments to search and modify
     * @param key             the argument key to look for
     * @param isValueRequired {@code true} if the key must be followed by a value,
     *                        {@code false} to treat the key as a flag without value
     * @param removeKey       {@code true} to remove the key (and its value when required)
     *                        from the list, {@code false} to leave the list unchanged
     * @return the value associated with the key, an empty string if the key is present
     * and {@code isValueRequired} is {@code false}, or {@code null} if the key
     * is not found or if a required value is missing
     */
    public static String findArgument(List<String> arguments, String key, boolean isValueRequired, boolean removeKey) {
        String value = null;

        for (int i = 0; i < arguments.size(); i++) {
            /* Search for key */
            if (arguments.get(i).equals(key)) {
                if (isValueRequired) {
                    if (i + 1 < arguments.size()) {
                        value = arguments.get(i + 1);
                    }
                } else {
                    value = "";
                }

                if (removeKey) {
                    arguments.remove(i);

                    if (isValueRequired && i < arguments.size()) {
                        arguments.remove(i);
                    }
                }

                break;
            }
        }

        return value;
    }

    /**
     * Parses a raw input line into a list of arguments.
     *
     * <p>The parser splits the input by whitespace, supports double-quoted segments
     * as single arguments and allows escaping characters using a backslash. Quotes
     * themselves are not included in the resulting tokens.</p>
     *
     * @param input the raw input string, for example a command line
     * @return a list of parsed argument tokens; never {@code null}
     */
    public static List<String> parseArgs(String input) {
        List<String> args = new ArrayList<>();

        /* If input was empty return empty args */
        if (input == null || input.isBlank()) {
            return args;
        }

        StringBuilder current = new StringBuilder();
        /* inQuotes         -> "inside quotes" */
        /* escaping         -> previous character was '\' */
        boolean inQuotes = false, escaping = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (escaping) {
                current.append(c);
                escaping = false;

                continue;
            }

            /* Read the next character after '\' */
            if (c == '\\') {
                escaping = true;
                continue;
            }

            if (c == '"') {
                inQuotes = !inQuotes;
                continue;
            }

            if (Character.isWhitespace(c) && !inQuotes) {
                /* Add aacurrent word to args and erase the string buffer */
                if (!current.isEmpty()) {
                    args.add(current.toString());
                    current.setLength(0);
                }

                continue;
            }

            current.append(c);
        }

        if (!current.isEmpty()) {
            args.add(current.toString());
        }

        return args;
    }

    /**
     * Validates that the number of arguments does not exceed the specified limit.
     *
     * <p>If the limitArgumentsSizeValid(arguments, maxSize, "error: command max arguments is %d.\n".formatted(maxSize));
     }

     /**
     * Validates the argument list size and displays a custom error message on failure.
     *
     * <p>Checks if the provided arguments exceed a maximum count. If the limit is exceeded,
     * the specified error message is printed. A specialized message is shown if the command
     * expects no arguments at all.</p>
     *
     * @param arguments    the list of arguments to check
     * @param maxSize      the maximum allowed number of arguments
     * @param errorMessage the custom error message to display if the validation fails
     * @return {@code true} if the size is within the allowed range, {@code false} otherwise
     */
    public static boolean isArgumentsSizeValid(List<String> arguments, int maxSize, String errorMessage) {
        if (arguments.size() > maxSize) {
            if (maxSize == 0) {
                System.out.println("error: command takes no arguments");
            } else {
                System.out.println(errorMessage);
                System.out.printf("error: command max arguments is %d.\n", maxSize);
            }

            return false;
        }

        return true;
    }

    /**
     * Validates that all option-like arguments belong to a set of allowed values.
     *
     * <p>The method checks each argument that starts with {@code argumentStart}
     * (for example a dash or double dash prefix) and verifies it is contained in
     * the {@code validArgs} list.</p>
     *
     * @param arguments     the list of arguments to validate
     * @param validArgs     the collection of allowed arguments
     * @param argumentStart the prefix that identifies option-style arguments
     * @return {@code true} if all matching arguments are valid, {@code false} if any
     * unsupported argument is found
     */
    public static boolean isArgumentsValid(List<String> arguments, List<String> validArgs, String argumentStart) {
        for (String argument : arguments) {
            if (argument.startsWith(argumentStart)) {
                if (!validArgs.contains(argument)) {
                    return false;
                }
            }
        }

        return true;
    }
}