/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.wires.core.grids.client.model;

import java.util.ArrayList;
import java.util.List;

public class BaseGridDataSelectionsManager {

    private final BaseGridData gridData;

    public BaseGridDataSelectionsManager( final BaseGridData gridData ) {
        this.gridData = gridData;
    }

    public void onMerge( final boolean isMerged ) {
        if ( isMerged ) {
            final List<IGridData.SelectedCell> selectedCells = gridData.getSelectedCells();
            final List<IGridData.SelectedCell> cloneSelectedCells = new ArrayList<IGridData.SelectedCell>( selectedCells );
            gridData.clearSelections();
            for ( IGridData.SelectedCell cell : cloneSelectedCells ) {
                gridData.selectCells( cell.getRowIndex(),
                                      findUiColumnIndex( cell.getColumnIndex() ),
                                      1,
                                      1 );
            }
        }
    }

    private int findUiColumnIndex( final int modelColumnIndex ) {
        final List<IGridColumn<?>> columns = gridData.getColumns();
        for ( int uiColumnIndex = 0; uiColumnIndex < columns.size(); uiColumnIndex++ ) {
            final IGridColumn<?> c = columns.get( uiColumnIndex );
            if ( c.getIndex() == modelColumnIndex ) {
                return uiColumnIndex;
            }
        }
        throw new IllegalStateException( "Column was not found!" );
    }

    public void onDeleteColumn( final int index ) {
        final List<IGridData.SelectedCell> selectedCells = gridData.getSelectedCells();
        final List<IGridData.SelectedCell> selectedCellsToRemove = new ArrayList<IGridData.SelectedCell>();
        final List<IGridData.SelectedCell> selectedCellsToUpdate = new ArrayList<IGridData.SelectedCell>();
        for ( IGridData.SelectedCell sc : selectedCells ) {
            if ( sc.getColumnIndex() == index ) {
                selectedCellsToRemove.add( sc );
            } else if ( sc.getColumnIndex() > index ) {
                selectedCellsToUpdate.add( sc );
            }
        }
        selectedCells.removeAll( selectedCellsToRemove );
        selectedCells.removeAll( selectedCellsToUpdate );
        for ( IGridData.SelectedCell sc : selectedCellsToUpdate ) {
            selectedCells.add( new IGridData.SelectedCell( sc.getRowIndex(),
                                                           sc.getColumnIndex() - 1 ) );
        }
    }

    public void onInsertRow( final int rowIndex ) {
        final List<IGridData.SelectedCell> selectedCells = gridData.getSelectedCells();
        final List<IGridData.SelectedCell> selectedCellsToUpdate = new ArrayList<IGridData.SelectedCell>();
        for ( IGridData.SelectedCell sc : selectedCells ) {
            if ( sc.getRowIndex() >= rowIndex ) {
                selectedCellsToUpdate.add( sc );
            }
        }
        selectedCells.removeAll( selectedCellsToUpdate );
        for ( IGridData.SelectedCell sc : selectedCellsToUpdate ) {
            selectedCells.add( new IGridData.SelectedCell( sc.getRowIndex() + 1,
                                                           sc.getColumnIndex() ) );
        }
    }

    public void onDeleteRow( final IGridData.Range range ) {
        final int minRowIndex = range.getMinRowIndex();
        final int maxRowIndex = range.getMaxRowIndex();
        final List<IGridData.SelectedCell> selectedCells = gridData.getSelectedCells();
        final List<IGridData.SelectedCell> selectedCellsToRemove = new ArrayList<IGridData.SelectedCell>();
        final List<IGridData.SelectedCell> selectedCellsToUpdate = new ArrayList<IGridData.SelectedCell>();
        for ( IGridData.SelectedCell sc : selectedCells ) {
            if ( sc.getRowIndex() >= minRowIndex && sc.getRowIndex() <= maxRowIndex ) {
                selectedCellsToRemove.add( sc );
            } else if ( sc.getRowIndex() > maxRowIndex ) {
                selectedCellsToUpdate.add( sc );
            }
        }
        selectedCells.removeAll( selectedCellsToRemove );
        selectedCells.removeAll( selectedCellsToUpdate );
        for ( IGridData.SelectedCell sc : selectedCellsToUpdate ) {
            selectedCells.add( new IGridData.SelectedCell( sc.getRowIndex() - 1,
                                                           sc.getColumnIndex() ) );
        }
    }

    public IGridData.Range onSelectCell( final int rowIndex,
                                         final int columnIndex ) {
        if ( gridData.isMerged() ) {
            return selectCellMerged( rowIndex,
                                     columnIndex );
        } else {
            selectCellNotMerged( rowIndex,
                                 columnIndex );
            return new IGridData.Range( rowIndex );
        }
    }

