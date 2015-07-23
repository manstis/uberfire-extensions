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

import com.ait.lienzo.client.core.event.NodeMouseDownEvent;
import com.ait.lienzo.client.core.event.NodeMouseDownHandler;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.types.Point2D;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.util.GridCoordinateUtils;
import org.uberfire.ext.wires.core.grids.client.widget.grid.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.layer.GridLayer;

/**
 * MouseDownHandler to handle the commencement of drag operations.
 */
public class GridWidgetMouseDownHandler implements NodeMouseDownHandler {

    private final GridLayer layer;
    private final GridWidgetHandlersState state;

    public GridWidgetMouseDownHandler( final GridLayer layer,
                                       final GridWidgetHandlersState state ) {
        this.layer = layer;
        this.state = state;
    }

    @Override
    public void onNodeMouseDown( final NodeMouseDownEvent event ) {
        //The Grid that the pointer is currently over is set by the MouseMoveHandler
        if ( state.getActiveGridWidget() == null || state.getActiveGridColumn() == null ) {
            return;
        }

        //Get the GridWidget for the grid.
        final IGridColumn<?, ?> activeGridColumn = state.getActiveGridColumn();
        final IBaseGridWidget<?, ?, ?> activeGridWidget = state.getActiveGridWidget();
        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( activeGridWidget,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );

        //Move from one of the pending operations to the actual operation, as appropriate.
        switch ( state.getOperation() ) {
            case COLUMN_RESIZE_PENDING:
                state.setEventInitialX( ap.getX() );
                state.setEventInitialColumnWidth( activeGridColumn.getWidth() );
                state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.COLUMN_RESIZE );
                break;

            case COLUMN_MOVE_PENDING:
                showColumnHighlight( activeGridWidget,
                                     activeGridColumn );
                state.setEventInitialX( ap.getX() );
                state.setEventInitialColumnWidth( state.getActiveGridColumn().getWidth() );
                state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.COLUMN_MOVE );
                break;
        }
    }

    private void showColumnHighlight( final IBaseGridWidget<?, ?, ?> gridWidget,
                                      final IGridColumn<?, ?> gridColumn ) {
        final IGridData<?, ?, ?> gridModel = gridWidget.getModel();
        final int columnIndex = gridModel.getColumns().indexOf( gridColumn );
        final double highlightOffsetX = gridModel.getColumnOffset( gridColumn );

        final Rectangle bounds = layer.getVisibleBounds();
        final double highlightHeight = Math.min( bounds.getY() + bounds.getHeight() - gridWidget.getY(),
                                                 gridWidget.getHeight() );

        state.getEventColumnHighlight().setWidth( gridColumn.getWidth() )
                .setHeight( highlightHeight )
                .setX( gridWidget.getX() + highlightOffsetX )
                .setY( gridWidget.getY() );
        layer.add( state.getEventColumnHighlight() );
        layer.getLayer().draw();
    }

}
