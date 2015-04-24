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
package org.uberfire.ext.wires.core.grids.client.widget.dnd;

import java.util.ArrayList;
import java.util.List;

import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.event.NodeMouseMoveHandler;
import com.ait.lienzo.client.core.mediator.IMediator;
import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.types.Point2D;
import com.google.gwt.dom.client.Style;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;
import org.uberfire.ext.wires.core.grids.client.util.CoordinateTransformationUtils;
import org.uberfire.ext.wires.core.grids.client.widget.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.IGridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.dom.IHasDOMElementResources;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.BaseGridRendererHelper;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.IGridRenderer;

/**
 * MouseMoveHandler to handle potential drag operations and handle the drag itself; if required.
 */
public class GridWidgetDnDMouseMoveHandler implements NodeMouseMoveHandler {

    // How close the mouse pointer needs to be to the column separator to initiate a resize operation.
    private static final int COLUMN_RESIZE_HANDLE_SENSITIVITY = 5;

    private final IGridLayer layer;
    private final GridWidgetDnDHandlersState state;

    public GridWidgetDnDMouseMoveHandler( final IGridLayer layer,
                                          final GridWidgetDnDHandlersState state ) {
        this.layer = layer;
        this.state = state;
    }

    @Override
    @SuppressWarnings("unchecked")
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

            case ROW_MOVE:
                //If we're currently moving a row we don't need to find a row
                handleRowMove( event );
                break;

            default:
                //Otherwise try to find a Grid and GridColumn(s)
                state.clearActiveGridWidget();
                state.clearActiveGridColumns();
                state.clearActiveHeaderMetaData();
                state.clearActiveGridRows();
                state.setOperation( GridWidgetDnDHandlersState.GridWidgetHandlersOperation.NONE );
                state.setCursor( Style.Cursor.DEFAULT );

                for ( IBaseGridWidget gridWidget : layer.getGridWidgets() ) {
                    final Group header = gridWidget.getHeader();
                    final IGridRenderer renderer = gridWidget.getRenderer();

                    final double headerHeight = renderer.getHeaderHeight();
                    final double headerMinY = ( header == null ? 0.0 : header.getY() );
                    final double headerMaxY = ( header == null ? headerHeight : headerHeight + header.getY() );

                    final Point2D ap = CoordinateTransformationUtils.convertDOMToGridCoordinate( gridWidget,
                                                                                                 new Point2D( event.getX(),
                                                                                                              event.getY() ) );

                    final double cx = ap.getX();
                    final double cy = ap.getY();
                    if ( cx < 0 || cx > gridWidget.getWidth() ) {
                        continue;
                    }
                    if ( cy < headerMinY || cy > gridWidget.getHeight() ) {
                        continue;

                    } else if ( cy < headerMaxY ) {
                        //Check for column moving
                        final MovableColumnsInformation mci = findMovableColumns( gridWidget,
                                                                                  headerHeight,
                                                                                  headerMinY,
                                                                                  cx,
                                                                                  cy );
                        if ( mci != null ) {
                            state.setActiveGridWidget( gridWidget );
                            state.setActiveGridColumns( mci.getHeaderColumns() );
                            state.setActiveHeaderMetaData( mci.getHeaderMetaData() );
                            state.setOperation( GridWidgetDnDHandlersState.GridWidgetHandlersOperation.COLUMN_MOVE_PENDING );
                            state.setCursor( Style.Cursor.MOVE );
                        }

                    } else {
                        //Check for movable rows
                        final MovableRowsInformation mri = findMovableRows( gridWidget,
                                                                            cx,
                                                                            cy );
                        if ( mri != null ) {
                            state.setActiveGridWidget( gridWidget );
                            state.setActiveGridRows( mri.getRows() );
                            state.setOperation( GridWidgetDnDHandlersState.GridWidgetHandlersOperation.ROW_MOVE_PENDING );
                            state.setCursor( Style.Cursor.MOVE );
                        }

                        //Check for column resizing
                        final IGridColumn<?> gridColumn = findResizableColumn( gridWidget,
                                                                               cx );
                        if ( gridColumn != null ) {
                            state.setActiveGridWidget( gridWidget );
                            state.setActiveGridColumns( new ArrayList<IGridColumn<?>>() {{
                                add( gridColumn );
                            }} );
                            state.setOperation( GridWidgetDnDHandlersState.GridWidgetHandlersOperation.COLUMN_RESIZE_PENDING );
                            state.setCursor( Style.Cursor.COL_RESIZE );
                            break;
                        }
                    }
                }

