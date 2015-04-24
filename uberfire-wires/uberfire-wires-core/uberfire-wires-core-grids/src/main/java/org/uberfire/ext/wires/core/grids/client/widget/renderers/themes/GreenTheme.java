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
import com.ait.lienzo.client.core.types.Shadow;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.widget.dnd.IsRowDragHandle;

/**
 * A renderer that draws a predominantly green GridWidget.
 */
public class GreenTheme implements IGridRendererTheme {

    @Override
    public String getName() {
        return "Green";
    }

    @Override
    public Rectangle getSelector() {
        final Rectangle selector = new Rectangle( 0, 0 )
                .setStrokeWidth( 2.0 )
                .setStrokeColor( ColorName.GREEN )
                .setShadow( new Shadow( ColorName.DARKGREEN, 4, 0.0, 0.0 ) );
        return selector;
    }

    @Override
    public Rectangle getCellSelector() {
        final Rectangle selector = new Rectangle( 0, 0 )
                .setStrokeColor( ColorName.GREEN );
        return selector;
    }

    @Override
    public Rectangle getHeaderBackground() {
        final Rectangle header = new Rectangle( 0, 0 )
                .setFillColor( ColorName.LAWNGREEN )
                .setStrokeColor( ColorName.BLACK )
                .setStrokeWidth( 0.5 );
        return header;
    }

    @Override
    public Rectangle getHeaderLinkBackground() {
        final Rectangle link = new Rectangle( 0, 0 )
                .setFillColor( ColorName.BROWN )
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 );
        return link;
    }

    @Override
    public MultiPath getHeaderGridLine() {
        final MultiPath headerGrid = new MultiPath()
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 )
                .setListening( false );
        return headerGrid;
    }

    @Override
    public Text getHeaderText() {
        final Text t = new Text( "" )
                .setFillColor( ColorName.DARKGREEN )
                .setFontSize( 12 )
                .setFontStyle( "bold" )
                .setFontFamily( "serif" )
                .setListening( false )
                .setTextBaseLine( TextBaseLine.MIDDLE )
                .setTextAlign( TextAlign.CENTER );
        return t;
    }

    @Override
    public Rectangle getBodyBackground( final IGridColumn<?> column ) {
        if ( column instanceof IsRowDragHandle ) {
            return getHeaderBackground();
        }
        final Rectangle body = new Rectangle( 0, 0 )
                .setFillColor( ColorName.LIGHTGREEN )
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 );
        return body;
    }

    @Override
    public MultiPath getBodyGridLine() {
        final MultiPath bodyGrid = new MultiPath()
                .setStrokeColor( ColorName.SLATEGRAY )
                .setStrokeWidth( 0.5 );
        return bodyGrid;
    }

    @Override
    public Text getBodyText() {
        final Text t = new Text( "" )
                .setFillColor( ColorName.GREEN )
                .setFontSize( 12 )
                .setFontFamily( "serif" )
                .setListening( false )
                .setTextBaseLine( TextBaseLine.MIDDLE )
                .setTextAlign( TextAlign.CENTER );
        return t;
    }

}
