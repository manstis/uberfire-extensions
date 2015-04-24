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
package org.uberfire.ext.wires.core.grids.client.model.basic;

import java.util.HashMap;
import java.util.Map;

import org.uberfire.ext.wires.core.grids.client.model.BaseGridData;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;

/**
 * Implementation of IGridData that cannot contain merged cells.
 */
public class GridData extends BaseGridData<GridRow, GridColumn<?>, GridCell<?>> {

    @Override
    public void updateColumn( final int index,
                              final GridColumn<?> column ) {
        //Destroy existing column
        final GridColumn<?> existing = columns.get( index );
        existing.destroyResources();

        //Replace existing with new column
        column.setIndex( columns.get( index ).getIndex() );
        columns.set( index,
                     column );

        //Clear column data
        for ( GridRow row : rows ) {
            row.deleteCell( column.getIndex() );
        }
    }

    @Override
    public void deleteColumn( final GridColumn<?> column ) {
        final int index = column.getIndex();
        for ( GridColumn<?> c : columns ) {
            if ( c.getIndex() > index ) {
                c.setIndex( c.getIndex() - 1 );
            }
        }

        //Destroy column
        column.destroyResources();
        columns.remove( column );

        for ( GridRow row : rows ) {
            row.deleteCell( index );
            final Map<Integer, GridCell<?>> clone = new HashMap<Integer, GridCell<?>>( row.getCells() );
            for ( Map.Entry<Integer, GridCell<?>> e : clone.entrySet() ) {
                if ( e.getKey() > index ) {
                    row.deleteCell( e.getKey() );
                    row.setCell( e.getKey() - 1,
                                 e.getValue().getValue() );
                }
            }
        }
    }

    @Override
    public void deleteRow( final int rowIndex ) {
        rows.remove( rowIndex );
    }

    @Override
    public Range setCell( final int rowIndex,
                          final int columnIndex,
                          final IGridCellValue<?> value ) {
        final Range range = new Range( rowIndex );
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return range;
        }
        if ( value == null ) {
            return range;
        }

        final GridRow row = rows.get( rowIndex );
        final int _columnIndex = columns.get( columnIndex ).getIndex();
        row.setCell( _columnIndex,
                     value );

        return range;
    }

    @Override
    public Range deleteCell( final int rowIndex,
                             final int columnIndex ) {
        final Range range = new Range( rowIndex );
        if ( rowIndex < 0 || rowIndex > rows.size() - 1 ) {
            return range;
        }
        final GridRow row = rows.get( rowIndex );
        final int _columnIndex = columns.get( columnIndex ).getIndex();
        row.deleteCell( _columnIndex );

        return range;
    }

}
