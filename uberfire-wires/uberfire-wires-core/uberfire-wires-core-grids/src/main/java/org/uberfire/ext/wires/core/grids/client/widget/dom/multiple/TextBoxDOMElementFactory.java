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
package org.uberfire.ext.wires.core.grids.client.widget.dom.multiple;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import org.gwtbootstrap3.client.ui.TextBox;
import org.uberfire.ext.wires.core.grids.client.widget.IBaseGridWidget;
import org.uberfire.ext.wires.core.grids.client.widget.IGridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.dom.TextBoxDOMElement;

/**
 * A DOMElement Factory for multi-instance TextBoxes.
 */
public class TextBoxDOMElementFactory extends BaseDOMElementFactory<String, TextBox, TextBoxDOMElement> {

    public TextBoxDOMElementFactory( final IGridLayer gridLayer,
                                     final IBaseGridWidget gridWidget ) {
        super( gridLayer,
               gridWidget );
    }

    @Override
    public TextBox createWidget() {
        return new TextBox();
    }

    @Override
    public TextBoxDOMElement createDomElement( final IGridLayer gridLayer,
                                               final IBaseGridWidget gridWidget,
                                               final GridBodyCellRenderContext context ) {
        final TextBox widget = createWidget();
        final TextBoxDOMElement e = new TextBoxDOMElement( widget,
                                                           gridLayer,
                                                           gridWidget );
        widget.addBlurHandler( new BlurHandler() {
            @Override
            public void onBlur( final BlurEvent event ) {
                e.flush( widget.getValue() );
                gridLayer.batch();
            }
        } );
        return e;
    }

}
