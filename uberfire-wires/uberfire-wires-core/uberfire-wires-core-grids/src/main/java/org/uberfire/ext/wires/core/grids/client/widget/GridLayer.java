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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ait.lienzo.client.core.event.NodeMouseDownEvent;
import com.ait.lienzo.client.core.event.NodeMouseDownHandler;
import com.ait.lienzo.client.core.event.NodeMouseMoveEvent;
import com.ait.lienzo.client.core.event.NodeMouseUpEvent;
import com.ait.lienzo.client.core.shape.Arrow;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.shared.core.types.ArrowType;
import com.ait.lienzo.shared.core.types.ColorName;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.widget.animation.GridWidgetScrollIntoViewAnimation;
import org.uberfire.ext.wires.core.grids.client.widget.dnd.GridWidgetDnDHandlersState;
import org.uberfire.ext.wires.core.grids.client.widget.dnd.GridWidgetDnDMouseDownHandler;
import org.uberfire.ext.wires.core.grids.client.widget.dnd.GridWidgetDnDMouseMoveHandler;
import org.uberfire.ext.wires.core.grids.client.widget.dnd.GridWidgetDnDMouseUpHandler;
import org.uberfire.ext.wires.core.grids.client.widget.dom.single.IHasSingletonDOMElementResource;

/**
 * Default implementation of IGridLayer
 */
public class GridLayer extends Layer implements IGridLayer {

    //This is helpful when debugging rendering issues to set the bounds smaller than the Viewport
    private static final int PADDING = 0;

    private Set<IBaseGridWidget> gridWidgets = new HashSet<IBaseGridWidget>();
    private Map<GridWidgetConnector, Arrow> gridWidgetConnectors = new HashMap<GridWidgetConnector, Arrow>();
    private AbsolutePanel domElementContainer;

    private Rectangle bounds;

    private final GridWidgetDnDMouseDownHandler mouseDownHandler;
    private final GridWidgetDnDMouseMoveHandler mouseMoveHandler;
    private final GridWidgetDnDMouseUpHandler mouseUpHandler;
    private final GridWidgetDnDHandlersState state = new GridWidgetDnDHandlersState();

    private final GridLayerRedrawManager.PrioritizedCommand REDRAW = new GridLayerRedrawManager.PrioritizedCommand( Integer.MIN_VALUE ) {
        @Override
        public void execute() {
            GridLayer.this.draw();
        }
    };

