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
package org.uberfire.ext.wires.core.grids.client.widget.layer.handlers;

import java.util.Set;

import com.ait.lienzo.client.core.event.INodeXYEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.types.Point2D;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.util.GridCoordinateUtils;
import org.uberfire.ext.wires.core.grids.client.widget.ISelectionManager;
import org.uberfire.ext.wires.core.grids.client.widget.grid.IBaseGridWidget;

/**
 * Base MouseClickHandler to handle clicks to either the GridWidgets Header or Body. This implementation
 * supports clicking on a "linked" column in the Header and delegating a response to the ISelectionManager.
 * @param <W> The GridWidget to which this MouseClickHandler is attached.
 */
public abstract class BaseGridWidgetMouseClickHandler<W extends IBaseGridWidget<?, ?, ?>> implements NodeMouseClickHandler {

    protected ISelectionManager selectionManager;

    public BaseGridWidgetMouseClickHandler( final ISelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    @Override
    public void onNodeMouseClick( final NodeMouseClickEvent event ) {
        final W activeGridWidget = getActiveGridWidget( event );
        selectionManager.select( activeGridWidget );
        handleHeaderCellClick( event );
        handleBodyCellClick( event );
    }

    /**
     * Check if a MouseClickEvent happened on a "linked" column. If it does then
     * delegate a response to ISelectionManager.
     * @param event
     */
    protected void handleHeaderCellClick( final NodeMouseClickEvent event ) {
        //Get GridWidget relating to event
        final W activeGridWidget = getActiveGridWidget( event );
        if ( activeGridWidget == null ) {
            return;
        }

        //Convert Canvas co-ordinate to Grid co-ordinate
        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( activeGridWidget,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );
        final double x = ap.getX();
        final double y = ap.getY();
        if ( x < 0 || x > activeGridWidget.getWidth() ) {
            return;
        }
        if ( y < 0 || y > activeGridWidget.getRenderer().getHeaderHeight() ) {
            return;
        }

        //Get column index
        double offsetX = 0;
        IGridColumn<?, ?> column = null;
        for ( IGridColumn<?, ?> gridColumn : activeGridWidget.getModel().getColumns() ) {
            if ( gridColumn.isVisible() ) {
                if ( x > offsetX && x < offsetX + gridColumn.getWidth() ) {
                    column = gridColumn;
                    break;
                }
                offsetX = offsetX + gridColumn.getWidth();
            }
        }
        if ( column == null ) {
            return;
        }

        //If linked scroll it into view
        if ( column.isLinked() ) {
            final IGridColumn<?, ?> link = column.getLink();
            selectionManager.selectLinkedColumn( link );
        }
    }

    /**
     * Does nothing by default, but allows sub-classes to provide their own behaviour.
     * @param event
     */
    protected void handleBodyCellClick( final NodeMouseClickEvent event ) {
        //Do nothing by default
    }

    @SuppressWarnings("unchecked")
    protected W getActiveGridWidget( final INodeXYEvent event ) {
        final Set<IBaseGridWidget<?, ?, ?>> gridWidgets = selectionManager.getGridWidgets();
        for ( IBaseGridWidget<?, ?, ?> gridWidget : gridWidgets ) {
            if ( accept( gridWidget ) ) {
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
                }
                return (W) gridWidget;
            }
        }
        return null;
    }

    protected abstract boolean accept( final IBaseGridWidget<?, ?, ?> gridWidget );

}
