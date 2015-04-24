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
package org.uberfire.ext.wires.core.grids.client.widget.dom.single;

import com.google.gwt.user.client.ui.Widget;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.wires.core.grids.client.widget.GridLayerRedrawManager;
import org.uberfire.ext.wires.core.grids.client.widget.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.IGridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.dom.BaseDOMElement;

/**
 * Base Factory for single-instance DOMElements, i.e. there can only be one instance "on screen" at any given time,
 * for example to handle "in cell" editing; when a DOMElement is required to "edit" the cell but not when the cell
 * is rendered ordinarily. This implementation keeps a single DOMElement that is detached from the GWT container
 * when not needed.
 * @param <T> The data-type of the cell
 * @param <W> The Widget to be wrapped by the DOMElement.
 * @param <E> The DOMElement type that this Factory generates.
 */
public abstract class BaseSingletonDOMElementFactory<T, W extends Widget, E extends BaseDOMElement<T, W>> implements ISingletonDOMElementFactory<W, E> {

    protected final IGridLayer gridLayer;
    protected final IBaseGridWidget gridWidget;

    protected W widget;
    protected E e;

    public BaseSingletonDOMElementFactory( final IGridLayer gridLayer,
                                           final IBaseGridWidget gridWidget ) {
        this.gridLayer = gridLayer;
        this.gridWidget = gridWidget;
    }

    @Override
    public void attachDomElement( final GridBodyCellRenderContext context,
                                  final Callback<E> onCreation,
                                  final Callback<E> onDisplay ) {
        gridLayer.batch( new GridLayerRedrawManager.PrioritizedCommand( Integer.MAX_VALUE ) {
            @Override
            public void execute() {
                final E domElement = createDomElement( gridLayer,
                                                       gridWidget,
                                                       context );
                domElement.setContext( context );
                domElement.initialise( context );
                onCreation.callback( domElement );

                domElement.attach();
                onDisplay.callback( domElement );
            }
        } );
    }

    @Override
    public void destroyResources() {
        flush();
    }

    @Override
    public void flush() {
        if ( e != null ) {
            if ( widget != null ) {
                e.flush( getValue() );
            }
            e.detach();
            widget = null;
            e = null;
        }
    }

    protected abstract T getValue();

}
