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
package org.uberfire.ext.wires.core.grids.client.widget.layer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.event.NodeMouseDownEvent;
import com.ait.lienzo.client.core.event.NodeMouseDownHandler;
import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.event.NodeMouseMoveHandler;
import com.ait.lienzo.client.core.event.NodeMouseUpEvent;
import com.ait.lienzo.client.core.event.NodeMouseUpHandler;
import com.ait.lienzo.client.core.shape.Arrow;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Shape;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.shared.core.types.ArrowType;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.model.mergable.MergableGridCell;
import org.uberfire.ext.wires.core.grids.client.model.mergable.MergableGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.mergable.MergableGridData;
import org.uberfire.ext.wires.core.grids.client.model.mergable.MergableGridRow;
import org.uberfire.ext.wires.core.grids.client.widget.GridWidgetConnector;
import org.uberfire.ext.wires.core.grids.client.widget.ISelectionManager;
import org.uberfire.ext.wires.core.grids.client.widget.animation.GridWidgetScrollIntoViewAnimation;
import org.uberfire.ext.wires.core.grids.client.widget.grid.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.grid.mergable.MergableGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.BaseGridWidgetMouseClickHandler;
import org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.BaseGridWidgetMouseDoubleClickHandler;
import org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.dnd.GridWidgetHandlersState;
import org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.dnd.GridWidgetMouseDownHandler;
import org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.dnd.GridWidgetMouseMoveHandler;
import org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.dnd.GridWidgetMouseUpHandler;
import org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.mergable.MergableGridWidgetMouseClickHandler;
import org.uberfire.ext.wires.core.grids.client.widget.layer.handlers.mergable.MergableGridWidgetMouseDoubleClickHandler;

/**
 * A specialised Layer that supports pass-through of MouseEvents from DOMElements to GridWidgets.
 * It also guarantees that Layer.draw() will only be invoked once per browser-loop; by scheduling
 * the actual draw() to GWT's Schedule scheduleFinally(). Furthermore this implementation handles
 * drawing connectors between "linked" grids and acts as a ISelection manager.
 */
