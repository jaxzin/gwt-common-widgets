package com.jaxzin.gwt.ui.client.autocompletion;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.GWT;

import java.util.Iterator;

/**
 * <pre>
 * Date: Nov 1, 2006
 * Time: 9:47:15 AM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public class AutoCompletionResultPanel extends PopupPanel implements SourcesClickEvents {

    public static final int NONE_SELECTED = -1;

    private VerticalPanel listPanel = new VerticalPanel();
    private MatcherResult result;
    private int selectedIndex = NONE_SELECTED;
    private ClickListenerCollection listeners = new ClickListenerCollection();

    public AutoCompletionResultPanel() {
        super(true); // auto-hide on outside click
        setWidget(listPanel);
        // Tell the listPanel to fill the popupPanel
        listPanel.setWidth("100%");
        listPanel.addStyleName("ew-AutoCompletionResultPanel");
    }

    public void setResult(MatcherResult result) {
        this.result = result;
        redraw();
    }

    public MatcherResult getResult() {
        return result;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(final int selectedIndex) {
        GWT.log("setSelectedIndex: "+selectedIndex, null);
        if(result != null && selectedIndex < result.getMatches().size()) {
            int old = this.selectedIndex;
            this.selectedIndex = selectedIndex;
            fixSelectedIndex();
            listPanel.getWidget(old).removeStyleName("ew-AutoCompletionResultPanel-selected");
            listPanel.getWidget(this.selectedIndex).addStyleName("ew-AutoCompletionResultPanel-selected");
        }
        // otherwise fail silently
    }

    public void addClickListener(ClickListener listener) {
        listeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
        listeners.remove(listener);
    }

    private void redraw() {
        fixSelectedIndex();
        listPanel.clear();
        int index = 0;
        for (Iterator i = result.getMatches().iterator(); i.hasNext(); index++) {
            String match = (String) i.next();
            final Label label = new Label(match);
            listPanel.add(label);

            // Attach a click listener to each label...
            final int labelIndex = index;
            label.addClickListener(
                    new ClickListener() {
                        public void onClick(Widget sender) {
                            // if the label is clicked, set the selected index...
                            setSelectedIndex(labelIndex);
                            // ...and notify any of the panel's listeners
                            listeners.fireClick(AutoCompletionResultPanel.this);
                        }
                    }
            );

            if(index == selectedIndex) {
                label.addStyleName("ew-AutoCompletionResultPanel-selected");
            }
        }
        if(result.getMatches().isEmpty()) {
            listPanel.add(new Label("No matches found."));
        }
        if(!result.isComplete()) {
            listPanel.add(new Label("..."));
        }
    }

    private void fixSelectedIndex() {
        if(result == null || result.getMatches().isEmpty()) {
            selectedIndex = NONE_SELECTED;
        } else if(selectedIndex >= result.getMatches().size()) {
            selectedIndex = result.getMatches().size() - 1;
        }
    }

}
