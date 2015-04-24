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
package org.uberfire.ext.wires.core.grids.client.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Transform;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.ListBox;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridCellValue;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridData;
import org.uberfire.ext.wires.core.grids.client.model.BaseGridRow;
import org.uberfire.ext.wires.core.grids.client.model.HeaderMetaDataImpl;
import org.uberfire.ext.wires.core.grids.client.model.IGridColumn;
import org.uberfire.ext.wires.core.grids.client.model.IGridData;
import org.uberfire.ext.wires.core.grids.client.model.IGridRow;
import org.uberfire.ext.wires.core.grids.client.util.GridDataFactory;
import org.uberfire.ext.wires.core.grids.client.widget.columns.BooleanDOMElementColumn;
import org.uberfire.ext.wires.core.grids.client.widget.columns.ListBoxDOMElementSingletonColumn;
import org.uberfire.ext.wires.core.grids.client.widget.columns.RowNumberColumn;
import org.uberfire.ext.wires.core.grids.client.widget.columns.StringDOMElementSingletonColumn;
import org.uberfire.ext.wires.core.grids.client.widget.columns.StringPopupColumn;
import org.uberfire.ext.wires.core.grids.client.widget.dom.multiple.CheckBoxDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.dom.single.ListBoxSingletonDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.dom.single.TextBoxSingletonDOMElementFactory;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.columns.StringColumnRenderer;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.grids.BaseGridRenderer;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.themes.BlueTheme;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.themes.GreenTheme;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.themes.IGridRendererTheme;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.themes.MultiColouredTheme;
import org.uberfire.ext.wires.core.grids.client.widget.renderers.themes.RedTheme;
import org.uberfire.ext.wires.core.grids.client.widget.selections.RowSelectionManager;
import org.uberfire.mvp.Command;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.Menus;

