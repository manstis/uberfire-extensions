/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.multiple;

import com.google.gwt.user.client.ui.Widget;
import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.ext.wires.core.grids.client.widget.dom.BaseDOMElement;
import org.uberfire.ext.wires.core.grids.client.widget.dom.multiple.IMultipleDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.BaseGridColumnRenderer;

public abstract class BaseGridColumnMultipleDOMElementRenderer<T, W extends Widget, E extends BaseDOMElement> extends BaseGridColumnRenderer<T> implements IGridColumnMultipleDOMElementRenderer<T> {

    protected final IMultipleDOMElementFactory<W, E> factory;

    public BaseGridColumnMultipleDOMElementRenderer( final IMultipleDOMElementFactory<W, E> factory ) {
        this.factory = PortablePreconditions.checkNotNull( "factory",
                                                           factory );
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
