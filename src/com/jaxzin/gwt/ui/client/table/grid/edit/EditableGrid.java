package com.jaxzin.gwt.ui.client.table.grid.edit;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import com.jaxzin.gwt.ui.client.table.model.TableModel;
import com.jaxzin.gwt.ui.client.table.model.TableModelEvent;
import com.jaxzin.gwt.ui.client.table.model.TableModelListener;

/**
 * <pre>
 * Date: Oct 2, 2006
 * Time: 3:29:39 PM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public class EditableGrid extends Grid implements TableModelListener {

    private TableModel tableModel;

    private TextBox entryBox;
    private int selectedRow = -1, selectedCell = -1;

    public EditableGrid(final TableModel tableModel) {
        this.tableModel = tableModel;
        this.tableModel.addTableModelListener(this);
        resize(tableModel.getRowCount(), tableModel.getColumnCount());
        redraw();
        addStyleName("cw-EditableGrid");

        setupEntryBox();


        selectFirstCell();
    }

    private void setupEntryBox() {
        entryBox = new TextBox();

        // Setup style, including defaults
        entryBox.addStyleName("cw-EditableGrid-cell-TextBox");
        entryBox.setWidth("100%");
        entryBox.setHeight("100%");
        Element e = entryBox.getElement();
        DOM.setStyleAttribute(e, "padding", "0px");
        DOM.setStyleAttribute(e, "margin", "0px 0px 0px -1px");

        final GridNavigator navigator = new GridNavigator();
        addTableListener(navigator);
        entryBox.addKeyboardListener(navigator);

//        entryBox.addFocusListener(
//                new FocusListenerAdapter() {
//                    public void onLostFocus(Widget sender) {
//                        // Place entry box in first cell of table so when editablegrid gains focus first cell is editable
//                        selectFirstCell();
//                    }
//                }
//        );

    }


    private void selectFirstCell() {
        selectNextCell(0, 0, NEXT_INCLUSIVE);
    }

    /**
     *
     * @return truth there is a next cell
     */
    private boolean selectNextCell(int row, int cell, SearchDirection direction) {
        boolean success;
        unselectCell();
        int[] coords = findEditableCell(row, cell, direction);
        success = coords[0] != -1 && coords[1] != -1;

        if(success) {
            selectCell(coords[0], coords[1]);
        } else if(row != 0 && cell != 0 && !direction.equals(NEXT_INCLUSIVE)) {
            selectFirstCell();
        }
        return success;
    }

    private int[] findEditableCell(final int row, final int cell, SearchDirection direction) {
        int[] coords = new int[]{row,cell};
        direction.search(coords);
        return coords;
    }

    private void selectCell(final int row, final int cell) {
        // If the given cell isn't already selected
        if(row != selectedRow || cell != selectedCell) {
            unselectCell();
            if(tableModel.isCellEditable(row, cell)) {
                entryBox.setText(tableModel.getTextAt(row, cell));
                entryBox.setStyleName("ew-EditableGrid-cell-TextBox "+tableModel.getStyleName(row, cell));
                setWidget(row, cell, entryBox);
//                entryBox.setFocus(true);
                entryBox.setCursorPos(0);
                selectedRow = row;
                selectedCell = cell;
            }
        }
    }

    private void unselectCell() {
        if(selectedRow != -1 || selectedCell != -1) {
            setWidget(selectedRow, selectedCell, null);
            setText(selectedRow, selectedCell, entryBox.getText());
            setDisplayText(selectedRow, selectedCell, tableModel.getTextAt(selectedRow, selectedCell));
            selectedRow = -1;
            selectedCell = -1;
        }
    }


    /**
     * For GWT 1.1.10, unused in 1.2.11
     * @param row
     * @param column
     */
    protected void insertCell(int row, int column) {
        super.insertCell(row, column);
        setDisplayText(row, column, tableModel.getTextAt(row, column));
        setCellStyleName(row, column, tableModel.getStyleName(row, column));
        setCellWidth(row, column, tableModel.getCellWidth(row, column));
    }

    /**
     * For GWT 1.1.10, unused in 1.2.11
     * @param row
     * @param column
     */
    protected void insertCells(int row, int column, int count) {
        super.insertCells(row, column, count);
        // Temporary for setWidget to work, assumes insertCells will only be called from resizeRows();
        ++numRows;
        for (int i = column; i < column+count; i++) {
            setDisplayText(row, i, tableModel.getTextAt(row, i));
            setCellStyleName(row, i, tableModel.getStyleName(row, i));
            setCellWidth(row, i, tableModel.getCellWidth(row, i));
        }
        // Removes temporary adjustment from above
        --numRows;
    }

    public String getText(int row, int column) {
        return tableModel.getTextAt(row, column);
    }


    private void setCellWidth(int row, int column, String width) {
        if(width != null) {
            getCellFormatter().setWidth(row, column, width);
        } else {
            getCellFormatter().setWidth(row, column, "");
        }
    }

    private void setCellStyleName(int row, int column, String styleName) {
        if(styleName != null && styleName.trim().length() > 0) {
            getCellFormatter().setStyleName(row, column, styleName);
        } else {
            final String oldStyleName = getCellFormatter().getStyleName(row, column);
            if(oldStyleName != null && oldStyleName.trim().length() > 0) {
                getCellFormatter().removeStyleName(row, column, oldStyleName);
            }
        }
    }

    public void setText(int row, int column, String text) {
        tableModel.setTextAt(text, row, column);
    }

    private void setDisplayText(int row, int column, String text) {
        super.setText(row, column, text);
        if(text == null || text.length() == 0)
            super.setHTML(row, column, "&nbsp;");
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void onTableChange(TableModelEvent e) {
        if(e.getType().equals(TableModelEvent.Type.DELETE) ||
           e.getType().equals(TableModelEvent.Type.INSERT)
                )
        {
            redraw();
        } else if(e.getColumn() != TableModelEvent.ALL_COLUMNS && tableModel.getRowCount() > 0) {
            setDisplayText(e.getFirstRow(), e.getColumn(), tableModel.getTextAt(e.getFirstRow(), e.getColumn()));
            setCellStyleName(e.getFirstRow(), e.getColumn(), tableModel.getStyleName(e.getFirstRow(), e.getColumn()));
            setCellWidth(e.getFirstRow(), e.getColumn(), tableModel.getCellWidth(e.getFirstRow(), e.getColumn()));
        } else if(e.getColumn() == TableModelEvent.ALL_COLUMNS && tableModel.getRowCount() > 0) {
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                setDisplayText(e.getFirstRow(), i, tableModel.getTextAt(e.getFirstRow(), i));
                setCellStyleName(e.getFirstRow(), i, tableModel.getStyleName(e.getFirstRow(), i));
                setCellWidth(e.getFirstRow(), i, tableModel.getCellWidth(e.getFirstRow(), i));
            }
        }
    }

    /**
     * Forceable redraw all cell in grid.  Resize first if necessary.
     */
    public void redraw() {
        // resize if needed
        if(getColumnCount() != tableModel.getColumnCount() || getRowCount() != tableModel.getRowCount()) {
            resize(tableModel.getRowCount(), tableModel.getColumnCount());
        }

        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                setDisplayText(row, column, tableModel.getTextAt(row, column));
                setCellStyleName(row, column, tableModel.getStyleName(row, column));
                setCellWidth(row, column, tableModel.getCellWidth(row, column));
            }
        }
    }

    /**
     * Responsible for listening to the entry textbox and determining if a
     * key press is a request to navigate to another cell and taking the
     * appropriate action.
     */
    private class GridNavigator extends KeyboardListenerAdapter implements TableListener {

        public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
            selectCell(row, cell);
            entryBox.setFocus(true);
        }

        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
