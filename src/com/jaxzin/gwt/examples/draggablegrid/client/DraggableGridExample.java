package com.jaxzin.gwt.examples.draggablegrid.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import com.jaxzin.gwt.ui.client.table.grid.drag.ColumnDragListener;
import com.jaxzin.gwt.ui.client.table.grid.drag.DraggableGrid;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DraggableGridExample implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        final DraggableGrid table = new DraggableGrid(3,6);
        table.setCellSpacing(0);
        table.setText(0, 0, "first");
        table.setText(0, 1, "second");
        table.setText(0, 2, "third");
        table.setText(0, 3, "fourth");
        table.setText(0, 4, "fifth");
        table.setText(0, 5, "sixth");
        table.setText(1, 0, "first");
        table.setText(1, 1, "second");
        table.setText(1, 2, "third");
        table.setText(1, 3, "fourth");
        table.setText(1, 4, "fifth");
        table.setText(1, 5, "sixth");
        table.setText(2, 0, "first");
        table.setText(2, 1, "second");
        table.setText(2, 2, "third");
        table.setText(2, 3, "fourth");
        table.setText(2, 4, "fifth");
        table.setText(2, 5, "sixth");
        table.setStyleName("dfte-Table");
        table.getRowFormatter().setStyleName(0, "dfte-TableHeader");

        final TextArea out = new TextArea();
        out.setWidth("100%");
        out.setVisibleLines(10);

        table.addColumnDragListener(
                new ColumnDragListener() {
                    public void onDragBegin(Widget sender, int column, int x, int y) {
                        out.setText("onDragBegin{column="+ column +", x="+x+", y="+y+"}");
                    }

                    public void onDragMove(Widget sender, int column, int newPosition, int x, int y) {
                        out.setText("onDragMove{column="+ column +", newPosition="+newPosition +", x="+x+", y="+y+"}");
                    }

                    public void onDragEnd(Widget sender, int column, int newPosition, int x, int y) {
                        out.setText("onDragEnd{column="+ column +", newPosition="+newPosition +", x="+x+", y="+y+"}");
                    }
                }
        );

        HorizontalPanel container = new HorizontalPanel();
        VerticalPanel settings = new VerticalPanel();
        final CheckBox slideTransitionEnabled = new CheckBox("slideTransitionEnabled (Experimental)");
        slideTransitionEnabled.setChecked(table.getDefaultListener().isSlideTransitionEnabled());
        slideTransitionEnabled.addClickListener(
                new ClickListener() {
                    public void onClick(Widget sender) {
                        table.getDefaultListener().setSlideTransitionEnabled(slideTransitionEnabled.isChecked());
                    }
                }
        );
        settings.add(slideTransitionEnabled);

        final CheckBox constrainedToHorizontal = new CheckBox("constrainedToHorizontal");
        constrainedToHorizontal.setChecked(table.getDefaultListener().isConstrainedToHorizontal());
        constrainedToHorizontal.addClickListener(
                new ClickListener() {
                    public void onClick(Widget sender) {
                        table.getDefaultListener().setConstrainedToHorizontal(constrainedToHorizontal.isChecked());
                    }
                }
        );
        settings.add(constrainedToHorizontal);

        final CheckBox updateAtDragEndOnlyEnabled = new CheckBox("updateAtDragEndOnlyEnabled");
        updateAtDragEndOnlyEnabled.setChecked(table.getDefaultListener().isUpdateAtDragEndOnlyEnabled());
        updateAtDragEndOnlyEnabled.addClickListener(
                new ClickListener() {
                    public void onClick(Widget sender) {
                        table.getDefaultListener().setUpdateAtDragEndOnlyEnabled(updateAtDragEndOnlyEnabled.isChecked());
                    }
                }
        );
        settings.add(updateAtDragEndOnlyEnabled);

        container.add(table);
        container.add(settings);

        RootPanel.get().add(container);
        RootPanel.get().add(out);

    }
}
