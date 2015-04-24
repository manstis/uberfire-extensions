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

import org.uberfire.client.callbacks.Callback;
import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridCell;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridCell;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.dom.TextBoxDOMElement;
import org.uberfire.ext.wires.core.grids.client.widget.dom.single.IHasSingletonDOMElementResource;
import org.uberfire.ext.wires.core.grids.client.widget.dom.single.TextBoxSingletonDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.single.StringColumnDOMElementSingletonRenderer;

public class StringDOMElementSingletonColumn extends BaseGridColumn<String> implements IHasSingletonDOMElementResource {

    private final TextBoxSingletonDOMElementFactory factory;

    public StringDOMElementSingletonColumn( final IGridColumn.HeaderMetaData headerMetaData,
                                            final TextBoxSingletonDOMElementFactory factory,
                                            final double width ) {
        this( new ArrayList<HeaderMetaData>() {{
                  add( headerMetaData );
              }},
              factory,
              width );
    }

    public StringDOMElementSingletonColumn( final List<IGridColumn.HeaderMetaData> headerMetaData,
                                            final TextBoxSingletonDOMElementFactory factory,
                                            final double width ) {
        super( headerMetaData,
               new StringColumnDOMElementSingletonRenderer( factory ),
               width );
        this.factory = PortablePreconditions.checkNotNull( "factory",
                                                           factory );
    }

    @Override
    public void edit( final IGridCell<String> cell,
                      final GridBodyCellRenderContext context,
                      final Callback<IGridCellValue<String>> callback ) {
        factory.attachDomElement( context,
                                  new Callback<TextBoxDOMElement>() {
                                      @Override
                                      public void callback( final TextBoxDOMElement e ) {
                                          e.getWidget().setValue( assertCell( cell ).getValue().getValue() );
                                      }
                                  },
                                  new Callback<TextBoxDOMElement>() {
                                      @Override
                                      public void callback( final TextBoxDOMElement e ) {
                                          e.getWidget().setFocus( true );
                                      }
                                  } );
    }

    private IGridCell<String> assertCell( final IGridCell<String> cell ) {
        if ( cell != null ) {
            return cell;
        }
        return new BaseGridCell<String>( new BaseGridCellValue<String>( "" ) );
    }

    @Override
    public void flush() {
        factory.flush();
    }

    @Override
    public void destroyResources() {
        factory.destroyResources();
    }

}