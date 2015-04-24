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
package org.uberfire.ext.wires.core.grids.client.model.basic;

import com.ait.lienzo.client.core.shape.Group;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.ext.wires.core.grids.client.model.IGridCellValue;
import org.uberfire.ext.wires.core.grids.client.widget.context.GridCellRenderContext;

import static org.junit.Assert.*;

public abstract class BaseGridTest {

    public void assertGridIndexes( final GridData data,
                                   final Expected[][] expectedCellStates ) {
        if ( data.getRowCount() != expectedCellStates.length ) {
            fail( "Size of parameter 'expectedCellStates' differs to expected row count." );
        }
        for ( int rowIndex = 0; rowIndex < data.getRowCount(); rowIndex++ ) {
            if ( data.getColumns().size() != expectedCellStates[ rowIndex ].length ) {
                fail( "Size of parameter 'expectedCellStates[" + rowIndex + "]' differs to expected column count." );
            }

            for ( int columnIndex = 0; columnIndex < data.getColumns().size(); columnIndex++ ) {
                final GridCell cell = data.getCell( rowIndex,
                                                    columnIndex );
                if ( cell == null ) {
                    assertNull( "Cell[" + columnIndex + ", " + rowIndex + "] was expected to be null.",
                                expectedCellStates[ rowIndex ][ columnIndex ].value );
                } else {
                    assertEquals( "Cell[" + columnIndex + ", " + rowIndex + "] actual getValue() differs to expected.",
                                  expectedCellStates[ rowIndex ][ columnIndex ].value,
                                  cell.getValue().getValue() );
                }
            }
        }
    }

    public static class Expected {

        public static Expected build( final Object value ) {
            return new Expected( value );
        }

        private Object value;

        private Expected( final Object value ) {
            this.value = value;
        }

    }

    static class MockGridColumn<T> extends GridColumn<T> {

        MockGridColumn( final String title,
                        final double width ) {
            super( title,
                   width );
        }

        @Override
        public void renderCell( final Group g,
                                final GridCell<T> cell,
                                final GridCellRenderContext context ) {
            //Do nothing
        }

        @Override
        public void edit( final GridCell<T> cell,
                          final GridCellRenderContext context,
                          final Callback<IGridCellValue<T>> callback ) {
            //Do nothing
        }

    }

}
