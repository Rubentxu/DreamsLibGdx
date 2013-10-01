package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.modelo.interfaces.IBox2DPhysicsObject;
import com.rubentxu.juegos.core.utils.dermetfan.box2d.Box2DUtils;

public class Box2DPhysicsObject implements IBox2DPhysicsObject, ContactListener {


    public enum grupos {
        HEROES, ENEMIGOS, PLATAFORMAS, PLATAFOR_MASMOVILES,
        MONEDAS, SENSORES
    }

    protected com.badlogic.gdx.physics.box2d.World box2D;
    protected BodyDef bodyDef;
    protected Body body;
    protected FixtureDef fixtureDef;
    protected Fixture fixture;

    private float x, y, width = 1, height = 1, radius = 0;


    private grupos grupo;
    private String nombre;

    protected Boolean beginContactCallEnabled;
    protected Boolean endContactCallEnabled;
    protected Boolean preContactCallEnabled;
    protected Boolean postContactCallEnabled;

    public Array points;
    public Array vertices;


    public Box2DPhysicsObject(String nombre, grupos tipo, com.badlogic.gdx.physics.box2d.World box2D,
                              float x, float y, float width, float height, float radius) {
        this.nombre = nombre;
        this.grupo = tipo;
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
        createFixture(fixtureDef);
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
        body = box2D.createBody(bodyDef);
        body.setUserData(this);
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
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0.6f;
        fixtureDef.restitution = 0.3f;
        //fixtureDef.filter.categoryBits=  PhysicsCollisionCategories.Get("Level");
        //fixtureDef.filter.maskBits = PhysicsCollisionCategories.GetAll();

        if (points != null && points.size > 1) {
            createVerticesFromPoint();

            PolygonShape polygonShape;
            int verticesLength = vertices.size;
            for (int i = 0; i < verticesLength; i++) {
                polygonShape = new PolygonShape();
                polygonShape.set((Vector2[]) vertices.get(i));
                fixtureDef.shape = polygonShape;
                body.createFixture(fixtureDef);
            }
        }
        return fixtureDef;
    }

    protected Fixture createFixture(FixtureDef fixtureDef) {
        fixture = body.createFixture(fixtureDef);
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
        return body.getPosition().x;
    }

    @Override
    public void setX(float value) {
        body.getPosition().x = value;
    }

    @Override
    public float getY() {
        return body.getPosition().y;

    }

    @Override
    public void setY(float value) {
        body.getPosition().y = value;
    }


    @Override
    public float getRotation() {
        return (float) (body.getAngle() * 180 / Math.PI);

    }

    @Override
    public float getWidth() {
        return Box2DUtils.width(body);
    }


    @Override
    public float getHeight() {
        return Box2DUtils.height(body);
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public Boolean getBeginContactCallEnabled() {
        return beginContactCallEnabled;
    }

    @Override
    public void setBeginContactCallEnabled(Boolean value) {
        beginContactCallEnabled = value;
    }

    @Override
    public Boolean getEndContactCallEnabled() {
        return endContactCallEnabled;
    }

    @Override
    public void setEndContactCallEnabled(Boolean value) {
        endContactCallEnabled = value;
    }

    @Override
    public Boolean getPreContactCallEnabled() {
        return preContactCallEnabled;
    }

    @Override
    public void setPreContactCallEnabled(Boolean value) {
        preContactCallEnabled = value;
    }

    @Override
    public Boolean getPostContactCallEnabled() {
        return postContactCallEnabled;
    }

    @Override
    public void setPostContactCallEnabled(Boolean value) {
        postContactCallEnabled = value;
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
