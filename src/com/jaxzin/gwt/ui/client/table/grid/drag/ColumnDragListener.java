package com.jaxzin.gwt.ui.client.table.grid.drag;

import com.google.gwt.user.client.ui.Widget;

import java.util.EventListener;

/**
 * Defines events that can occur during a column drag.
 * This breaks down into three types of events, one to mark the beginning of the drag,
 * many to represent the intermediate states during the move,
 * and one to mark the end of the drag.
 * <pre>
 * Date: Aug 5, 2006
 * Time: 12:28:15 AM
 * </pre>
 *
 * @author <a href="mailto:brian@jaxzin.com">Brian R. Jackson</a>
 */
public interface ColumnDragListener extends EventListener {

    void onDragBegin(Widget sender, int column, int x, int y);
    void onDragMove(Widget sender, int column, int newPosition, int x, int y);
    void onDragEnd(Widget sender, int column, int newPosition, int x, int y);
}
