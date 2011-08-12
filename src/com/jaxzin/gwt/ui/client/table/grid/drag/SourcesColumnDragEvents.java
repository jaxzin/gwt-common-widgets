package com.jaxzin.gwt.ui.client.table.grid.drag;

import com.jaxzin.gwt.ui.client.table.grid.drag.ColumnDragListener;

/**
 * Implementations are producers of column-drag events for which ColumnDragListeners can be attached.
 * <pre>
 * Date: Aug 5, 2006
 * Time: 12:32:32 AM
 * </pre>
 *
 * @author <a href="mailto:brian@jaxzin.com">Brian R. Jackson</a>
 */
public interface SourcesColumnDragEvents {

    void addColumnDragListener(ColumnDragListener listener);
    boolean removeColumnDragListener(ColumnDragListener listener);
}
