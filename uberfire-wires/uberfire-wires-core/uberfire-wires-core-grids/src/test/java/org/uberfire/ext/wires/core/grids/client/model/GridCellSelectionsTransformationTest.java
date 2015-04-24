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

import org.junit.Test;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.DefaultSelectionsTransformer;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.FloatingSelectionsTransformer;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.ISelectionsTransformer;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.SelectedRange;

import static org.junit.Assert.*;

public class GridCellSelectionsTransformationTest extends BaseGridTest {

    @Test
    public void testTransformationCross1() {
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

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );

        final ISelectionsTransformer transformer = new DefaultSelectionsTransformer( data,
                                                                                     data.getColumns() );
        final List<SelectedRange> selectedRanges1 = transformer.transformToSelectedRanges();
        assertEquals( 0,
                      selectedRanges1.size() );

        // - - x
        // x x x
        // - - x
        data.selectCell( 1,
                         0 );
        data.selectCell( 1,
                         1 );
        data.selectCell( 1,
                         2 );
        data.selectCell( 0,
                         2 );
        data.selectCell( 2,
                         2 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 5,
                      data.getSelectedCells().size() );

        final List<SelectedRange> selectedRanges2 = transformer.transformToSelectedRanges();
        assertEquals( 2,
                      selectedRanges2.size() );
    }

    @Test
    public void testTransformationCross2() {
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

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );

        final ISelectionsTransformer transformer = new DefaultSelectionsTransformer( data,
                                                                                     data.getColumns() );
        final List<SelectedRange> selectedRanges1 = transformer.transformToSelectedRanges();
        assertEquals( 0,
                      selectedRanges1.size() );

        // - x -
        // x x x
        // - x -
        data.selectCell( 1,
                         0 );
        data.selectCell( 1,
                         1 );
        data.selectCell( 1,
                         2 );
        data.selectCell( 0,
                         1 );
        data.selectCell( 2,
                         1 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 5,
                      data.getSelectedCells().size() );

        final List<SelectedRange> selectedRanges2 = transformer.transformToSelectedRanges();
        assertEquals( 3,
                      selectedRanges2.size() );
    }

    @Test
    public void testTransformationColumnSubset1() {
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

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );

        final ISelectionsTransformer transformer1 = new DefaultSelectionsTransformer( data,
                                                                                      data.getColumns() );
        final List<SelectedRange> selectedRanges1 = transformer1.transformToSelectedRanges();
        assertEquals( 0,
                      selectedRanges1.size() );

        // - - x
        // x x x
        // - - x
        data.selectCell( 1,
                         0 );
        data.selectCell( 1,
                         1 );
        data.selectCell( 1,
                         2 );
        data.selectCell( 0,
                         2 );
        data.selectCell( 2,
                         2 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 5,
                      data.getSelectedCells().size() );

        final List<IGridColumn<?>> columns = new ArrayList<IGridColumn<?>>() {{
            add( gc1 );
            add( gc2 );
        }};
        final ISelectionsTransformer transformer2 = new FloatingSelectionsTransformer( data,
                                                                                       columns );
        final List<SelectedRange> selectedRanges2 = transformer2.transformToSelectedRanges();
        assertEquals( 1,
                      selectedRanges2.size() );
    }

    @Test
    public void testTransformationColumnSubset2() {
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

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );

        final ISelectionsTransformer transformer1 = new DefaultSelectionsTransformer( data,
                                                                                      data.getColumns() );
        final List<SelectedRange> selectedRanges1 = transformer1.transformToSelectedRanges();
        assertEquals( 0,
                      selectedRanges1.size() );

        // - x -
        // x x x
        // - x -
        data.selectCell( 1,
                         0 );
        data.selectCell( 1,
                         1 );
        data.selectCell( 1,
                         2 );
        data.selectCell( 0,
                         1 );
        data.selectCell( 2,
                         1 );

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 5,
                      data.getSelectedCells().size() );

        final List<IGridColumn<?>> columns = new ArrayList<IGridColumn<?>>() {{
            add( gc1 );
            add( gc3 );
        }};
        final ISelectionsTransformer transformer2 = new FloatingSelectionsTransformer( data,
                                                                                       columns );
        final List<SelectedRange> selectedRanges2 = transformer2.transformToSelectedRanges();
        assertEquals( 1,
                      selectedRanges2.size() );
    }

    @Test
    public void testTransformationSplitRows() {
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

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );

        final ISelectionsTransformer transformer = new DefaultSelectionsTransformer( data,
                                                                                     data.getColumns() );
        final List<SelectedRange> selectedRanges1 = transformer.transformToSelectedRanges();
        assertEquals( 0,
                      selectedRanges1.size() );

        // x x x
        // - - -
        // x x x
        data.selectCell( 0,
                         0 );
        data.selectCell( 0,
                         1 );
        data.selectCell( 0,
                         2 );
        data.selectCell( 2,
                         0 );
        data.selectCell( 2,
                         1 );
        data.selectCell( 2,
                         2 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 6,
                      data.getSelectedCells().size() );

        final List<SelectedRange> selectedRanges2 = transformer.transformToSelectedRanges();
        assertEquals( 2,
                      selectedRanges2.size() );
    }

    @Test
    public void testTransformationNeighbours() {
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

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );

        final ISelectionsTransformer transformer = new DefaultSelectionsTransformer( data,
                                                                                     data.getColumns() );
        final List<SelectedRange> selectedRanges1 = transformer.transformToSelectedRanges();
        assertEquals( 0,
                      selectedRanges1.size() );

        // x - x
        // x - x
        // - x -
        data.selectCell( 0,
                         0 );
        data.selectCell( 0,
                         2 );
        data.selectCell( 1,
                         0 );
        data.selectCell( 1,
                         2 );
        data.selectCell( 2,
                         1 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 5,
                      data.getSelectedCells().size() );

        final List<SelectedRange> selectedRanges2 = transformer.transformToSelectedRanges();
        assertEquals( 3,
                      selectedRanges2.size() );
    }

    @Test
    public void testTransformationDiagonalCross() {
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

        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 0,
                      data.getSelectedCells().size() );

        final ISelectionsTransformer transformer = new DefaultSelectionsTransformer( data,
                                                                                     data.getColumns() );
        final List<SelectedRange> selectedRanges1 = transformer.transformToSelectedRanges();
        assertEquals( 0,
                      selectedRanges1.size() );

        // x - x
        // - x -
        // x - x
        data.selectCell( 0,
                         0 );
        data.selectCell( 0,
                         2 );
        data.selectCell( 1,
                         1 );
        data.selectCell( 2,
                         0 );
        data.selectCell( 2,
                         2 );

        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 0, 2 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 0 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 1 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 1, 2 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 0 ) ) );
        assertFalse( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 1 ) ) );
        assertTrue( data.getSelectedCells().contains( new IGridData.SelectedCell( 2, 2 ) ) );
        assertEquals( 5,
                      data.getSelectedCells().size() );

        final List<SelectedRange> selectedRanges2 = transformer.transformToSelectedRanges();
        assertEquals( 5,
                      selectedRanges2.size() );
    }

}
