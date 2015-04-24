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

import java.util.ArrayList;
import java.util.List;

import org.uberfire.client.callbacks.Callback;
import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridBodyCellRenderContext;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.IGridColumnRenderer;

/**
 * Base implementation of a grid column to avoid boiler-plate for more specific implementations.
 */
public class BaseGridColumn<T> implements IGridColumn<T> {

    //Default minimum width of a column.
    private static final double COLUMN_MIN_WIDTH = 100;

    private double width;
    private boolean isResizable = true;
    private boolean isMovable = true;
    private boolean isFloatable = false;
    private boolean isVisible = true;
    private Double minimumWidth = COLUMN_MIN_WIDTH;
    private Double maximumWidth = null;
    private IGridColumn<?> link;
    private int index = -1;
    private List<HeaderMetaData> headerMetaData = new ArrayList<HeaderMetaData>();
    private IGridColumnRenderer<T> columnRenderer;

    public BaseGridColumn( final HeaderMetaData headerMetaData,
                           final IGridColumnRenderer<T> columnRenderer,
                           final double width ) {
        PortablePreconditions.checkNotNull( "headerMetaData",
                                            headerMetaData );
        PortablePreconditions.checkNotNull( "columnRenderer",
                                            columnRenderer );
        this.headerMetaData.add( headerMetaData );
        this.columnRenderer = columnRenderer;
        this.width = width;
    }

    public BaseGridColumn( final List<HeaderMetaData> headerMetaData,
                           final IGridColumnRenderer<T> columnRenderer,
                           final double width ) {
        PortablePreconditions.checkNotNull( "headerMetaData",
                                            headerMetaData );
        PortablePreconditions.checkCondition( "headerMetaData has at least one entry",
                                              headerMetaData.size() > 0 );
        PortablePreconditions.checkNotNull( "columnRenderer",
                                            columnRenderer );
        this.headerMetaData.addAll( headerMetaData );
        this.columnRenderer = columnRenderer;
        this.width = width;
    }

    @Override
    public List<HeaderMetaData> getHeaderMetaData() {
        return headerMetaData;
    }

    @Override
    public IGridColumnRenderer<T> getColumnRenderer() {
        return columnRenderer;
    }

    @Override
    public void edit( final IGridCell<T> cell,
                      final GridBodyCellRenderContext context,
                      final Callback<IGridCellValue<T>> callback ) {
        //Do nothing by default
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
    public IGridColumn<?> getLink() {
        return link;
    }

    @Override
    public void setLink( final IGridColumn<?> link ) {
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
    public boolean isResizable() {
        return isResizable;
    }

    @Override
    public void setResizable( final boolean isResizable ) {
        this.isResizable = isResizable;
    }

    @Override
    public boolean isMovable() {
        return this.isMovable;
    }

    @Override
    public void setMovable( final boolean isMovable ) {
        this.isMovable = isMovable;
    }

    @Override
    public boolean isFloatable() {
        return isFloatable;
    }

    @Override
    public void setFloatable( final boolean isFloatable ) {
        this.isFloatable = isFloatable;
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
        if ( !( o instanceof BaseGridColumn ) ) {
            return false;
        }

        BaseGridColumn that = (BaseGridColumn) o;

        if ( Double.compare( that.width, width ) != 0 ) {
            return false;
        }
        if ( isResizable != that.isResizable ) {
            return false;
        }
        if ( isMovable != that.isMovable ) {
            return false;
        }
        if ( isVisible != that.isVisible ) {
            return false;
        }
        if ( index != that.index ) {
            return false;
        }
        if ( minimumWidth != null ? !minimumWidth.equals( that.minimumWidth ) : that.minimumWidth != null ) {
            return false;
        }
        if ( maximumWidth != null ? !maximumWidth.equals( that.maximumWidth ) : that.maximumWidth != null ) {
            return false;
        }
        if ( link != null ? !link.equals( that.link ) : that.link != null ) {
            return false;
        }
        return headerMetaData.equals( that.headerMetaData );

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits( width );
        result = (int) ( temp ^ ( temp >>> 32 ) );
        result = ~~result;
        result = 31 * result + ( isResizable ? 1 : 0 );
        result = ~~result;
        result = 31 * result + ( isMovable ? 1 : 0 );
        result = ~~result;
        result = 31 * result + ( isVisible ? 1 : 0 );
        result = ~~result;
        result = 31 * result + ( minimumWidth != null ? minimumWidth.hashCode() : 0 );
        result = ~~result;
        result = 31 * result + ( maximumWidth != null ? maximumWidth.hashCode() : 0 );
        result = ~~result;
        result = 31 * result + ( link != null ? link.hashCode() : 0 );
        result = ~~result;
        result = 31 * result + index;
        result = ~~result;
        result = 31 * result + headerMetaData.hashCode();
        result = ~~result;
        return result;
    }
}
