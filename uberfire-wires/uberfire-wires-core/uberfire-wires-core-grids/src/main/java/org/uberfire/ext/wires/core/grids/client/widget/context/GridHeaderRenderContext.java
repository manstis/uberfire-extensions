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
package org.uberfire.ext.wires.core.grids.client.widget.context;

import java.util.List;

import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;

/**
 * The context of a Grid's header during the rendering phase.
 */
public class GridHeaderRenderContext {

    private final List<IGridColumn<?>> allColumns;
    private final List<IGridColumn<?>> blockColumns;
    private final boolean isSelectionLayer;

    public GridHeaderRenderContext( final List<IGridColumn<?>> allColumns,
                                    final List<IGridColumn<?>> blockColumns,
                                    final boolean isSelectionLayer ) {
        this.allColumns = allColumns;
        this.blockColumns = blockColumns;
        this.isSelectionLayer = isSelectionLayer;
    }

    /**
     * All columns in the block, some of which may not be visible or rendered within the Viewport.
     * @return
     */
    public List<IGridColumn<?>> getAllColumns() {
        return allColumns;
    }

    /**
     * The columns to render for the block.
     * @return
     */
    public List<IGridColumn<?>> getBlockColumns() {
        return blockColumns;
    }

    /**
     * Is the SelectionLayer being rendered.
     * @return
     */
    public boolean isSelectionLayer() {
        return isSelectionLayer;
    }

}
