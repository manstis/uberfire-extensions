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
package org.uberfire.ext.wires.core.grids.client.model;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;

/**
 * Base implementation of a grid column to avoid boiler-plate for more specific implementations.
 * @param <R> The generic type of the Rows handled by the column
 * @param <V> The generic type of the Cells handled by the column
 */
public abstract class BaseGridColumn<R extends IGridRow<V>, V extends IGridCell<?>> implements IGridColumn<R, V> {

    //Default group for columns
    private static final String DEFAULT_GROUP_IDENTIFIER = "";

    //Default minimum width of a column.
    private static final double COLUMN_MIN_WIDTH = 100;

    private String title;
    private double width;
    private boolean isResizable = true;
    private boolean isMoveable = true;
    private boolean isVisible = true;
    private String columnGroup = DEFAULT_GROUP_IDENTIFIER;
    private Double minimumWidth = COLUMN_MIN_WIDTH;
    private Double maximumWidth = null;
    private IGridColumn<R, V> link;
    private int index = -1;

    public BaseGridColumn( final String title,
                           final double width ) {
        this.title = title;
        this.width = width;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle( final String title ) {
        this.title = title;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public void setWidth( final double width ) {
        this.width = width;
    }

    @Override
    public boolean isLinked() {
        return link != null;
    }

    @Override
    public IGridColumn<R, V> getLink() {
        return link;
    }

    @Override
    public void setLink( final IGridColumn<R, V> link ) {
        this.link = link;
    }

    @Override
    public int getIndex() {
        if ( index == -1 ) {
            throw new IllegalStateException( "Column has not been added to a Grid and hence has no index." );
        }
        return index;
    }

    @Override
    public void setIndex( final int index ) {
        this.index = index;
    }

    @Override
    public Group renderHeader() {
        final Group g = new Group();
        final Text t = new Text( getTitle() )
                .setFillColor( ColorName.DARKGOLDENROD )
                .setFontSize( 12 )
                .setFontStyle( "bold" )
                .setFontFamily( "serif" )
                .setListening( false )
                .setTextBaseLine( TextBaseLine.MIDDLE )
                .setTextAlign( TextAlign.CENTER );
        g.add( t );
        return g;
    }

    @Override
    public void initialiseResources() {
        //Do nothing by default
    }

    @Override
    public void destroyResources() {
        //Do nothing by default
    }

    @Override
    public void freeUnusedResources() {
        //Do nothing by default
    }

    @Override
    public boolean isResizable() {
        return isResizable;
    }

    @Override
    public void setResizable( final boolean isResizable ) {
        this.isResizable = isResizable;
    }

    @Override
    public boolean isMoveable() {
        return isMoveable;
    }

    @Override
    public void setMoveable( final boolean isMoveable ) {
        this.isMoveable = isMoveable;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible( final boolean isVisible ) {
        this.isVisible = isVisible;
    }

    @Override
    public String getColumnGroup() {
        return columnGroup;
    }

    @Override
    public void setColumnGroup( final String columnGroup ) {
        this.columnGroup = columnGroup;
    }

    @Override
    public Double getMinimumWidth() {
        return minimumWidth;
    }

    @Override
    public void setMinimumWidth( final Double minimumWidth ) {
        this.minimumWidth = minimumWidth;
    }

    @Override
    public Double getMaximumWidth() {
        return maximumWidth;
    }

    @Override
    public void setMaximumWidth( final Double maximumWidth ) {
        this.maximumWidth = maximumWidth;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof IGridColumn ) ) {
            return false;
        }

        IGridColumn column = (IGridColumn) o;

        if ( title != null ? !title.equals( column.getTitle() ) : column.getTitle() != null ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = ~~result;
        return result;
    }

}
