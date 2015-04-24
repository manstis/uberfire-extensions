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

/**
 * An interface defining a generic grid of data.
 */
public interface IGridData {

    /**
     * Get the columns associated with the grid.
     * @return
     */
    List<IGridColumn<?>> getColumns();

    /**
     * Append a column to the end of the grid. End being considered the far most right.
     * @param column
     */
    void appendColumn( final IGridColumn<?> column );

    /**
     * Insert a column to the grid at the specified index.
     * @param index
     * @param column
     */
    void insertColumn( final int index,
                       final IGridColumn<?> column );

    /**
     * Update a column in the grid at the specified index. All existing row data will be cleared.
     * @param index
     * @param column
     */
    void updateColumn( final int index,
                       final IGridColumn<?> column );

    /**
     * Delete a column from the grid.
     * @param column
     */
    void deleteColumn( final IGridColumn<?> column );

    /**
     * Move a column to a new index within the grid
     * @param index
     * @param column
     */
    void moveColumnTo( final int index,
                       final IGridColumn<?> column );

    /**
     * Move columns to a new index within the grid
     * @param index
     * @param columns
     */
    void moveColumnsTo( final int index,
                        final List<IGridColumn<?>> columns );

    /**
     * Get the rows associated with the grid.
     * @return
     */
    List<IGridRow> getRows();

    /**
     * Get a row at the specified index.
     * @param rowIndex
     * @return
     */
    IGridRow getRow( final int rowIndex );

    /**
     * Append a row to the end of the grid.
     * @param row
     */
    void appendRow( final IGridRow row );

    /**
     * Insert a row to the grid at the specified index.
     * @param rowIndex
     * @param row
     */
    void insertRow( final int rowIndex,
                    final IGridRow row );

    /**
     * delete a row from the grid at the specified index.
     * @param rowIndex
     * @return The Range of rows affected by the operation.
     */
    Range deleteRow( final int rowIndex );

    /**
     * Move a row to a new index within the grid
     * @param index
     * @param row
     */
    void moveRowTo( final int index,
                    final IGridRow row );

    /**
     * Move rowss to a new index within the grid
     * @param index
     * @param rows
     */
    void moveRowsTo( final int index,
                     final List<IGridRow> rows );

    /**
     * Get the total number of rows in the grid.
     * @return
     */
    int getRowCount();

    /**
     * Get a cell at the specified physical coordinate.
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    IGridCell<?> getCell( final int rowIndex,
                          final int columnIndex );

    /**
     * Set a cell at the specified physical coordinate.
     * @param rowIndex
     * @param columnIndex
     * @param value
     * @return The Range of rows affected by the operation.
     */
    Range setCell( final int rowIndex,
                   final int columnIndex,
                   final IGridCellValue<?> value );

    /**
     * Delete a cell at the specified physical coordinate.
     * @param rowIndex
     * @param columnIndex
     * @return The Range of rows affected by the operation.
     */
    Range deleteCell( final int rowIndex,
                      final int columnIndex );

    /**
     * Select a cell at the specified physical coordinate.
     * @param rowIndex
     * @param columnIndex
     * @return The Range of rows affected by the operation.
     */
    Range selectCell( final int rowIndex,
                      final int columnIndex );

    /**
     * Select a cell at the specified physical coordinate.
     * @param rowIndex
     * @param columnIndex
     * @param width
     * @param height
     * @return The Range of rows affected by the operation.
     */
    Range selectCells( final int rowIndex,
                       final int columnIndex,
                       final int width,
                       final int height );

    /**
     * Get the origin of a selected range.
     * @return null if no origin has been defined.
     */
    SelectedCell getSelectedCellsOrigin();

    /**
     * Get all selected cells.
     * @return
     */
    List<SelectedCell> getSelectedCells();

    /**
     * Clear cell selections.
     */
    void clearSelections();

    /**
     * Whether the data in a merged state.
     * @return true if merged.
     */
    boolean isMerged();

    /**
     * Set whether the data is in merged state.
     * @param isMerged
     */
    void setMerged( final boolean isMerged );

    /**
     * Collapse a cell and corresponding rows. The cell being collapsed has all other merged
     * cells below it collapsed into the single cell. The cell itself remains not collapsed.
     * @param rowIndex
     * @param columnIndex
     */
    void collapseCell( final int rowIndex,
                       final int columnIndex );

    /**
     * Expand a cell and corresponding rows. The cell being collapsed has all other merged cells
     * below it expanded. Expanding collapsed cells should not expand nested collapsed cells.
     * @param rowIndex
     * @param columnIndex
     */
    void expandCell( final int rowIndex,
                     final int columnIndex );

    /**
     * A range of rows.
     */
    class Range {

        private int minRowIndex;
        private int maxRowIndex;

        public Range( final int rowIndex ) {
            this( rowIndex,
                  rowIndex );
        }

        public Range( final int minRowIndex,
                      final int maxRowIndex ) {
            this.minRowIndex = minRowIndex;
            this.maxRowIndex = maxRowIndex;
        }

        public int getMinRowIndex() {
            return minRowIndex;
        }

        public int getMaxRowIndex() {
            return maxRowIndex;
        }
    }

    /**
     * A selected cell within the data. Selected state is not stored in the IGridCell implementation
     * as we'd need to scan the whole grid to retrieve selected cells. The assumption is that the number
     * of selected cells is invariably far, far fewer than the total number of cells in the grid.
     */
    class SelectedCell {

        private final int rowIndex;
        private final int columnIndex;

        public SelectedCell( final int rowIndex,
                             final int columnIndex ) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        @Override
        public boolean equals( final Object o ) {
            if ( this == o ) {
                return true;
            }
            if ( !( o instanceof SelectedCell ) ) {
                return false;
            }

            SelectedCell that = (SelectedCell) o;

            if ( rowIndex != that.rowIndex ) {
                return false;
            }
            if ( columnIndex != that.columnIndex ) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = rowIndex;
            result = ~~result;
            result = 31 * result + columnIndex;
            result = ~~result;
            return result;
        }
    }

}
