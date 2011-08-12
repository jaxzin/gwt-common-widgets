package com.jaxzin.gwt.ui.client.table.model;

/**
 * <pre>
 * Date: Oct 3, 2006
 * Time: 2:21:36 PM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public abstract class AbstractTableModel implements TableModel {
    protected TableModelListenerCollection listeners = new TableModelListenerCollection();

    /**
     * Returns an empty string.
     * @param column
     * @return ""
     */
    public String getColumnName(int column) {
        return null;
    }

    /**
     * Returns false. This is the default implementation for all cells.
     * @param row
     * @param column
     * @return false
     */
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * This empty implementation is provided so users don't have to implement this method if their data model is not editable.
     * @param text
     * @param row
     * @param column
     */
    public void setTextAt(String text, int row, int column) {
    }

    public String getStyleName(int row, int column) {
        return "";
    }

    public String getCellWidth(int row, int column) {
        return "";
    }

    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }
}