@Dependent
@WorkbenchScreen(identifier = "WiresGridsScreen")
public class WiresGridsScreen extends Composite implements RequiresResize,
                                                           IGridSelectionManager {

    private static final double VP_SCALE = 1.0;

    private static final int GRID1_ROWS = 100;
    private static final int GRID2_ROWS = 100;
    private static final int GRID3_ROWS = 5;
    private static final int GRID4_ROWS = 100;

    interface WiresGridsScreenUiBinder extends UiBinder<Widget, WiresGridsScreen> {

    }

    private static WiresGridsScreenUiBinder uiBinder = GWT.create( WiresGridsScreenUiBinder.class );

    @UiField(provided = true)
    GridLienzoPanel gridPanel = new GridLienzoPanel( 200, 200 );

    @UiField
    ListBox zoom;

    @UiField
    ListBox basicRendererSelector;

    @UiField
    CheckBox chkShowMerged;

    @UiField
    Button btnAppendRow;

    private Menus menus;

    private GridLayer gridLayer = new GridLayer();

    public WiresGridsScreen() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @WorkbenchPartTitle
    @Override
    public String getTitle() {
        return "Grids";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return this;
    }

    @WorkbenchMenu
    public Menus getMenus() {
        return menus;
    }

    @PostConstruct
    public void setup() {
        //Menus
        this.menus = MenuFactory
                .newTopLevelMenu( "Clear selections" )
                .respondsWith( new Command() {
                    @Override
                    public void execute() {
                        for ( IBaseGridWidget gridWidget : gridLayer.getGridWidgets() ) {
                            if ( gridWidget.isSelected() ) {
                                gridWidget.getModel().clearSelections();
                            }
                        }
                        gridLayer.batch();
                        menus.getItems().get( 0 ).setEnabled( false );
                        menus.getItems().get( 1 ).setEnabled( false );
                    }
                } )
                .endMenu()
                .newTopLevelMenu( "Clear cells" )
                .respondsWith( new Command() {
                    @Override
                    public void execute() {
                        clearCells();
                    }
                } )
                .endMenu()
                .build();
        menus.getItems().get( 0 ).setEnabled( false );
        menus.getItems().get( 1 ).setEnabled( false );

        //Lienzo stuff - Set default scale
        final Transform transform = new Transform().scale( VP_SCALE );
        gridPanel.getViewport().setTransform( transform );

        //Lienzo stuff - Add mouse pan support
        final GridPanelMousePanMediator mediator1 = new GridPanelMousePanMediator();
        mediator1.setBatchDraw( true );
        gridPanel.getViewport().getMediators().push( mediator1 );

        //Add support for deleting cells' content with the DELETE key
        gridPanel.addKeyDownHandler( new KeyDownHandler() {
            @Override
            public void onKeyDown( final KeyDownEvent event ) {
                if ( event.getNativeKeyCode() == KeyCodes.KEY_DELETE ) {
                    clearCells();
                }
            }
        } );

        //Wire-up widgets
        gridPanel.add( gridLayer );

        //--- Grid 1
        final IGridData grid1 = new BaseGridData();
        final BaseGridWidget gridWidget1 = new BaseGridWidget( grid1,
                                                               this,
                                                               new BaseGridRenderer( new MultiColouredTheme() ) );

        //Add a floating column for row number
        final IGridColumn.HeaderMetaData grid1ColumnRowNumberHeaderMetaData = new HeaderMetaDataImpl( "#" );
        final BaseGridColumn<String> grid1ColumnRowNumber = new BaseGridColumn<String>( grid1ColumnRowNumberHeaderMetaData,
                                                                                        new StringColumnRenderer(),
                                                                                        100 );
        grid1ColumnRowNumber.setMovable( false );
        grid1ColumnRowNumber.setResizable( true );
        grid1ColumnRowNumber.setFloatable( true );
        grid1.appendColumn( grid1ColumnRowNumber );

        //Add a floating column
        final IGridColumn.HeaderMetaData grid1ColumnFloatingHeaderMetaData = new HeaderMetaDataImpl( "Floating" );
        final TextBoxSingletonDOMElementFactory grid1ColumnFloatingFactory = new TextBoxSingletonDOMElementFactory( gridLayer,
                                                                                                                    gridWidget1 );
        final BaseGridColumn<String> grid1ColumnFloating = new StringDOMElementSingletonColumn( grid1ColumnFloatingHeaderMetaData,
                                                                                                grid1ColumnFloatingFactory,
                                                                                                100 );
        grid1ColumnFloating.setMovable( false );
        grid1ColumnFloating.setResizable( true );
        grid1ColumnFloating.setFloatable( true );
        grid1.appendColumn( grid1ColumnFloating );

        for ( int idx = 0; idx < 8; idx++ ) {
            final int grid1ColumnGroupSuffix = ( idx < 3 ? 0 : ( idx < 6 ? 1 : 2 ) );
            final boolean isFloatable = ( idx == 0 );
            final IGridColumn.HeaderMetaData grid1ColumnHeaderMetaData1 = new HeaderMetaDataImpl( "G1-G" + grid1ColumnGroupSuffix + "-a-Long-Caption-1",
                                                                                                  "grid1ColumnGroup" );
            final IGridColumn.HeaderMetaData grid1ColumnHeaderMetaData2 = new HeaderMetaDataImpl( "G1-G" + grid1ColumnGroupSuffix + "-C" + idx + "-a-Long-Caption-2",
                                                                                                  "grid1ColumnGroup" + grid1ColumnGroupSuffix );
            final List<IGridColumn.HeaderMetaData> grid1ColumnHeaderMetaData = new ArrayList<IGridColumn.HeaderMetaData>();
            grid1ColumnHeaderMetaData.add( grid1ColumnHeaderMetaData1 );
            grid1ColumnHeaderMetaData.add( grid1ColumnHeaderMetaData2 );
            final BaseGridColumn<String> grid1Column = new StringPopupColumn( grid1ColumnHeaderMetaData,
                                                                              new StringColumnRenderer(),
                                                                              100 );
            grid1Column.setMinimumWidth( 50.0 );
            grid1Column.setFloatable( isFloatable );
            grid1.appendColumn( grid1Column );
        }

        GridDataFactory.populate( grid1,
                                  GRID1_ROWS );

        //Overwrite row number values
        for ( int idx = 0; idx < GRID1_ROWS; idx++ ) {
            grid1.setCell( idx,
                           0,
                           new BaseGridCellValue<String>( Integer.toString( idx + 1 ) ) );
            grid1.getCell( idx,
                           0 ).setSelectionManager( RowSelectionManager.INSTANCE );
        }

        //--- Grid 2
        final IGridData grid2 = new BaseGridData();
        final BaseGridWidget gridWidget2 = new BaseGridWidget( grid2,
                                                               this,
                                                               new BaseGridRenderer( new MultiColouredTheme() ) );
        for ( int idx = 0; idx < 5; idx++ ) {
            final IGridColumn.HeaderMetaData grid2ColumnHeaderMetaData = new HeaderMetaDataImpl( "G2-G0-C" + idx );
            final BaseGridColumn<String> grid2Column = new StringPopupColumn( grid2ColumnHeaderMetaData,
                                                                              new StringColumnRenderer(),
                                                                              150 );
            grid2.appendColumn( grid2Column );
        }
        GridDataFactory.populate( grid2,
                                  GRID2_ROWS );

        //--- Grid 3
        final IGridData grid3 = new BaseGridData();
        final BaseGridWidget gridWidget3 = new BaseGridWidget( grid3,
                                                               this,
                                                               new BaseGridRenderer( new MultiColouredTheme() ) );

        //RowNumber column supporting row drag-and-drop
        final RowNumberColumn grid3RowNumberColumn = new RowNumberColumn();
        grid3.appendColumn( grid3RowNumberColumn );

        for ( int idx = 0; idx < 2; idx++ ) {
            final boolean isResizeable = idx > 1;
            final boolean isMovable = idx > 1;
            final IGridColumn.HeaderMetaData grid3ColumnHeaderMetaData = new HeaderMetaDataImpl( "G3-G0-C" + idx );
            final BaseGridColumn<String> grid3Column = new StringPopupColumn( grid3ColumnHeaderMetaData,
                                                                              new StringColumnRenderer(),
                                                                              100 );
            grid3Column.setResizable( isResizeable );
            grid3Column.setMovable( isMovable );
            grid3.appendColumn( grid3Column );
        }
        GridDataFactory.populate( grid3,
                                  GRID3_ROWS );

        //Add DOM Column - TextBox (Lazy show)
        final String grid3ColumnGroup1 = "grid3ColumnGroup1";
        final IGridColumn.HeaderMetaData grid3Column2HeaderMetaData = new HeaderMetaDataImpl( "G3-G1-C2",
                                                                                              grid3ColumnGroup1 );
        final TextBoxSingletonDOMElementFactory grid3Column2Factory = new TextBoxSingletonDOMElementFactory( gridLayer,
                                                                                                             gridWidget3 );
        final BaseGridColumn<String> grid3Column2 = new StringDOMElementSingletonColumn( grid3Column2HeaderMetaData,
                                                                                         grid3Column2Factory,
                                                                                         100 );
        grid3.appendColumn( grid3Column2 );
        for ( int rowIndex = 0; rowIndex < GRID4_ROWS; rowIndex++ ) {
            grid3.setCell( rowIndex,
                           3,
                           new BaseGridCellValue<String>( "(" + 2 + ", " + rowIndex + ")" ) );
        }

        //Add DOM Column - CheckBox
        final IGridColumn.HeaderMetaData grid3Column3HeaderMetaData = new HeaderMetaDataImpl( "G3-G1-C3",
                                                                                              grid3ColumnGroup1 );
        final CheckBoxDOMElementFactory grid3Column3Factory = new CheckBoxDOMElementFactory( gridLayer,
                                                                                             gridWidget3 );
        final BaseGridColumn<Boolean> grid3Column3 = new BooleanDOMElementColumn( grid3Column3HeaderMetaData,
                                                                                  grid3Column3Factory,
                                                                                  100 );
        grid3Column3.setFloatable( true );
        grid3.appendColumn( grid3Column3 );
        for ( int rowIndex = 0; rowIndex < GRID4_ROWS; rowIndex++ ) {
            grid3.setCell( rowIndex,
                           4,
                           new BaseGridCellValue<Boolean>( Math.random() < GridDataFactory.FILL_FACTOR ) );
        }

        //Add DOM Column - ListBox
        final IGridColumn.HeaderMetaData grid3Column4HeaderMetaData = new HeaderMetaDataImpl( "G3-G1-C4",
                                                                                              grid3ColumnGroup1 );
        final ListBoxSingletonDOMElementFactory grid3Column4Factory = new ListBoxSingletonDOMElementFactory( gridLayer,
                                                                                                             gridWidget3 );
        final BaseGridColumn<String> grid3Column4 = new ListBoxDOMElementSingletonColumn( grid3Column4HeaderMetaData,
                                                                                          grid3Column4Factory,
                                                                                          100 );
        grid3.appendColumn( grid3Column4 );
        for ( int rowIndex = 0; rowIndex < GRID4_ROWS; rowIndex++ ) {
            grid3.setCell( rowIndex,
                           5,
                           new BaseGridCellValue<String>( rowIndex % 2 == 0 ? "one" : "two" ) );
        }

        //--- Grid 4
        final IGridData grid4 = new BaseGridData( false );
        final BaseGridWidget gridWidget4 = new BaseGridWidget( grid4,
                                                               this,
                                                               new BaseGridRenderer( new RedTheme() ) );

        //Add DOM Column - TextBox
        final IGridColumn.HeaderMetaData grid4Column1HeaderMetaData = new HeaderMetaDataImpl( "G4-G0-C0" );
        final BaseGridColumn<String> grid4Column1 = new StringPopupColumn( grid4Column1HeaderMetaData,
                                                                           new StringColumnRenderer(),
                                                                           100 );
        grid4.appendColumn( grid4Column1 );

        //Add DOM Column - CheckBox
        final IGridColumn.HeaderMetaData grid4Column2HeaderMetaData = new HeaderMetaDataImpl( "G4-G0-C1" );
        final CheckBoxDOMElementFactory grid4Column2Factory = new CheckBoxDOMElementFactory( gridLayer,
                                                                                             gridWidget4 );
        final BaseGridColumn<Boolean> grid4Column2 = new BooleanDOMElementColumn( grid4Column2HeaderMetaData,
                                                                                  grid4Column2Factory,
                                                                                  100 );
        grid4.appendColumn( grid4Column2 );

        for ( int rowIndex = 0; rowIndex < GRID4_ROWS; rowIndex++ ) {
            final IGridRow row = new BaseGridRow();
            grid4.appendRow( row );
            for ( int columnIndex = 0; columnIndex < grid4.getColumns().size(); columnIndex++ ) {
                switch ( columnIndex ) {
                    case 0:
                        if ( Math.random() > 0.5 ) {
                            grid4.setCell( rowIndex,
                                           columnIndex,
                                           new BaseGridCellValue<String>( "(" + columnIndex + ", " + rowIndex + ")" ) );
                        }
                        break;
                    case 1:
                        grid4.setCell( rowIndex,
                                       columnIndex,
                                       new BaseGridCellValue<Boolean>( Math.random() < 0.5 ) );
                        break;
                }
            }
        }

        //Link grids
        grid1.getColumns().get( 9 ).setLink( grid2.getColumns().get( 0 ) );
        grid2.getColumns().get( 3 ).setLink( grid3.getColumns().get( 0 ) );
        grid3.getColumns().get( 1 ).setLink( grid1.getColumns().get( 0 ) );

        //Add Widgets to the Layer
        gridWidget1.setLocation( new Point2D( -1300,
                                              0 ) );
        gridWidget2.setLocation( new Point2D( 0,
                                              750 ) );
        gridWidget3.setLocation( new Point2D( 1050,
                                              0 ) );
        gridWidget4.setLocation( new Point2D( 1800,
                                              200 ) );
        gridLayer.add( gridWidget1 );
        gridLayer.add( gridWidget2 );
        gridLayer.add( gridWidget3 );
        gridLayer.add( gridWidget4 );

        //Slider
        for ( int pct = 50; pct <= 150; pct = pct + 10 ) {
            zoom.addItem( Integer.toString( pct ) );
        }
        zoom.setSelectedIndex( 5 );
        zoom.addChangeHandler( new ChangeHandler() {

            private double m_currentZoom = 1.0;

            @Override
            public void onChange( final ChangeEvent event ) {
                final int selectedIndex = zoom.getSelectedIndex();
                if ( selectedIndex < 0 ) {
                    return;
                }
                final double pct = Double.parseDouble( zoom.getValue( selectedIndex ) );
                final int compare = Double.compare( m_currentZoom,
                                                    pct );
                if ( compare == 0 ) {
                    return;
                }
                m_currentZoom = pct;

                final Transform transform = new Transform();
                final double tx = gridPanel.getViewport().getTransform().getTranslateX();
                final double ty = gridPanel.getViewport().getTransform().getTranslateY();
                transform.translate( tx, ty );
                transform.scale( m_currentZoom / 100 );

                gridPanel.getViewport().setTransform( transform );
                gridPanel.getViewport().batch();
            }

        } );

        //Style selectors
        final Map<String, IGridRendererTheme> themes = new HashMap<String, IGridRendererTheme>();
        final RedTheme redRenderer = new RedTheme();
        final GreenTheme greenRenderer = new GreenTheme();
        final BlueTheme blueRenderer = new BlueTheme();
        final MultiColouredTheme multiColouredTheme = new MultiColouredTheme();
        themes.put( redRenderer.getName(),
                    redRenderer );
        themes.put( greenRenderer.getName(),
                    greenRenderer );
        themes.put( blueRenderer.getName(),
                    blueRenderer );
        themes.put( multiColouredTheme.getName(),
                    multiColouredTheme );
        for ( String name : themes.keySet() ) {
            basicRendererSelector.addItem( name );
        }
        basicRendererSelector.addChangeHandler( new ChangeHandler() {
            @Override
            @SuppressWarnings("unused")
            public void onChange( final ChangeEvent event ) {
                final IGridRendererTheme theme = themes.get( basicRendererSelector.getItemText( basicRendererSelector.getSelectedIndex() ) );
                gridWidget4.getRenderer().setTheme( theme );
                gridLayer.batch();
            }
        } );

        //Merged indicator
        chkShowMerged.setValue( grid1.isMerged() );
        chkShowMerged.addValueChangeHandler( new ValueChangeHandler<Boolean>() {
            @Override
            @SuppressWarnings("unused")
            public void onValueChange( final ValueChangeEvent<Boolean> event ) {
                grid1.setMerged( chkShowMerged.getValue() );
                grid2.setMerged( chkShowMerged.getValue() );
                grid3.setMerged( chkShowMerged.getValue() );
                gridLayer.batch();
            }
        } );

        //Append Row
        btnAppendRow.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( final ClickEvent event ) {
                for ( IBaseGridWidget gridWidget : gridLayer.getGridWidgets() ) {
                    if ( gridWidget.isSelected() ) {
                        gridWidget.getModel().appendRow( new BaseGridRow() );
                    }
                }
                gridLayer.batch();
            }
        } );
    }

    private void clearCells() {
        for ( IBaseGridWidget gridWidget : gridLayer.getGridWidgets() ) {
            if ( gridWidget.isSelected() ) {
                for ( IGridData.SelectedCell cell : gridWidget.getModel().getSelectedCells() ) {
                    gridWidget.getModel().deleteCell( cell.getRowIndex(),
                                                      cell.getColumnIndex() );
                }
            }
        }
        gridLayer.batch();
        menus.getItems().get( 0 ).setEnabled( false );
        menus.getItems().get( 1 ).setEnabled( false );
    }

    @Override
    public void select( final IBaseGridWidget selectedGridWidget ) {
        gridLayer.select( selectedGridWidget );
        final boolean hasSelections = selectedGridWidget.getModel().getSelectedCells().size() > 0;
        menus.getItems().get( 0 ).setEnabled( hasSelections );
        menus.getItems().get( 1 ).setEnabled( hasSelections );
    }

    @Override
    public void selectLinkedColumn( final IGridColumn<?> selectedGridColumn ) {
        gridLayer.selectLinkedColumn( selectedGridColumn );
    }

    @Override
    public Set<IBaseGridWidget> getGridWidgets() {
        return gridLayer.getGridWidgets();
    }

    @Override
    public void onResize() {
        gridPanel.onResize();
    }

}
