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
package org.uberfire.ext.wires.core.grids.client.widget.renderers.columns;

import java.util.List;

import com.ait.lienzo.client.core.shape.Group;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyColumnRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridHeaderCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.BaseGridRendererHelper;

public interface IGridColumnRenderer<T> {

    /**
     * Render the column's Header..
     * @param headerMetaData MetaData for the header
     * @param context Contextual information to support rendering
     * @return
     */
    Group renderHeader( final List<IGridColumn.HeaderMetaData> headerMetaData,
                        final GridHeaderCellRenderContext context );

    /**
     * Render the column.textual information to support rendering
     * @param column The column to render
     * @param context Contextual information to support rendering
     * @return
     */
    Group renderColumn( final IGridColumn<?> column,
                        final GridBodyColumnRenderContext context,
                        final BaseGridRendererHelper rendererHelper );

    /**
     * Render a cell for the column for a row. Normally a column would use its logical index
     * to retrieve the corresponding element from the row to be rendered.
     * @param cell The cell to render
     * @param context Contextual information to support rendering
     * @return
     */
    Group renderCell( final IGridCell<T> cell,
                      final GridBodyCellRenderContext context );

}
