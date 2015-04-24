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

import com.ait.lienzo.client.core.event.NodeMouseDownHandler;
import com.ait.lienzo.client.core.event.NodeMouseMoveHandler;
import com.ait.lienzo.client.core.event.NodeMouseUpHandler;
import com.ait.lienzo.client.core.shape.IContainer;
import com.ait.lienzo.client.core.shape.IDrawable;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.google.gwt.user.client.ui.AbsolutePanel;
import org.uberfire.ext.wires.core.grids.client.widget.dnd.GridWidgetDnDHandlersState;

/**
 * A specialised Layer that supports pass-through of MouseEvents from DOMElements to GridWidgets.
 * This implementation handles  drawing connectors between "linked" grids and acts as a ISelection manager.
 */
public interface IGridLayer extends IGridSelectionManager,
                                    IContainer<Layer, IPrimitive<?>>,
                                    IDrawable<Layer>,
                                    NodeMouseDownHandler,
                                    NodeMouseMoveHandler,
                                    NodeMouseUpHandler {

    /**
     * Get the visible bounds of the Layer in the Viewport
     * @return
     */
    Rectangle getVisibleBounds();

    /**
     * Get the state of any Handlers registered to the Grid
     * @return
     */
    GridWidgetDnDHandlersState getGridWidgetHandlersState();

    /**
     * Set a reference to an AbsolutePanel that overlays the Canvas.
     * This can be used to overlay DOM elements on top of the Canvas.
     * @param getDomElementContainer The overlay panel
     */
    void setDomElementContainer( final AbsolutePanel getDomElementContainer );

    /**
     * Get the overlay panel.
     * @return
     */
    AbsolutePanel getDomElementContainer();

    /**
     * Redraw the Grid. All updates are batched into a single draw on the next animation frame.
     * @return
     */
    Layer batch();

    /**
     * Redraw the Grid. All updates are batched into a single draw on the next animation
     * frame. Execute the provided command after the batch redraw has been scheduled.
     * @param command The command to execute
     * @return
     */
    Layer batch( final GridLayerRedrawManager.PrioritizedCommand command );

}