                layer.getViewport().getElement().getStyle().setCursor( state.getCursor() );
                for ( IMediator mediator : layer.getViewport().getMediators() ) {
                    mediator.setEnabled( state.getActiveGridWidget() == null );
                }
        }
    }

    private IGridColumn<?> findResizableColumn( final IBaseGridWidget view,
                                                final double cx ) {
        //Gather information on columns
        final BaseGridRendererHelper rendererHelper = view.getRendererHelper();
        final BaseGridRendererHelper.RenderingInformation ri = rendererHelper.getRenderingInformation();
        final BaseGridRendererHelper.RenderingBlockInformation bodyBlockInformation = ri.getBodyBlockInformation();
        final BaseGridRendererHelper.RenderingBlockInformation floatingBlockInformation = ri.getFloatingBlockInformation();
        final List<IGridColumn<?>> bodyColumns = bodyBlockInformation.getColumns();
        final List<IGridColumn<?>> floatingColumns = floatingBlockInformation.getColumns();
        final double bodyX = bodyBlockInformation.getX();
        final double floatingX = floatingBlockInformation.getX();
        final double floatingWidth = floatingBlockInformation.getWidth();

        //Check floating columns
        double offsetX = floatingX;
        IGridColumn<?> column = null;
        for ( IGridColumn<?> gridColumn : floatingColumns ) {
            if ( gridColumn.isVisible() ) {
                if ( gridColumn.isResizable() ) {
                    final double columnWidth = gridColumn.getWidth();
                    if ( cx > columnWidth + offsetX - COLUMN_RESIZE_HANDLE_SENSITIVITY && cx < columnWidth + offsetX + COLUMN_RESIZE_HANDLE_SENSITIVITY ) {
                        column = gridColumn;
                        break;
                    }
                }
                offsetX = offsetX + gridColumn.getWidth();
            }
        }
        if ( column != null ) {
            return column;
        }

        //Check all other columns
        offsetX = bodyX;
        for ( IGridColumn<?> gridColumn : bodyColumns ) {
            if ( gridColumn.isVisible() ) {
                if ( gridColumn.isResizable() ) {
                    final double columnWidth = gridColumn.getWidth();
                    if ( offsetX + gridColumn.getWidth() > floatingX + floatingWidth ) {
                        if ( cx > columnWidth + offsetX - COLUMN_RESIZE_HANDLE_SENSITIVITY && cx < columnWidth + offsetX + COLUMN_RESIZE_HANDLE_SENSITIVITY ) {
                            column = gridColumn;
                            break;
                        }
                    }
                }
                offsetX = offsetX + gridColumn.getWidth();
            }
        }
        return column;
    }

    private MovableColumnsInformation findMovableColumns( final IBaseGridWidget view,
                                                          final double headerHeight,
                                                          final double headerMinY,
                                                          final double cx,
                                                          final double cy ) {
        //Gather information on columns
        final BaseGridRendererHelper rendererHelper = view.getRendererHelper();
        final BaseGridRendererHelper.RenderingInformation ri = rendererHelper.getRenderingInformation();
        final BaseGridRendererHelper.RenderingBlockInformation bodyBlockInformation = ri.getBodyBlockInformation();
        final BaseGridRendererHelper.RenderingBlockInformation floatingBlockInformation = ri.getFloatingBlockInformation();
        final List<IGridColumn<?>> allColumns = view.getModel().getColumns();
        final List<IGridColumn<?>> bodyColumns = bodyBlockInformation.getColumns();
        final List<IGridColumn<?>> floatingColumns = floatingBlockInformation.getColumns();
        final double bodyX = bodyBlockInformation.getX();
        final double floatingX = floatingBlockInformation.getX();
        final double floatingWidth = floatingBlockInformation.getWidth();

        //Check all other columns. Floating columns cannot be moved.
        double offsetX = bodyX;
        for ( int headerColumnIndex = 0; headerColumnIndex < bodyColumns.size(); headerColumnIndex++ ) {
            final IGridColumn<?> gridColumn = bodyColumns.get( headerColumnIndex );
            final double columnWidth = gridColumn.getWidth();

            if ( gridColumn.isVisible() ) {
                final List<IGridColumn.HeaderMetaData> headerMetaData = gridColumn.getHeaderMetaData();
                final double headerRowHeight = headerHeight / headerMetaData.size();

                for ( int headerRowIndex = 0; headerRowIndex < headerMetaData.size(); headerRowIndex++ ) {
                    final IGridColumn.HeaderMetaData md = headerMetaData.get( headerRowIndex );
                    if ( gridColumn.isMovable() ) {
                        if ( cy < ( headerRowIndex + 1 ) * headerRowHeight + headerMinY ) {
                            if ( cx > floatingX + floatingWidth ) {
                                if ( cx > offsetX && cx < offsetX + columnWidth ) {
                                    //Get the block of columns to be moved.
                                    final List<IGridColumn<?>> blockColumns = getBlockColumns( allColumns,
                                                                                               headerMetaData,
                                                                                               headerRowIndex,
                                                                                               allColumns.indexOf( gridColumn ) );
                                    //If the columns to move are split between body and floating we cannot move them.
                                    for ( IGridColumn<?> blockColumn : blockColumns ) {
                                        if ( floatingColumns.contains( blockColumn ) ) {
                                            return null;
                                        }
                                    }
                                    return new MovableColumnsInformation( blockColumns,
                                                                          md );
                                }
                            }
                        }
                    }
                }
                offsetX = offsetX + columnWidth;
            }
        }
        return null;
    }

    private List<IGridColumn<?>> getBlockColumns( final List<IGridColumn<?>> allColumns,
                                                  final List<IGridColumn.HeaderMetaData> headerMetaData,
                                                  final int headerRowIndex,
                                                  final int headerColumnIndex ) {
        final int blockStartColumnIndex = getBlockStartColumnIndex( allColumns,
                                                                    headerMetaData.get( headerRowIndex ),
                                                                    headerRowIndex,
                                                                    headerColumnIndex );
        final int blockEndColumnIndex = getBlockEndColumnIndex( allColumns,
                                                                headerMetaData.get( headerRowIndex ),
                                                                headerRowIndex,
                                                                headerColumnIndex );

        final List<IGridColumn<?>> columns = new ArrayList<IGridColumn<?>>();
        columns.addAll( allColumns.subList( blockStartColumnIndex,
                                            blockEndColumnIndex + 1 ) );
        return columns;
    }

    @SuppressWarnings("unchecked")
    private int getBlockStartColumnIndex( final List<? extends IGridColumn<?>> allColumns,
                                          final IGridColumn.HeaderMetaData headerMetaData,
                                          final int headerRowIndex,
                                          final int headerColumnIndex ) {
        //Back-track adding width of proceeding columns sharing header MetaData
        int candidateHeaderColumnIndex = headerColumnIndex;
        if ( candidateHeaderColumnIndex == 0 ) {
            return candidateHeaderColumnIndex;
        }
        while ( candidateHeaderColumnIndex > 0 ) {
            final IGridColumn candidateColumn = allColumns.get( candidateHeaderColumnIndex - 1 );
            final List<IGridColumn.HeaderMetaData> candidateHeaderMetaData = candidateColumn.getHeaderMetaData();
            if ( candidateHeaderMetaData.size() - 1 < headerRowIndex ) {
                break;
            }
            if ( !candidateHeaderMetaData.get( headerRowIndex ).equals( headerMetaData ) ) {
                break;
            }
            candidateHeaderColumnIndex--;
        }

        return candidateHeaderColumnIndex;
    }

    @SuppressWarnings("unchecked")
    private int getBlockEndColumnIndex( final List<? extends IGridColumn<?>> allColumns,
                                        final IGridColumn.HeaderMetaData headerMetaData,
                                        final int headerRowIndex,
                                        final int headerColumnIndex ) {
        //Forward-track adding width of following columns sharing header MetaData
        int candidateHeaderColumnIndex = headerColumnIndex;
        if ( candidateHeaderColumnIndex == allColumns.size() - 1 ) {
            return candidateHeaderColumnIndex;
        }
        while ( candidateHeaderColumnIndex < allColumns.size() - 1 ) {
            final IGridColumn candidateColumn = allColumns.get( candidateHeaderColumnIndex + 1 );
            final List<IGridColumn.HeaderMetaData> candidateHeaderMetaData = candidateColumn.getHeaderMetaData();
            if ( candidateHeaderMetaData.size() - 1 < headerRowIndex ) {
                break;
            }
            if ( !candidateHeaderMetaData.get( headerRowIndex ).equals( headerMetaData ) ) {
                break;
            }
            candidateHeaderColumnIndex++;
        }

        return candidateHeaderColumnIndex;
    }

    private MovableRowsInformation findMovableRows( final IBaseGridWidget view,
                                                    final double cx,
                                                    final double cy ) {
        if ( !isOverRowDragHandleColumn( view,
                                         cx ) ) {
            return null;
        }

        final IGridData gridModel = view.getModel();
        final IGridRenderer renderer = view.getRenderer();

        //Get row index
        IGridRow row;
        int uiRowIndex = 0;
        double offsetY = cy - renderer.getHeaderHeight();
        while ( ( row = gridModel.getRow( uiRowIndex ) ).getHeight() < offsetY ) {
            offsetY = offsetY - row.getHeight();
            uiRowIndex++;
        }
        if ( uiRowIndex < 0 || uiRowIndex > gridModel.getRowCount() - 1 ) {
            return null;
        }

        //Add row over which MouseEvent occurred
        final List<IGridRow> rows = new ArrayList<IGridRow>();
        rows.add( gridModel.getRow( uiRowIndex ) );

        //Add any other collapsed rows
        IGridRow collapsedRow;
        while ( uiRowIndex + 1 < gridModel.getRowCount() && ( collapsedRow = gridModel.getRow( uiRowIndex + 1 ) ).isCollapsed() ) {
            rows.add( collapsedRow );
            uiRowIndex++;
        }

        return new MovableRowsInformation( rows );
    }

    private boolean isOverRowDragHandleColumn( final IBaseGridWidget view,
                                               final double cx ) {
        //Gather information on columns
        final BaseGridRendererHelper rendererHelper = view.getRendererHelper();
        final BaseGridRendererHelper.RenderingInformation ri = rendererHelper.getRenderingInformation();
        final BaseGridRendererHelper.RenderingBlockInformation bodyBlockInformation = ri.getBodyBlockInformation();
        final BaseGridRendererHelper.RenderingBlockInformation floatingBlockInformation = ri.getFloatingBlockInformation();
        final List<IGridColumn<?>> bodyColumns = bodyBlockInformation.getColumns();
        final List<IGridColumn<?>> floatingColumns = floatingBlockInformation.getColumns();
        final double bodyX = bodyBlockInformation.getX();
        final double floatingX = floatingBlockInformation.getX();

        //Check floating columns
        double offsetX = floatingX;
        IGridColumn<?> column = null;
        for ( IGridColumn<?> gridColumn : floatingColumns ) {
            if ( gridColumn.isVisible() ) {
                if ( gridColumn instanceof IsRowDragHandle ) {
                    final double columnWidth = gridColumn.getWidth();
                    if ( cx > offsetX && cx < offsetX + columnWidth ) {
                        column = gridColumn;
                        break;
                    }
                }
                offsetX = offsetX + gridColumn.getWidth();
            }
        }
        if ( column != null ) {
            return true;
        }

        //Check all other columns
        offsetX = bodyX;
        for ( IGridColumn<?> gridColumn : bodyColumns ) {
            if ( gridColumn.isVisible() ) {
                if ( gridColumn instanceof IsRowDragHandle ) {
                    final double columnWidth = gridColumn.getWidth();
                    if ( cx > offsetX && cx < offsetX + columnWidth ) {
                        column = gridColumn;
                        break;
                    }
                }
                offsetX = offsetX + gridColumn.getWidth();
            }
        }
        return column != null;
    }

    private void handleColumnResize( final NodeMouseMoveEvent event ) {
        final IBaseGridWidget activeGridWidget = state.getActiveGridWidget();
        final List<IGridColumn<?>> activeGridColumns = state.getActiveGridColumns();
        if ( activeGridColumns.size() > 1 ) {
            return;
        }
        final IGridColumn<?> activeGridColumn = activeGridColumns.get( 0 );
        final IGridData activeGridModel = activeGridWidget.getModel();
        final List<IGridColumn<?>> allGridColumns = activeGridModel.getColumns();

        final Point2D ap = CoordinateTransformationUtils.convertDOMToGridCoordinate( activeGridWidget,
                                                                                     new Point2D( event.getX(),
                                                                                                  event.getY() ) );
        final double deltaX = ap.getX() - state.getEventInitialX();
        final Double columnMinimumWidth = activeGridColumn.getMinimumWidth();
        final Double columnMaximumWidth = activeGridColumn.getMaximumWidth();
        double columnNewWidth = state.getEventInitialColumnWidth() + deltaX;
        if ( columnMinimumWidth != null ) {
            if ( columnNewWidth < columnMinimumWidth ) {
                columnNewWidth = columnMinimumWidth;
            }
        }
        if ( columnMaximumWidth != null ) {
            if ( columnNewWidth > columnMaximumWidth ) {
                columnNewWidth = columnMaximumWidth;
            }
        }
        destroyColumns( allGridColumns );
        activeGridColumn.setWidth( columnNewWidth );
        layer.batch();
    }

    @SuppressWarnings("unchecked")
    private void handleColumnMove( final NodeMouseMoveEvent event ) {
        final IBaseGridWidget activeGridWidget = state.getActiveGridWidget();
        final List<IGridColumn<?>> activeGridColumns = state.getActiveGridColumns();
        final IGridColumn.HeaderMetaData activeHeaderMetaData = state.getActiveHeaderMetaData();

        final IGridData activeGridModel = activeGridWidget.getModel();
        final List<IGridColumn<?>> allGridColumns = activeGridModel.getColumns();
        final BaseGridRendererHelper rendererHelper = activeGridWidget.getRendererHelper();
        final BaseGridRendererHelper.RenderingInformation ri = rendererHelper.getRenderingInformation();
        final BaseGridRendererHelper.RenderingBlockInformation floatingBlockInformation = ri.getFloatingBlockInformation();

        final double floatingX = floatingBlockInformation.getX();
        final double floatingWidth = floatingBlockInformation.getWidth();

        final Point2D ap = CoordinateTransformationUtils.convertDOMToGridCoordinate( activeGridWidget,
                                                                                     new Point2D( event.getX(),
                                                                                                  event.getY() ) );
        final double cx = ap.getX();
        if ( cx < floatingX + floatingWidth ) {
            return;
        }

        final double activeBlockWidth = getBlockWidth( allGridColumns,
                                                       allGridColumns.indexOf( activeGridColumns.get( 0 ) ),
                                                       allGridColumns.indexOf( activeGridColumns.get( activeGridColumns.size() - 1 ) ) );

        for ( int headerColumnIndex = 0; headerColumnIndex < allGridColumns.size(); headerColumnIndex++ ) {
            final IGridColumn<?> candidateGridColumn = allGridColumns.get( headerColumnIndex );
            if ( candidateGridColumn.isVisible() ) {
                if ( !activeGridColumns.contains( candidateGridColumn ) ) {
                    for ( int headerRowIndex = 0; headerRowIndex < candidateGridColumn.getHeaderMetaData().size(); headerRowIndex++ ) {
                        final IGridColumn.HeaderMetaData candidateHeaderMetaData = candidateGridColumn.getHeaderMetaData().get( headerRowIndex );
                        if ( candidateHeaderMetaData.getColumnGroup().equals( activeHeaderMetaData.getColumnGroup() ) ) {
                            final int candidateBlockStartColumnIndex = getBlockStartColumnIndex( allGridColumns,
                                                                                                 candidateHeaderMetaData,
                                                                                                 headerRowIndex,
                                                                                                 headerColumnIndex );
                            final int candidateBlockEndColumnIndex = getBlockEndColumnIndex( allGridColumns,
                                                                                             candidateHeaderMetaData,
                                                                                             headerRowIndex,
                                                                                             headerColumnIndex );
                            final double candidateBlockOffset = rendererHelper.getColumnOffset( candidateBlockStartColumnIndex );
                            final double candidateBlockWidth = getBlockWidth( allGridColumns,
                                                                              candidateBlockStartColumnIndex,
                                                                              candidateBlockEndColumnIndex );

                            final double minColX = Math.max( candidateBlockOffset,
                                                             candidateBlockOffset + ( candidateBlockWidth - activeBlockWidth ) / 2 );
                            final double maxColX = Math.min( candidateBlockOffset + candidateBlockWidth,
                                                             candidateBlockOffset + ( candidateBlockWidth + activeBlockWidth ) / 2 );
                            final double midColX = candidateBlockOffset + candidateBlockWidth / 2;
                            if ( cx > minColX && cx < maxColX ) {
                                if ( cx < midColX ) {
                                    destroyColumns( allGridColumns );
                                    activeGridModel.moveColumnsTo( candidateBlockEndColumnIndex,
                                                                   activeGridColumns );
                                    state.getEventColumnHighlight().setX( activeGridWidget.getX() + rendererHelper.getColumnOffset( activeGridColumns.get( 0 ) ) );
                                    layer.batch();
                                    return;

                                } else {
                                    destroyColumns( allGridColumns );
                                    activeGridModel.moveColumnsTo( candidateBlockStartColumnIndex,
                                                                   activeGridColumns );
                                    state.getEventColumnHighlight().setX( activeGridWidget.getX() + rendererHelper.getColumnOffset( activeGridColumns.get( 0 ) ) );
                                    layer.batch();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private double getBlockWidth( final List<? extends IGridColumn> columns,
                                  final int blockStartColumnIndex,
                                  final int blockEndColumnIndex ) {
        double blockWidth = 0;
        for ( int blockColumnIndex = blockStartColumnIndex; blockColumnIndex <= blockEndColumnIndex; blockColumnIndex++ ) {
            final IGridColumn column = columns.get( blockColumnIndex );
            if ( column.isVisible() ) {
                blockWidth = blockWidth + column.getWidth();
            }
        }
        return blockWidth;
    }

    private void handleRowMove( final NodeMouseMoveEvent event ) {
        final IBaseGridWidget activeGridWidget = state.getActiveGridWidget();
        final List<IGridRow> activeGridRows = state.getActiveGridRows();

        final IGridData activeGridModel = activeGridWidget.getModel();
        final List<IGridColumn<?>> allGridColumns = activeGridModel.getColumns();

        final BaseGridRendererHelper rendererHelper = activeGridWidget.getRendererHelper();
        final IGridRenderer renderer = activeGridWidget.getRenderer();
        final double headerHeight = renderer.getHeaderHeight();

        final IGridRow leadRow = activeGridRows.get( 0 );
        final int leadRowIndex = activeGridModel.getRows().indexOf( leadRow );

        final Point2D ap = CoordinateTransformationUtils.convertDOMToGridCoordinate( activeGridWidget,
                                                                                     new Point2D( event.getX(),
                                                                                                  event.getY() ) );
        final double cy = ap.getY();
        if ( cy < headerHeight || cy > activeGridWidget.getHeight() ) {
            return;
        }

        //Find new row index
        IGridRow row;
        int uiRowIndex = 0;
        double offsetY = cy - headerHeight;
        while ( ( row = activeGridModel.getRow( uiRowIndex ) ).getHeight() < offsetY ) {
            offsetY = offsetY - row.getHeight();
            uiRowIndex++;
        }
        if ( uiRowIndex < 0 || uiRowIndex > activeGridModel.getRowCount() - 1 ) {
            return;
        }

        if ( uiRowIndex == leadRowIndex ) {
            //Don't move if the new rowIndex equals the index of the row(s) being moved
            return;

        } else if ( uiRowIndex < activeGridModel.getRows().indexOf( leadRow ) ) {
            //Don't move up if the pointer is in the bottom half of the target row.
            if ( offsetY > activeGridModel.getRow( uiRowIndex ).getHeight() / 2 ) {
                return;
            }

        } else if ( uiRowIndex > activeGridModel.getRows().indexOf( leadRow ) ) {
            //Don't move down if the pointer is in the top half of the target row.
            if ( offsetY < activeGridModel.getRow( uiRowIndex ).getHeight() / 2 ) {
                return;
            }
        }

        //Move row(s) and update highlight
        destroyColumns( allGridColumns );
        activeGridModel.moveRowsTo( uiRowIndex,
                                    activeGridRows );

        final double rowOffsetY = rendererHelper.getRowOffset( leadRow ) + headerHeight;
        state.getEventColumnHighlight().setY( activeGridWidget.getY() + rowOffsetY );
        layer.batch();
    }

    //Destroy all DOMElement based columns as their creation stores the GridBodyCellRenderContext
    //which is used to write updated IGridCellValue(s) to the underlying IGridData at the coordinate
    //at which the DOMElement was created. Moving Rows and Columns changes these coordinates and
    //hence the reference held in the DOMElement becomes out of date.
    private void destroyColumns( final List<IGridColumn<?>> columns ) {
        for ( IGridColumn<?> column : columns ) {
            if ( column instanceof IHasDOMElementResources ) {
                ( (IHasDOMElementResources) column ).destroyResources();
            }
        }
    }

}
