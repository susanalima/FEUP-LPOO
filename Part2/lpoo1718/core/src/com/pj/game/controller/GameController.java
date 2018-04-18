package com.pj.game.controller;

import com.pj.game.controller.entities.BigAsteroidBody;
import com.pj.game.controller.entities.BulletBody;
import com.pj.game.controller.entities.MediumAsteroidBody;
import com.pj.game.controller.entities.ShipBody;
import com.pj.game.model.GameModel;
import com.pj.game.model.entities.AsteroidModel;
import com.pj.game.model.entities.BulletModel;
import com.pj.game.model.entities.EntityModel;
import com.pj.game.model.entities.ShipModel;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Controls the physics aspect of the game.
 */

public class GameController implements ContactListener {
    /**
     * The singleton instance of this controller
     */
    private static GameController instance;

    /**
     * The arena width in meters.
     */
    public static final int ARENA_WIDTH = 100;

    /**
     * The arena height in meters.
     */
    public static final int ARENA_HEIGHT = 50;

    /**
     * The rotation speed in radians per second.
     */
    private static final float ROTATION_SPEED = 5f;

    /**
     * The acceleration impulse in newtons.
     */
    private static final float ACCELERATION_FORCE = 1000f;

    /**
     * The speed of bullets
     */
    private static final float BULLET_SPEED = 30f;

    /**
     * The number of fragments an asteroid breaks into
     */
    private static final int FRAGMENT_COUNT = 5;

    /**
     * Minimum time between consecutive shots in seconds
     */
    private static final float TIME_BETWEEN_SHOTS = .2f;

    /**
     * The physics world controlled by this controller.
     */
    private final World world;

    /**
     * The spaceship body.
     */
    private final ShipBody shipBody;

    /**
     * Accumulator used to calculate the simulation step.
     */
    private float accumulator;

    /**
     * Asteroids that should be added in the next simulation step.
     */
    private List<AsteroidModel> asteroidsToAdd = new ArrayList<AsteroidModel>();

    /**
     * Time left until gun cools down
     */
    private float timeToNextShoot;

    /**
     * Creates a new GameController that controls the physics of a certain GameModel.
     *
     */
    private GameController() {
        world = new World(new Vector2(0, 0), true);

        shipBody = new ShipBody(world, GameModel.getInstance().getShip());

        List<AsteroidModel> asteroids = GameModel.getInstance().getAsteroids();
        for (AsteroidModel asteroid : asteroids)
        if (asteroid.getSize() == AsteroidModel.AsteroidSize.BIG)
            new BigAsteroidBody(world, asteroid);
        else if (asteroid.getSize() == AsteroidModel.AsteroidSize.MEDIUM)
            new MediumAsteroidBody(world, asteroid);

        world.setContactListener(this);
    }

    /**
     * Returns a singleton instance of a game controller
     *
     * @return the singleton instance
     */
    public static GameController getInstance() {
        if (instance == null)
            instance = new GameController();
        return instance;
    }

