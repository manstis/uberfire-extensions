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
package org.uberfire.ext.wires.core.grids.client.widget.columns;

import java.util.ArrayList;
import java.util.List;

import org.uberfire.ext.wires.core.grids.client.model.BaseGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.widget.dom.multiple.CheckBoxDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.dom.multiple.IHasMultipleDOMElementResources;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.multiple.BooleanColumnDOMElementRenderer;

public class BooleanDOMElementColumn extends BaseGridColumn<Boolean> implements IHasMultipleDOMElementResources {

    private CheckBoxDOMElementFactory factory;

    public BooleanDOMElementColumn( final IGridColumn.HeaderMetaData headerMetaData,
                                    final CheckBoxDOMElementFactory factory,
                                    final double width ) {
        this( new ArrayList<HeaderMetaData>() {{
                  add( headerMetaData );
              }},
              factory,
              width );
    }

    public BooleanDOMElementColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                    final CheckBoxDOMElementFactory factory,
                                    final double width ) {
        super( headerMetaData,
               new BooleanColumnDOMElementRenderer( factory ),
               width );
        this.factory = factory;
    }

    @Override
    public void initialiseResources() {
        factory.initialiseResources();
    }

    @Override
    public void destroyResources() {
        factory.destroyResources();
    }

    @Override
    public void freeUnusedResources() {
        factory.freeUnusedResources();
    }

}