    public IGridData.Range onSelectCells( final int rowIndex,
                                          final int columnIndex,
                                          final int width,
                                          final int height ) {
        //If we're not merged just set the value of a single cell
        if ( !gridData.isMerged() ) {
            selectCellsNotMerged( rowIndex,
                                  columnIndex,
                                  width,
                                  height );
            return new IGridData.Range( rowIndex );
        }

        //Find affected rows for merged data
        int _columnIndex;
        int minRowIndex = rowIndex;
        int maxRowIndex = rowIndex + height - 1;
        final List<IGridColumn<?>> columns = gridData.getColumns();
        for ( int ci = columnIndex; ci < columnIndex + width; ci++ ) {
            _columnIndex = columns.get( ci ).getIndex();
            minRowIndex = Math.min( minRowIndex,
                                    findMinRowIndex( minRowIndex,
                                                     _columnIndex ) );
            maxRowIndex = Math.max( maxRowIndex,
                                    findMaxRowIndex( maxRowIndex,
                                                     _columnIndex ) );
        }

        //Select all applicable rows' cells
        selectCellsNotMerged( minRowIndex,
                              columnIndex,
                              width,
                              maxRowIndex - minRowIndex + 1 );

        return new IGridData.Range( minRowIndex,
                                    maxRowIndex );
    }

    private IGridData.Range selectCellMerged( final int rowIndex,
                                              final int columnIndex ) {
        //Find affected rows for merged data
        final List<IGridColumn<?>> columns = gridData.getColumns();
        final int _columnIndex = columns.get( columnIndex ).getIndex();
        final int minRowIndex = findMinRowIndex( rowIndex,
                                                 _columnIndex );
        final int maxRowIndex = findMaxRowIndex( rowIndex,
                                                 _columnIndex );

        //Select all applicable rows' cells
        selectCellsNotMerged( minRowIndex,
                              columnIndex,
                              1,
                              maxRowIndex - minRowIndex + 1 );

        return new IGridData.Range( minRowIndex,
                                    maxRowIndex );
    }

    private IGridData.Range selectCellNotMerged( final int rowIndex,
                                                 final int columnIndex ) {
        final List<IGridRow> rows = gridData.getRows();
        final List<IGridColumn<?>> columns = gridData.getColumns();
        final List<IGridData.SelectedCell> selectedCells = gridData.getSelectedCells();
        final IGridData.Range range = new IGridData.Range( rowIndex );
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return range;
        }
        if ( columnIndex < 0 || columnIndex > columns.size() - 1 ) {
            return range;
        }
        final int _columnIndex = columns.get( columnIndex ).getIndex();
        final IGridData.SelectedCell selectedCell = new IGridData.SelectedCell( rowIndex,
                                                                                _columnIndex );

        if ( !selectedCells.contains( selectedCell ) ) {
            selectedCells.add( selectedCell );
        }

