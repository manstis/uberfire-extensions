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

package org.uberfire.ext.wires.core.grids.client.widget.animation;

import java.util.List;

import com.ait.lienzo.client.core.animation.AnimationTweener;
import com.ait.lienzo.client.core.animation.IAnimation;
import com.ait.lienzo.client.core.animation.IAnimationCallback;
import com.ait.lienzo.client.core.animation.IAnimationHandle;
import com.ait.lienzo.client.core.animation.TimedAnimation;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.Point2D;
import org.uberfire.commons.data.Pair;
import org.uberfire.ext.wires.core.grids.client.widget.IGridLayer;
import org.uberfire.mvp.ParameterizedCommand;

public class ShowRowControlsAnimation extends TimedAnimation {

    private static final double CONTROLS_RADIUS = 25.0;

    public ShowRowControlsAnimation( final List<Pair<IPrimitive<?>, ParameterizedCommand<Integer>>> toolBoxItems,
                                     final IGridLayer gridLayer,
                                     final double rowX,
                                     final double rowY ) {
        super( 500,
               new IAnimationCallback() {

                   private AnimationTweener tweener = AnimationTweener.EASE_OUT;

                   @Override
                   public void onStart( final IAnimation animation,
                                        final IAnimationHandle handle ) {
                       for ( Pair<IPrimitive<?>, ParameterizedCommand<Integer>> toolBoxItem : toolBoxItems ) {
                           final IPrimitive<?> prim = toolBoxItem.getK1();
                           prim.setAlpha( 0.0 );
                           prim.setLocation( new Point2D( rowX,
                                                          rowY ) );
                           gridLayer.add( prim );
                           prim.moveToTop();
                       }
                   }

                   @Override
                   public void onFrame( final IAnimation animation,
                                        final IAnimationHandle handle ) {
                       //Set the rows' height to their starting height down to zero
                       double theta = 0.0;
                       final double controlSpacing = 2 * Math.PI / toolBoxItems.size();
                       final double pct = assertPct( animation.getPercent() );
                       for ( int iCtrl = 0; iCtrl < toolBoxItems.size(); iCtrl++ ) {
                           final Pair<IPrimitive<?>, ParameterizedCommand<Integer>> control = toolBoxItems.get( iCtrl );
                           final double cx = ( Math.cos( theta ) * CONTROLS_RADIUS * pct ) + rowX;
                           final double cy = ( Math.sin( theta ) * CONTROLS_RADIUS * pct ) + rowY;
                           final IPrimitive<?> prim = control.getK1();
                           prim.setX( cx );
                           prim.setY( cy );
                           prim.setAlpha( pct );
                           theta = theta + controlSpacing;
                       }
                       gridLayer.batch();
                   }

                   @Override
                   public void onClose( final IAnimation animation,
                                        final IAnimationHandle handle ) {
                       //Do nothing
                   }

                   private double assertPct( final double pct ) {
                       if ( pct < 0 ) {
                           return 0;
                       }
                       if ( pct > 1.0 ) {
                           return 1.0;
                       }
                       return tweener.apply( pct );
                   }

               } );

    }
}