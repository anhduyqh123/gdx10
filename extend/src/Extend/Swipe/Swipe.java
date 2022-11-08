package Extend.Swipe;

import Extend.Swipe.simplify.ResolverRadialChaikin;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Swipe {
    private FixedList<Vector2> inputPoints;

    /** The minimum distance between the first and second point in a drawn line. */
    public int initialDistance = 10;

    /** The minimum distance between two points in a drawn line (starting at the second point). */
    public int minDistance = 20;

    private Vector2 lastPoint = new Vector2();

    private boolean isDrawing = false;

    private ResolverRadialChaikin simplifier = new ResolverRadialChaikin();
    private Array<Vector2> simplified;

    public Swipe(int maxInputPoints) {
        this.inputPoints = new FixedList<Vector2>(maxInputPoints, Vector2.class);
        simplified = new Array<Vector2>(true, maxInputPoints, Vector2.class);
        resolve(); //copy initial empty list
    }

    /**
     * Returns the fixed list of input points (not simplified).
     * @return the list of input points
     */
    public Array<Vector2> input() {
        return this.inputPoints;
    }

    /**
     * Returns the simplified list of points representing this swipe.
     * @return
     */
    public Array<Vector2> path() {
        return simplified;
    }

    /**
     * If the points are dirty, the line is simplified.
     */
    public void resolve() {
        simplifier.resolve(inputPoints, simplified);
    }

    public void Start(float x,float y)
    {
        isDrawing = true;
        //clear points
        inputPoints.clear();
        //starting point
        lastPoint = new Vector2(x, y);
        inputPoints.insert(lastPoint);
        resolve();
    }
    public void Update(float x,float y)
    {
        isDrawing = true;

        Vector2 v = new Vector2(x, y);

        //calc length
        float dx = v.x - lastPoint.x;
        float dy = v.y - lastPoint.y;
        float len = (float)Math.sqrt(dx*dx + dy*dy);
        //TODO: use minDistanceSq

        //if we are under required distance
        //if (len < minDistance && (inputPoints.size>1 || len<initialDistance)) return;

        //add new point
        inputPoints.insert(v);

        lastPoint = v;

        //simplify our new line
        resolve();
    }
    public void End()
    {
        //on release, the line is simplified
        resolve();
        isDrawing = false;
    }
}
