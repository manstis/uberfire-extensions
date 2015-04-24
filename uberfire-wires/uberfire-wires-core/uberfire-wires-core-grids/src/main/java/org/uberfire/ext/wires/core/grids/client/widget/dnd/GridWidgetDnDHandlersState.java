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
package org.uberfire.ext.wires.core.grids.client.widget.dnd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.dom.client.Style;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;
import org.uberfire.ext.wires.core.grids.client.widget.IBaseGridWidget;

/**
 * A container for the state of the MouseDown, MouseMove and MouseUp handlers during a drag operation.
 */
public class GridWidgetDnDHandlersState {

    private IBaseGridWidget activeGridWidget = null;
    private IGridColumn.HeaderMetaData activeHeaderMetaData = null;
    private List<IGridColumn<?>> activeGridColumns = new ArrayList<IGridColumn<?>>();
    private List<IGridRow> activeGridRows = new ArrayList<IGridRow>();

    private GridWidgetHandlersOperation operation = GridWidgetHandlersOperation.NONE;
    private Style.Cursor cursor = Style.Cursor.DEFAULT;

    private double eventInitialX = 0;
    private double eventInitialColumnWidth = 0;
    private GridWidgetDnDProxy eventColumnHighlight = new GridWidgetDnDProxy();

    /**
     * The different states of the drag operation.
     */
    public enum GridWidgetHandlersOperation {
        NONE,
        COLUMN_RESIZE_PENDING,
        COLUMN_RESIZE,
        COLUMN_MOVE_PENDING,
        COLUMN_MOVE,
        ROW_MOVE_PENDING,
        ROW_MOVE
    }

    /**
     * The active GridWidget.
     * @return
     */
    public IBaseGridWidget getActiveGridWidget() {
        return activeGridWidget;
    }

    /**
     * Set the active GridWidget.
     * @param activeGridWidget
     */
    public void setActiveGridWidget( final IBaseGridWidget activeGridWidget ) {
        this.activeGridWidget = activeGridWidget;
    }

    /**
     * Clear the active GridWidget.
     */
    public void clearActiveGridWidget() {
        this.activeGridWidget = null;
    }

    /**
     * The active HeaderMetaData.
     * @return
     */
    public IGridColumn.HeaderMetaData getActiveHeaderMetaData() {
        return activeHeaderMetaData;
    }

    /**
     * Set the active HeaderMetaData.
     * @param activeHeaderMetaData
     */
    public void setActiveHeaderMetaData( final IGridColumn.HeaderMetaData activeHeaderMetaData ) {
        this.activeHeaderMetaData = activeHeaderMetaData;
    }

    /**
     * Clear the active HeaderMetaData.
     */
    public void clearActiveHeaderMetaData() {
        this.activeHeaderMetaData = null;
    }

    /**
     * The active columns being affected by the current the operation.
     * @return
     */
    public List<IGridColumn<?>> getActiveGridColumns() {
        return Collections.unmodifiableList( activeGridColumns );
    }

    /**
     * Set the active columns to be affected by the current the operation.
     */
    public void setActiveGridColumns( final List<IGridColumn<?>> activeGridColumns ) {
        this.activeGridColumns.clear();
        this.activeGridColumns.addAll( activeGridColumns );
    }

    /**
     * The active rows being affected by the current the operation.
     * @return
     */
    public List<IGridRow> getActiveGridRows() {
        return Collections.unmodifiableList( activeGridRows );
    }

    /**
     * Set the active rows to be affected by the current the operation.
     */
    public void setActiveGridRows( final List<IGridRow> activeGridRows ) {
        this.activeGridRows.clear();
        this.activeGridRows.addAll( activeGridRows );
    }

    /**
     * Clear the active columns affected by the current the operation.
     */
    public void clearActiveGridColumns() {
        this.activeGridColumns.clear();
    }

    /**
     * Clear the active rows affected by the current the operation.
     */
    public void clearActiveGridRows() {
        this.activeGridRows.clear();
    }

    /**
     * The current drag operation in progress.
     * @return
     */
    public GridWidgetHandlersOperation getOperation() {
        return operation;
    }

    /**
     * Set the current drag operation in progress.
     * @param operation
     */
    public void setOperation( final GridWidgetHandlersOperation operation ) {
        this.operation = operation;
    }

    /**
     * The Cursor type to be shown for the current operation. This primarily used in conjunction with DOMElement based cells.
     * When the pointer moves over a DOM element the browser determines the Cursor to show based on the DOM element's CSS. This
     * however can be different to the pointer required during, for example, a column resize operation. In such cases the
     * browser changes the pointer to that defined by CSS replacing that set by the MouseMove handler.
     * @return
     */
    public Style.Cursor getCursor() {
        return cursor;
    }

    /**
     * Set the Cursor type to be shown for the current operation.
     * @param cursor
     */
    public void setCursor( Style.Cursor cursor ) {
        this.cursor = cursor;
    }

    /**
     * Get the grid-relative x-coordinate of the Mouse Event.
     * @return
     */
    public double getEventInitialX() {
        return eventInitialX;
    }

    /**
     * Set the grid-relative x-coordinate of the Mouse Event.
     * @param eventInitialX
     */
    public void setEventInitialX( final double eventInitialX ) {
        this.eventInitialX = eventInitialX;
    }

    /**
     * The width of a column being re-sized at the commencement of the resize operation.
     * During a re-size operation the new width is determined by calculating the delta of
     * the MouseMoveEvent coordinates. The initial width is therefore required to apply
     * the same delta.
     * @return
     */
    public double getEventInitialColumnWidth() {
        return eventInitialColumnWidth;
    }

    /**
     * Set the initial width of a column to be resized.
     * @param eventInitialColumnWidth
     */
    public void setEventInitialColumnWidth( final double eventInitialColumnWidth ) {
        this.eventInitialColumnWidth = eventInitialColumnWidth;
    }

    /**
     * Get the Group representing the column during a drag operation of the column being moved
     * @return
     */
    public GridWidgetDnDProxy getEventColumnHighlight() {
        return eventColumnHighlight;
    }

}
