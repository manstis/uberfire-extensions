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
package org.uberfire.ext.wires.core.grids.client.widget.edit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import org.gwtbootstrap3.client.shared.event.ModalShowEvent;
import org.gwtbootstrap3.client.shared.event.ModalShowHandler;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;

/**
 * A simple popup to demonstrate "out of cell" editing for Strings.
 */
public class EditorPopup extends Modal {

    private final TextBox textBox = new TextBox();
    private final ModalBody modalBody = new ModalBody();

    private IGridCellValue<String> value;
    private Callback<IGridCellValue<String>> callback = null;

    public EditorPopup() {
        setTitle( "Edit" );

        textBox.addKeyDownHandler( new KeyDownHandler() {
            @Override
            public void onKeyDown( final KeyDownEvent event ) {
                if ( event.getNativeKeyCode() == KeyCodes.KEY_ENTER ) {
                    commit();
                }
            }
        } );

        modalBody.add( textBox );
        add( modalBody );

        final ModalFooter footer = new ModalFooter();
        final Button okButton = new Button( "OK" );
        okButton.setIcon( IconType.EDIT );
        okButton.setType( ButtonType.PRIMARY );
        okButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( final ClickEvent event ) {
                commit();
            }
        } );

        final Button cancelButton = new Button( "Cancel" );
        cancelButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( final ClickEvent event ) {
                cancel();
            }
        } );

        footer.add( okButton );
        footer.add( cancelButton );
        add( footer );

        addShowHandler( new ModalShowHandler() {
            @Override
            public void onShow( final ModalShowEvent evt ) {
                textBox.setFocus( true );
            }
        } );

    }

    /**
     * Show the popup
     * @param value The value to show in the editor.
     * @param callback Callback to invoke when the popup is "OK'ed".
     */
    public void edit( final IGridCellValue<String> value,
                      final Callback<IGridCellValue<String>> callback ) {
        this.value = value;
        this.callback = callback;
        textBox.setText( value == null ? "" : value.getValue() );
        show();
    }

    private void cancel() {
        hide();
    }

    private void commit() {
        if ( callback != null ) {
            callback.callback( new BaseGridCellValue<String>( textBox.getText() ) );
        }
        hide();
    }

}
