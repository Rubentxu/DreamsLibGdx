package com.rubentxu.juegos.core.modelo;


import com.badlogic.gdx.math.Vector2;
import  com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.utils.Array;
import com.rubentxu.juegos.core.modelo.interfaces.IBox2DPhysicsObject;

public class Box2DPhysicsObject implements IBox2DPhysicsObject {

    protected com.badlogic.gdx.physics.box2d.World box2D;
    protected BodyDef bodyDef;
    protected Body body;
    protected Shape shape;
    protected FixtureDef fixtureDef;
    protected Fixture fixture;

    protected float x;
    protected float y;
    protected float z;
    protected float rotation;
    protected float radius;

    private int group;


    protected  float width;
    protected  float height;

    protected  Boolean beginContactCallEnabled;
    protected  Boolean endContactCallEnabled;
    protected  Boolean preContactCallEnabled;
    protected  Boolean postContactCallEnabled;

    public Array points;
    public Array vertices;


    public Box2DPhysicsObject(String nombre, com.badlogic.gdx.physics.box2d.World box2D,float x,float y) {
        this.box2D = box2D;
        this.x=x;
        this.y=y;
    }

    public void addPhysics() throws Exception {
        if(box2D==null){
            throw new Exception("No se pudo crear el objeto porque no existe una referencia a Box2d");
        }

        defineBody();
        createBody();
        createShape();
        defineFixture();
        createFixture();
        defineJoint();
        createJoint();

    }


    private void defineBody() {
        bodyDef= new BodyDef();
        bodyDef.type= BodyDef.BodyType.DynamicBody;
        bodyDef.position.x =  this.x;
        bodyDef.position.y= this.y;
        bodyDef.angle= rotation;
    }

    private void createBody() {
        body= box2D.createBody(bodyDef);
        body.setUserData(this);
    }

    private void createShape() {
        if(radius !=0){
            shape= new CircleShape();
            shape.setRadius(radius);
        } else {
            shape= new PolygonShape();
            ((PolygonShape)shape).setAsBox(width/2,height/2);
        }
    }

    private void defineFixture() {
        fixtureDef= new FixtureDef();
        fixtureDef.shape=shape;
        fixtureDef.density=1;
        fixtureDef.friction=0.6f;
        fixtureDef.restitution=0.3f;
        //fixtureDef.filter.categoryBits=  PhysicsCollisionCategories.Get("Level");
        //fixtureDef.filter.maskBits = PhysicsCollisionCategories.GetAll();

        if(points!=null && points.size > 1) {
            createVerticesFromPoint();

            PolygonShape polygonShape;
            int verticesLength= vertices.size;
            for (int i=0; i < verticesLength; i++){
                polygonShape= new PolygonShape();
                polygonShape.set((Vector2[]) vertices.get(i));
                fixtureDef.shape= polygonShape;
                body.createFixture(fixtureDef);
            }
        }

    }

    private void createFixture() {
        fixture= body.createFixture(fixtureDef);
    }

    private void defineJoint() {

    }

    private void createJoint() {
    }


    private void createVerticesFromPoint() {
        vertices= new Array();
        Array v= new Array();

        int len= points.size;
        for (int i=0; i < len; ++i){
            v.add(new Vector2(((Vector2)points.get(i)).x , ((Vector2)points.get(i)).y ));
        }
        vertices.add(v);

    }


    @Override
    public void handleBeginContact(Contact contact) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handleEndContact(Contact contact) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handlePreSolve(Contact contact, Manifold oldManifold) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void handlePostSolve(Contact contact, ContactImpulse impulse) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fixedUpdate() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getX() {
       if(body!=null)
           return body.getPosition().x;
        else
           return x;
    }

    @Override
    public void setX(float value) {
        x= value;
    }

    @Override
    public float getY() {
        if(body!=null)
            return body.getPosition().y;
        else
            return y;
    }

    @Override
    public void setY(float value) {
        y= value;
    }


    @Override
    public float getRotation() {
        if(body!=null)
            return (float) (body.getAngle() * 180 / Math.PI);
        else
            return (float) (rotation * 180 / Math.PI);
    }

    @Override
    public void setRotation(float value) {
        rotation= (float) (value * Math.PI / 180);

        if(body!=null){
            body.setTransform(body.getPosition(),body.getAngle());
        }
    }

    @Override
    public float getWidth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float setWidth(float value) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getHeight() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float setHeight(float value) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getDepth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getRadius() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float setRadius(float value) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Body getBody() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean getBeginContactCallEnabled() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean setBeginContactCallEnabled(Boolean value) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean getEndContactCallEnabled() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean setEndContactCallEnabled(Boolean value) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean getPreContactCallEnabled() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean setPreContactCallEnabled(Boolean value) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean getPostContactCallEnabled() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean setPostContactCallEnabled(Boolean value) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
