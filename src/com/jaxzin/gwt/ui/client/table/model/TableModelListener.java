package com.jaxzin.gwt.ui.client.table.model;

import com.jaxzin.gwt.ui.client.table.model.TableModelEvent;

/**
 * <pre>
 * Date: Oct 3, 2006
 * Time: 1:50:58 PM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public interface TableModelListener {
    /**
     * This fine grain notification tells listeners the exact range of cells, rows, or columns that changed
     * @param e
     */
    void onTableChange(TableModelEvent e);
}
