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
package org.uberfire.ext.wires.core.grids.client.widget.renderers.grids;

import com.ait.lienzo.client.core.shape.Group;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridHeaderRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.themes.IGridRendererTheme;

/**
 * Definition of a render for the pluggable rendering mechanism.
 */
public interface IGridRenderer {

    /**
     * Get the height of the header built by this renderer.
     * @return
     */
    double getHeaderHeight();

    /**
     * Set the theme.
     * @param theme
     */
    void setTheme( final IGridRendererTheme theme );

    /**
     * Get the theme.
     */
    IGridRendererTheme getTheme();

    /**
     * Render a "selector" when a grid has been selected, i.e. clicked.
     * @param width The width of the GridWidget.
     * @param height The height of the GridWidget including header and body.
     * @return
     */
    Group renderSelector( final double width,
                          final double height );

    /**
     * Render the selected ranges and append to the Body Group.
     * @param model The data model for the GridWidget.
     * @param context The context of the render phase.
     * @param rendererHelper Helper for rendering.
     * @return
     */
    Group renderSelectedCells( final IGridData model,
                               final GridBodyRenderContext context,
                               final BaseGridRendererHelper rendererHelper );

    /**
     * Render the header for the Grid.
     * @param model The data model for the GridWidget.
     * @param context The context of the render phase.
     * @param rendererHelper Helper for rendering.
     * @return A Group containing all Shapes representing the Header.
     */
    Group renderHeader( final IGridData model,
                        final GridHeaderRenderContext context,
                        final BaseGridRendererHelper rendererHelper,
                        final BaseGridRendererHelper.RenderingInformation renderingInformation );

    /**
     * Render the body for the Grid.
     * @param model The data model for the GridWidget.
     * @param context The context of the render phase.
     * @param rendererHelper Helper for rendering.
     * @return A Group containing all Shapes representing the Body.
     */
    Group renderBody( final IGridData model,
                      final GridBodyRenderContext context,
                      final BaseGridRendererHelper rendererHelper,
                      final BaseGridRendererHelper.RenderingInformation renderingInformation );

    /**
     * Check whether a cell-relative coordinate is "on" the hot-spot to toggle the collapsed/expanded state.
     * @param cellX The MouseEvent relative to the cell's x-coordinate.
     * @param cellY The MouseEvent relative to the cell's y-coordinate.
     * @param cellWidth Width of the containing cell.
     * @param cellHeight Height of the containing cell.
     * @return true if the cell coordinate is on the hot-spot.
     */
    boolean onGroupingToggle( final double cellX,
                              final double cellY,
                              final double cellWidth,
                              final double cellHeight );

}
