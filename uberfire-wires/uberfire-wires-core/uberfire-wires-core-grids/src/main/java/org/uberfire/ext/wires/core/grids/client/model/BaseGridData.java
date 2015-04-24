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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uberfire.ext.wires.core.grids.client.widget.dom.IHasDOMElementResources;

/**
 * Base implementation of a grid to avoid boiler-plate for more specific implementations.
 */
public class BaseGridData implements IGridData {

    protected boolean isMerged = true;
    protected List<IGridRow> rows = new ArrayList<IGridRow>();
    protected List<IGridColumn<?>> columns = new ArrayList<IGridColumn<?>>();
    protected List<SelectedCell> selectedCells = new ArrayList<SelectedCell>();

    protected BaseGridDataIndexManager indexManager = new BaseGridDataIndexManager( this );
    protected BaseGridDataSelectionsManager selectionsManager = new BaseGridDataSelectionsManager( this );

    public BaseGridData() {
        this( true );
    }

    public BaseGridData( final boolean isMerged ) {
        this.isMerged = isMerged;
    }

    @Override
    public List<IGridColumn<?>> getColumns() {
        return Collections.unmodifiableList( columns );
    }

    @Override
    public void appendColumn( final IGridColumn<?> column ) {
        column.setIndex( columns.size() );
        columns.add( column );
    }

    @Override
    public void insertColumn( final int index,
                              final IGridColumn<?> column ) {
        column.setIndex( columns.size() );
        columns.add( index,
                     column );
    }

    @Override
    public void deleteColumn( final IGridColumn<?> column ) {
        final int index = column.getIndex();
        for ( IGridColumn<?> c : columns ) {
            if ( c.getIndex() > index ) {
                c.setIndex( c.getIndex() - 1 );
            }
        }

        //Destroy column
        if ( column.getColumnRenderer() instanceof IHasDOMElementResources ) {
            ( (IHasDOMElementResources) column.getColumnRenderer() ).destroyResources();
        }
        columns.remove( column );

        //Destroy column data
        for ( IGridRow row : rows ) {
            ( (BaseGridRow) row ).deleteCell( index );
            final Map<Integer, IGridCell<?>> clone = new HashMap<Integer, IGridCell<?>>( row.getCells() );
            for ( Map.Entry<Integer, IGridCell<?>> e : clone.entrySet() ) {
                if ( e.getKey() > index ) {
                    ( (BaseGridRow) row ).deleteCell( e.getKey() );
                    ( (BaseGridRow) row ).setCell( e.getKey() - 1,
                                                   e.getValue().getValue() );
                }
            }
        }

        selectionsManager.onDeleteColumn( index );
    }

    @Override
    public void moveColumnTo( final int index,
                              final IGridColumn<?> column ) {
        moveColumnsTo( index,
                       new ArrayList<IGridColumn<?>>() {{
                           add( column );
                       }} );
    }

    @Override
    public void moveColumnsTo( final int index,
                               final List<IGridColumn<?>> columns ) {
        if ( columns == null || columns.isEmpty() ) {
            return;
        }
        final int currentIndex = this.columns.indexOf( columns.get( 0 ) );

        //Moving left
        if ( index < currentIndex ) {
            this.columns.removeAll( columns );
            this.columns.addAll( index,
                                 columns );
        }

        //Moving right
        if ( index > currentIndex ) {
            this.columns.removeAll( columns );
            this.columns.addAll( index - columns.size() + 1,
                                 columns );
        }
    }

    @Override
    public List<IGridRow> getRows() {
        return Collections.unmodifiableList( rows );
    }

    @Override
    public IGridRow getRow( final int rowIndex ) {
        return rows.get( rowIndex );
    }

    @Override
    public void appendRow( final IGridRow row ) {
        this.rows.add( row );
    }

    @Override
    public void insertRow( final int rowIndex,
                           final IGridRow row ) {
        this.rows.add( rowIndex,
                       row );

        indexManager.onInsertRow( rowIndex );
        selectionsManager.onInsertRow( rowIndex );
    }

    @Override
    public Range deleteRow( final int rowIndex ) {
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex;

        //Find row that is the "lead" in a merged collapsed block
        IGridRow row;
        while ( ( row = rows.get( minRowIndex ) ).isMerged() && row.isCollapsed() && minRowIndex > 0 ) {
            minRowIndex--;
        }

        //Find last row in a merged collapsed block
        do {
            maxRowIndex++;
        }
        while ( maxRowIndex < rows.size() && rows.get( maxRowIndex ).isCollapsed() );
        maxRowIndex--;

        final Range range = new Range( minRowIndex,
                                       maxRowIndex );

        for ( int _rowIndex = minRowIndex; _rowIndex <= maxRowIndex; _rowIndex++ ) {
            rows.remove( minRowIndex );
        }

        indexManager.onDeleteRow( range );
        selectionsManager.onDeleteRow( range );

        return range;
    }