//            GWT.log("onKeyDown{sender="+sender+", keyCode="+keyCode+", modifiers="+modifiers+"}",null);
            if(keyCode == KEY_TAB || keyCode == KEY_ENTER) {
                SearchDirection direction = direction(keyCode, modifiers);
                if(direction != null && selectNextCell(selectedRow, selectedCell, direction)) {
                    ((TextBox)sender).cancelKey();
                    // HACK: The focus appears to be caught on something else, so reset focus after delay
                    Timer t = new Timer() {
                        public void run() {
                            entryBox.setFocus(true);
                        }
                    };
                    t.schedule(25);
                }
            }
        }

        private SearchDirection direction(final char keyCode,
                                          final int modifiers)
        {
            SearchDirection direction = null;
            switch(keyCode) {
                case KEY_TAB:
                    direction = shiftKey(modifiers)
                                ? PREVIOUS
                                : NEXT;
                    break;
                case KEY_ENTER:
                    direction = shiftKey(modifiers)
                                ? UP
                                : DOWN;
                    break;
            }
            return direction;
        }


        private boolean shiftKey(final int modifiers) {
//            GWT.log("modifiers: "+ modifiers+"; KEY_SHIFT: "+MODIFIER_SHIFT+"; shiftKey: "+ (0 != (modifiers & KEY_SHIFT)), null);
            return 0 != (modifiers & MODIFIER_SHIFT);
        }

    }


    /**
     * Instances are responsible for taking a 2D coordinate and determining
     * the next 2D coordinate.
     */
    private interface SearchAlgorithm {
        static final int ROW_INDEX = 0;
        static final int CELL_INDEX = 1;
        void search(int[] coordinates);
    }

    /**
     * Instances represent searching in a particular direction in a table.
     */
    private abstract class SearchDirection implements SearchAlgorithm {
        private String name;

        protected SearchDirection(final String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }

        public void search(int[] coordinates) {
            do {
                searchOnce(coordinates);
            } while(coordinates[SearchDirection.CELL_INDEX] != -1 &&
                    coordinates[SearchDirection.ROW_INDEX] != -1 &&
                    !tableModel.isCellEditable(
                            coordinates[SearchDirection.ROW_INDEX],
                            coordinates[SearchDirection.CELL_INDEX])
                    );
        }

        abstract protected void searchOnce(int[] coordinates);
    }

    public final SearchDirection NEXT =
            new SearchDirection("NEXT") {
                public void searchOnce(int[] coordinates) {
                    if(coordinates[CELL_INDEX] + 1 > getColumnCount() - 1) {
                        if(coordinates[ROW_INDEX] + 1 > getRowCount() - 1) {
                            coordinates[CELL_INDEX] = coordinates[ROW_INDEX] = -1;
                        } else {
                            coordinates[CELL_INDEX] = 0;
                            coordinates[ROW_INDEX]++;
                        }
                    } else {
                        coordinates[CELL_INDEX]++;
                    }
                }
            };

    public final SearchDirection PREVIOUS =
            new SearchDirection("PREVIOUS") {
                public void searchOnce(int[] coordinates) {
                    if(coordinates[CELL_INDEX] - 1 < 0) {
                        if(coordinates[ROW_INDEX] - 1 < 0) {
                            coordinates[CELL_INDEX] = coordinates[ROW_INDEX] = -1;
                        } else {
                            coordinates[CELL_INDEX] = getColumnCount() - 1;
                            coordinates[ROW_INDEX]--;
                        }
                    } else {
                        coordinates[CELL_INDEX]--;
                    }
                }
            };

    public final SearchDirection UP =
            new SearchDirection("UP") {
                public void searchOnce(int[] coordinates) {
                    if(coordinates[ROW_INDEX] - 1 < 0) {
                        coordinates[CELL_INDEX] = coordinates[ROW_INDEX] = -1;
                    } else {
                        coordinates[ROW_INDEX]--;
                    }
                }
            };

    public final SearchDirection DOWN =
            new SearchDirection("DOWN") {
                public void searchOnce(int[] coordinates) {
                    if(coordinates[ROW_INDEX] + 1 > getRowCount() - 1) {
                        coordinates[CELL_INDEX] = coordinates[ROW_INDEX] = -1;
                    } else {
                        coordinates[ROW_INDEX]++;
                    }
                }
            };

    public final SearchDirection NEXT_INCLUSIVE =
            new SearchDirection("NEXT_INCLUSIVE") {
                public void searchOnce(int[] coordinates) {
                    NEXT.searchOnce(coordinates);
                }

                public void search(int[] coordinates) {
                    // If the initial cell is not editable...
                    if( coordinates[SearchDirection.CELL_INDEX] == -1 ||
                        coordinates[SearchDirection.ROW_INDEX] == -1 ||
                        !tableModel.isCellEditable(
                            coordinates[SearchDirection.ROW_INDEX],
                            coordinates[SearchDirection.CELL_INDEX])
                    ) {
                        // ...search for one that is editable
                        super.search(coordinates);
                    }
                }
            };

}
