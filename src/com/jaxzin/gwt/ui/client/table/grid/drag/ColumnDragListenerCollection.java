package com.jaxzin.gwt.ui.client.table.grid.drag;

import com.google.gwt.user.client.ui.Widget;
import com.jaxzin.gwt.ui.client.table.grid.drag.ColumnDragListener;

import java.util.Vector;
import java.util.Iterator;

/**
 * Contains convenience methods to fire events to a Collection of ColumnDragListeners
 * <pre>
 * Date: Aug 5, 2006
 * Time: 12:34:11 AM
 * </pre>
 *
 * @author <a href="mailto:brian@jaxzin.com">Brian R. Jackson</a>
 */
public class ColumnDragListenerCollection extends Vector {
    void fireDragBegin(Widget sender, int column, int x, int y) {
        for (Iterator i = this.iterator(); i.hasNext();) {
            ColumnDragListener listener = (ColumnDragListener) i.next();
            listener.onDragBegin(sender, column, x, y);
        }
    }

    void fireDragMove(Widget sender, int column, int newPosition, int x, int y) {
        for (Iterator i = this.iterator(); i.hasNext();) {
            ColumnDragListener listener = (ColumnDragListener) i.next();
            listener.onDragMove(sender, column, newPosition, x, y);
        }
    }

    void fireDragEnd(Widget sender, int column, int newPosition, int x, int y) {
        for (Iterator i = this.iterator(); i.hasNext();) {
            ColumnDragListener listener = (ColumnDragListener) i.next();
            listener.onDragEnd(sender, column, newPosition, x, y);
        }
    }

}
