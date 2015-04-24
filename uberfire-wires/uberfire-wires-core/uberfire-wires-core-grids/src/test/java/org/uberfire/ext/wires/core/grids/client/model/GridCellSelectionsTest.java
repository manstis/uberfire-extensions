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
package org.uberfire.ext.wires.core.grids.client.model;

import java.util.ArrayList;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.uberfire.ext.wires.core.grids.client.model.BaseGridTest.Expected.*;

public class GridCellSelectionsTest extends BaseGridTest {

    @Test
    public void testSelectCell() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ) },
                                   { build( false, 1, "(0, 2)" ), build( false, 1, "(1, 2)" ) }
                           } );

        data.selectCell( 0,
                         0 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellMergedData() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        data.setCell( 1,
                      0,
                      new BaseGridCellValue<String>( "(0, 0)" ) );

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { build( true, 2, "(0, 0)" ), build( false, 1, "(1, 0)" ) },
                                   { build( true, 0, "(0, 0)" ), build( false, 1, "(1, 1)" ) },
                                   { build( false, 1, "(0, 2)" ), build( false, 1, "(1, 2)" ) }
                           } );

        data.selectCell( 0,
                         0 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellGroupedDataSelectGroupedCell() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        data.setCell( 1,
                      0,
                      new BaseGridCellValue<String>( "(0, 0)" ) );
        data.collapseCell( 0,
                           0 );

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, true, false },
                           new Expected[][]{
                                   { build( true, 2, "(0, 0)" ), build( false, 1, "(1, 0)" ) },
                                   { build( true, 0, "(0, 0)" ), build( false, 1, "(1, 1)" ) },
                                   { build( false, 1, "(0, 2)" ), build( false, 1, "(1, 2)" ) }
                           } );

        data.selectCell( 0,
                         0 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellGroupedDataSelectMergedCell() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        data.setCell( 1,
                      0,
                      new BaseGridCellValue<String>( "(0, 0)" ) );
        data.collapseCell( 0,
                           0 );

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, true, false },
                           new Expected[][]{
                                   { build( true, 2, "(0, 0)" ), build( false, 1, "(1, 0)" ) },
                                   { build( true, 0, "(0, 0)" ), build( false, 1, "(1, 1)" ) },
                                   { build( false, 1, "(0, 2)" ), build( false, 1, "(1, 2)" ) }
                           } );

        data.selectCell( 0,
                         1 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellMultipleTimes() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ) },
                                   { build( false, 1, "(0, 2)" ), build( false, 1, "(1, 2)" ) }
                           } );

        //Select once
        data.selectCell( 0,
                         0 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );

        //Select again
        data.selectCell( 0,
                         0 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testClearSelections() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ) },
                                   { build( false, 1, "(0, 2)" ), build( false, 1, "(1, 2)" ) }
                           } );

        data.selectCell( 0,
                         0 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );

        data.clearSelections();

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellMoveColumn() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false },
                           new boolean[]{ false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ) }
                           } );

        //Select cell
        data.selectCell( 0,
                         0 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );

        assertEquals( "(0, 0)",
                      data.getCell( 0, 0 ).getValue().getValue() );
        assertEquals( "(1, 0)",
                      data.getCell( 0, 1 ).getValue().getValue() );
        assertEquals( "(0, 1)",
                      data.getCell( 1, 0 ).getValue().getValue() );
        assertEquals( "(1, 1)",
                      data.getCell( 1, 1 ).getValue().getValue() );

        //Move column
        data.moveColumnTo( 1,
                           gc1 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );

        assertEquals( "(1, 0)",
                      data.getCell( 0, 0 ).getValue().getValue() );
        assertEquals( "(0, 0)",
                      data.getCell( 0, 1 ).getValue().getValue() );
        assertEquals( "(1, 1)",
                      data.getCell( 1, 0 ).getValue().getValue() );
        assertEquals( "(0, 1)",
                      data.getCell( 1, 1 ).getValue().getValue() );
    }

    @Test
    public void testMoveColumnSelectCell() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false },
                           new boolean[]{ false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ) }
                           } );

        //Move column
        data.moveColumnTo( 1,
                           gc1 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );

        assertEquals( "(1, 0)",
                      data.getCell( 0, 0 ).getValue().getValue() );
        assertEquals( "(0, 0)",
                      data.getCell( 0, 1 ).getValue().getValue() );
        assertEquals( "(1, 1)",
                      data.getCell( 1, 0 ).getValue().getValue() );
        assertEquals( "(0, 1)",
                      data.getCell( 1, 1 ).getValue().getValue() );

        //Select cell
        data.selectCell( 0,
                         0 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );

        assertEquals( "(1, 0)",
                      data.getCell( 0, 0 ).getValue().getValue() );
        assertEquals( "(0, 0)",
                      data.getCell( 0, 1 ).getValue().getValue() );
        assertEquals( "(1, 1)",
                      data.getCell( 1, 0 ).getValue().getValue() );
        assertEquals( "(0, 1)",
                      data.getCell( 1, 1 ).getValue().getValue() );
    }

    @Test
    public void testSelectCellsMoveColumn() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        final IGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );
        data.appendColumn( gc3 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false },
                           new boolean[]{ false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ), build( false, 1, "(2, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ), build( false, 1, "(2, 1)" ) }
                           } );

        //Select cell
        data.selectCells( 0,
                          0,
                          2,
                          1 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );

        assertEquals( "(0, 0)",
                      data.getCell( 0, 0 ).getValue().getValue() );
        assertEquals( "(1, 0)",
                      data.getCell( 0, 1 ).getValue().getValue() );
        assertEquals( "(2, 0)",
                      data.getCell( 0, 2 ).getValue().getValue() );
        assertEquals( "(0, 1)",
                      data.getCell( 1, 0 ).getValue().getValue() );
        assertEquals( "(1, 1)",
                      data.getCell( 1, 1 ).getValue().getValue() );
        assertEquals( "(2, 1)",
                      data.getCell( 1, 2 ).getValue().getValue() );

        //Move column
        data.moveColumnTo( 1,
                           gc3 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );

        assertEquals( "(0, 0)",
                      data.getCell( 0, 0 ).getValue().getValue() );
        assertEquals( "(2, 0)",
                      data.getCell( 0, 1 ).getValue().getValue() );
        assertEquals( "(1, 0)",
                      data.getCell( 0, 2 ).getValue().getValue() );
        assertEquals( "(0, 1)",
                      data.getCell( 1, 0 ).getValue().getValue() );
        assertEquals( "(2, 1)",
                      data.getCell( 1, 1 ).getValue().getValue() );
        assertEquals( "(1, 1)",
                      data.getCell( 1, 2 ).getValue().getValue() );
    }

    @Test
    public void testMoveColumnSelectCells() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        final IGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );
        data.appendColumn( gc3 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false },
                           new boolean[]{ false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ), build( false, 1, "(2, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ), build( false, 1, "(2, 1)" ) }
                           } );

        //Move column
        data.moveColumnTo( 1,
                           gc3 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );

        assertEquals( "(0, 0)",
                      data.getCell( 0, 0 ).getValue().getValue() );
        assertEquals( "(2, 0)",
                      data.getCell( 0, 1 ).getValue().getValue() );
        assertEquals( "(1, 0)",
                      data.getCell( 0, 2 ).getValue().getValue() );
        assertEquals( "(0, 1)",
                      data.getCell( 1, 0 ).getValue().getValue() );
        assertEquals( "(2, 1)",
                      data.getCell( 1, 1 ).getValue().getValue() );
        assertEquals( "(1, 1)",
                      data.getCell( 1, 2 ).getValue().getValue() );

        //Select cell
        data.selectCells( 0,
                          0,
                          2,
                          1 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );

        assertEquals( "(0, 0)",
                      data.getCell( 0, 0 ).getValue().getValue() );
        assertEquals( "(2, 0)",
                      data.getCell( 0, 1 ).getValue().getValue() );
        assertEquals( "(1, 0)",
                      data.getCell( 0, 2 ).getValue().getValue() );
        assertEquals( "(0, 1)",
                      data.getCell( 1, 0 ).getValue().getValue() );
        assertEquals( "(2, 1)",
                      data.getCell( 1, 1 ).getValue().getValue() );
        assertEquals( "(1, 1)",
                      data.getCell( 1, 2 ).getValue().getValue() );
    }

    @Test
    public void testSelectCellMergedDataInsertRow() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            data.setCell( rowIndex,
                          0,
                          new BaseGridCellValue<String>( "(0, 0)" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ true, true },
                           new boolean[]{ false, false },
                           new Expected[][]{
                                   { build( true, 2, "(0, 0)" ) },
                                   { build( true, 0, "(0, 0)" ) }
                           } );

        data.selectCell( 0,
                         0 );
        assertEquals( 2,
                      data.getSelectedCells().size() );

        data.insertRow( 1,
                        new BaseGridRow() );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellUnmergedDataInsertRow() {
        final IGridData data = new BaseGridData( false );
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            data.setCell( rowIndex,
                          0,
                          new BaseGridCellValue<String>( "(0, 0)" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false },
                           new boolean[]{ false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ) },
                                   { build( false, 1, "(0, 0)" ) }
                           } );

        data.selectCell( 0,
                         0 );
        data.selectCell( 1,
                         0 );
        assertEquals( 2,
                      data.getSelectedCells().size() );

        data.insertRow( 1,
                        new BaseGridRow() );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellMergedDataDeleteRow() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            data.setCell( rowIndex,
                          0,
                          new BaseGridCellValue<String>( "(0, 0)" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ true, true },
                           new boolean[]{ false, false },
                           new Expected[][]{
                                   { build( true, 2, "(0, 0)" ) },
                                   { build( true, 0, "(0, 0)" ) }
                           } );

        data.selectCell( 0,
                         0 );
        assertEquals( 2,
                      data.getSelectedCells().size() );

        data.deleteRow( 1 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellMergedDataDeleteRowWithAdditionalSelections() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            data.setCell( rowIndex,
                          0,
                          new BaseGridCellValue<String>( rowIndex < 2 ? "a" : "b" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { build( true, 2, "a" ) },
                                   { build( true, 0, "a" ) },
                                   { build( false, 1, "b" ) }
                           } );

        data.selectCell( 0,
                         0 );
        data.selectCell( 2,
                         0 );
        assertEquals( 3,
                      data.getSelectedCells().size() );

        data.deleteRow( 1 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellGroupedDataDeleteRow() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            data.setCell( rowIndex,
                          0,
                          new BaseGridCellValue<String>( "(0, 0)" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ true, true },
                           new boolean[]{ false, false },
                           new Expected[][]{
                                   { build( true, 2, "(0, 0)" ) },
                                   { build( true, 0, "(0, 0)" ) }
                           } );

        data.selectCell( 0,
                         0 );
        data.collapseCell( 0,
                           0 );
        assertEquals( 2,
                      data.getSelectedCells().size() );

        data.deleteRow( 0 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellGroupedDataDeleteRowWithAdditionalSelections() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            data.setCell( rowIndex,
                          0,
                          new BaseGridCellValue<String>( rowIndex < 2 ? "a" : "b" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ true, true, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { build( true, 2, "a" ) },
                                   { build( true, 0, "a" ) },
                                   { build( false, 1, "b" ) }
                           } );

        data.selectCell( 0,
                         0 );
        data.selectCell( 2,
                         0 );
        data.collapseCell( 0,
                           0 );
        assertEquals( 3,
                      data.getSelectedCells().size() );

        data.deleteRow( 0 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellUnmergedDataDeleteRow() {
        final IGridData data = new BaseGridData( false );
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            data.setCell( rowIndex,
                          0,
                          new BaseGridCellValue<String>( "(0, 0)" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false },
                           new boolean[]{ false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ) },
                                   { build( false, 1, "(0, 0)" ) }
                           } );

        data.selectCell( 0,
                         0 );
        data.selectCell( 1,
                         0 );
        assertEquals( 2,
                      data.getSelectedCells().size() );

        data.deleteRow( 1 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellInsertColumn() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            data.setCell( rowIndex,
                          0,
                          new BaseGridCellValue<String>( "(0, 0)" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ true, true },
                           new boolean[]{ false, false },
                           new Expected[][]{
                                   { build( true, 2, "(0, 0)" ) },
                                   { build( true, 0, "(0, 0)" ) }
                           } );

        data.selectCell( 0,
                         0 );
        assertEquals( 2,
                      data.getSelectedCells().size() );

        data.insertColumn( 0,
                           new MockMergableGridColumn<String>( "col1",
                                                               100 ) );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellDeleteColumn() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );

        for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
            data.setCell( 0,
                          columnIndex,
                          new BaseGridCellValue<String>( "(0, " + columnIndex + ")" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ false },
                           new boolean[]{ false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(0, 1)" ) }
                           } );

        data.selectCell( 0,
                         0 );
        assertEquals( 1,
                      data.getSelectedCells().size() );

        data.deleteColumn( gc1 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellDeleteColumnWithAdditionalSelections() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );

        for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
            data.setCell( 0,
                          columnIndex,
                          new BaseGridCellValue<String>( "(0, " + columnIndex + ")" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ false },
                           new boolean[]{ false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(0, 1)" ) }
                           } );

        data.selectCell( 0,
                         0 );
        data.selectCell( 0,
                         1 );
        assertEquals( 2,
                      data.getSelectedCells().size() );

        data.deleteColumn( gc1 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertEquals( 1,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellMoveColumnDeleteColumn() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        data.appendRow( new BaseGridRow() );

        for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
            data.setCell( 0,
                          columnIndex,
                          new BaseGridCellValue<String>( "(0, " + columnIndex + ")" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ false },
                           new boolean[]{ false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(0, 1)" ) }
                           } );

        data.selectCell( 0,
                         0 );
        assertEquals( 1,
                      data.getSelectedCells().size() );
        data.moveColumnTo( 1,
                           gc1 );

        data.deleteColumn( gc1 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testSelectCellMoveColumnToSplitSelectionsDeleteColumn() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        final IGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                            100 );
        final IGridColumn<String> gc4 = new MockMergableGridColumn<String>( "col4",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );
        data.appendColumn( gc3 );
        data.appendColumn( gc4 );

        data.appendRow( new BaseGridRow() );

        for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
            data.setCell( 0,
                          columnIndex,
                          new BaseGridCellValue<String>( "(0, " + columnIndex + ")" ) );
        }

        assertGridIndexes( data,
                           new boolean[]{ false },
                           new boolean[]{ false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(0, 1)" ), build( false, 1, "(0, 2)" ), build( false, 1, "(0, 3)" ) }
                           } );

        data.selectCell( 0,
                         0 );
        data.selectCell( 0,
                         1 );
        data.selectCell( 0,
                         2 );
        assertEquals( 3,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 3 ) ) );

        data.moveColumnTo( 1,
                           gc4 );

        data.deleteColumn( gc2 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertEquals( 2,
                      data.getSelectedCells().size() );
    }

    @Test
    public void testUnmergedMoveRowUpWithSelections() {
        final IGridData data = new BaseGridData( false );
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 1 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[ ], 0[ ]
        // row1 = b[X], 1[X]
        // row2 = a[ ], 2[ ]

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "2" ) }
                           } );

        data.selectCell( 1,
                         0 );
        data.selectCell( 1,
                         1 );
        assertEquals( 2,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );

        //Move row
        data.moveRowTo( 0,
                        row1 );

        // row0 = b[X], 1[X]
        // row1 = a[ ], 0[ ]
        // row2 = a[ ], 2[ ]

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "2" ) }
                           } );

        assertEquals( 2,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
    }

    @Test
    public void testUnmergedMoveRowDownWithSelections() {
        final IGridData data = new BaseGridData( false );
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 0 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = b[X], 0[X]
        // row1 = a[ ], 1[ ]
        // row2 = a[ ], 2[ ]

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "2" ) }
                           } );

        data.selectCell( 0,
                         0 );
        data.selectCell( 0,
                         1 );
        assertEquals( 2,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );

        //Move row
        data.moveRowTo( 1,
                        row0 );

        // row0 = a[ ], 1[ ]
        // row1 = b[X], 0[X]
        // row2 = a[ ], 2[ ]

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "2" ) }
                           } );

        assertEquals( 2,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
    }

    @Test
    public void testMergedMoveRowUpWithSelections1() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 4 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[ ], 0[X]
        // row1 = a[ ], 1[ ]
        // row2 = a[ ], 2[ ]
        // row3 = a[ ], 3[ ]
        // row4 = b[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) }
                           } );

        data.selectCell( 0,
                         1 );
        data.selectCell( 4,
                         0 );
        assertEquals( 2,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowTo( 3,
                        row4 );

        // row0 = a[ ], 0[X]
        // row1 = a[ ], 1[ ]
        // row2 = a[ ], 2[ ]
        // row3 = b[X], 4[ ]
        // row4 = a[ ], 3[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, false, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 3, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "3" ) }
                           } );

        assertEquals( 2,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
    }

    @Test
    public void testMergedMoveRowUpWithSelections2() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 4 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = a[X], 3[ ]
        // row4 = b[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) }
                           } );

        data.selectCell( 0,
                         0 );
        data.selectCell( 4,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowTo( 3,
                        row4 );

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = b[X], 4[ ]
        // row4 = a[X], 3[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, false, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 3, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "3" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );
    }

    @Test
    public void testMergedMoveRowUpWithSelections3() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? "a" : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = a[X], 3[ ]
        // row4 = a[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, true },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 5, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) }
                           } );

        data.selectCell( 0,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowTo( 3,
                        row4 );

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = a[X], 4[ ]
        // row4 = a[X], 3[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, true },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 5, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );
    }

    @Test
    public void testMergedMoveRowUpWithSelections4() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? "a" : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = a[X], 3[ ]
        // row4 = a[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, true },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 5, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) }
                           } );

        data.selectCell( 0,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowTo( 2,
                        row3 );

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = a[X], 4[ ]
        // row4 = a[X], 3[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, true },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 5, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );
    }

    @Test
    public void testMergedMoveRowDownWithSelections1() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 4 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[ ], 0[X]
        // row1 = a[ ], 1[ ]
        // row2 = a[ ], 2[ ]
        // row3 = a[ ], 3[ ]
        // row4 = b[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) }
                           } );

        data.selectCell( 0,
                         1 );
        data.selectCell( 4,
                         0 );
        assertEquals( 2,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowTo( 4,
                        row3 );

        // row0 = a[ ], 0[X]
        // row1 = a[ ], 1[ ]
        // row2 = a[ ], 2[ ]
        // row3 = b[X], 4[ ]
        // row4 = a[ ], 3[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, false, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 3, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "3" ) }
                           } );

        assertEquals( 2,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
    }

    @Test
    public void testMergedMoveRowDownWithSelections2() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 4 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = a[X], 3[ ]
        // row4 = b[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) }
                           } );

        data.selectCell( 0,
                         0 );
        data.selectCell( 4,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowTo( 4,
                        row3 );

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = b[X], 4[ ]
        // row4 = a[X], 3[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, false, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 3, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( false, 1, "a" ), Expected.build( false, 1, "3" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );
    }

    @Test
    public void testMergedMoveRowDownWithSelections3() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? "a" : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = a[X], 3[ ]
        // row4 = a[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, true },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 5, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) }
                           } );

        data.selectCell( 0,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowTo( 4,
                        row3 );

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = a[X], 4[ ]
        // row4 = a[X], 3[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, true },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 5, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );
    }

    @Test
    public void testMergedMoveRowDownWithSelections4() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? "a" : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = a[X], 3[ ]
        // row4 = a[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, true },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 5, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) }
                           } );

        data.selectCell( 0,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowTo( 1,
                        row0 );

        // row0 = a[X], 0[ ]
        // row1 = a[X], 1[ ]
        // row2 = a[X], 2[ ]
        // row3 = a[X], 4[ ]
        // row4 = a[X], 3[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, true },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 5, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );
    }

    @Test
    public void testGroupedMoveRowUpWithSelections1() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 4 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[ ], 0[X] } Collapse (Lead)
        // row1 = a[ ], 1[X] } Collapse (Child)
        // row2 = a[ ], 2[X] } Collapse (Child)
        // row3 = a[ ], 3[X] } Collapse (Child)
        // row4 = b[X], 4[ ]

        //Collapse cell
        data.collapseCell( 0,
                           0 );

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, false },
                           new boolean[]{ false, true, true, true, false },
                           new Expected[][]{
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) }
                           } );

        data.selectCell( 0,
                         1 );
        data.selectCell( 4,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowsTo( 0,
                         new ArrayList<IGridRow>() {{
                             add( row4 );
                         }} );

        // row0 = b[X], 4[ ]
        // row1 = a[ ], 0[X] } Collapse (Lead)
        // row2 = a[ ], 1[X] } Collapse (Child)
        // row3 = a[ ], 2[X] } Collapse (Child)
        // row4 = a[ ], 3[X] } Collapse (Child)

        assertGridIndexes( data,
                           new boolean[]{ false, true, true, true, true },
                           new boolean[]{ false, false, true, true, true },
                           new Expected[][]{
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 1 ) ) );
    }

    @Test
    public void testGroupedMoveRowUpWithSelections2() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 4 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[X], 0[ ] } Collapse (Lead)
        // row1 = a[X], 1[ ] } Collapse (Child)
        // row2 = a[X], 2[ ] } Collapse (Child)
        // row3 = a[X], 3[ ] } Collapse (Child)
        // row4 = b[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) }
                           } );

        //Collapse cell
        data.collapseCell( 0,
                           0 );

        data.selectCell( 0,
                         0 );
        data.selectCell( 4,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowsTo( 0,
                         new ArrayList<IGridRow>() {{
                             add( row4 );
                         }} );

        // row0 = b[X], 4[ ]
        // row1 = a[X], 0[ ] } Collapse (Lead)
        // row2 = a[X], 1[ ] } Collapse (Child)
        // row3 = a[X], 2[ ] } Collapse (Child)
        // row4 = a[X], 3[ ] } Collapse (Child)

        assertGridIndexes( data,
                           new boolean[]{ false, true, true, true, true },
                           new boolean[]{ false, false, true, true, true },
                           new Expected[][]{
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );
    }

    @Test
    public void testGroupedMoveRowUpWithSelections3() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 0 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = b[X], 0[ ]
        // row1 = a[X], 1[ ] } Collapse (Lead)
        // row2 = a[X], 2[ ] } Collapse (Child)
        // row3 = a[X], 3[ ] } Collapse (Child)
        // row4 = a[X], 4[ ] } Collapse (Child)

        assertGridIndexes( data,
                           new boolean[]{ false, true, true, true, true },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) }
                           } );

        //Collapse cell
        data.collapseCell( 1,
                           0 );

        data.selectCell( 0,
                         0 );
        data.selectCell( 1,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move row
        data.moveRowsTo( 0,
                         new ArrayList<IGridRow>() {{
                             add( row1 );
                             add( row2 );
                             add( row3 );
                             add( row4 );
                         }} );

        // row0 = a[X], 1[ ] } Collapse (Lead)
        // row1 = a[X], 2[ ] } Collapse (Child)
        // row2 = a[X], 3[ ] } Collapse (Child)
        // row3 = a[X], 4[ ] } Collapse (Child)
        // row4 = b[X], 0[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, false },
                           new boolean[]{ false, true, true, true, false },
                           new Expected[][]{
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "0" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );
    }

    @Test
    public void testGroupedMoveRowDownWithSelections1() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 4 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[ ], 0[X] } Collapse (Lead)
        // row1 = a[ ], 1[X] } Collapse (Child)
        // row2 = a[ ], 2[X] } Collapse (Child)
        // row3 = a[ ], 3[X] } Collapse (Child)
        // row4 = b[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) }
                           } );

        //Collapse cell
        data.collapseCell( 0,
                           0 );

        data.selectCell( 0,
                         1 );
        data.selectCell( 4,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move rows
        data.moveRowsTo( 4,
                         new ArrayList<IGridRow>() {{
                             add( row0 );
                             add( row1 );
                             add( row2 );
                             add( row3 );
                         }} );

        // row0 = b[X], 4[ ]
        // row1 = a[ ], 0[X] } Collapse (Lead)
        // row2 = a[ ], 1[X] } Collapse (Child)
        // row3 = a[ ], 2[X] } Collapse (Child)
        // row4 = a[ ], 3[X] } Collapse (Child)

        assertGridIndexes( data,
                           new boolean[]{ false, true, true, true, true },
                           new boolean[]{ false, false, true, true, true },
                           new Expected[][]{
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 1 ) ) );
    }

    @Test
    public void testGroupedMoveRowDownWithSelections2() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 4 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = a[X], 0[ ] } Collapse (Lead)
        // row1 = a[X], 1[ ] } Collapse (Child)
        // row2 = a[X], 2[ ] } Collapse (Child)
        // row3 = a[X], 3[ ] } Collapse (Child)
        // row4 = b[X], 4[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, false },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) }
                           } );

        //Collapse cell
        data.collapseCell( 0,
                           0 );

        data.selectCell( 0,
                         0 );
        data.selectCell( 4,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move rows
        data.moveRowsTo( 4,
                         new ArrayList<IGridRow>() {{
                             add( row0 );
                             add( row1 );
                             add( row2 );
                             add( row3 );
                         }} );

        // row0 = b[X], 4[ ]
        // row1 = a[X], 0[ ] } Collapse (Lead)
        // row2 = a[X], 1[ ] } Collapse (Child)
        // row3 = a[X], 2[ ] } Collapse (Child)
        // row4 = a[X], 3[ ] } Collapse (Child)

        assertGridIndexes( data,
                           new boolean[]{ false, true, true, true, true },
                           new boolean[]{ false, false, true, true, true },
                           new Expected[][]{
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );
    }

    @Test
    public void testGroupedMoveRowDownWithSelections3() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );

        final IGridRow row0 = new BaseGridRow();
        final IGridRow row1 = new BaseGridRow();
        final IGridRow row2 = new BaseGridRow();
        final IGridRow row3 = new BaseGridRow();
        final IGridRow row4 = new BaseGridRow();
        data.appendRow( row0 );
        data.appendRow( row1 );
        data.appendRow( row2 );
        data.appendRow( row3 );
        data.appendRow( row4 );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final String value = columnIndex == 0 ? ( rowIndex == 0 ? "b" : "a" ) : Integer.toString( rowIndex );
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( value ) );
            }
        }

        // row0 = b[X], 0[ ]
        // row1 = a[X], 1[ ] } Collapse (Lead)
        // row2 = a[X], 2[ ] } Collapse (Child)
        // row3 = a[X], 3[ ] } Collapse (Child)
        // row4 = a[X], 4[ ] } Collapse (Child)

        assertGridIndexes( data,
                           new boolean[]{ false, true, true, true, true },
                           new boolean[]{ false, false, false, false, false },
                           new Expected[][]{
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "0" ) },
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) }
                           } );

        //Collapse cell
        data.collapseCell( 1,
                           0 );

        data.selectCell( 0,
                         0 );
        data.selectCell( 1,
                         0 );
        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );

        //Move rows
        data.moveRowsTo( 4,
                         new ArrayList<IGridRow>() {{
                             add( row0 );
                         }} );

        // row0 = a[X], 1[ ] } Collapse (Lead)
        // row1 = a[X], 2[ ] } Collapse (Child)
        // row2 = a[X], 3[ ] } Collapse (Child)
        // row3 = a[X], 4[ ] } Collapse (Child)
        // row4 = b[X], 0[ ]

        assertGridIndexes( data,
                           new boolean[]{ true, true, true, true, false },
                           new boolean[]{ false, true, true, true, false },
                           new Expected[][]{
                                   { Expected.build( true, 4, "a" ), Expected.build( false, 1, "1" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "2" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "3" ) },
                                   { Expected.build( true, 0, "a" ), Expected.build( false, 1, "4" ) },
                                   { Expected.build( false, 1, "b" ), Expected.build( false, 1, "0" ) }
                           } );

        assertEquals( 5,
                      data.getSelectedCells().size() );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 3, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 4, 0 ) ) );
    }

    @Test
    public void testSelectCellSelectedRangeChangeTopLeft() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        final IGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );
        data.appendColumn( gc3 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ), build( false, 1, "(2, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ), build( false, 1, "(2, 1)" ) },
                                   { build( false, 1, "(0, 2)" ), build( false, 1, "(1, 2)" ), build( false, 1, "(2, 2)" ) }
                           } );

        data.selectCell( 1,
                         1 );

        assertEquals( 1,
                      data.getSelectedCells().size() );
        assertEquals( data.getSelectedCellsOrigin(),
                      new IGridData.SelectedCell( 1, 1 ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );

        data.selectCells( 0,
                          0,
                          2,
                          2 );

        assertEquals( 4,
                      data.getSelectedCells().size() );
        assertEquals( data.getSelectedCellsOrigin(),
                      new IGridData.SelectedCell( 1, 1 ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
    }

    @Test
    public void testSelectCellSelectedRangeChangeTopRight() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        final IGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );
        data.appendColumn( gc3 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ), build( false, 1, "(2, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ), build( false, 1, "(2, 1)" ) },
                                   { build( false, 1, "(0, 2)" ), build( false, 1, "(1, 2)" ), build( false, 1, "(2, 2)" ) }
                           } );

        data.selectCell( 1,
                         1 );

        assertEquals( 1,
                      data.getSelectedCells().size() );
        assertEquals( data.getSelectedCellsOrigin(),
                      new IGridData.SelectedCell( 1, 1 ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );

        data.selectCells( 0,
                          1,
                          2,
                          2 );

        assertEquals( 4,
                      data.getSelectedCells().size() );
        assertEquals( data.getSelectedCellsOrigin(),
                      new IGridData.SelectedCell( 1, 1 ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
    }

    @Test
    public void testSelectCellSelectedRangeChangeBottomLeft() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        final IGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );
        data.appendColumn( gc3 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ), build( false, 1, "(2, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ), build( false, 1, "(2, 1)" ) },
                                   { build( false, 1, "(0, 2)" ), build( false, 1, "(1, 2)" ), build( false, 1, "(2, 2)" ) }
                           } );

        data.selectCell( 1,
                         1 );

        assertEquals( 1,
                      data.getSelectedCells().size() );
        assertEquals( data.getSelectedCellsOrigin(),
                      new IGridData.SelectedCell( 1, 1 ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );

        data.selectCells( 1,
                          0,
                          2,
                          2 );

        assertEquals( 4,
                      data.getSelectedCells().size() );
        assertEquals( data.getSelectedCellsOrigin(),
                      new IGridData.SelectedCell( 1, 1 ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
    }

    @Test
    public void testSelectCellSelectedRangeChangeBottomRight() {
        final IGridData data = new BaseGridData();
        final IGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                            100 );
        final IGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                            100 );
        final IGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                            100 );
        data.appendColumn( gc1 );
        data.appendColumn( gc2 );
        data.appendColumn( gc3 );

        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );
        data.appendRow( new BaseGridRow() );

        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                data.setCell( rowIndex,
                              columnIndex,
                              new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
            }
        }

        assertGridIndexes( data,
                           new boolean[]{ false, false, false },
                           new boolean[]{ false, false, false },
                           new Expected[][]{
                                   { build( false, 1, "(0, 0)" ), build( false, 1, "(1, 0)" ), build( false, 1, "(2, 0)" ) },
                                   { build( false, 1, "(0, 1)" ), build( false, 1, "(1, 1)" ), build( false, 1, "(2, 1)" ) },
                                   { build( false, 1, "(0, 2)" ), build( false, 1, "(1, 2)" ), build( false, 1, "(2, 2)" ) }
                           } );

        data.selectCell( 1,
                         1 );

        assertEquals( 1,
                      data.getSelectedCells().size() );
        assertEquals( data.getSelectedCellsOrigin(),
                      new IGridData.SelectedCell( 1, 1 ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );

        data.selectCells( 1,
                          1,
                          2,
                          2 );

        assertEquals( 4,
                      data.getSelectedCells().size() );
        assertEquals( data.getSelectedCellsOrigin(),
                      new IGridData.SelectedCell( 1, 1 ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
    }

}
