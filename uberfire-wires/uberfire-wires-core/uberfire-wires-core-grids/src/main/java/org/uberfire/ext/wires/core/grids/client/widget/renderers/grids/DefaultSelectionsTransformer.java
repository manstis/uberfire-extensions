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
package org.uberfire.ext.wires.core.grids.client.widget.renderers.grids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;

/**
 * Helper functions to convert SelectedCells into SelectedRanges.
 */
public class DefaultSelectionsTransformer implements ISelectionsTransformer {

    protected final IGridData model;
    protected final List<IGridColumn<?>> columns;

    public DefaultSelectionsTransformer( final IGridData model,
                                         final List<IGridColumn<?>> columns ) {
        this.model = PortablePreconditions.checkNotNull( "model",
                                                         model );
        this.columns = PortablePreconditions.checkNotNull( "columns",
                                                           columns );
    }

    @Override
    public List<SelectedRange> transformToSelectedRanges() {
        //Group into vertical ranges translating modelColumnIndexes to uiColumnIndexes
        int currentUiColumnIndex = -1;
        SelectedRange currentRange = null;
        final List<IGridData.SelectedCell> orderedSelectedCells = sortSelectedCells();
        final Map<Integer, List<SelectedRange>> orderedSelectedRanges = new TreeMap<Integer, List<SelectedRange>>();

        for ( IGridData.SelectedCell selectedCell : orderedSelectedCells ) {
            final int scRowIndex = selectedCell.getRowIndex();
            final int scColumnIndex = selectedCell.getColumnIndex();
            final int uiColumnIndex = findUiColumnIndex( scColumnIndex );
            if ( uiColumnIndex != currentUiColumnIndex ) {
                storeSelectedRange( orderedSelectedRanges,
                                    currentRange,
                                    currentUiColumnIndex );
                currentUiColumnIndex = uiColumnIndex;
                currentRange = new SelectedRange( scRowIndex,
                                                  uiColumnIndex,
                                                  1,
                                                  1 );
            } else if ( scRowIndex == currentRange.getUiRowIndex() + currentRange.getHeight() ) {
                currentRange.setHeight( currentRange.getHeight() + 1 );
            } else {
                storeSelectedRange( orderedSelectedRanges,
                                    currentRange,
                                    uiColumnIndex );
                currentRange = new SelectedRange( scRowIndex,
                                                  uiColumnIndex,
                                                  1,
                                                  1 );
            }
        }
        storeSelectedRange( orderedSelectedRanges,
                            currentRange,
                            currentUiColumnIndex );

        //Group vertical ranges horizontally
        final int maxColumnIndex = getMaximumColumnIndex( orderedSelectedRanges );
        for ( Map.Entry<Integer, List<SelectedRange>> e : orderedSelectedRanges.entrySet() ) {
            for ( SelectedRange selectedRange : e.getValue() ) {
                for ( int mergeColumnIndex = e.getKey() + 1; mergeColumnIndex <= maxColumnIndex; mergeColumnIndex++ ) {
                    final List<SelectedRange> mergeRanges = orderedSelectedRanges.get( mergeColumnIndex );
                    if ( mergeRanges == null ) {
                        break;
                    }
                    final Iterator<SelectedRange> srIterator = mergeRanges.iterator();
                    while ( srIterator.hasNext() ) {
                        final SelectedRange mergeRange = srIterator.next();
                        if ( selectedRange.getUiRowIndex() == mergeRange.getUiRowIndex() ) {
                            if ( selectedRange.getUiColumnIndex() + selectedRange.getWidth() == mergeRange.getUiColumnIndex() ) {
                                if ( selectedRange.getHeight() == mergeRange.getHeight() ) {
                                    selectedRange.setWidth( selectedRange.getWidth() + 1 );
                                    srIterator.remove();
                                }
                            }
                        }
                    }
                }
            }
        }

        //Dump into a single list
        final List<SelectedRange> selectedRanges = new ArrayList<SelectedRange>();
        for ( List<SelectedRange> ranges : orderedSelectedRanges.values() ) {
            selectedRanges.addAll( ranges );
        }

        return selectedRanges;
    }

    private int getMaximumColumnIndex( final Map<Integer, List<SelectedRange>> selectedRanges ) {
        int maxColumnIndex = 0;
        for ( Integer idx : selectedRanges.keySet() ) {
            maxColumnIndex = Math.max( maxColumnIndex,
                                       idx );
        }
        return maxColumnIndex;
    }

    private void storeSelectedRange( final Map<Integer, List<SelectedRange>> orderedSelectedRanges,
                                     final SelectedRange currentRange,
                                     final int uiColumnIndex ) {
        if ( currentRange != null ) {
            List<SelectedRange> selectedRanges = orderedSelectedRanges.get( uiColumnIndex );
            if ( selectedRanges == null ) {
                selectedRanges = new ArrayList<SelectedRange>();
                orderedSelectedRanges.put( uiColumnIndex,
                                           selectedRanges );
            }
            selectedRanges.add( currentRange );
        }
    }

    protected int findUiColumnIndex( final int modelColumnIndex ) {
        for ( int uiColumnIndex = 0; uiColumnIndex < model.getColumns().size(); uiColumnIndex++ ) {
            final IGridColumn<?> c = model.getColumns().get( uiColumnIndex );
            if ( c.getIndex() == modelColumnIndex ) {
                return uiColumnIndex;
            }
        }
        throw new IllegalStateException( "Column was not found!" );
    }

    //Sort arbitrary selections by column->row to simplify grouping
    private List<IGridData.SelectedCell> sortSelectedCells() {
        final List<IGridData.SelectedCell> selectedCells = new ArrayList<IGridData.SelectedCell>();
        for ( IGridData.SelectedCell sc : model.getSelectedCells() ) {
            if ( isSelectionInColumns( sc ) ) {
                selectedCells.add( sc );
            }
        }
        final int rowCount = model.getRowCount();
        Collections.sort( selectedCells,
                          new Comparator<IGridData.SelectedCell>() {

                              @Override
                              public int compare( final IGridData.SelectedCell o1,
                                                  final IGridData.SelectedCell o2 ) {
                                  //(0,0)->0, (1,0)->3, (2,0)->6
                                  //(0,1)->1, (1,1)->4, (2,1)->7
                                  //(0,2)->2, (1,2)->5, (2,2)->8
                                  final int o1Index = o1.getRowIndex() + o1.getColumnIndex() * rowCount;
                                  final int o2Index = o2.getRowIndex() + o2.getColumnIndex() * rowCount;
                                  return o1Index - o2Index;
                              }
                          } );
        return selectedCells;
    }

    private boolean isSelectionInColumns( final IGridData.SelectedCell sc ) {
        final int scColumnIndex = sc.getColumnIndex();
        for ( IGridColumn<?> column : columns ) {
            final int columnIndex = column.getIndex();
            if ( scColumnIndex == columnIndex ) {
                return true;
            }
        }
        return false;
    }

}
