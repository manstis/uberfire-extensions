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
package org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.mergable;

import java.util.List;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.types.Point2D;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;
import org.uberfire.ext.wires.core.grids.client.model.mergable.MergableGridCell;
import org.uberfire.ext.wires.core.grids.client.model.mergable.MergableGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.mergable.MergableGridRow;
import org.uberfire.ext.wires.core.grids.client.util.GridCoordinateUtils;
import org.uberfire.ext.wires.core.grids.client.widget.ISelectionManager;
import org.uberfire.ext.wires.core.grids.client.widget.animation.MergableGridWidgetCollapseRowsAnimation;
import org.uberfire.ext.wires.core.grids.client.widget.animation.MergableGridWidgetExpandRowsAnimation;
import org.uberfire.ext.wires.core.grids.client.widget.grid.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.grid.mergable.MergableGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.BaseGridWidgetMouseClickHandler;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.IGridRenderer;

/**
 * MouseClickHandler for a Grid containing merged cells. Merge cells have an additional "widget" that
 * can be clicked to collapse or expand the merged cells. This class handles clicks on this "widget".
 */
public class MergableGridWidgetMouseClickHandler extends BaseGridWidgetMouseClickHandler<MergableGridWidget> {

    public MergableGridWidgetMouseClickHandler( final ISelectionManager selectionManager ) {
        super( selectionManager );
    }

    @Override
    protected void handleBodyCellClick( final NodeMouseClickEvent event ) {
        //Get GridWidget relating to event
        final MergableGridWidget activeGridWidget = getActiveGridWidget( event );
        if ( activeGridWidget == null ) {
            return;
        }

        //Convert Canvas co-ordinate to Grid co-ordinate
        final IGridRenderer<?> renderer = activeGridWidget.getRenderer();
        final Point2D ap = GridCoordinateUtils.mapToGridWidgetAbsolutePoint( activeGridWidget,
                                                                             new Point2D( event.getX(),
                                                                                          event.getY() ) );
        final double x = ap.getX();
        final double y = ap.getY();
        if ( x < 0 || x > activeGridWidget.getWidth() ) {
            return;
        }
        if ( y < renderer.getHeaderHeight() || y > activeGridWidget.getHeight() ) {
            return;
        }

        //Get row index
        IGridRow<?> row;
        int rowIndex = 0;
        double offsetY = y - renderer.getHeaderHeight();
        while ( ( row = activeGridWidget.getModel().getRow( rowIndex ) ).getHeight() < offsetY ) {
            offsetY = offsetY - row.getHeight();
            rowIndex++;
        }
        if ( rowIndex < 0 || rowIndex > activeGridWidget.getModel().getRowCount() - 1 ) {
            return;
        }

        //Get column index
        int columnIndex = -1;
        double offsetX = 0;
        final List<MergableGridColumn<?>> columns = activeGridWidget.getModel().getColumns();
        for ( int idx = 0; idx < columns.size(); idx++ ) {
            final MergableGridColumn gridColumn = columns.get( idx );
            if ( gridColumn.isVisible() ) {
                final double width = gridColumn.getWidth();
                if ( x > offsetX && x < offsetX + width ) {
                    columnIndex = idx;
                    break;
                }
                offsetX = offsetX + width;
            }
        }
        if ( columnIndex < 0 || columnIndex > columns.size() - 1 ) {
            return;
        }

        //Check if the cell can be Grouped
        final MergableGridCell cell = activeGridWidget.getModel().getCell( rowIndex,
                                                                           columnIndex );
        if ( cell == null ) {
            return;
        }
        if ( cell.getMergedCellCount() < 2 ) {
            return;
        }

        //Check if the Grouping control has been clicked
        final MergableGridRow gridRow = activeGridWidget.getModel().getRow( rowIndex );
        final MergableGridColumn gridColumn = columns.get( columnIndex );
        final MergableGridCell nextRowCell = activeGridWidget.getModel().getCell( rowIndex + 1,
                                                                                  columnIndex );
        final double cellX = x - offsetX;
        final double cellY = y - activeGridWidget.getModel().getRowOffset( rowIndex ) - renderer.getHeaderHeight();
        if ( !activeGridWidget.onGroupingToggle( cellX,
                                                 cellY,
                                                 gridColumn.getWidth(),
                                                 gridRow.getHeight() ) ) {
            return;
        }

        //Collapse or expand rows as needed
        if ( !nextRowCell.isCollapsed() ) {
            collapseRows( activeGridWidget,
                          rowIndex,
                          cell.getMergedCellCount(),
                          columnIndex );
        } else {
            expandRows( activeGridWidget,
                        rowIndex,
                        cell.getMergedCellCount(),
                        columnIndex );
        }
    }

    @Override
    protected boolean accept( final IBaseGridWidget<?, ?, ?> gridWidget ) {
        return gridWidget instanceof MergableGridWidget;
    }

    protected void collapseRows( final MergableGridWidget gridWidget,
                                 final int rowIndex,
                                 final int rowCount,
                                 final int columnIndex ) {
        final MergableGridWidgetCollapseRowsAnimation a = new MergableGridWidgetCollapseRowsAnimation( gridWidget,
                                                                                                       rowIndex,
                                                                                                       rowCount,
                                                                                                       columnIndex );
        a.run();
    }

    protected void expandRows( final MergableGridWidget gridWidget,
                               final int rowIndex,
                               final int rowCount,
                               final int columnIndex ) {
        final MergableGridWidgetExpandRowsAnimation a = new MergableGridWidgetExpandRowsAnimation( gridWidget,
                                                                                                   rowIndex,
                                                                                                   rowCount,
                                                                                                   columnIndex );
        a.run();
    }

}
