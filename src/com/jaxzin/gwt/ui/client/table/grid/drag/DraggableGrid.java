package com.jaxzin.gwt.ui.client.table.grid.drag;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Extends Grid to support draggable columns.  The DraggableGrid only implements the logic to
 * detect the begin, movement and end of a column drag.  The default ColumnDragListener that is attached
 * is responsible for creating the visual representation of the column drag event, such as the dimmed column and
 * detached column and the actual movement of column data.  If the default listener is not appropriate for your
 * needs you can remove it with a call to {@link #removeDefaultListener()}.
 * <p>
 * <b>Usage:</b>
 * </p>
 * <p>
 * There are three CSS classes used by this object:
 * <blockquote>
 * <dl>
 * <dt><b><code>gwt-DraggableGrid-draggedOriginal</code></b></dt>
 * <dd>This style controls the look of the column that is still attached to the table during the drag event.
 *  This style is applied on to each 'td' element, that is, to the individual table cells that make up the column.</dd>
 * <dt><b><code>gwt-DraggableGrid-draggedCopy</code></b></dt>
 * <dd>This style controls the look of the column that is detached from the table and tracks the cursor
 * during the drag event.  The detached column is actually a full HTMLTable, and this style is applied to the
 * 'table' element.
 * </dd>
 * <dt><b><code>gwt-DraggableGrid-draggedTransition</code></b></dt>
 * <dd>This style controls the look of the column that is detached from the table during the slide transition.
 * The detached column is actually a full HTMLTable, and this style is applied to the 'table' element.
 * </dd>
 * </dl>
 * </blockquote>
 * There are several properties that can be set to control the behavior of the DraggableGrid and its default listener.
 * <blockquote>
 * <dl>
 * <dt><b><code>{@link #setDragBeginThreshold setDragBeginThreshold(int)}</code></b></dt>
 * <dd>This controls the number of pixels the user needs to drag the mouse on a column head before it is considered the
 * beginning of a column drag event.  <em>Default: 7</em></dd>
 * <dt><b><code>{@link DefaultColumnDragListener#setConstrainedToHorizontal(boolean) getDefaultListener().setConstrainedToHorizontal(boolean)}</code></b></dt>
 * <dd>This controls if the detached column should track the mouse vertically during a drag event.  <em>Default: true</em></dd>
 * <dt><b><code>{@link DefaultColumnDragListener#setUpdateAtDragEndOnlyEnabled(boolean)  getDefaultListener().setUpdateAtDragEndOnlyEnabled(boolean)}</code></b></dt>
 * <dd>This controls if the "void" of the detached column should track the mouse as the drag occurs or
 * if the update to the table should only happen on the drag-end event. <em>Default: false</em></dd>
 * <dt><b><code>{@link DefaultColumnDragListener#setXOffset(int)  getDefaultListener().setXOffset(int)}</code></b></dt>
 * <dd>This controls the horizontal offset of the detached column in relation to the cursor. <em>Default: 5 (pixels to the right)</em></dd>
 * <dt><b><code>{@link DefaultColumnDragListener#setYOffset(int)  getDefaultListener().setYOffset(int)}</code></b></dt>
 * <dd>This controls the vertical offset of the detached column in relation to the cursor. <em>Default: 5 (pixels down, but is disregarded by default since constrainedToHorizontal is true)</em></dd>
 * <dt><b><code>{@link DefaultColumnDragListener#setSlideTransitionEnabled(boolean) getDefaultListener().setSlideTransitionEnabled(boolean)}</code></b></dt>
 * <dd>This controls whether the column movements should be animated with a slide transition. This functionality is currently very experimental (a.ka. buggy). <em>Default: false</em></dd>
 * </dl>
 * </blockquote>
 * </p>
 *
 * <pre>
 * Date: Aug 4, 2006
 * Time: 11:44:21 PM
 * </pre>
 *
 * @author <a href="mailto:brian@jaxzin.com">Brian R. Jackson</a>
 */
public class DraggableGrid extends Grid implements SourcesColumnDragEvents {


    private int currentDragColumn = -1;
    private int startX = -1;
    private boolean dragging = false;
    private Grid draggedColumn;
    private int dragBeginThreshold = 7;
    ColumnDragListenerCollection listeners = new ColumnDragListenerCollection();
    private final DefaultColumnDragListener defaultListener = new DefaultColumnDragListener(5);

    public DraggableGrid() {
        init();
    }

    public DraggableGrid(int rows, int columns) {
        super(rows, columns);
        init();
    }

    private void init() {
        sinkEvents(Event.MOUSEEVENTS | Event.ONCLICK);
        addColumnDragListener(defaultListener);
    }

    public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEDOWN:
                onMouseDown(event);
                break;
            case Event.ONMOUSEMOVE:
                onMouseMove(event);
                break;
            case Event.ONMOUSEUP:
                onMouseUp(event);
                break;
            default:
                super.onBrowserEvent(event);
        }
    }

    private void onMouseDown(Event event) {
        // If there are drag listeners...
        if (!listeners.isEmpty()) {
            // Find out which cell was actually clicked.
            Element td = getEventTargetCell(event);
            // If it wasn't a cell, then we will ignore the event.
            if (td == null)
                return;

            // Find the row number of the cell...
            Element tr = DOM.getParent(td);
            Element body = DOM.getParent(tr);
            int row = DOM.getChildIndex(body, tr);

            // Only the first row can be dragged
            if (row != 0)
                return;

            // Find the column number of the cell...
            int column = DOM.getChildIndex(tr, td);

            // Record which column is being dragged...
            currentDragColumn = column;
            // Record the mouse position so we can test the mouse movement against the dragBeginThreshold
            startX = DOM.eventGetClientX(event);

            // Prevent the default action (which is to startX a selection)
            DOM.eventPreventDefault(event);
        }
    }

    private void onMouseMove(Event event) {
        // If there are listeners, and a column is being dragged...
        if (!listeners.isEmpty() && currentDragColumn != -1) {
            // Find out which cell was actually clicked.
            Element td = getEventTargetCell(event);
            // If it wasn't a cell, then we will ignore the event.
            if (td == null)
                return;

            // Find the column number of the cell...
            Element tr = DOM.getParent(td);
            int column = DOM.getChildIndex(tr, td);

            // If the drag event hasn't begun yet, and the move distance is above the threshold...
            if (!dragging && Math.abs(DOM.eventGetClientX(event) - startX) > dragBeginThreshold) {
                // Begin the drag event...
                dragging = true;
                listeners.fireDragBegin(this, currentDragColumn, DOM.eventGetClientX(event), DOM.eventGetClientY(event));
            }
            // Otherwise if the drag event has already begun...
            else if (dragging) {
                // Notify the listeners of the movement
                listeners.fireDragMove(this, currentDragColumn, column, DOM.eventGetClientX(event), DOM.eventGetClientY(event));
            }

            // Prevent the default action (which is to continue a selection, needed for IE)
            DOM.eventPreventDefault(event);
        }
    }

    private void onMouseUp(Event event) {
        // If there are listeners and the drag event has begun...
        if (!listeners.isEmpty() && dragging && currentDragColumn != -1) {
            // Find out which cell was actually clicked.
            Element td = getEventTargetCell(event);
            // If it wasn't a cell, then we will ignore the event.
            if (td == null)
                return;

            // Find the column number of the cell...
            Element tr = DOM.getParent(td);
            int column = DOM.getChildIndex(tr, td);

            // Notify the listeners that the drag event has ended...
            listeners.fireDragEnd(this, currentDragColumn, column, DOM.eventGetClientX(event), DOM.eventGetClientY(event));

            // Reset the variables used to track the drag event
            currentDragColumn = -1;
            startX = -1;
            dragging = false;

            // Prevent the default action (which is to endX a selection, needed for IE)
            DOM.eventPreventDefault(event);
        }
    }

//    /**
//     * Determines the TD associated with the specified event.
//     *
//     * @param event the event to be queried
//     * @return the TD associated with the event, or <code>null</code> if none is
//     *         found.
//     */
//    private Element getEventTargetCell(Event event) {
//        Element td = DOM.eventGetTarget(event);
//        while (!DOM.getAttribute(td, "tagName").equalsIgnoreCase("td")) {
//            // If we run out of elements, or run into the table itself, then give up.
//            if ((td == null) || DOM.compare(td, getElement()))
//                return null;
//            td = DOM.getParent(td);
//        }
//
//        return td;
//    }

    public void addColumnDragListener(ColumnDragListener listener) {
        listeners.add(listener);
    }

    public boolean removeColumnDragListener(ColumnDragListener listener) {
        return listeners.remove(listener);
    }

    public int getDragBeginThreshold() {
        return dragBeginThreshold;
    }

    public void setDragBeginThreshold(int dragBeginThreshold) {
        this.dragBeginThreshold = dragBeginThreshold;
    }

    /**
     * Returns the default instance of the ColumnDragListener.
     * @return the default instance of the ColumnDragListener.
     */
    public DefaultColumnDragListener getDefaultListener() {
        return defaultListener;
    }

    /**
     * Call this if the default ColumnDragListener is not appropriate for your needs.
     */
    public void removeDefaultListener() {
        removeColumnDragListener(getDefaultListener());
    }


    //==============================================================
    // Inner classes
    //==============================================================

    /**
     * Implements the following functionality on the DraggableGrid
     * <ul>
     * <li>Moves column data, either when mouse hovers over new position or when drag ends, depending on <code>updateAtDragOnlyEnabled</code></li>
     * <li>Changes the style of the dragged column still attached to the table to <code>gwt-DraggableGrid-draggedOriginal</code></li>
     * <li>Creates a detached copy of the dragged column and tracks the mouse with it.</li>
     * <li>Adds the style <code>gwt-DraggableGrid-draggedCopy</code> to the detached copy of the dragged column</li>
     * <li>Depending on the property <code>slideTransitionEnabled</code>, animates the transition of the dragged column to its new position</li>
     * <li>If <code>slideTransitionEnabled</code>, then add the style <code>gwt-DraggableGrid-draggedTransition</code> to the original column during the transition.
     * </ul>
     */
    public class DefaultColumnDragListener implements ColumnDragListener {

        private int xOffset, yOffset;
        private boolean updateAtDragEndOnlyEnabled = false;
        // Used during move to track currentX column position when updateAtDragEndOnlyEnabled is true
        private int currentColumnPosition = -1;
        private boolean constrainedToHorizontal;
        private boolean slideTransitionEnabled = false;
        private FlowPanel columnPanel = new FlowPanel();

        protected DefaultColumnDragListener(int offset) {
            this(offset, offset, true);
        }

        protected DefaultColumnDragListener(int xOffset, int yOffset, boolean constrainedToHorizontal) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.constrainedToHorizontal = constrainedToHorizontal;
            columnPanel.setStyleName("gwt-DraggableGrid-columnPanel");

            columnPanel.setPixelSize(0,0);
            RootPanel.get().add(columnPanel);
        }

        public void onDragBegin(Widget sender, int column, int x, int y) {

            int rowCount = getRowCount();

            // Create clone of dragged column
            draggedColumn = new DetachedColumn(rowCount, xOffset, yOffset, constrainedToHorizontal);
            draggedColumn.setVisible(false);

            draggedColumn.setStyleName(getStyleName());
            draggedColumn.addStyleName("gwt-DraggableGrid-draggedCopy");
            draggedColumn.setCellSpacing(getCellSpacing());
//                    draggedColumn.setCellPadding(getCellPadding()); // throws exception in IE
            for (int row = 0; row < rowCount; row++) {
                draggedColumn.setHTML(row, 0, getHTML(row, column));
                draggedColumn.getCellFormatter().setStyleName(row, 0, getCellFormatter().getStyleName(row, column));
                draggedColumn.getRowFormatter().setStyleName(row, getRowFormatter().getStyleName(row));
            }

            columnPanel.add(draggedColumn);

            final Element draggedColumnElement = draggedColumn.getElement();
            DOM.setStyleAttribute(draggedColumnElement, "position", "absolute");
            DOM.setIntStyleAttribute(draggedColumnElement, "left", x + xOffset);
            if(constrainedToHorizontal) {
                DOM.setIntStyleAttribute(draggedColumnElement, "top", getAbsoluteTop());
            } else {
                DOM.setIntStyleAttribute(draggedColumnElement, "top", y + yOffset);
            }

            draggedColumn.setVisible(true);

            // Set all of the cells in the column to the CSS style that represents the detached column
            for (int row = 0; row < rowCount; row++) {
                getCellFormatter().addStyleName(row, column, "gwt-DraggableGrid-draggedOriginal");
            }

            currentColumnPosition = column;
        }

        public void onDragMove(Widget sender, int column, int newPosition, int x, int y) {
            DOM.setIntStyleAttribute(draggedColumn.getElement(), "left", x + xOffset);
            if(!constrainedToHorizontal) {
                DOM.setIntStyleAttribute(draggedColumn.getElement(), "top", y + yOffset);
            }

            if(!updateAtDragEndOnlyEnabled && currentColumnPosition != newPosition) {
                moveColumn(currentColumnPosition, newPosition);
                currentColumnPosition = newPosition;
            }
        }

        public void onDragEnd(Widget sender, int column, int newPosition, int x, int y) {
            // Set all of the cells in the column to visible
            int rowCount = getRowCount();

            if(!updateAtDragEndOnlyEnabled) {
                if(currentColumnPosition != newPosition) {
                    moveColumn(currentColumnPosition, newPosition);
                }
            } else {
                moveColumn(column, newPosition);
            }

            // Reset the
            for (int row = 0; row < rowCount; row++) {
                getCellFormatter().removeStyleName(row, newPosition, "gwt-DraggableGrid-draggedOriginal");
            }

            draggedColumn.setVisible(false);
            columnPanel.remove(draggedColumn);
            draggedColumn = null;
            currentColumnPosition = -1;
        }

        private void moveColumn(int column, int newPosition) {
            moveColumn(column, newPosition, this.slideTransitionEnabled);
        }
        private void moveColumn(int column, int newPosition, boolean slideTransitionEnabled) {
            if (column == newPosition)
                return;

            int rowCount = getRowCount();
            if(slideTransitionEnabled) {
                // create clone of column in newPosition
                Grid newPositionColumn = new TransitionColumn(rowCount);
                newPositionColumn.setVisible(false);

                newPositionColumn.setStyleName(getStyleName());
                newPositionColumn.addStyleName("gwt-DraggableGrid-draggedTransition");

                newPositionColumn.setCellSpacing(getCellSpacing());
//                    draggedColumn.setCellPadding(getCellPadding()); // throws exception in IE
                for (int row = 0; row < rowCount; row++) {
                    newPositionColumn.setHTML(row, 0, getHTML(row, newPosition));
                    newPositionColumn.getCellFormatter().setStyleName(row, 0, getCellFormatter().getStyleName(row, newPosition));
                    newPositionColumn.getRowFormatter().setStyleName(row, getRowFormatter().getStyleName(row));
                }

                // Set the absolute position of the detached clone of newPosition column
                Element newPositionDetachedE = newPositionColumn.getElement();
                Element newPositionAttachedE = getCellFormatter().getElement(0, newPosition);
                int startLeft = DOM.getAbsoluteLeft(newPositionAttachedE);
                DOM.setStyleAttribute(newPositionDetachedE, "position", "absolute");
                DOM.setIntStyleAttribute(newPositionDetachedE, "left", startLeft);
                DOM.setIntStyleAttribute(newPositionDetachedE, "top", getAbsoluteTop());

                // Insert as the first child of the columnPanel so draggedColumn will
                // visually display above the transition column.
                DOM.insertChild(columnPanel.getElement(), newPositionColumn.getElement(), 0);

                // reset opacity to 1 of transition columns (incase another transistion was still in progress)
                for (int row = 0; row < rowCount; row++) {
                    Element td = newPositionColumn.getCellFormatter().getElement(row, 0);
                    DOM.setStyleAttribute(td, "opacity", "");
                    DOM.setStyleAttribute(td, "filter", "");
                }
                newPositionColumn.setVisible(true);

                // set opacity to 0 of column in newPosition (don't set to visibility=hidden otherwise mouse events over table are not caught)
                for (int row = 0; row < rowCount; row++) {
                    Element td = getCellFormatter().getElement(row, newPosition);
                    DOM.setStyleAttribute(td, "opacity", "0.0");
                    DOM.setStyleAttribute(td, "filter", "alpha(opacity=0)");
                }

                // Move the columns now incase the transition takes too long and another begins
                for (int row = 0; row < rowCount; row++) {
                    Element rowElement = getRowFormatter().getElement(row);
                    Element cellElement = DOM.getChild(rowElement, column);
                    DOM.removeChild(rowElement, cellElement);
                    DOM.insertChild(rowElement, cellElement, newPosition);
                }

                // Figure out the correct endpoint of the animation
                int end;
                int startRight = DOM.getAbsoluteLeft(getCellFormatter().getElement(0, newPosition+1));
                int startWidth = startRight - startLeft;
                Element destE = getCellFormatter().getElement(0,column);
                int endLeft = DOM.getAbsoluteLeft(destE);
                int endRight = DOM.getAbsoluteLeft(getCellFormatter().getElement(0, column+1));
                //if its a left transition...
                if(startLeft > endLeft) {
                    end = endLeft;
                }
                // otherwise its a right transition, so if the right side's are found
                    // (will be true for all instances except when transition the penultimate column to the last column)
                else if(endRight > 0 && startRight > 0) {
                    end = endRight - startWidth;
                }
                // TODO: For the penultimate->last transistion, fall back to just using the left side
                // of the current last column, even though this is buggy.  I don't have a better solution yet.
                else {
                    end = endLeft;
                }

                // kick off Timer to handle animation
                Timer mover = new SlideTransition(startLeft, end, newPositionColumn, column);
                mover.run();
            } else {
                for (int row = 0; row < rowCount; row++) {
                    Element rowElement = getRowFormatter().getElement(row);
                    Element cellElement = DOM.getChild(rowElement, column);
                    DOM.removeChild(rowElement, cellElement);
                    DOM.insertChild(rowElement, cellElement, newPosition);
                }
            }
        }

        public int getXOffset() {
            return xOffset;
        }

        public void setXOffset(int xOffset) {
            this.xOffset = xOffset;
        }

        public int getYOffset() {
            return yOffset;
        }

        public void setYOffset(int yOffset) {
            this.yOffset = yOffset;
        }

        public boolean isUpdateAtDragEndOnlyEnabled() {
            return updateAtDragEndOnlyEnabled;
        }

        public void setUpdateAtDragEndOnlyEnabled(boolean updateAtDragEndOnlyEnabled) {
            this.updateAtDragEndOnlyEnabled = updateAtDragEndOnlyEnabled;
        }

        public boolean isConstrainedToHorizontal() {
            return constrainedToHorizontal;
        }

        public void setConstrainedToHorizontal(boolean constrainedToHorizontal) {
            this.constrainedToHorizontal = constrainedToHorizontal;
        }

        public boolean isSlideTransitionEnabled() {
            return slideTransitionEnabled;
        }

        public void setSlideTransitionEnabled(boolean slideTransitionEnabled) {
            this.slideTransitionEnabled = slideTransitionEnabled;
        }

        //======================================================
        // Inner classes of DefaultColumnDragListener
        //======================================================

        /**
         * Instances are used to create the illusion of an attached column sliding out of the way of the
         * detached column as the user drags the column across the table.
         */
        private class TransitionColumn extends Grid {
            public TransitionColumn(int rows) {
                super(rows, 1);
            }
            public void onBrowserEvent(Event event) {
                switch (DOM.eventGetType(event)) {
                    case Event.ONMOUSEMOVE:
                        // Prevent the default action (which is to continue a selection, needed for IE)
                        DOM.eventPreventDefault(event);
                        break;
                    default:
                        super.onBrowserEvent(event);
                }
            }
        }

        /**
         * Instances are used to create the illusion of a column detaching from the table to track
         * the mouse as a user drags across the table.
         */
        private class DetachedColumn extends Grid {
            private int xOffset, yOffset;
            private boolean constrainedToHorizontal;

            /**
             *
             * @param rows The number of rows in the column
             * @param xOffset The horizontal pixel offset of the left side of this widget from the mouse cursor (right is positive).
             * @param yOffset The vertical pixel offset of the top side of this widget from the mouse cursor (down is positive).
             * @param constrainedToHorizontal The truth that this column should only track the mouse in the horizontal.
             */
            public DetachedColumn(int rows, int xOffset, int yOffset, boolean constrainedToHorizontal) {
                super(rows, 1);
                sinkEvents(Event.ONMOUSEMOVE);
                this.xOffset = xOffset;
                this.yOffset = yOffset;
                this.constrainedToHorizontal = constrainedToHorizontal;
            }

            public void onBrowserEvent(Event event) {
                switch (DOM.eventGetType(event)) {
                    case Event.ONMOUSEMOVE:
                        // If the mouse gets on top of clone, just keep moving it. (otherwise the ui will feel like its stuttering).
                        DOM.setIntStyleAttribute(getElement(), "left", DOM.eventGetClientX(event) + xOffset);
                        if(!constrainedToHorizontal) {
                            DOM.setIntStyleAttribute(getElement(), "top", DOM.eventGetClientY(event) + yOffset);
                        }
                        // Prevent the default action (which is to continue a selection, needed for IE)
                        DOM.eventPreventDefault(event);
                        break;
                    default:
                        super.onBrowserEvent(event);
                }
            }
        }

        /**
         * Responsible for sliding the transition column "out of the way" of the detached column.
         */
        private class SlideTransition extends Timer {
            private int endX;
            private Widget w;
            private int currentX;
            private int deltaX;
            private int column;

            private final int FRAMES = 10;

            /**
             *
             * @param start The horizontal pixel position to start the transition at.
             * @param end The horizontal pixel position to end the transition at.
             * @param w The widget to slide.
             * @param column The index of the column that is being dragged.
             */
            private SlideTransition(int start, int end, Widget w, int column) {
                this.endX = end;
                this.w = w;
                this.column = column;

                currentX = start;
                // Figure out the pixel distance to travel per frame
                deltaX = (end - start) / FRAMES;

                // If the distance was calculated to be zero, make it 1 or -1 (otherwise we get an infinite loop!)
                if(deltaX == 0) {
                    deltaX = (end - start) > 0 ? -1 : 1;
                }
            }

            public void run() {
                DOM.setIntStyleAttribute(w.getElement(), "left", currentX);
                if(currentX == endX) {

                    int rowCount = getRowCount();
                    // reset opacity to 1 of column in column
                    for (int row = 0; row < rowCount; row++) {
                        Element td = getCellFormatter().getElement(row, column);
                        DOM.setStyleAttribute(td, "opacity", "");
                        DOM.setStyleAttribute(td, "filter", "");
                    }

                    w.setVisible(false);
                    columnPanel.remove(w);
                } else {
                    currentX += deltaX;
                    if((deltaX > 0 && currentX > endX) ||
                        (deltaX < 0 && currentX < endX))
                        currentX = endX;
                    schedule(15);
                }
            }
        }
    }

}
