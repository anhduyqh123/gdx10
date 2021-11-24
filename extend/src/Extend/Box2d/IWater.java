package Extend.Box2d;

import GameGDX.GDX;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.*;

public class IWater extends IBody{
    public float dragMod=0.25f,liftMod=0.25f;
    public float maxDrag=2000,maxLift=500;

    private GDX.Func<Set<Pair>> getFixturePairs;
    private GDX.Func<Float> getDensity;
    private List<Event> events = new ArrayList(){};

    public IWater()
    {
        category = "water";
    }

    @Override
    protected void InitBody() {
        super.InitBody();
        if (GetBody()==null) return;
        HashSet hashSet = new HashSet();
        getFixturePairs = ()->hashSet;
        float density = GetBody().getFixtureList().get(0).getDensity();
        getDensity = ()->density;
    }

    @Override
    protected void Update(float delta) {
        super.Update(delta);
        if (GetBody()!=null)
            update();
    }

    private void update() {
        Set<Pair> fixturePairs = getFixturePairs.Run();
        float density = getDensity.Run();
        Body body = GetBody();
        if (body != null && fixturePairs != null) {
            World world = body.getWorld();
            for (Pair pair : fixturePairs) {

                Fixture fixtureA = pair.a;
                Fixture fixtureB = pair.b;

                List<Vector2> clippedPolygon = new ArrayList<>();
                if (findIntersectionOfFixtures(fixtureA, fixtureB, clippedPolygon)) {

                    // find centroid and area
                    Polygon interPolygon = getIntersectionPolygon(clippedPolygon);
                    Vector2 centroid = new Vector2();
                    GeometryUtils.polygonCentroid(interPolygon.getVertices(), 0, interPolygon.getVertices().length, centroid);
                    float area = interPolygon.area();
                    ForEvent(i->i.Area(fixtureB,area));

                    /* Get fixtures bodies */
                    Body fluidBody = fixtureA.getBody();
                    Body fixtureBody = fixtureB.getBody();

                    // apply buoyancy force (fixtureA is the fluid)
                    float displacedMass = density * area;
                    Vector2 buoyancyForce = new Vector2(displacedMass * -world.getGravity().x,
                            displacedMass * -world.getGravity().y);
                    fixtureB.getBody().applyForce(buoyancyForce, centroid, true);

//                    float dragMod = 4; // adjust as desired
//                    float liftMod = 4; // adjust as desired
//                    float maxDrag = 2000; // adjust as desired
//                    float maxLift = 500; // adjust as desired

                    /* Apply drag and lift forces */
                    int polygonVertices = clippedPolygon.size();
                    for (int i = 0; i < polygonVertices; i++) {

                        /* End points and mid point of the edge */
                        Vector2 firstPoint = clippedPolygon.get(i);
                        Vector2 secondPoint = clippedPolygon.get((i + 1) % polygonVertices);
                        Vector2 midPoint = firstPoint.cpy().add(secondPoint).scl(0.5f);

                        /*
                         * Find relative velocity between the object and the fluid at edge
                         * mid point.
                         */
                        Vector2 velocityDirection = new Vector2(fixtureBody
                                .getLinearVelocityFromWorldPoint(midPoint)
                                .sub(fluidBody.getLinearVelocityFromWorldPoint(midPoint)));
                        float velocity = velocityDirection.len();
                        velocityDirection.nor();

                        Vector2 edge = secondPoint.cpy().sub(firstPoint);
                        float edgeLength = edge.len();
                        edge.nor();

                        Vector2 normal = new Vector2(edge.y, -edge.x);
                        float dragDot = normal.dot(velocityDirection);

                        if (dragDot >= 0) {

                            /*
                             * Normal don't point backwards. This is a leading edge. Store
                             * the result of multiply edgeLength, density and velocity
                             * squared
                             */
                            float tempProduct = edgeLength * density * velocity * velocity;

                            float drag = dragDot * dragMod * tempProduct;
                            drag = Math.min(drag, maxDrag);
                            Vector2 dragForce = velocityDirection.cpy().scl(-drag);
                            fixtureBody.applyForce(dragForce, midPoint, true);

                            /* Apply lift force */
                            float liftDot = edge.dot(velocityDirection);
                            float lift = dragDot * liftDot * liftMod * tempProduct;
                            lift = Math.min(lift, maxLift);
                            Vector2 liftDirection = new Vector2(-velocityDirection.y,
                                    velocityDirection.x);
                            Vector2 liftForce = liftDirection.scl(lift);
                            fixtureBody.applyForce(liftForce, midPoint, true);


                            fixtureBody.applyTorque(-fixtureBody.getAngularVelocity()/100, true);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void OnBeginContact(IBody iBody, Fixture fixture, Contact contact) {
        Set<Pair> fixturePairs = getFixturePairs.Run();
        Body bodyB = iBody.GetBody();
        if (bodyB.getType()== BodyDef.BodyType.DynamicBody)
        {
            Fixture fixture0 = GetBody().getFixtureList().get(0);
            fixturePairs.add(new Pair(fixture0,fixture));
        }
        super.OnBeginContact(iBody, fixture, contact);
    }

    @Override
    public void OnEndContact(IBody iBody, Fixture fixture, Contact contact) {
        Set<Pair> fixturePairs = getFixturePairs.Run();
        Body bodyB = iBody.GetBody();
        if (bodyB.getType()== BodyDef.BodyType.DynamicBody)
        {
            Fixture fixture0 = GetBody().getFixtureList().get(0);
            fixturePairs.remove(new Pair(fixture0,fixture));
        }
        super.OnEndContact(iBody, fixture, contact);
    }

    //events
    private void ForEvent(GDX.Runnable<Event> cb)
    {
        for (Event i : events)
            cb.Run(i);
    }
    public void AddEvent(Event cb)
    {
        events.add(cb);
    }
    public void ClearEvent()
    {
        events.clear();
    }

    public interface Event
    {
        void Area(Fixture fixture,float area);
    }

    public class Pair
    {
        public Fixture a,b;
        public Pair(Fixture a,Fixture b)
        {
            this.a = a;
            this.b = b;
        }
    }
    //static
    /**
     * Gets the point where two lines intersect
     * @param cp1 Polygon side point 1
     * @param cp2 Polygon side point 2
     * @param s Line start point
     * @param e Line end point
     * @return The point where the two lines intersect or null if the don't cross
     */
    public static Vector2 intersection(Vector2 cp1, Vector2 cp2, Vector2 s, Vector2 e) {
        Vector2 dc = new Vector2(cp1.x - cp2.x, cp1.y - cp2.y);
        Vector2 dp = new Vector2(s.x - e.x, s.y - e.y);
        float n1 = cp1.x * cp2.y - cp1.y * cp2.x;
        float n2 = s.x * e.y - s.y * e.x;
        float n3 = (dc.x * dp.y - dc.y * dp.x);
        if(n3 != 0){
            n3 = 1.0f / n3;
            return new Vector2((n1 * dp.x - n2 * dc.x) * n3, (n1 * dp.y - n2 * dc.y) * n3);
        }

        return null;
    }
    /**
     * Checks if one point is inside of a side of a polygon
     * @param cp1 Polygon point 1
     * @param cp2 Polygon point 2
     * @param p Point to check
     * @return True if the point is inside of the polygon
     */
    public static boolean inside(Vector2 cp1, Vector2 cp2, Vector2 p) {
        return (cp2.x - cp1.x) * (p.y - cp1.y) > (cp2.y - cp1.y) * (p.x - cp1.x);
    }
    /**
     * Finds the points where two fixtures intersects
     * @param fA Fixture A (water)
     * @param fB Fixture B (dynamic body)
     * @param outputVertices It will be set with the points that form the result intersection polygon
     * @return True if the two fixtures intersect
     */
    public static boolean findIntersectionOfFixtures(Fixture fA, Fixture fB, List<Vector2> outputVertices) {
        // currently this only handles polygon or circles
        if (fA.getShape().getType() != Shape.Type.Polygon && fA.getShape().getType() != Shape.Type.Circle ||
                fB.getShape().getType() != Shape.Type.Polygon && fB.getShape().getType() != Shape.Type.Circle)
            return false;

        PolygonShape polyA = new PolygonShape();
        PolygonShape polyB = new PolygonShape();

        // if there is a circle, convert to octagon
        if(fA.getShape().getType() == Shape.Type.Circle)
            polyA = circleToSquare(fA);
        else
            polyA = (PolygonShape) fA.getShape();

        if(fB.getShape().getType() == Shape.Type.Circle)
            polyB = circleToSquare(fB);
        else
            polyB = (PolygonShape) fB.getShape();

        // fill subject polygon from fixtureA polygon
        for (int i = 0; i < polyA.getVertexCount(); i++) {
            Vector2 vertex = new Vector2();
            polyA.getVertex(i, vertex);
            vertex = fA.getBody().getWorldPoint(vertex);
            outputVertices.add(new Vector2(vertex));
        }

        // fill clip polygon from fixtureB polygon
        List<Vector2> clipPolygon = new ArrayList<Vector2>();
        for (int i = 0; i < polyB.getVertexCount(); i++) {
            Vector2 vertex = new Vector2();
            polyB.getVertex(i, vertex);
            vertex = fB.getBody().getWorldPoint(vertex);
            clipPolygon.add(new Vector2(vertex));
        }

        Vector2 cp1 = clipPolygon.get(clipPolygon.size() - 1);
        for (int j = 0; j < clipPolygon.size(); j++) {
            Vector2 cp2 = clipPolygon.get(j);
            if (outputVertices.isEmpty())
                return false;
            List<Vector2> inputList = new ArrayList<Vector2>(outputVertices);
            outputVertices.clear();
            Vector2 s = inputList.get(inputList.size() - 1); // last on the input list
            for (int i = 0; i < inputList.size(); i++) {
                Vector2 e = inputList.get(i);
                if (inside(cp1, cp2, e)) {
                    if (!inside(cp1, cp2, s)) {
                        outputVertices.add(intersection(cp1, cp2, s, e));
                    }
                    outputVertices.add(e);
                } else if (inside(cp1, cp2, s)) {
                    outputVertices.add(intersection(cp1, cp2, s, e));
                }
                s = e;
            }
            cp1 = cp2;
        }

        return !outputVertices.isEmpty();
    }
    /**
     * Creates a SimplePolygon2d object
     * @param vertices Vertices of the polygon
     * @return Polygon result
     */
    public static Polygon getIntersectionPolygon(List<Vector2> vertices) {

        float[] points = new float[vertices.size()*2];
        for(int i=0, j=0; i< vertices.size(); i++, j+=2){
            points[j] = vertices.get(i).x;
            points[j+1] = vertices.get(i).y;
        }

        return new Polygon(points);
    }
    /**
     * Because the algorithm is based on vertices, and the circles do not have vertices, we create a square around it.
     * @param fixture Circle fixture
     * @return A square instead of the circle
     */
    private static PolygonShape circleToSquare(Fixture fixture) {
        Vector2 position = fixture.getBody().getLocalCenter();
        float x = position.x;
        float y = position .y;
        float radius = fixture.getShape().getRadius();

        PolygonShape octagon = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(x-radius, y+radius);
        vertices[1]= new Vector2(x+radius, y+radius);
        vertices[2]= new Vector2(x-radius, y-radius);
        vertices[3]= new Vector2(x+radius, y-radius);
        octagon.set((Vector2[]) vertices);

        return octagon;
    }
    /**
     * Obtains a random vector
     * @param maxLength Max length
     * @return A randon vector
     */
    public static Vector2 getRandomVector(float maxLength) {
        return fromPolar(getRandomFloat(-Math.PI, Math.PI), getRandomFloat(0, maxLength));
    }

    /**
     * Obtains a random float between min and max value
     * @param min Min value
     * @param max Max value
     * @return Random float
     */
    private static float getRandomFloat(double min, double max) {
        return (float) (new Random().nextDouble() * (max - min) + min);
    }

    private static Vector2 fromPolar(float angle, float magnitude) {
        Vector2 res = new Vector2((float) Math.cos(angle), (float) Math.sin(angle));
        res.x = res.x * magnitude;
        res.y = res.y * magnitude;

        return res;
    }
}
