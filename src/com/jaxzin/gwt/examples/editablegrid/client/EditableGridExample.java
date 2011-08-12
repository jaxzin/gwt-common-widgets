package com.jaxzin.gwt.examples.editablegrid.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.jaxzin.gwt.ui.client.table.grid.edit.EditableGrid;
import com.jaxzin.gwt.ui.client.table.model.AbstractTableModel;
import com.jaxzin.gwt.ui.client.table.model.TableModelEvent;

/**
 * <pre>
 * Date: Jan 9, 2007
 * Time: 8:33:26 PM
 * </pre>
 *
 * @author <a href="mailto:brian@jaxzin.com">Brian R. Jackson</a>
 */
public class EditableGridExample implements EntryPoint {
    public void onModuleLoad() {
        EditableGrid grid =
                new EditableGrid(
                    new AbstractTableModel() {
                        private int[] colsizes = new int[] {100, 20, 120, 100};
                        private String[][] data = new String[][] {
                                {"Name", "Age", "Email", "Phone"},
                                {"Brian Jackson", "28", "brian@jaxzin.com", "(123) 456-7890"},
                                {"Bill Gates", "52", "bill@microsoft.com", "(800) 555-5748"},
                                {"Larry Ellison", "45", "larry@oracle.com", "(800) 555-9436"},
                                {"Eric Schmidt", "42", "eric@google.com", "(800) 314-1592"},
                        };
                        public int getColumnCount() {
                            return data[0].length;
                        }

                        public int getRowCount() {
                            return data.length;
                        }

                        public String getTextAt(int row, int column) {
                            return data[row][column];
                        }

                        public boolean isCellEditable(int row, int column) {
                            return row != 0;
                        }

                        public String getStyleName(int row, int column) {
                            return "cw-EditableGrid-cell" + ((row == 0 ? " cw-EditableGrid-header" : ""));
                        }

                        public String getCellWidth(int row, int column) {
                            return colsizes[column]+"px";
                        }

                        public void setTextAt(String text, int row, int column) {
                            data[row][column] = text;
                            listeners.fireTableChange(new TableModelEvent(this, row, row, column));
                        }
                    }
                );

        grid.setCellSpacing(0);

        RootPanel.get("grid").add(grid);
    }
}
