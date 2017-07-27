package com.threeDBJ.MGraphicsLib;

import com.threeDBJ.MGraphicsLib.math.Vec2;

public interface Clickable {

    public boolean handleActionDown(Vec2 p);

    public boolean handleActionUp(Vec2 p);

    public boolean handleActionMove(Vec2 p);

}