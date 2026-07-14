package com.zen.lib.terminal.table;

import com.zen.lib.terminal.driver.TerminalDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a text-based table for terminal output.
 *
 * <p>This class stores table headers and rows, calculates column widths,
 * and renders the table based on the current terminal width.</p>
 *
 * @author Mehdi Lavasani (zenDEv2)
 * @version 5.0
 * @since 21
 */
public class Table {
    private List<String> headers;
    private List<TableRow> rows;

    private int columnMaxSize;

    /**
     * Pads the given text with spaces until it reaches the specified width.
     *
     * @param text the text to pad
     * @param width the target width
     * @return the padded text
     */
    private String setPadding(String text, int width) {
        StringBuilder sb = new StringBuilder(text);

        /* Fill the remaining space with spaces */
        while (sb.length() < width) {
            sb.append(" ");
        }

        return sb.toString();
    }

    /**
     * Truncates the given text if it exceeds the specified width.
     *
     * <p>If the width is greater than 3, the truncated text ends with ellipsis.</p>
     *
     * @param text the text to truncate
     * @param width the maximum allowed width
     * @return the truncated text
     */
    private String truncate(String text, int width) {
        if (text.length() <= width) {
            return text;
        }

        if (width <= 3) {
            return text.substring(0, width);
        }

        return text.substring(0, width - 3) + "...";
    }

    /**
     * Constructs a new table with the specified maximum column size.
     *
     * @param columnMaxSize the maximum width allowed for each column
     */
    public Table(int columnMaxSize) {
        headers = new ArrayList<>();
        rows = new ArrayList<>();

        this.columnMaxSize = columnMaxSize;
    }

    /**
     * Returns the table headers.
     *
     * @return the list of header values
     */
    public List<String> getHeaders() {
        return headers;
    }

    /**
     * Sets the table headers.
     *
     * @param headers the header values to set
     */
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    /**
     * Returns the table rows.
     *
     * @return the list of table rows
     */
    public List<TableRow> getRows() {
        return rows;
    }

    /**
     * Sets the table rows.
     *
     * @param rows the rows to set
     */
    public void setRows(List<TableRow> rows) {
        this.rows = rows;
    }

    /**
     * Returns the maximum allowed column size.
     *
     * @return the maximum column width
     */
    public int getColumnMaxSize() {
        return columnMaxSize;
    }

    /**
     * Sets the maximum allowed column size.
     *
     * @param columnMaxSize the maximum column width to set
     */
    public void setColumnMaxSize(int columnMaxSize) {
        this.columnMaxSize = columnMaxSize;
    }

    /**
     * Renders the table as a formatted string.
     *
     * <p>This method calculates column widths from headers and rows, adjusts them
     * to fit the terminal width, and returns the final table output.</p>
     *
     * @return the rendered table as a string
     */
    public String render() {
        int cols = headers.size();
        int[] widths = new int[cols];

        int terminalWidth = TerminalDriver.getTerminalWindowSize().width();
        int separatorWidth = (cols - 1) * 2;

        /* Calculate the column widths from headers */
        for (int i = 0; i < cols; i++) {
            widths[i] = headers.get(i).length();
        }

        /* Calculate the column widths from rows */
        for (TableRow row : rows) {
            List<Object> items = row.getItems();

            for (int i = 0; i < items.size() && i < cols; i++) {
                Object cell = items.get(i);
                int len = cell == null ? 0 : String.valueOf(cell).length();

                if (len > widths[i]) {
                    widths[i] = len;
                }
            }
        }

        if (columnMaxSize > 0) {
            for (int i = 0; i < widths.length; i++) {
                widths[i] = Math.min(widths[i], columnMaxSize);
            }
        }

        int totalWidth = separatorWidth;

        /* Sum of all cell widths */
        for (int w : widths) {
            totalWidth += w;
        }

        if (totalWidth > terminalWidth) {
            int excess = totalWidth - terminalWidth;

            while (excess > 0) {
                boolean reduced = false;

                for (int i = 0; i < cols && excess > 0; i++) {
                    if (widths[i] > 4) {
                        widths[i]--;
                        excess--;

                        reduced = true;
                    }
                }

                if (!reduced) {
                    break;
                }
            }
        }

        StringBuilder headerLine = new StringBuilder();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < cols; i++) {
            String value = truncate(headers.get(i), widths[i]);
            headerLine.append(setPadding(value, widths[i]));

            /* Check to see if this is the last column or not */
            if (i < cols - 1) {
                headerLine.append("  ");
            }
        }

        output.append(headerLine).append("\n");
        output.repeat("-", headerLine.length()).append("\n");

        for (TableRow row : rows) {
            StringBuilder line = new StringBuilder();
            List<Object> items = row.getItems();

            for (int i = 0; i < cols; i++) {
                String value = "";

                if (i < items.size() && items.get(i) != null) {
                    value = String.valueOf(items.get(i));
                }

                value = truncate(value, widths[i]);
                line.append(setPadding(value, widths[i]));

                if (i < cols - 1) {
                    line.append("  ");
                }
            }

            output.append(line).append("\n");
        }

        return output.toString();
    }
}