    @Override
    public void moveRowTo( final int index,
                           final IGridRow row ) {
        moveRowsTo( index,
                    new ArrayList<IGridRow>() {{
                        add( row );
                    }} );
    }

    @Override
    public void moveRowsTo( final int index,
                            final List<IGridRow> rows ) {
        if ( rows == null || rows.isEmpty() ) {
            return;
        }

        //Get extent of block being moved
        final int oldBlockStart = this.rows.indexOf( rows.get( 0 ) );
        final int oldBlockEnd = this.rows.indexOf( rows.get( rows.size() - 1 ) );

        //If we're attempting to move it to its current index just exit
        if ( index == oldBlockStart ) {
            return;
        }

        this.rows.removeAll( rows );

        if ( index < oldBlockStart ) {
            this.rows.addAll( index,
                              rows );

        } else if ( index > oldBlockStart ) {
            this.rows.addAll( index - rows.size() + 1,
                              rows );
        }

        final Range oldBlockExtent = new Range( oldBlockStart,
                                                oldBlockEnd );
        indexManager.onMoveRows( rows,
                                 oldBlockExtent );
        selectionsManager.onMoveRows( rows,
                                      oldBlockExtent );
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public IGridCell<?> getCell( final int rowIndex,
                                 final int columnIndex ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return null;
        }
        final int _columnIndex = columns.get( columnIndex ).getIndex();
        return rows.get( rowIndex ).getCells().get( _columnIndex );
    }

    @Override
    public SelectedCell getSelectedCellsOrigin() {
        return selectedCells.isEmpty() ? null : selectedCells.get( 0 );
    }

    @Override
    public List<SelectedCell> getSelectedCells() {
        return selectedCells;
    }

    @Override
    public void clearSelections() {
        selectedCells.clear();
    }

    @Override
    public void updateColumn( final int index,
                              final IGridColumn<?> column ) {
        //Destroy existing column
        final IGridColumn<?> existing = columns.get( index );
        if ( existing.getColumnRenderer() instanceof IHasDOMElementResources ) {
            ( (IHasDOMElementResources) existing.getColumnRenderer() ).destroyResources();
        }

        //Replace existing with new column
        column.setIndex( columns.get( index ).getIndex() );
        columns.set( index,
                     column );

        //Clear column data
        for ( IGridRow row : rows ) {
            ( (BaseGridRow) row ).deleteCell( column.getIndex() );
        }
    }

    @Override
    public boolean isMerged() {
        return this.isMerged;
    }

    @Override
    public void setMerged( final boolean isMerged ) {
        if ( this.isMerged == isMerged ) {
            return;
        }
        this.isMerged = isMerged;
        indexManager.onMerge( isMerged );
        selectionsManager.onMerge( isMerged );
    }

    @Override
    public Range setCell( final int rowIndex,
                          final int columnIndex,
                          final IGridCellValue<?> value ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return new Range( rowIndex );
        }
        if ( columnIndex < 0 || columnIndex > columns.size() - 1 ) {
            return new Range( rowIndex );
        }
        if ( value == null ) {
            return new Range( rowIndex );
        }

        final int _columnIndex = columns.get( columnIndex ).getIndex();

        //If we're not merged just set the value of a single cell
        if ( !isMerged ) {
            ( (BaseGridRow) rows.get( rowIndex ) ).setCell( _columnIndex,
                                                            value );
            return new Range( rowIndex );
        }

        //Find affected rows for merged data
        final int minRowIndex = findMinRowIndexForCellUpdate( rowIndex,
                                                              _columnIndex );
        final int maxRowIndex = findMaxRowIndexForCellUpdate( rowIndex,
                                                              _columnIndex );

        //Update all rows' value
        final Range range = new Range( minRowIndex,
                                       maxRowIndex );
        for ( int i = minRowIndex; i <= maxRowIndex; i++ ) {
            final IGridRow row = rows.get( i );
            ( (BaseGridRow) row ).setCell( _columnIndex,
                                           value );
        }

        indexManager.onSetCell( range,
                                _columnIndex );

