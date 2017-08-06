package com.threeDBJ.MGraphicsLib;

import com.threeDBJ.MGraphicsLib.math.Vec2;

public interface Clickable {

    boolean handleActionDown(Vec2 p);

    boolean handleActionUp(Vec2 p);

    boolean handleActionMove(Vec2 p);

}