    public GridLayer() {
        bounds = new Rectangle( 0, 0 )
                .setVisible( false );
        add( bounds );

        //Column DnD handlers
        mouseDownHandler = new GridWidgetDnDMouseDownHandler( this,
                                                              state );
        mouseMoveHandler = new GridWidgetDnDMouseMoveHandler( this,
                                                              state );
        mouseUpHandler = new GridWidgetDnDMouseUpHandler( this,
                                                          state );
        addNodeMouseDownHandler( mouseDownHandler );
        addNodeMouseMoveHandler( mouseMoveHandler );
        addNodeMouseUpHandler( mouseUpHandler );

        //Destroy SingletonDOMElements on MouseDownEvents to ensure they're hidden:-
        // 1) When moving columns
        // 2) When resizing columns
        // 3) When the User clicks outside of a GridWidget
        // We do this rather than setFocus on GridPanel as the FocusImplSafari implementation of
        // FocusPanel sets focus at unpredictable times which can lead to SingletonDOMElements
        // loosing focus after they've been attached to the DOM and hence disappearing.
        addNodeMouseDownHandler( new NodeMouseDownHandler() {
            @Override
            public void onNodeMouseDown( final NodeMouseDownEvent event ) {
                for ( IBaseGridWidget gridWidget : gridWidgets ) {
                    for ( IGridColumn<?> gridColumn : gridWidget.getModel().getColumns() ) {
                        if ( gridColumn instanceof IHasSingletonDOMElementResource ) {
                            ( (IHasSingletonDOMElementResource) gridColumn ).destroyResources();
                        }
                    }
                }
            }
        } );
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
    public Layer draw() {
        //We use Layer.batch() to ensure rendering is tied to the browser's requestAnimationFrame()
        //however this calls back into Layer.draw() so update dependent Shapes here.
        updateVisibleBounds();
        updateGridWidgetConnectors();
        return super.draw();
    }

    @Override
    public Layer batch() {
        return batch( REDRAW );
    }

    @Override
    public Layer batch( final GridLayerRedrawManager.PrioritizedCommand command ) {
        GridLayerRedrawManager.get().schedule( command );
        return this;
    }

    private void updateVisibleBounds() {
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

    private void updateGridWidgetConnectors() {
        for ( Map.Entry<GridWidgetConnector, Arrow> e : gridWidgetConnectors.entrySet() ) {
            final GridWidgetConnector connector = e.getKey();
            final Arrow arrow = e.getValue();
            final IGridColumn<?> sourceGridColumn = connector.getSourceColumn();
            final IGridColumn<?> targetGridColumn = connector.getTargetColumn();
            final IBaseGridWidget sourceGridWidget = getLinkedGridWidget( sourceGridColumn );
            final IBaseGridWidget targetGridWidget = getLinkedGridWidget( targetGridColumn );
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
     * Add a child to this Layer. If the child is a GridWidget then also add
     * a Connector between the Grid Widget and any "linked" GridWidgets.
     * @param child Primitive to add to the Layer
     * @return The Layer
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
            if ( c instanceof IBaseGridWidget ) {
                final IBaseGridWidget gridWidget = (IBaseGridWidget) c;
                gridWidgets.add( gridWidget );
                addGridWidgetConnectors();
            }
        }
    }

    private void addGridWidgetConnectors() {
        for ( IBaseGridWidget gridWidget : gridWidgets ) {
            final IGridData gridModel = gridWidget.getModel();
            for ( IGridColumn<?> gridColumn : gridModel.getColumns() ) {
                if ( gridColumn.isVisible() ) {
                    if ( gridColumn.isLinked() ) {
                        final IBaseGridWidget linkedGridWidget = getLinkedGridWidget( gridColumn.getLink() );
                        if ( linkedGridWidget != null ) {
                            GridWidgetConnector.Direction direction;
                            final Point2D sp = new Point2D( gridWidget.getX() + gridWidget.getWidth() / 2,
                                                            gridWidget.getY() + gridWidget.getHeight() / 2 );
                            final Point2D ep = new Point2D( linkedGridWidget.getX() + linkedGridWidget.getWidth() / 2,
                                                            linkedGridWidget.getY() + linkedGridWidget.getHeight() / 2 );
                            if ( sp.getX() < ep.getX() ) {
                                direction = GridWidgetConnector.Direction.EAST_WEST;
                                sp.setX( sp.getX() + gridWidget.getWidth() / 2 );
                                ep.setX( ep.getX() - linkedGridWidget.getWidth() / 2 );
                            } else {
                                direction = GridWidgetConnector.Direction.WEST_EAST;
                                sp.setX( sp.getX() - gridWidget.getWidth() / 2 );
                                ep.setX( ep.getX() + linkedGridWidget.getWidth() / 2 );
                            }

                            final GridWidgetConnector connector = new GridWidgetConnector( gridColumn,
                                                                                           gridColumn.getLink(),
                                                                                           direction );

                            if ( !gridWidgetConnectors.containsKey( connector ) ) {
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
                                gridWidgetConnectors.put( connector,
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

    private IBaseGridWidget getLinkedGridWidget( final IGridColumn<?> linkedGridColumn ) {
        IBaseGridWidget linkedGridWidget = null;
        for ( IBaseGridWidget gridWidget : gridWidgets ) {
            final IGridData gridModel = gridWidget.getModel();
            if ( gridModel.getColumns().contains( linkedGridColumn ) ) {
                linkedGridWidget = gridWidget;
                break;
            }
        }
        return linkedGridWidget;
    }

    /**
     * Add a child and other children to this Layer. If the child or any children is a GridWidget
     * then also add a Connector between the Grid Widget and any "linked" GridWidgets.
     * @param child Primitive to add to the Layer
     * @param children Additional primitive(s) to add to the Layer
     * @return The Layer
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
     * @param child Primitive to remove from the Layer
     * @return The Layer
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
            if ( c instanceof IBaseGridWidget ) {
                final IBaseGridWidget gridWidget = (IBaseGridWidget) c;
                gridWidgets.remove( gridWidget );
                removeGridWidgetConnectors( gridWidget );
            }
        }
    }

    private void removeGridWidgetConnectors( final IBaseGridWidget gridWidget ) {
        final IGridData gridModel = gridWidget.getModel();
        final List<GridWidgetConnector> removedConnectors = new ArrayList<GridWidgetConnector>();
        for ( Map.Entry<GridWidgetConnector, Arrow> e : gridWidgetConnectors.entrySet() ) {
            if ( gridModel.getColumns().contains( e.getKey().getSourceColumn() ) || gridModel.getColumns().contains( e.getKey().getTargetColumn() ) ) {
                remove( e.getValue() );
                removedConnectors.add( e.getKey() );
            }
        }
        //Remove Connectors from HashMap after iteration of EntrySet to avoid ConcurrentModificationException
        for ( GridWidgetConnector c : removedConnectors ) {
            gridWidgetConnectors.remove( c );
        }
    }

    @Override
    public Layer removeAll() {
        gridWidgets.clear();
        return super.removeAll();
    }

    @Override
    public void select( final IBaseGridWidget selectedGridWidget ) {
        boolean selectionChanged = false;
        for ( IBaseGridWidget gridWidget : gridWidgets ) {
            if ( gridWidget.isSelected() ) {
                if ( !gridWidget.equals( selectedGridWidget ) ) {
                    selectionChanged = true;
                    gridWidget.deselect();
                }
            } else if ( gridWidget.equals( selectedGridWidget ) ) {
                selectionChanged = true;
                gridWidget.select();
            }
        }
        if ( selectionChanged ) {
            batch();
        }
    }

    @Override
    public void selectLinkedColumn( final IGridColumn<?> selectedGridColumn ) {
        final IBaseGridWidget gridWidget = getLinkedGridWidget( selectedGridColumn );
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
    public Set<IBaseGridWidget> getGridWidgets() {
        return Collections.unmodifiableSet( gridWidgets );
    }

    @Override
    public Rectangle getVisibleBounds() {
        return bounds;
    }

    @Override
    public GridWidgetDnDHandlersState getGridWidgetHandlersState() {
        return state;
    }

    @Override
    public void setDomElementContainer( final AbsolutePanel domElementContainer ) {
        this.domElementContainer = domElementContainer;
    }

    @Override
    public AbsolutePanel getDomElementContainer() {
        return domElementContainer;
    }

}
