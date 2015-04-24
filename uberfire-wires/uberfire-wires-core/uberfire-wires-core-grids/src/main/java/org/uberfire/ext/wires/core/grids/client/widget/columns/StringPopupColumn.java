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
package org.uberfire.ext.wires.core.grids.client.widget.columns;

import java.util.List;

import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridCell;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.edit.EditorPopup;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.IGridColumnRenderer;

public class StringPopupColumn extends BaseGridColumn<String> {

    private final EditorPopup editor = new EditorPopup();

    public StringPopupColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                              final IGridColumnRenderer<String> columnRenderer,
                              final double width ) {
        super( headerMetaData,
               columnRenderer,
               width );
    }

    public StringPopupColumn( final IGridColumn.HeaderMetaData headerMetaData,
                              final IGridColumnRenderer<String> columnRenderer,
                              final double width ) {
        super( headerMetaData,
               columnRenderer,
               width );
    }

    @Override
    public void edit( final IGridCell<String> cell,
                      final GridBodyCellRenderContext context,
                      final Callback<IGridCellValue<String>> callback ) {
        editor.edit( assertCell( cell ).getValue(),
                     callback );
    }

    private IGridCell<String> assertCell( final IGridCell<String> cell ) {
        if ( cell != null ) {
            return cell;
        }
        return new BaseGridCell<String>( new BaseGridCellValue<String>( "" ) );
    }

}