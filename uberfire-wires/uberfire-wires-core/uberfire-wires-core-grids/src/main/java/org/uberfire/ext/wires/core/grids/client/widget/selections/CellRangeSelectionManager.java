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

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;

public class CellRangeSelectionManager extends BaseCellSelectionManager {

    public static ICellSelectionManager INSTANCE = new CellRangeSelectionManager();

    @Override
    public boolean handleSelection( final NodeMouseClickEvent event,
                                    final int uiRowIndex,
                                    final int uiColumnIndex,
                                    final IGridData model ) {
        //Remember origin which is required if we're selecting a range with the shift-key pressed
        final IGridData.SelectedCell selectedCellsOrigin = model.getSelectedCellsOrigin();
        final List<IGridData.SelectedCell> originalSelections = new ArrayList<IGridData.SelectedCell>( model.getSelectedCells() );

        //If the Control Key is pressed add additional cells to the selection
        if ( !event.isControlKeyDown() ) {
            model.clearSelections();
        }

        if ( event.isShiftKeyDown() ) {
            if ( selectedCellsOrigin == null ) {
                model.selectCell( uiRowIndex,
                                  uiColumnIndex );
            } else {
                model.selectCell( selectedCellsOrigin.getRowIndex(),
                                  selectedCellsOrigin.getColumnIndex() );
                final int uiOriginRowIndex = selectedCellsOrigin.getRowIndex();
                final int uiOriginColumnIndex = findUiColumnIndex( model,
                                                                   selectedCellsOrigin.getColumnIndex() );
                model.selectCells( ( uiRowIndex > uiOriginRowIndex ? uiOriginRowIndex : uiRowIndex ),
                                   ( uiColumnIndex > uiOriginColumnIndex ? uiOriginColumnIndex : uiColumnIndex ),
                                   Math.abs( uiColumnIndex - uiOriginColumnIndex ) + 1,
                                   Math.abs( uiRowIndex - uiOriginRowIndex ) + 1 );
            }

        } else {
            model.selectCell( uiRowIndex,
                              uiColumnIndex );
        }

        return hasSelectionChanged( model.getSelectedCells(),
                                    originalSelections );
    }

}
