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
package org.uberfire.ext.wires.core.grids.client.widget.dom;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.user.client.ui.AbsolutePanel;
import org.uberfire.ext.wires.core.grids.client.widget.layer.GridLayer;
import org.uberfire.ext.wires.core.grids.client.widget.grid.IBaseGridWidget;

/**
 * A DOMElement Factory for single-instance TextBoxes.
 */
public class TextBoxSingletonDOMElementFactory extends BaseSingletonDOMElementFactory<String, TextBox, TextBoxDOMElement> {

    public TextBoxSingletonDOMElementFactory( final GridLayer gridLayer,
                                              final IBaseGridWidget<?, ?, ?> gridWidget,
                                              final AbsolutePanel domElementContainer ) {
        super( gridLayer,
               gridWidget,
               domElementContainer );
    }

    @Override
    public TextBox createWidget() {
        return new TextBox();
    }

    @Override
    public TextBoxDOMElement createDomElement( final GridLayer gridLayer,
                                               final IBaseGridWidget<?, ?, ?> gridWidget,
                                               final AbsolutePanel domElementContainer ) {
        return new TextBoxDOMElement( createWidget(),
                                      gridLayer,
                                      gridWidget,
                                      this,
                                      domElementContainer );
    }

}
