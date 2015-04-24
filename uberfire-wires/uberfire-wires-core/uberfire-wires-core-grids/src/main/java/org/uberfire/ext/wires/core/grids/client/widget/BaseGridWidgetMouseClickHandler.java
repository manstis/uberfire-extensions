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
package org.uberfire.ext.wires.core.grids.client.widget;

import java.util.List;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.types.Point2D;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;
import org.uberfire.ext.wires.core.grids.client.util.CoordinateTransformationUtils;
import org.uberfire.ext.wires.core.grids.client.widget.animation.MergableGridWidgetCollapseRowsAnimation;
import org.uberfire.ext.wires.core.grids.client.widget.animation.MergableGridWidgetExpandRowsAnimation;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.BaseGridRendererHelper;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.IGridRenderer;

/**
 * Base MouseClickHandler to handle clicks to either the GridWidgets Header or Body. This implementation
 * supports clicking on a "linked" column in the Header and delegating a response to the IGridSelectionManager.
 */
public class BaseGridWidgetMouseClickHandler implements NodeMouseClickHandler {

    protected IGridData gridModel;
    protected IBaseGridWidget gridWidget;
    protected BaseGridRendererHelper rendererHelper;
    protected IGridSelectionManager selectionManager;
    protected IGridRenderer renderer;

    public BaseGridWidgetMouseClickHandler( final IBaseGridWidget gridWidget,
                                            final IGridSelectionManager selectionManager,
                                            final IGridRenderer renderer ) {
        this.gridWidget = gridWidget;
        this.gridModel = gridWidget.getModel();
        this.rendererHelper = gridWidget.getRendererHelper();
        this.selectionManager = selectionManager;
        this.renderer = renderer;
    }

    @Override
    public void onNodeMouseClick( final NodeMouseClickEvent event ) {
        handleHeaderCellClick( event );
        handleBodyCellClick( event );
        selectionManager.select( gridWidget );
    }

    /**
     * Check if a MouseClickEvent happened on a "linked" column. If it does then
     * delegate a response to IGridSelectionManager.
     * @param event
     */
    protected void handleHeaderCellClick( final NodeMouseClickEvent event ) {
        //Convert Canvas co-ordinate to Grid co-ordinate
        final Point2D ap = CoordinateTransformationUtils.convertDOMToGridCoordinate( gridWidget,
                                                                                     new Point2D( event.getX(),
                                                                                                  event.getY() ) );
        final double cx = ap.getX();
        final double cy = ap.getY();

        final Group header = gridWidget.getHeader();
        final double headerMinY = ( header == null ? 0.0 : header.getY() );
        final double headerMaxY = ( header == null ? renderer.getHeaderHeight() : renderer.getHeaderHeight() + header.getY() );

        if ( cx < 0 || cx > gridWidget.getWidth() ) {
            return;
        }
        if ( cy < headerMinY || cy > headerMaxY ) {
            return;
        }

        //Get column index
        final BaseGridRendererHelper.ColumnInformation ci = rendererHelper.getColumnInformation( cx );
        final IGridColumn<?> column = ci.getColumn();
        if ( column == null ) {
            return;
        }

        //If linked scroll it into view
        if ( column.isLinked() ) {
            final IGridColumn<?> link = column.getLink();
            selectionManager.selectLinkedColumn( link );
        }
    }

    protected void handleBodyCellClick( final NodeMouseClickEvent event ) {
        //Convert Canvas co-ordinate to Grid co-ordinate
        final Point2D ap = CoordinateTransformationUtils.convertDOMToGridCoordinate( gridWidget,
                                                                                     new Point2D( event.getX(),
                                                                                                  event.getY() ) );
        final double cx = ap.getX();
        final double cy = ap.getY();

        final Group header = gridWidget.getHeader();
        final double headerMaxY = ( header == null ? renderer.getHeaderHeight() : renderer.getHeaderHeight() + header.getY() );

        if ( cx < 0 || cx > gridWidget.getWidth() ) {
            return;
        }
        if ( cy < headerMaxY || cy > gridWidget.getHeight() ) {
            return;
        }

        //Get row index
        IGridRow row;
        int uiRowIndex = 0;
        double offsetY = cy - renderer.getHeaderHeight();
        while ( ( row = gridModel.getRow( uiRowIndex ) ).getHeight() < offsetY ) {
            offsetY = offsetY - row.getHeight();
            uiRowIndex++;
        }
        if ( uiRowIndex < 0 || uiRowIndex > gridModel.getRowCount() - 1 ) {
            return;
        }

        //Get column index
        final BaseGridRendererHelper.ColumnInformation ci = rendererHelper.getColumnInformation( cx );
        final double offsetX = ci.getOffsetX();
        final IGridColumn<?> column = ci.getColumn();
        final List<IGridColumn<?>> columns = gridModel.getColumns();

        if ( column == null ) {
            return;
        }
        final int uiColumnIndex = ci.getUiColumnIndex();
        if ( uiColumnIndex < 0 || uiColumnIndex > columns.size() - 1 ) {
            return;
        }

        //Check if the cell can be Grouped
        final IGridCell<?> cell = gridModel.getCell( uiRowIndex,
                                                     uiColumnIndex );
        if ( cell == null ) {
            return;
        }
        if ( cell.getMergedCellCount() < 2 ) {
            return;
        }

        //Check if the Grouping control has been clicked
        final IGridRow gridRow = gridModel.getRow( uiRowIndex );
        final IGridColumn<?> gridColumn = columns.get( uiColumnIndex );
        final IGridCell<?> nextRowCell = gridModel.getCell( uiRowIndex + 1,
                                                            uiColumnIndex );
        final double cellX = cx - offsetX;
        final double cellY = cy - rendererHelper.getRowOffset( uiRowIndex ) - renderer.getHeaderHeight();
        if ( !gridWidget.onGroupingToggle( cellX,
                                           cellY,
                                           gridColumn.getWidth(),
                                           gridRow.getHeight() ) ) {
            return;
        }

        //Collapse or expand rows as needed
        if ( !nextRowCell.isCollapsed() ) {
            collapseRows( uiRowIndex,
                          uiColumnIndex,
                          cell.getMergedCellCount() );
        } else {
            expandRows( uiRowIndex,
                        uiColumnIndex,
                        cell.getMergedCellCount() );
        }
    }

    protected void collapseRows( final int uiRowIndex,
                                 final int uiColumnIndex,
                                 final int rowCount ) {
        final MergableGridWidgetCollapseRowsAnimation a = new MergableGridWidgetCollapseRowsAnimation( gridWidget,
                                                                                                       uiRowIndex,
                                                                                                       uiColumnIndex,
                                                                                                       rowCount );
        a.run();
    }

    protected void expandRows( final int uiRowIndex,
                               final int uiColumnIndex,
                               final int rowCount ) {
        final MergableGridWidgetExpandRowsAnimation a = new MergableGridWidgetExpandRowsAnimation( gridWidget,
                                                                                                   uiRowIndex,
                                                                                                   uiColumnIndex,
                                                                                                   rowCount );
        a.run();
    }

}
