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

import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Node;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.IGridRenderer;

/**
 * The base of all GridWidgets.
 * @param <M> The Model backing the GridWidget.
 * @param <R> The Renderer to be used to render the GridWidget.
 */
public interface IBaseGridWidget<C extends Node<C>, M extends IGridData<?, ?, ?>, R extends IGridRenderer<M>> extends IPrimitive<C>,
                                                                                                                      NodeMouseClickHandler {

    /**
     * Get the Model backing the Widget.
     * @return
     */
    M getModel();

    /**
     * Get the Renderer used to render the Widget.
     * @return
     */
    IGridRenderer<M> getRenderer();

    /**
     * Set the Rendered used to render the Widget.
     * @param renderer
     */
    void setRenderer( final R renderer );

    /**
     * Get the width of the whole Widget.
     * @return
     */
    double getWidth();

    /**
     * Get the height of the whole Widget, including Header and Body.
     * @return
     */
    double getHeight();

    /**
     * Select the Widget; i.e. it has been clicked on, so show some visual indicator that it has been selected.
     */
    void select();

    /**
     * Deselect the Widget; i.e. another GridWidget has been clicked on, so hide
     * any visual indicator that this Widget was selected.
     */
    void deselect();

}
