/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uberfire.ext.wires.core.grids.client.model;

import java.util.List;

import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.IGridColumnRenderer;

/**
 * Interface defining a Column within a grid.
 */
public interface IGridColumn<T> {

    /**
     * Get the MetaData for the Header. Each entry represents a row in the header.
     * Index 0 represents the first (top-down) row, Index 1 represents the second row etc.
     * @return
     */
    List<HeaderMetaData> getHeaderMetaData();

    /**
     * Get the Render for the column
     * @return
     */
    IGridColumnRenderer<T> getColumnRenderer();

    /**
     * Edit the cell (normally in response to a mouse double-click event)
     * @param cell
     * @param context
     * @param callback
     */
    void edit( final IGridCell<T> cell,
               final GridBodyCellRenderContext context,
               final Callback<IGridCellValue<T>> callback );

    /**
     * Get the column's width
     * @return
     */
    double getWidth();

    /**
     * Set the columns width
     * @param width
     */
    void setWidth( final double width );

    /**
     * Flag indicating this column is linked to another
     * @return
     */
    boolean isLinked();

    /**
     * Get the column to which this column is linked
     * @return
     */
    IGridColumn<?> getLink();

    /**
     * Set the column to which this column is linked
     * @param link
     */
    void setLink( final IGridColumn<?> link );

    /**
     * Get the logical index to which this column relates. Columns may be re-ordered and therefore, to
     * avoid manipulating the underlying row data, the logical index of the column may be different to their
     * physical index (i.e. the order in which they were added to the grid).
     * @return
     */
    int getIndex();

    /**
     * Set the logical index of the column, to support indirection of columns' access to row data.
     * @param index
     */
    void setIndex( final int index );

    /**
     * Flag indicating whether a column can be re-sized.
     * @return true if the column can be re-sized.
     */
    boolean isResizable();

    /**
     * Set whether the column can be re-sized.
     * @param isResizable true if the column can be re-sized.
     */
    void setResizable( final boolean isResizable );

    /**
     * Flag indicating whether a column can be moved.
     * @return true if the column can be moved.
     */
    boolean isMovable();

    /**
     * Set whether the column can be moved.
     * @param isMovable true if the column can be moved.
     */
    void setMovable( final boolean isMovable );

    /**
     * Flag indicating whether a column is capable of floating on the left-hand side of the table when clipped horizontally.
     * @return true if the column is capable of floating.
     */
    boolean isFloatable();

    /**
     * Set whether the column is capable of floating on the left-hand side of the table when clipped horizontally.
     * @param isFloatable true if the column can be floated.
     */
    void setFloatable( final boolean isFloatable );

    /**
     * Flag indicating whether a column is visible.
     * @return true if the column is visible.
     */
    boolean isVisible();

    /**
     * Set whether the column is visible.
     * @param isVisible true if the column is visible.
     */
    void setVisible( final boolean isVisible );

    /**
     * Get the minimum width to which the column can be re-sized
     * @return null if no minimum
     */
    Double getMinimumWidth();

    /**
     * Set the minimum width to which the column can be re-sized
     * @param minimumWidth Minimum width, or null if no minimum width
     */
    void setMinimumWidth( final Double minimumWidth );

    /**
     * Get the maximum width to which the column can be re-sized
     * @return null if no maximum
     */
    Double getMaximumWidth();

    /**
     * Set the maximum width to which the column can be re-sized
     * @param maximumWidth Maximum width, or null if no minimum width
     */
    void setMaximumWidth( final Double maximumWidth );

    /**
     * MetaData for the column's header
     */
    interface HeaderMetaData {

        /**
         * An identifier for a group of Columns. Columns in one group cannot be moved to another group.
         * @return The group identifier. It should not be null.
         */
        String getColumnGroup();

        /**
         * Set the identifier for a group of Columns. Columns in one group cannot be moved to another group.
         * @The group identifier. It should not be null.
         */
        void setColumnGroup( final String columnGroup );

        /**
         * Get the column's title
         * @return
         */
        String getTitle();

        /**
         * Set the column's title
         * @param title
         */
        void setTitle( final String title );

    }

}