        return range;
    }

    @Override
    public Range deleteCell( final int rowIndex,
                             final int columnIndex ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return new Range( rowIndex );
        }
        if ( columnIndex < 0 || columnIndex > columns.size() - 1 ) {
            return new Range( rowIndex );
        }

        final int _columnIndex = columns.get( columnIndex ).getIndex();

        //If we're not merged just set the value of a single cell
        if ( !isMerged ) {
            ( (BaseGridRow) rows.get( rowIndex ) ).deleteCell( _columnIndex );
            return new Range( rowIndex );
        }

        //Find affected rows for merged data
        final int minRowIndex = findMinRowIndexForCellUpdate( rowIndex,
                                                              _columnIndex );
        final int maxRowIndex = findMaxRowIndexForCellUpdate( rowIndex,
                                                              _columnIndex );

        //Update all rows' value
        final Range range = new Range( minRowIndex,
                                       maxRowIndex );
        for ( int i = minRowIndex; i <= maxRowIndex; i++ ) {
            final IGridRow row = rows.get( i );
            ( (BaseGridRow) row ).deleteCell( _columnIndex );
            row.expand();
        }

        indexManager.onDeleteCell( range,
                                   _columnIndex );

        return range;
    }

    @Override
    public Range selectCell( final int rowIndex,
                             final int columnIndex ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return new Range( rowIndex );
        }
        if ( columnIndex < 0 || columnIndex > columns.size() - 1 ) {
            return new Range( rowIndex );
        }

        return selectionsManager.onSelectCell( rowIndex,
                                               columnIndex );
    }

    @Override
    public Range selectCells( final int rowIndex,
                              final int columnIndex,
                              final int width,
                              final int height ) {
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return new Range( rowIndex );
        }
        if ( columnIndex < 0 || columnIndex > columns.size() - 1 ) {
            return new Range( rowIndex );
        }

        return selectionsManager.onSelectCells( rowIndex,
                                                columnIndex,
                                                width,
                                                height );
    }

    @Override
    public void collapseCell( final int rowIndex,
                              final int columnIndex ) {
        //Data needs to be merged to collapse cells
        if ( !isMerged ) {
            return;
        }

        final int _columnIndex = columns.get( columnIndex ).getIndex();
        final IGridRow row = rows.get( rowIndex );
        final IGridCell<?> cell = row.getCells().get( _columnIndex );
        if ( cell == null ) {
            return;
        }
        if ( !cell.isMerged() ) {
            return;
        }
        indexManager.onCollapseCell( rowIndex,
                                     _columnIndex );
    }

    @Override
    public void expandCell( final int rowIndex,
                            final int columnIndex ) {
        //Data needs to be merged to expand cells
        if ( !isMerged ) {
            return;
        }

        final int _columnIndex = columns.get( columnIndex ).getIndex();
        final IGridRow row = rows.get( rowIndex );
        final IGridCell<?> cell = row.getCells().get( _columnIndex );
        if ( cell == null ) {
            return;
        }
        indexManager.onExpandCell( rowIndex,
                                   _columnIndex );
    }

    private int findMinRowIndexForCellUpdate( final int rowIndex,
                                              final int columnIndex ) {
        int minRowIndex = rowIndex;
        final IGridRow currentRow = getRow( rowIndex );
        final IGridCell<?> currentRowCell = currentRow.getCells().get( columnIndex );

        //Find minimum row with a cell containing the same value as that being updated
        boolean foundTopSplitMarker = currentRowCell != null && currentRowCell.getMergedCellCount() > 0;
        while ( minRowIndex > 0 ) {
            final IGridRow previousRow = rows.get( minRowIndex - 1 );
            final IGridCell<?> previousRowCell = previousRow.getCells().get( columnIndex );
            if ( !( previousRow.isCollapsed() && currentRow.isCollapsed() ) ) {
                if ( previousRowCell == null ) {
                    break;
                }
                if ( previousRowCell.isCollapsed() && foundTopSplitMarker ) {
                    break;
                }
                if ( !previousRowCell.equals( currentRowCell ) ) {
                    break;
                }
                if ( previousRowCell.getMergedCellCount() > 0 ) {
                    foundTopSplitMarker = true;
                }
            }
            minRowIndex--;
        }
        return minRowIndex;
    }

    private int findMaxRowIndexForCellUpdate( final int rowIndex,
                                              final int columnIndex ) {
        int maxRowIndex = rowIndex + 1;
        final IGridRow currentRow = getRow( rowIndex );
        final IGridCell<?> currentRowCell = currentRow.getCells().get( columnIndex );

        //Find maximum row with a cell containing the same value as that being updated
        boolean foundBottomSplitMarker = false;
        while ( maxRowIndex < rows.size() ) {
            final IGridRow nextRow = rows.get( maxRowIndex );
            final IGridCell<?> nextRowCell = nextRow.getCells().get( columnIndex );
            if ( !nextRow.isCollapsed() ) {
                if ( nextRowCell == null ) {
                    break;
                }
                if ( nextRowCell.isCollapsed() && foundBottomSplitMarker ) {
                    maxRowIndex--;
                    break;
                }
                if ( !nextRowCell.equals( currentRowCell ) ) {
                    break;
                }
                if ( nextRowCell.getMergedCellCount() > 0 ) {
                    foundBottomSplitMarker = true;
                }
            }
            maxRowIndex++;
        }
        return maxRowIndex - 1;
    }

}
