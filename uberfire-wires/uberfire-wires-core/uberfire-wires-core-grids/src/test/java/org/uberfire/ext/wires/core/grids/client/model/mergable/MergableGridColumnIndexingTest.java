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
package org.uberfire.ext.wires.core.grids.client.model.mergable;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class MergableGridColumnIndexingTest extends BaseMergableGridTest {

    @Test
    public void testAddInitialColumns() {
        final MergableGridData grid = new MergableGridData();
        final MergableGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                                   100 );
        final MergableGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                                   100 );
        grid.appendColumn( gc1 );
        grid.appendColumn( gc2 );

        final List<MergableGridColumn<?>> columns = grid.getColumns();

        assertEquals( 2,
                      columns.size() );
        assertEquals( 0,
                      gc1.getIndex() );
        assertEquals( 1,
                      gc2.getIndex() );
        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc2 );
    }

    @Test
    public void testAddColumn() {
        final MergableGridData grid = new MergableGridData();
        final MergableGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                                   100 );
        final MergableGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                                   100 );
        final MergableGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                                   100 );
        grid.appendColumn( gc1 );
        grid.appendColumn( gc2 );
        grid.insertColumn( 1,
                           gc3 );

        final List<MergableGridColumn<?>> columns = grid.getColumns();

        assertEquals( 3,
                      columns.size() );
        assertEquals( 0,
                      gc1.getIndex() );
        assertEquals( 1,
                      gc2.getIndex() );
        assertEquals( 2,
                      gc3.getIndex() );
        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc3 );
        assertEquals( columns.get( 2 ),
                      gc2 );
    }

    @Test
    public void testRemoveColumn() {
        final MergableGridData grid = new MergableGridData();
        final MergableGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                                   100 );
        final MergableGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                                   100 );
        final MergableGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                                   100 );
        grid.appendColumn( gc1 );
        grid.appendColumn( gc2 );
        grid.appendColumn( gc3 );

        grid.deleteColumn( gc2 );

        final List<MergableGridColumn<?>> columns = grid.getColumns();

        assertEquals( 2,
                      columns.size() );
        assertEquals( 0,
                      gc1.getIndex() );
        assertEquals( 1,
                      gc3.getIndex() );
        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc3 );
    }

    @Test
    public void testMoveColumnToLeft() {
        final MergableGridData grid = new MergableGridData();
        final MergableGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                                   100 );
        final MergableGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                                   100 );
        final MergableGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                                   100 );
        final MergableGridColumn<String> gc4 = new MockMergableGridColumn<String>( "col4",
                                                                                   100 );
        grid.appendColumn( gc1 );
        grid.appendColumn( gc2 );
        grid.appendColumn( gc3 );
        grid.appendColumn( gc4 );

        grid.moveColumnTo( 1,
                           gc4 );

        final List<MergableGridColumn<?>> columns = grid.getColumns();

        assertEquals( 4,
                      columns.size() );
        assertEquals( 0,
                      gc1.getIndex() );
        assertEquals( 1,
                      gc2.getIndex() );
        assertEquals( 2,
                      gc3.getIndex() );
        assertEquals( 3,
                      gc4.getIndex() );
        assertEquals( columns.get( 0 ),
                      gc1 );
        assertEquals( columns.get( 1 ),
                      gc4 );
        assertEquals( columns.get( 2 ),
                      gc2 );
        assertEquals( columns.get( 3 ),
                      gc3 );
    }

    @Test
    public void testMoveColumnToRight() {
        final MergableGridData grid = new MergableGridData();
        final MergableGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                                   100 );
        final MergableGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                                   100 );
        final MergableGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                                   100 );
        final MergableGridColumn<String> gc4 = new MockMergableGridColumn<String>( "col4",
                                                                                   100 );
        grid.appendColumn( gc1 );
        grid.appendColumn( gc2 );
        grid.appendColumn( gc3 );
        grid.appendColumn( gc4 );

        grid.moveColumnTo( 3,
                           gc1 );

        final List<MergableGridColumn<?>> columns = grid.getColumns();

        assertEquals( 4,
                      columns.size() );
        assertEquals( 0,
                      gc1.getIndex() );
        assertEquals( 1,
                      gc2.getIndex() );
        assertEquals( 2,
                      gc3.getIndex() );
        assertEquals( 3,
                      gc4.getIndex() );
        assertEquals( columns.get( 0 ),
                      gc2 );
        assertEquals( columns.get( 1 ),
                      gc3 );
        assertEquals( columns.get( 2 ),
                      gc4 );
        assertEquals( columns.get( 3 ),
                      gc1 );
    }

    @Test
    public void testRemoveRow() {
        final MergableGridData grid = new MergableGridData();
        final MergableGridColumn<String> gc1 = new MockMergableGridColumn<String>( "col1",
                                                                                   100 );
        final MergableGridColumn<String> gc2 = new MockMergableGridColumn<String>( "col2",
                                                                                   100 );
        final MergableGridColumn<String> gc3 = new MockMergableGridColumn<String>( "col3",
                                                                                   100 );
        grid.appendColumn( gc1 );
        grid.appendColumn( gc2 );
        grid.appendColumn( gc3 );

        final MergableGridRow gr1 = new MergableGridRow();
        final MergableGridRow gr2 = new MergableGridRow();
        final MergableGridRow gr3 = new MergableGridRow();

        grid.appendRow( gr1 );
        grid.appendRow( gr2 );
        grid.appendRow( gr3 );

        grid.deleteRow( 1 );

        assertEquals( 2,
                      grid.getRowCount() );
        assertEquals( gr1,
                      grid.getRow( 0 ) );
        assertEquals( gr3,
                      grid.getRow( 1 ) );
    }

}
