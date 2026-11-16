package org.firstinspires.ftc.teamcode.util.lib;

import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.paths.Path;

import java.util.ArrayList;
import java.util.List;

public class PathOptions {

    public static Pose[] reflect(Pose... poses){
        Pose[] reflected = new Pose[poses.length];



        for(int i = 0; i<poses.length; i++){
            reflected[i] = new Pose(141.5 - poses[i].x(), 141.5 - poses[i].y(),poses[i].heading() - 180);
        }

        return  reflected;

    }
    public static Vector2D[] reflect(Vector2D start, Vector2D end){






        start = new Pose(141.5 - start.x(), 141.5 - start.y()).toVector2D();
        end = new Pose(141.5 - end.x(), 141.5 - end.y()).toVector2D();

        Vector2D[] reflected = {start,end};



        return reflected;

    }

}
