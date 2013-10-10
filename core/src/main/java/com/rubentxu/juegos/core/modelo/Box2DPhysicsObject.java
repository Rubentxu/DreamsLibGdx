package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.modelo.interfaces.IBox2DPhysicsObject;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DUtils;

public class Box2DPhysicsObject implements IBox2DPhysicsObject {

    public static enum GRUPOS {
        HEROES, ENEMIGOS, PLATAFORMAS, PLATAFORMAS_MOVILES,
        MONEDAS, SENSORES, ESTATICOS
    }

    protected com.badlogic.gdx.physics.box2d.World box2D;
    protected BodyDef bodyDef;
    private Body body;
    private FixtureDef fixtureDef;
    private Fixture fixture;
    private float x;
    private float y;
    private float width = 1;
    private float height = 1;
    private float radius = 0;

    private GRUPOS grupo;
    private String nombre;
    public Array points;
    public Array vertices;

    public Box2DPhysicsObject(String nombre, GRUPOS grupo, Body body) {
        this.nombre=nombre;
        this.grupo=grupo;
        this.body=body;
    }

    public Box2DPhysicsObject(String nombre, GRUPOS grupo, com.badlogic.gdx.physics.box2d.World box2D,
                              float x, float y, float width, float height, float radius) {
        this.nombre=nombre;
        this.grupo=grupo;
        this.box2D = box2D;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.radius = radius;
    }

    public void addPhysics() throws Exception {
        if (box2D == null) {
            throw new Exception("No se pudo crear el objeto porque no existe una referencia a Box2d");
        }

        defineBody();
        createBody();
        Shape shape = createShape(width, height, 0);
        defineFixture(shape);
        createFixture(getFixtureDef());
        defineJoint();
        createJoint();

    }

    protected void defineBody() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.x = this.x;
        bodyDef.position.y = this.y;
    }

    protected void createBody() {
        setBody(box2D.createBody(bodyDef));
        getBody().setUserData(this);
    }

    protected Shape createShape(float width, float height, float radius) {
        Shape shape;
        if (radius != 0) {
            shape = new CircleShape();
            shape.setRadius(radius);
        } else {
            shape = new PolygonShape();
            ((PolygonShape) shape).setAsBox(width, height);
        }
        return shape;
    }

    protected Shape createShape(float width, float height, Vector2 center, float angle) {
        Shape shape;
        if (radius != 0) {
            shape = new CircleShape();
            shape.setRadius(radius);
        } else {
            shape = new PolygonShape();
            ((PolygonShape) shape).setAsBox(width, height, center, angle);
        }
        return shape;
    }

    protected FixtureDef defineFixture(Shape shape) {
        setFixtureDef(new FixtureDef());
        getFixtureDef().shape = shape;
        getFixtureDef().density = 1;
        getFixtureDef().friction = 0.6f;
        getFixtureDef().restitution = 0.3f;
        //fixtureDef.filter.categoryBits=  PhysicsCollisionCategories.Get("Level");
        //fixtureDef.filter.maskBits = PhysicsCollisionCategories.GetAll();

        if (points != null && points.size > 1) {
            createVerticesFromPoint();

            PolygonShape polygonShape;
            int verticesLength = vertices.size;
            for (int i = 0; i < verticesLength; i++) {
                polygonShape = new PolygonShape();
                polygonShape.set((Vector2[]) vertices.get(i));
                getFixtureDef().shape = polygonShape;
                getBody().createFixture(getFixtureDef());
            }
        }
        return getFixtureDef();
    }

    protected Fixture createFixture(FixtureDef fixtureDef) {
        fixture= body.createFixture(fixtureDef);
        fixture.setUserData(this);
        return fixture;
    }

    protected void defineJoint() {

    }

    protected void createJoint() {
    }


    private void createVerticesFromPoint() {
        vertices = new Array();
        Array v = new Array();

        int len = points.size;
        for (int i = 0; i < len; ++i) {
            v.add(new Vector2(((Vector2) points.get(i)).x, ((Vector2) points.get(i)).y));
        }
        vertices.add(v);

    }

    @Override
    public void fixedUpdate() {

    }

    @Override
    public float getX() {
        return getBody().getPosition().x;
    }

    @Override
    public void setX(float value) {
        getBody().getPosition().x = value;
    }

    @Override
    public float getY() {
        return getBody().getPosition().y;

    }

    @Override
    public void setY(float value) {
        getBody().getPosition().y = value;
    }


    @Override
    public float getRotation() {
        return (float) (getBody().getAngle() * 180 / Math.PI);

    }

    @Override
    public float getWidth() {
        return Box2DUtils.width(getBody());
    }


    @Override
    public float getHeight() {
        return Box2DUtils.height(getBody());
    }

    @Override
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public void setFixtureDef(FixtureDef fixtureDef) {
        this.fixtureDef = fixtureDef;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public GRUPOS getGrupo() {
        return grupo;
    }

    public void setGrupo(GRUPOS grupo) {
        this.grupo = grupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