        return range;
    }

    private IGridData.Range selectCellsNotMerged( final int rowIndex,
                                                  final int columnIndex,
                                                  final int width,
                                                  final int height ) {
        final List<IGridRow> rows = gridData.getRows();
        final List<IGridColumn<?>> columns = gridData.getColumns();
        final List<IGridData.SelectedCell> selectedCells = gridData.getSelectedCells();
        final IGridData.Range range = new IGridData.Range( rowIndex );
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return range;
        }
        if ( columnIndex < 0 || columnIndex > columns.size() - 1 ) {
            return range;
        }
        if ( width < 1 ) {
            return range;
        }
        if ( height < 1 ) {
            return range;
        }
        for ( int ri = rowIndex; ri < rowIndex + height; ri++ ) {
            for ( int ci = columnIndex; ci < columnIndex + width; ci++ ) {
                final int _columnIndex = columns.get( ci ).getIndex();
                final IGridData.SelectedCell selectedCell = new IGridData.SelectedCell( ri,
                                                                                        _columnIndex );
                if ( !selectedCells.contains( selectedCell ) ) {
                    selectedCells.add( selectedCell );
                }
            }
        }

        return new IGridData.Range( rowIndex,
                                    rowIndex + height - 1 );
    }

    private int findMinRowIndex( final int rowIndex,
                                 final int columnIndex ) {
        int minRowIndex = rowIndex;
        final IGridRow currentRow = gridData.getRow( rowIndex );
        final IGridCell<?> currentRowCell = currentRow.getCells().get( columnIndex );

        //Find minimum row with a cell containing the same value as that being updated
        boolean foundTopSplitMarker = currentRowCell != null && currentRowCell.getMergedCellCount() > 0;
        while ( minRowIndex > 0 ) {
            final IGridRow previousRow = gridData.getRow( minRowIndex - 1 );
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

    private int findMaxRowIndex( final int rowIndex,
                                 final int columnIndex ) {
        int maxRowIndex = rowIndex + 1;
        final IGridRow currentRow = gridData.getRow( rowIndex );
        final IGridCell<?> currentRowCell = currentRow.getCells().get( columnIndex );

        //Find maximum row with a cell containing the same value as that being updated
        boolean foundBottomSplitMarker = false;
        while ( maxRowIndex < gridData.getRowCount() ) {
            final IGridRow nextRow = gridData.getRow( maxRowIndex );
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

    public void onMoveRows( final List<IGridRow> rowsMoved,
                            final IGridData.Range oldBlockExtent ) {
        final List<IGridRow> rows = gridData.getRows();
        final int oldBlockStart = oldBlockExtent.getMinRowIndex();
        final int oldBlockEnd = oldBlockExtent.getMaxRowIndex();
        final int newBlockStart = rows.indexOf( rowsMoved.get( 0 ) );
        final int newBlockEnd = rows.indexOf( rowsMoved.get( rowsMoved.size() - 1 ) );
        final List<IGridData.SelectedCell> selectedCells = gridData.getSelectedCells();
        final List<IGridData.SelectedCell> selectedCellsToMoveUp = new ArrayList<IGridData.SelectedCell>();
        final List<IGridData.SelectedCell> selectedCellsToMoveDown = new ArrayList<IGridData.SelectedCell>();
        final List<IGridData.SelectedCell> selectedCellsToUpdate = new ArrayList<IGridData.SelectedCell>();

        if ( newBlockStart < oldBlockStart ) {
            //Moving row(s) up
            for ( IGridData.SelectedCell sc : selectedCells ) {
                if ( sc.getRowIndex() >= oldBlockStart && sc.getRowIndex() <= oldBlockEnd ) {
                    selectedCellsToMoveUp.add( sc );

                } else if ( sc.getRowIndex() >= newBlockStart && sc.getRowIndex() <= newBlockEnd ) {
                    selectedCellsToMoveDown.add( sc );

                } else if ( sc.getRowIndex() > newBlockEnd && sc.getRowIndex() < oldBlockStart ) {
                    selectedCellsToUpdate.add( sc );
                }
            }
            selectedCells.removeAll( selectedCellsToMoveUp );
            selectedCells.removeAll( selectedCellsToMoveDown );
            selectedCells.removeAll( selectedCellsToUpdate );
            for ( IGridData.SelectedCell sc : selectedCellsToMoveUp ) {
                selectedCells.add( new IGridData.SelectedCell( sc.getRowIndex() - ( oldBlockStart - newBlockStart ),
                                                               sc.getColumnIndex() ) );
            }
            for ( IGridData.SelectedCell sc : selectedCellsToMoveDown ) {
                selectedCells.add( new IGridData.SelectedCell( sc.getRowIndex() + ( oldBlockEnd - oldBlockStart ) + 1,
                                                               sc.getColumnIndex() ) );
            }
            for ( IGridData.SelectedCell sc : selectedCellsToUpdate ) {
                selectedCells.add( new IGridData.SelectedCell( sc.getRowIndex() + ( oldBlockEnd - oldBlockStart ) + 1,
                                                               sc.getColumnIndex() ) );
            }

        } else if ( newBlockStart > oldBlockStart ) {
            //Moving row(s) down
            for ( IGridData.SelectedCell sc : selectedCells ) {
                if ( sc.getRowIndex() >= oldBlockStart && sc.getRowIndex() <= oldBlockEnd ) {
                    selectedCellsToMoveDown.add( sc );

                } else if ( sc.getRowIndex() >= newBlockStart && sc.getRowIndex() <= newBlockEnd ) {
                    selectedCellsToMoveUp.add( sc );

                } else if ( sc.getRowIndex() > oldBlockEnd && sc.getRowIndex() < newBlockStart ) {
                    selectedCellsToUpdate.add( sc );
                }
            }
            selectedCells.removeAll( selectedCellsToMoveUp );
            selectedCells.removeAll( selectedCellsToMoveDown );
            selectedCells.removeAll( selectedCellsToUpdate );
            for ( IGridData.SelectedCell sc : selectedCellsToMoveUp ) {
                selectedCells.add( new IGridData.SelectedCell( sc.getRowIndex() - ( oldBlockEnd - oldBlockStart ) - 1,
                                                               sc.getColumnIndex() ) );
            }
            for ( IGridData.SelectedCell sc : selectedCellsToMoveDown ) {
                selectedCells.add( new IGridData.SelectedCell( sc.getRowIndex() + ( newBlockStart - oldBlockStart ),
                                                               sc.getColumnIndex() ) );
            }
            for ( IGridData.SelectedCell sc : selectedCellsToUpdate ) {
                selectedCells.add( new IGridData.SelectedCell( sc.getRowIndex() - ( newBlockEnd - newBlockStart ) - 1,
                                                               sc.getColumnIndex() ) );
            }
        }
    }

}
