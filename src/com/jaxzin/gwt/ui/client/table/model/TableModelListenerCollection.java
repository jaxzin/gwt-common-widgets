package com.jaxzin.gwt.ui.client.table.model;

import java.util.Iterator;
import java.util.Vector;

/**
 * A helper class for implementers of the SourcesChangeEvents interface. This
 * subclass of Vector assumes that all objects added to it will be of type
 * {@link com.google.gwt.user.client.ui.ChangeListener}.
 * <pre>
 * Date: Oct 3, 2006
 * Time: 1:55:10 PM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public class TableModelListenerCollection extends Vector {

    /**
     * Fires a change event to all listeners.
     *
     * @param e the event.
     */
    public void fireTableChange(TableModelEvent e) {
        for (Iterator it = iterator(); it.hasNext();) {
            TableModelListener listener = (TableModelListener) it.next();
            listener.onTableChange(e);
        }
    }
}
