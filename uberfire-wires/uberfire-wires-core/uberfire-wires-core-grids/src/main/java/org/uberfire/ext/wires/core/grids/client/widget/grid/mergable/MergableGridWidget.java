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
package org.uberfire.ext.wires.core.grids.client.widget.grid.mergable;

import org.uberfire.ext.wires.core.grids.client.model.mergable.MergableGridData;
import org.uberfire.ext.wires.core.grids.client.widget.grid.BaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.ISelectionManager;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.mergable.IMergableGridRenderer;

/**
 * A Grid that contains mergable cells
 */
public class MergableGridWidget extends BaseGridWidget<MergableGridData, IMergableGridRenderer> {

    public MergableGridWidget( final MergableGridData model,
                               final ISelectionManager selectionManager,
                               final IMergableGridRenderer renderer ) {
        super( model,
               selectionManager,
               renderer );

    }

    public boolean onGroupingToggle( final double cellX,
                                     final double cellY,
                                     final double columnWidth,
                                     final double rowHeight ) {
        return renderer.onGroupingToggle( cellX,
                                          cellY,
                                          columnWidth,
                                          rowHeight );
    }

}
