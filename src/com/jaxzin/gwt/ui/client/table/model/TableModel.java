package com.jaxzin.gwt.ui.client.table.model;

/**
 * <pre>
 * Date: Oct 3, 2006
 * Time: 2:18:03 PM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public interface TableModel extends SourcesTableModelEvents {
    int getColumnCount();
    String getColumnName(int column);
    int getRowCount();
    String getTextAt(int row, int column);
    boolean isCellEditable(int row, int column);
    void setTextAt(String text, int row, int column);

    /**
     * Returns a space-separated list of style names to apply to the given cell.
     * @param row
     * @param column
     * @return a space-separated list of style names to apply to the given cell.
     */
    String getStyleName(int row, int column);

    /**
     * Returns a valid CSS width to apply to the given cell.
     * @param row
     * @param column
     * @return A valid CSS width to apply to the given cell.
     */
    String getCellWidth(int row, int column);
}
