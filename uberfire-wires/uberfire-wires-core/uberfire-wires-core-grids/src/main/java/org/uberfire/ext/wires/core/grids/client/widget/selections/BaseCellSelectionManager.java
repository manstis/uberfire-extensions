/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.uberfire.ext.wires.core.grids.client.widget.selections;

import java.util.ArrayList;
import java.util.List;

import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;

public abstract class BaseCellSelectionManager implements ICellSelectionManager {

    protected int findUiColumnIndex( final IGridData model,
                                     final int modelColumnIndex ) {
        for ( int uiColumnIndex = 0; uiColumnIndex < model.getColumns().size(); uiColumnIndex++ ) {
            final IGridColumn<?> c = model.getColumns().get( uiColumnIndex );
            if ( c.getIndex() == modelColumnIndex ) {
                return uiColumnIndex;
            }
        }
        throw new IllegalStateException( "Column was not found!" );
    }

    protected boolean hasSelectionChanged( final List<IGridData.SelectedCell> currentSelections,
                                           final List<IGridData.SelectedCell> originalSelections ) {
        final List<IGridData.SelectedCell> cloneCurrentSelections = new ArrayList<IGridData.SelectedCell>( currentSelections );
        final List<IGridData.SelectedCell> cloneOriginalSelections = new ArrayList<IGridData.SelectedCell>( originalSelections );
        cloneCurrentSelections.removeAll( originalSelections );
        cloneOriginalSelections.removeAll( currentSelections );
        return cloneCurrentSelections.size() > 0 || cloneOriginalSelections.size() > 0;
    }

}
