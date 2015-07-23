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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.event.NodeMouseMoveHandler;
import com.ait.lienzo.client.core.mediator.IMediator;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.dom.client.Style;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;
import org.uberfire.ext.wires.core.grids.client.util.GridCoordinateUtils;
import org.uberfire.ext.wires.core.grids.client.widget.grid.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.layer.GridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.IGridRenderer;

/**
 * MouseMoveHandler to handle to detect potential drag operations and handle the drag itself; if required.
 */
public class GridWidgetMouseMoveHandler<W extends IBaseGridWidget<?, M, ?>, M extends IGridData<R, C, V>, R extends IGridRow<V>, C extends IGridColumn<R, V>, V extends IGridCell<?>> implements NodeMouseMoveHandler {

    // How close the mouse pointer needs to be to the column separator to initiate a resize operation.
    private static final int COLUMN_RESIZE_HANDLE_SENSITIVITY = 5;

    private final GridLayer layer;
    private final GridWidgetHandlersState<W, M, R, C, V> state;

    public GridWidgetMouseMoveHandler( final GridLayer layer,
                                       final GridWidgetHandlersState<W, M, R, C, V> state ) {
        this.layer = layer;
        this.state = state;
    }

    @Override
    public void onNodeMouseMove( final NodeMouseMoveEvent event ) {
        switch ( state.getOperation() ) {
            case COLUMN_RESIZE:
                //If we're currently resizing a column we don't need to find a column
                handleColumnResize( event );
                break;

            case COLUMN_MOVE:
                //If we're currently moving a column we don't need to find a column
                handleColumnMove( event );
                break;

            default:
                //Otherwise try to find a Grid and GridColumn
                state.setActiveGridColumn( null );
                state.setActiveGridWidget( null );
                state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.NONE );
                state.setCursor( Style.Cursor.DEFAULT );

                final Set<W> gridWidgets = getGridWidgets();
                for ( W gridWidget : gridWidgets ) {
                    final M gridModel = gridWidget.getModel();
                    final IGridRenderer<?> renderer = gridWidget.getRenderer();
                    final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( gridWidget,
                                                                                         new Point2D( event.getX(),
                                                                                                      event.getY() ) );

                    final double ax = ap.getX();
                    final double ay = ap.getY();
                    if ( ax < 0 || ax > gridWidget.getWidth() ) {
                        continue;
                    }
                    if ( ay < 0 || ay > gridWidget.getHeight() ) {
                        continue;
                    } else if ( ay < renderer.getHeaderHeight() ) {
                        double offsetX = 0;
                        for ( C gc : gridModel.getColumns() ) {
                            //Check for column moving
                            if ( gc.isVisible() ) {
                                final double columnWidth = gc.getWidth();
                                if ( gc.isMoveable() ) {
                                    if ( ax > offsetX && ax < offsetX + columnWidth ) {
                                        state.setActiveGridColumn( gc );
                                        state.setActiveGridWidget( gridWidget );
                                        state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.COLUMN_MOVE_PENDING );
                                        state.setCursor( Style.Cursor.MOVE );
                                        break;
                                    }
                                }
                                offsetX = offsetX + columnWidth;
                            }
                        }

                    } else {
                        double offsetX = 0;
                        for ( C gc : gridModel.getColumns() ) {
                            //Check for column resizing
                            if ( gc.isVisible() ) {
                                final double columnWidth = gc.getWidth();
                                if ( gc.isResizable() ) {
                                    if ( ax > columnWidth + offsetX - COLUMN_RESIZE_HANDLE_SENSITIVITY && ax < columnWidth + offsetX + COLUMN_RESIZE_HANDLE_SENSITIVITY ) {
                                        state.setActiveGridColumn( gc );
                                        state.setActiveGridWidget( gridWidget );
                                        state.setOperation( GridWidgetHandlersState.GridWidgetHandlersOperation.COLUMN_RESIZE_PENDING );
                                        state.setCursor( Style.Cursor.COL_RESIZE );
                                        break;
                                    }
                                }
                                offsetX = offsetX + columnWidth;
                            }
                        }
                    }
                }

                layer.getViewport().getElement().getStyle().setCursor( state.getCursor() );
                for ( IMediator mediator : layer.getViewport().getMediators() ) {
                    mediator.setEnabled( state.getActiveGridWidget() == null );
                }
        }
        event.getHumanInputEvent().stopPropagation();
        event.getHumanInputEvent().preventDefault();
    }

    private void handleColumnResize( final NodeMouseMoveEvent event ) {
        final IGridColumn gridColumn = state.getActiveGridColumn();
        final IBaseGridWidget<?, ?, ?> gridWidget = state.getActiveGridWidget();
        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( gridWidget,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );
        final double deltaX = ap.getX() - state.getEventInitialX();
        final Double columnMinimumWidth = gridColumn.getMinimumWidth();
        final Double columnMaxiumumWidth = gridColumn.getMaximumWidth();
        double columnNewWidth = state.getEventInitialColumnWidth() + deltaX;
        if ( columnMinimumWidth != null ) {
            if ( columnNewWidth < columnMinimumWidth ) {
                columnNewWidth = columnMinimumWidth;
            }
        }
        if ( columnMaxiumumWidth != null ) {
            if ( columnNewWidth > columnMaxiumumWidth ) {
                columnNewWidth = columnMaxiumumWidth;
            }
        }
        gridColumn.setWidth( columnNewWidth );
        layer.batch();
        return;
    }

    private void handleColumnMove( final NodeMouseMoveEvent event ) {
        final W activeGridWidget = state.getActiveGridWidget();
        final M activeGridModel = activeGridWidget.getModel();
        final List<C> columns = activeGridModel.getColumns();
        final C activeGridColumn = state.getActiveGridColumn();

        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( activeGridWidget,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );
        final double ax = ap.getX();

        double offsetX = 0;
        for ( int index = 0; index < columns.size(); index++ ) {
            final C gc = columns.get( index );
            if ( gc.isVisible() ) {
                final double columnWidth = gc.getWidth();
                if ( activeGridColumn.getColumnGroup().equals( gc.getColumnGroup() ) ) {
                    final double columnMovedWidth = activeGridColumn.getWidth();
                    final double minColX = Math.max( offsetX, offsetX + ( columnWidth - columnMovedWidth ) / 2 );
                    final double maxColX = Math.min( offsetX + columnWidth, offsetX + ( columnWidth + columnMovedWidth ) / 2 );
                    if ( ax > minColX && ax < maxColX ) {
                        activeGridModel.moveColumnTo( index,
                                                      activeGridColumn );
                        state.getEventColumnHighlight().setX( activeGridWidget.getX() + activeGridModel.getColumnOffset( activeGridColumn ) );
                        layer.batch();
                        break;
                    }
                }
                offsetX = offsetX + columnWidth;
            }
        }
    }

    private Set<W> getGridWidgets() {
        final Set<W> gridWidgets = new HashSet<W>();
        for ( IBaseGridWidget<?, ?, ?> gridWidget : layer.getGridWidgets() ) {
            try {
                final W g = ( (W) gridWidget );
                gridWidgets.add( g );

            } catch ( ClassCastException cce ) {
                continue;
            }
        }
        return gridWidgets;
    }

}
