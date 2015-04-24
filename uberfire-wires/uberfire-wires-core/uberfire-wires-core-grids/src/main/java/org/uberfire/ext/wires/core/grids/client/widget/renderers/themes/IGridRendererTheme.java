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
package org.uberfire.ext.wires.core.grids.client.widget.renderers.themes;

import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Text;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;

/**
 * Definition of themes used by a render for the pluggable rendering mechanism.
 */
public interface IGridRendererTheme {

    /**
     * Get a display name for the theme.
     * @return
     */
    String getName();

    /**
     * Delegate construction of the "selector" to sub-classes. All implementations
     * are to provide a Rectangle surrounding the whole GridWidget.
     * @return
     */
    Rectangle getSelector();

    /**
     * Delegate construction of the cell "selector" to sub-classes. All implementations
     * are to provide a Rectangle surrounding the whole cell.
     * @return
     */
    Rectangle getCellSelector();

    /**
     * Delegate the Header's background Rectangle to sub-classes.
     * @return
     */
    Rectangle getHeaderBackground();

    /**
     * Delegate the Header's background Rectangle, used for "linked" columns to sub-classes.
     * @return
     */
    Rectangle getHeaderLinkBackground();

    /**
     * Delegate the Header's grid lines to sub-classes.
     * @return
     */
    MultiPath getHeaderGridLine();

    /**
     * Delegate the Header's Text to sub-classes.
     * @return
     */
    Text getHeaderText();

    /**
     * Delegate the Body's background Rectangle to sub-classes.
     * @param column The column being rendered.
     * @return
     */
    Rectangle getBodyBackground( final IGridColumn<?> column );

    /**
     * Delegate the Body's grid lines to sub-classes.
     * @return
     */
    MultiPath getBodyGridLine();

    /**
     * Delegate the Body's Text to sub-classes.
     * @return
     */
    Text getBodyText();

}
