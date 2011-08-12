package com.jaxzin.gwt.ui.client.table.grid.drag;

import com.google.gwt.user.client.ui.Widget;
import com.jaxzin.gwt.ui.client.table.grid.drag.ColumnDragListener;

/**
 * A convenience class that implements the methods of ColumnDragListener with empty methods so
 * an implementor of the method only need to implement the methods they need.
 * <pre>
 * Date: Aug 5, 2006
 * Time: 2:17:36 AM
 * </pre>
 *
 * @author <a href="mailto:brian@jaxzin.com">Brian R. Jackson</a>
 */
public abstract class ColumnDragListenerAdapter implements ColumnDragListener {
    public void onDragBegin(Widget sender, int column, int x, int y) {
        // do nothing
        ;
    }

    public void onDragMove(Widget sender, int column, int newPosition, int x, int y) {
        // do nothing
        ;
    }

    public void onDragEnd(Widget sender, int column, int newPosition, int x, int y) {
        // do nothing
        ;
    }
}
