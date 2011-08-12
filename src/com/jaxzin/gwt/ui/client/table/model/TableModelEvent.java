package com.jaxzin.gwt.ui.client.table.model;

/**
 * <pre>
 * Date: Oct 3, 2006
 * Time: 1:53:40 PM
 * </pre>
 *
 * @author <a href="mailto:Brian-R.Jackson@ubs.com">Brian-R Jackson</a>
 */
public class TableModelEvent {

    public static final int ALL_COLUMNS = -1;

    private TableModel source;
    private int firstRow = -1;
    private int lastRow = -1;
    private int column = -1;
    /** @noinspection InstanceVariableOfConcreteClass*/
    private Type type = Type.UPDATE;

    public TableModelEvent(final TableModel source) {
        this.source = source;
    }

    public TableModelEvent(final TableModel source, final int firstRow) {
        this.source = source;
        this.firstRow = firstRow;
    }

    public TableModelEvent(final TableModel source, final int firstRow, final int lastRow) {
        this.source = source;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
    }

    public TableModelEvent(final TableModel source, final int firstRow, final int lastRow,
                           final int column)
    {
        this.source = source;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.column = column;
    }

    public TableModelEvent(final TableModel source, final int firstRow, final int lastRow,
                           final int column, final Type type)
    {
        this.source = source;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.column = column;
        this.type = type;
    }

    public TableModel getSource() {
        return source;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public int getColumn() {
        return column;
    }

    public Type getType() {
        return type;
    }


    public String toString() {
        return "TableModelEvent{" +
               "source=" + source +
               ", firstRow=" + firstRow +
               ", lastRow=" + lastRow +
               ", column=" + column +
               ", type=" + type +
               '}';
    }

    public static class Type {
        public static final Type DELETE = new Type("DELETE");
        public static final Type INSERT = new Type("INSERT");
        public static final Type UPDATE = new Type("UPDATE");


        private String name;
        private Type(String name) {
            this.name = name;
        }

        public boolean equals(final Object o) {
            if (this == o) return true;

            final Type type = (Type) o;

            if (!name.equals(type.name)) return false;

            return true;
        }

        public int hashCode() {
            return name.hashCode();
        }

        public String toString() {
            final StringBuffer sb = new StringBuffer();
            sb.append("Type");
            sb.append("{name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