public class GridLayer extends Layer implements ISelectionManager,
                                                NodeMouseDownHandler,
                                                NodeMouseMoveHandler,
                                                NodeMouseUpHandler,
                                                NodeMouseClickHandler {

    private Set<IBaseGridWidget<?, ?, ?>> gridWidgets = new HashSet<IBaseGridWidget<?, ?, ?>>();
    private Map<GridWidgetConnector, Arrow> connectors = new HashMap<GridWidgetConnector, Arrow>();

    private Rectangle bounds;
    private boolean isRedrawScheduled = false;

    private final GridWidgetMouseDownHandler<MergableGridWidget, MergableGridData, MergableGridRow, MergableGridColumn<?>, MergableGridCell<?>> mouseDownHandler;
    private final GridWidgetMouseMoveHandler<MergableGridWidget, MergableGridData, MergableGridRow, MergableGridColumn<?>, MergableGridCell<?>> mouseMoveHandler;
    private final GridWidgetMouseUpHandler<MergableGridWidget, MergableGridData, MergableGridRow, MergableGridColumn<?>, MergableGridCell<?>> mouseUpHandler;

    private final BaseGridWidgetMouseClickHandler<MergableGridWidget> mouseClickHandler;
    private final BaseGridWidgetMouseDoubleClickHandler<MergableGridWidget, MergableGridData> mouseDoubleClickHandler;

    private final GridWidgetHandlersState<MergableGridWidget, MergableGridData, MergableGridRow, MergableGridColumn<?>, MergableGridCell<?>> state = new GridWidgetHandlersState<MergableGridWidget, MergableGridData, MergableGridRow, MergableGridColumn<?>, MergableGridCell<?>>();

    private static final Command NOP_COMMAND = new Command() {
        @Override
        public void execute() {
            //Do nothing
        }
    };

    public GridLayer() {
        bounds = new Rectangle( 0, 0 ).setVisible( false );
        add( bounds );

        //Mouse handlers
        this.mouseClickHandler = new MergableGridWidgetMouseClickHandler( this );
        this.mouseDoubleClickHandler = new MergableGridWidgetMouseDoubleClickHandler( this );

        this.mouseDownHandler = new GridWidgetMouseDownHandler<MergableGridWidget, MergableGridData, MergableGridRow, MergableGridColumn<?>, MergableGridCell<?>>( this,
                                                                                                                                                                   state );
        this.mouseMoveHandler = new GridWidgetMouseMoveHandler<MergableGridWidget, MergableGridData, MergableGridRow, MergableGridColumn<?>, MergableGridCell<?>>( this,
                                                                                                                                                                   state );
        this.mouseUpHandler = new GridWidgetMouseUpHandler<MergableGridWidget, MergableGridData, MergableGridRow, MergableGridColumn<?>, MergableGridCell<?>>( this,
                                                                                                                                                               state );
        addNodeMouseClickHandler( mouseClickHandler );
        addNodeMouseDoubleClickHandler( mouseDoubleClickHandler );
        addNodeMouseDownHandler( mouseDownHandler );
        addNodeMouseMoveHandler( mouseMoveHandler );
        addNodeMouseUpHandler( mouseUpHandler );
    }

    @Override
    public void onNodeMouseDown( final NodeMouseDownEvent event ) {
        mouseDownHandler.onNodeMouseDown( event );
    }

    @Override
    public void onNodeMouseMove( final NodeMouseMoveEvent event ) {
        mouseMoveHandler.onNodeMouseMove( event );
    }

    @Override
    public void onNodeMouseUp( final NodeMouseUpEvent event ) {
        mouseUpHandler.onNodeMouseUp( event );
    }

    @Override
    public void onNodeMouseClick( final NodeMouseClickEvent event ) {
        mouseClickHandler.onNodeMouseClick( event );
    }

    public GridWidgetHandlersState getGridWidgetHandlersState() {
        return this.state;
    }

    @Override
    public Shape<?> findShapeAtPoint( int x,
                                      int y ) {
        return null;
    }

    /**
     * Schedule a draw with out additional command.
     */
    @Override
    public void draw() {
        draw( NOP_COMMAND );
    }

    public native void requestAnimationFrame( final Command command ) /*-{
        var that = this;
        $wnd.requestAnimationFrame(function () {
            that.@org.uberfire.ext.wires.core.grids.client.widget.layer.GridLayer::doDraw(Lcom/google/gwt/user/client/Command;)(command);
        });
    }-*/;

    public void doDraw( final Command command ) {
        updateBounds();
        updateConnectors();
        GridLayer.super.draw();
        command.execute();
    }

    private static final int PADDING = 0;

    private void updateBounds() {
        final Viewport viewport = GridLayer.this.getViewport();
        Transform transform = viewport.getTransform();
        if ( transform == null ) {
            viewport.setTransform( transform = new Transform() );
        }
        final double x = ( PADDING - transform.getTranslateX() ) / transform.getScaleX();
        final double y = ( PADDING - transform.getTranslateY() ) / transform.getScaleY();
        bounds.setLocation( new Point2D( x,
                                         y ) );
        bounds.setHeight( ( viewport.getHeight() - PADDING * 2 ) / transform.getScaleX() );
        bounds.setWidth( ( viewport.getWidth() - PADDING * 2 ) / transform.getScaleY() );
        bounds.setStrokeWidth( 1.0 / transform.getScaleX() );
    }

    private void updateConnectors() {
        for ( Map.Entry<GridWidgetConnector, Arrow> e : connectors.entrySet() ) {
            final GridWidgetConnector connector = e.getKey();
            final Arrow arrow = e.getValue();
            final IGridColumn<?, ?> sourceColumn = connector.getSourceColumn();
            final IGridColumn<?, ?> targetColumn = connector.getTargetColumn();
            final IBaseGridWidget<?, ?, ?> sourceGridWidget = getLinkedGridWidget( sourceColumn );
            final IBaseGridWidget<?, ?, ?> targetGridWidget = getLinkedGridWidget( targetColumn );
            if ( connector.getDirection() == GridWidgetConnector.Direction.EAST_WEST ) {
                arrow.setStart( new Point2D( sourceGridWidget.getX() + sourceGridWidget.getWidth() / 2,
                                             arrow.getStart().getY() ) );
            } else {
                arrow.setEnd( new Point2D( targetGridWidget.getX() + targetGridWidget.getWidth(),
                                           arrow.getEnd().getY() ) );
            }
        }

    }

    /**
     * Schedule a draw with a command to be executed once the draw() has completed.
     * @param command
     */
    public void draw( final Command command ) {
        if ( !isRedrawScheduled ) {
            isRedrawScheduled = true;
            Scheduler.get().scheduleFinally( new Command() {

                @Override
                public void execute() {
                    requestAnimationFrame( command );
                    isRedrawScheduled = false;
                }

            } );
        }
    }

    /**
     * Add a child to this Layer. If the child is a GridWidget then also add
     * a Connector between the Grid Widget and any "linked" GridWidgets.
     * @param child
     * @return
     */
    @Override
    public Layer add( final IPrimitive<?> child ) {
        addGridWidget( child );
        return super.add( child );
    }

    private void addGridWidget( final IPrimitive<?> child,
                                final IPrimitive<?>... children ) {
        final List<IPrimitive<?>> all = new ArrayList<IPrimitive<?>>();
        all.add( child );
        all.addAll( Arrays.asList( children ) );
        for ( IPrimitive<?> c : all ) {
            if ( c instanceof IBaseGridWidget<?, ?, ?> ) {
                final IBaseGridWidget<?, ?, ?> gridWidget = (IBaseGridWidget<?, ?, ?>) c;
                gridWidgets.add( gridWidget );
                addGridWidgetConnectors();
            }
        }
    }

    private void addGridWidgetConnectors() {
        for ( IBaseGridWidget<?, ?, ?> gridWidget : gridWidgets ) {
            final IGridData<?, ?, ?> gridModel = gridWidget.getModel();
            for ( IGridColumn<?, ?> gridColumn : gridModel.getColumns() ) {
                if ( gridColumn.isVisible() ) {
                    if ( gridColumn.isLinked() ) {
                        final IBaseGridWidget<?, ?, ?> linkWidget = getLinkedGridWidget( gridColumn.getLink() );
                        if ( linkWidget != null ) {
                            GridWidgetConnector.Direction direction;
                            final Point2D sp = new Point2D( gridWidget.getX() + gridWidget.getWidth() / 2,
                                                            gridWidget.getY() + gridWidget.getHeight() / 2 );
                            final Point2D ep = new Point2D( linkWidget.getX() + linkWidget.getWidth() / 2,
                                                            linkWidget.getY() + linkWidget.getHeight() / 2 );
                            if ( sp.getX() < ep.getX() ) {
                                direction = GridWidgetConnector.Direction.EAST_WEST;
                                sp.setX( sp.getX() + gridWidget.getWidth() / 2 );
                                ep.setX( ep.getX() - linkWidget.getWidth() / 2 );
                            } else {
                                direction = GridWidgetConnector.Direction.WEST_EAST;
                                sp.setX( sp.getX() - gridWidget.getWidth() / 2 );
                                ep.setX( ep.getX() + linkWidget.getWidth() / 2 );
                            }

                            final GridWidgetConnector connector = new GridWidgetConnector( gridColumn,
                                                                                           gridColumn.getLink(),
                                                                                           direction );

                            if ( !connectors.containsKey( connector ) ) {
                                final Arrow arrow = new Arrow( sp,
                                                               ep,
                                                               10.0,
                                                               40.0,
                                                               45.0,
                                                               45.0,
                                                               ArrowType.AT_END )
                                        .setStrokeColor( ColorName.DARKGRAY )
                                        .setFillColor( ColorName.TAN )
                                        .setStrokeWidth( 2.0 );
                                connectors.put( connector,
                                                arrow );
                                super.add( arrow );
                                arrow.moveToBottom();
                            }
                        }
                    }
                }
            }
        }
    }

    private IBaseGridWidget<?, ?, ?> getLinkedGridWidget( final IGridColumn<?, ?> link ) {
        IBaseGridWidget<?, ?, ?> linkedGridWidget = null;
        for ( IBaseGridWidget<?, ?, ?> gridWidget : gridWidgets ) {
            final IGridData<?, ?, ?> gridModel = gridWidget.getModel();
            if ( gridModel.getColumns().contains( link ) ) {
                linkedGridWidget = gridWidget;
                break;
            }
        }
        return linkedGridWidget;
    }

    /**
     * Add a child and other children to this Layer. If the child or any children is a GridWidget
     * then also add a Connector between the Grid Widget and any "linked" GridWidgets.
     * @param child
     * @return
     */
    @Override
    public Layer add( final IPrimitive<?> child,
                      final IPrimitive<?>... children ) {
        addGridWidget( child,
                       children );
        return super.add( child,
                          children );
    }

    /**
     * Remove a child from this Layer. if the child is a GridWidget also remove
     * any Connectors that have been added between the GridWidget being removed
     * and any of GridWidgets.
     * @param child
     * @return
     */
    @Override
    public Layer remove( final IPrimitive<?> child ) {
        removeGridWidget( child );
        return super.remove( child );
    }

    private void removeGridWidget( final IPrimitive<?> child,
                                   final IPrimitive<?>... children ) {
        final List<IPrimitive<?>> all = new ArrayList<IPrimitive<?>>();
        all.add( child );
        all.addAll( Arrays.asList( children ) );
        for ( IPrimitive<?> c : all ) {
            if ( c instanceof IBaseGridWidget<?, ?, ?> ) {
                final IBaseGridWidget<?, ?, ?> gridWidget = (IBaseGridWidget<?, ?, ?>) c;
                gridWidgets.remove( gridWidget );
                removeGridWidgetConnectors( gridWidget );
            }
        }
    }

    private void removeGridWidgetConnectors( final IBaseGridWidget<?, ?, ?> gridWidget ) {
        final IGridData<?, ?, ?> gridModel = gridWidget.getModel();
        final List<GridWidgetConnector> removedConnectors = new ArrayList<GridWidgetConnector>();
        for ( Map.Entry<GridWidgetConnector, Arrow> e : connectors.entrySet() ) {
            if ( gridModel.getColumns().contains( e.getKey().getSourceColumn() ) || gridModel.getColumns().contains( e.getKey().getTargetColumn() ) ) {
                remove( e.getValue() );
                removedConnectors.add( e.getKey() );
            }
        }
        //Remove Connectors from HashMap after iteration of EntrySet to avoid ConcurrentModificationException
        for ( GridWidgetConnector c : removedConnectors ) {
            connectors.remove( c );
        }
    }

    @Override
    public Layer removeAll() {
        gridWidgets.clear();
        return super.removeAll();
    }

    @Override
    public void select( final IBaseGridWidget<?, ?, ?> selectedGridWidget ) {
        for ( IBaseGridWidget<?, ?, ?> gridWidget : gridWidgets ) {
            gridWidget.deselect();
            if ( gridWidget.equals( selectedGridWidget ) ) {
                selectedGridWidget.select();
            }
        }
        draw();
    }

    @Override
    public void selectLinkedColumn( final IGridColumn<?, ?> link ) {
        final IBaseGridWidget<?, ?, ?> gridWidget = getLinkedGridWidget( link );
        if ( gridWidget == null ) {
            return;
        }

        final GridWidgetScrollIntoViewAnimation a = new GridWidgetScrollIntoViewAnimation( gridWidget,
                                                                                           new Command() {
                                                                                               @Override
                                                                                               public void execute() {
                                                                                                   select( gridWidget );
                                                                                               }
                                                                                           } );
        a.run();
    }

    @Override
    public Set<IBaseGridWidget<?, ?, ?>> getGridWidgets() {
        return Collections.unmodifiableSet( gridWidgets );
    }

    public Rectangle getVisibleBounds() {
        return bounds;
    }

}
