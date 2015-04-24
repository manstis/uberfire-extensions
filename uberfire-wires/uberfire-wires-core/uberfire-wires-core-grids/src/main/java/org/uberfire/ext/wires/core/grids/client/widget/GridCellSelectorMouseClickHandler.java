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
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.BaseGridRendererHelper;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.IGridRenderer;
import org.uberfire.ext.wires.core.grids.client.widget.selections.CellRangeSelectionManager;
import org.uberfire.ext.wires.core.grids.client.widget.selections.ICellSelectionManager;

/**
 * MouseClickHandler to handle selection of cells.
 */
public class GridCellSelectorMouseClickHandler implements NodeMouseClickHandler {

    protected IGridData gridModel;
    protected IBaseGridWidget gridWidget;
    protected BaseGridRendererHelper rendererHelper;
    protected IGridSelectionManager selectionManager;
    protected IGridRenderer renderer;

    public GridCellSelectorMouseClickHandler( final IBaseGridWidget gridWidget,
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
        handleBodyCellClick( event );
    }

    /**
     * Select cells.
     * @param event
     */
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
        final List<IGridColumn<?>> columns = gridModel.getColumns();
        final BaseGridRendererHelper.ColumnInformation ci = rendererHelper.getColumnInformation( cx );
        final IGridColumn<?> column = ci.getColumn();
        final int uiColumnIndex = ci.getUiColumnIndex();

        if ( column == null ) {
            return;
        }
        if ( uiColumnIndex < 0 || uiColumnIndex > columns.size() - 1 ) {
            return;
        }

        //Lookup ICellSelectionManager for cell
        ICellSelectionManager selectionManager;
        final IGridCell<?> cell = gridModel.getCell( uiRowIndex,
                                                     uiColumnIndex );
        if ( cell == null ) {
            selectionManager = CellRangeSelectionManager.INSTANCE;
        } else {
            selectionManager = cell.getSelectionManager();
        }
        if ( selectionManager == null ) {
            return;
        }

        //Handle selection
        if ( selectionManager.handleSelection( event,
                                               uiRowIndex,
                                               uiColumnIndex,
                                               gridModel ) ) {
            gridWidget.getLayer().batch();
        }
    }

}