    /**
     * Calculates the next physics step of duration delta (in seconds).
     *
     * @param delta The size of this physics step in seconds.
     */
    public void update(float delta) {
        GameModel.getInstance().update(delta);

        timeToNextShoot -= delta;

        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        while (accumulator >= 1/60f) {
            world.step(1/60f, 6, 2);
            accumulator -= 1/60f;
        }

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            verifyBounds(body);
            ((EntityModel) body.getUserData()).setPosition(body.getPosition().x, body.getPosition().y);
            ((EntityModel) body.getUserData()).setRotation(body.getAngle());
        }
    }

    /**
     * Verifies if the body is inside the arena bounds and if not
     * wraps it around to the other side.
     *
     * @param body The body to be verified.
     */
    private void verifyBounds(Body body) {
        if (body.getPosition().x < 0)
            body.setTransform(ARENA_WIDTH, body.getPosition().y, body.getAngle());

        if (body.getPosition().y < 0)
            body.setTransform(body.getPosition().x, ARENA_HEIGHT, body.getAngle());

        if (body.getPosition().x > ARENA_WIDTH)
            body.setTransform(0, body.getPosition().y, body.getAngle());

        if (body.getPosition().y > ARENA_HEIGHT)
            body.setTransform(body.getPosition().x, 0, body.getAngle());
    }

    /**
     * Returns the world controlled by this controller. Needed for debugging purposes only.
     *
     * @return The world controlled by this controller.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Rotates the spaceship left. The rotation takes into consideration the
     * constant rotation speed and the delta for this simulation step.
     *
     * @param delta Duration of the rotation in seconds.
     */
    public void rotateLeft(float delta) {
        shipBody.setTransform(shipBody.getX(), shipBody.getY(), shipBody.getAngle() + ROTATION_SPEED * delta);
        shipBody.setAngularVelocity(0);
    }

    /**
     * Rotates the spaceship right. The rotation takes into consideration the
     * constant rotation speed and the delta for this simulation step.
     *
     * @param delta Duration of the rotation in seconds.
     */
    public void rotateRight(float delta) {
        shipBody.getX();

        shipBody.setTransform(shipBody.getX(), shipBody.getY(), shipBody.getAngle() - ROTATION_SPEED * delta);
        shipBody.setAngularVelocity(0);
    }

    /**
     * Acceleratesins the spaceship. The acceleration takes into consideration the
     * constant acceleration force and the delta for this simulation step.
     *
     * @param delta Duration of the rotation in seconds.
     */
    public void accelerate(float delta) {
        shipBody.applyForceToCenter(-(float) sin(shipBody.getAngle()) * ACCELERATION_FORCE * delta, (float) cos(shipBody.getAngle()) * ACCELERATION_FORCE * delta, true);
        ((ShipModel)shipBody.getUserData()).setAccelerating(true);
    }

    /**
     * Shoots a bullet from the spaceship at 10m/s
     */
    public void shoot() {
        if (timeToNextShoot < 0) {
            BulletModel bullet = GameModel.getInstance().createBullet(GameModel.getInstance().getShip());
            BulletBody body = new BulletBody(world, bullet);
            body.setLinearVelocity(BULLET_SPEED);
            timeToNextShoot = TIME_BETWEEN_SHOTS;
        }
    }

    /**
     * A contact between two objects was detected
     *
     * @param contact the detected contact
     */
    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (bodyA.getUserData() instanceof BulletModel)
            bulletCollision(bodyA);
        if (bodyB.getUserData() instanceof BulletModel)
            bulletCollision(bodyB);

        if (bodyA.getUserData() instanceof BulletModel && bodyB.getUserData() instanceof AsteroidModel)
            bulletAsteroidCollision(bodyA, bodyB);
        if (bodyA.getUserData() instanceof AsteroidModel && bodyB.getUserData() instanceof BulletModel)
            bulletAsteroidCollision(bodyB, bodyA);

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

    /**
     * A bullet colided with something. Lets remove it.
     *
     * @param bulletBody the bullet that colided
     */
    private void bulletCollision(Body bulletBody) {
        ((BulletModel)bulletBody.getUserData()).setFlaggedForRemoval(true);
    }

    /**
     * A bullet collided with an asteroid. Lets remove the asteroids and break into
     * pieces if needed.
     * @param bulletBody the bullet that collided
     * @param asteroidBody the asteroid that collided
     */
    private void bulletAsteroidCollision(Body bulletBody, Body asteroidBody) {
        AsteroidModel asteroidModel = (AsteroidModel) asteroidBody.getUserData();
        asteroidModel.setFlaggedForRemoval(true);

        if (asteroidModel.getSize() == AsteroidModel.AsteroidSize.BIG) {
            for (int i = 0; i < FRAGMENT_COUNT; i++)
                asteroidsToAdd.add(new AsteroidModel(asteroidModel.getX(), asteroidModel.getY(), (float) (asteroidModel.getRotation() * i * 2 * PI / 5), AsteroidModel.AsteroidSize.MEDIUM));
        }
    }

    /**
     * Creates the asteroids that have been added in the previous
     * simulation step.
     */
    public void createNewAsteroids() {
        for (AsteroidModel asteroidModel : asteroidsToAdd) {
            GameModel.getInstance().addAsteroid(asteroidModel);
            if (asteroidModel.getSize() == AsteroidModel.AsteroidSize.MEDIUM) {
                MediumAsteroidBody body = new MediumAsteroidBody(world, asteroidModel);
                body.setLinearVelocity((float) (Math.random() * 5));
            }
        }
        asteroidsToAdd.clear();
    }

    /**
     * Removes objects that have been flagged for removal on the
     * previous step.
     */
    public void removeFlagged() {
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            if (((EntityModel)body.getUserData()).isFlaggedToBeRemoved()) {
                GameModel.getInstance().remove((EntityModel) body.getUserData());
                world.destroyBody(body);
            }
        }
    }
}

