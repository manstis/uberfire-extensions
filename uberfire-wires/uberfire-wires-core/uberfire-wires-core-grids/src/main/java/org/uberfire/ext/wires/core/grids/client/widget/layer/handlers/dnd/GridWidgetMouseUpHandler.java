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
package org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.dnd;

import com.ait.lienzo.client.core.event.NodeMouseUpEvent;
import com.ait.lienzo.client.core.event.NodeMouseUpHandler;
import com.google.gwt.dom.client.Style;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;
import org.uberfire.ext.wires.core.grids.client.widget.grid.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.layer.GridLayer;

/**
 * MouseUpHandler to handle completion of drag operations and release resources.
 */
public class GridWidgetMouseUpHandler<W extends IBaseGridWidget<?, M, ?>, M extends IGridData<R, C, V>, R extends IGridRow<V>, C extends IGridColumn<R, V>, V extends IGridCell<?>> implements NodeMouseUpHandler {

    private final GridLayer layer;
    private final GridWidgetHandlersState<W, M, R, C, V> state;

    public GridWidgetMouseUpHandler( final GridLayer layer,
                                     final GridWidgetHandlersState<W, M, R, C, V> state ) {
        this.layer = layer;
        this.state = state;
    }

    @Override
    public void onNodeMouseUp( final NodeMouseUpEvent event ) {
        switch ( state.getOperation() ) {
            case NONE:
            case COLUMN_MOVE_PENDING:
            case COLUMN_RESIZE_PENDING:
            case COLUMN_RESIZE:
                break;
            case COLUMN_MOVE:
                //Clean-up the GridWidgetColumnProxy
                layer.remove( state.getEventColumnHighlight() );
                layer.draw();
        }

        //Reset state
        state.setActiveGridWidget( null );
        state.setActiveGridColumn( null );
        state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.NONE );
        state.setCursor( Style.Cursor.DEFAULT );
        layer.getViewport().getElement().getStyle().setCursor( state.getCursor() );
    }

}
