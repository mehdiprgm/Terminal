package com.zen.lib.terminal.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single row in a terminal table.
 *
 * <p>This class stores the cell values of one table row as a list of objects.</p>
 *
 * @author Mehdi Lavasani (zenDEv2)
 * @version 1.0
 * @since 21
 */
public class TableRow {
    private List<Object> items;

    /**
     * Constructs an empty table row.
     */
    public TableRow() {
        items = new ArrayList<>();
    }

    /**
     * Constructs a table row with the specified cell values.
     *
     * @param objects the values to add to the row
     */
    public TableRow(Object... objects) {
        this();
        Collections.addAll(items, objects);
    }

    /**
     * Returns the items stored in this row.
     *
     * @return the row items
     */
    public List<Object> getItems() {
        return items;
    }

    /**
     * Sets the items of this row.
     *
     * @param items the row items to set
     */
    public void setItems(List<Object> items) {
        this.items = items;
    }
}