package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Actors.Background;
import com.mygdx.game.Actors.Cannon;
import com.mygdx.game.Actors.Ground;
import com.mygdx.game.Actors.Rocket;
import com.mygdx.game.Actors.UserInterface;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Stats;

import static com.mygdx.game.Constants.DEFAULT_ZOOM;
import static com.mygdx.game.Constants.GRAVITY;
import static com.mygdx.game.Constants.PPM;

public class GameScreen implements Screen, InputProcessor {
    //Images
    SpriteBatch batch;
    Texture groundTexture, cannonSheet,backgroundTexture,rocketSheet,boostIcon;

    //Actors
    Rocket rocket;
    Cannon cannon;
    Background background;
    Ground ground;
    UserInterface userInterface;

    World world;

    //Debug
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    boolean drawDebug = false;

    //State control
    boolean changedScreen;
    public static boolean rocketLaunched ;
    private Stage stage;
    public static Game game;
    public static OrthographicCamera camera;
    public static Viewport viewport;
    public boolean screenInit = false;

    public GameScreen(Game aGame) {
        //Variable initialisation
        game = aGame;
        stage = new Stage();
        changedScreen = false;
        batch = new SpriteBatch();
        rocketLaunched=false;
        viewport = new FitViewport(640/PPM, 480/PPM,camera);
        camera = (OrthographicCamera)stage.getViewport().getCamera();
        camera.zoom = DEFAULT_ZOOM;

        //World init
        world = new World(GRAVITY, true);
        world.setContactListener(new CollisionListener());
        Gdx.graphics.setVSync(true);

        //Textures init
        cannonSheet = new Texture(Gdx.files.internal("imgs/cannonSheet.png"));
        rocketSheet = new Texture(Gdx.files.internal("imgs/rocket.png"));
        backgroundTexture = new Texture(Gdx.files.internal("imgs/background.png"));
        groundTexture = new Texture("imgs/sand.png");
        boostIcon = new Texture(Gdx.files.internal("icons/motor_efficiency_icon.png"));


        //Actor init
        rocket = new Rocket(rocketSheet, world);
        ground = new Ground(groundTexture, world);
        cannon = new Cannon(cannonSheet,ground);
        background = new Background(backgroundTexture, ground);
        this.updateCam();
        userInterface= new UserInterface(boostIcon);

        screenInit = true;

        userInterface.launchButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                launch();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        //Adding actors
        stage.addActor(background);
        stage.addActor(cannon);
        stage.addActor(ground);
        stage.addActor(rocket);
        stage.addActor(userInterface);
        userInterface.addUI();
        Stats.currentFuel=(float)Stats.fuel;

        //ADDING INPUTS
        InputMultiplexer mult = new InputMultiplexer();
        mult.addProcessor(stage);
        mult.addProcessor(this);
        Gdx.input.setInputProcessor(mult);
        //debug
        debugRenderer = new Box2DDebugRenderer();

    }


    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
    }

    @Override
    public void render(float delta) {
        //Back coloration and gl handling
        Gdx.gl.glClearColor(255/256f,212/256f,167/256f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Camera
        updateCam();

        //World physics interpolation
        world.step(delta, 6, 2);

        //debug
        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(1, 1, 0);


        //Rocket boosting
        if(rocketLaunched) {
            //Cannon animation
            if(!cannon.cannonAnimation.isAnimationFinished(cannon.stateTime)){
                cannon.stateTime += Gdx.graphics.getDeltaTime();
            }
            if (userInterface.boostButton.isPressed()) {
                boost();
            } else {
                rocket.stateTime=0;
            }
        }
        buttonInput();
        userInterface.moveButtons();
        stage.act(delta);
        stage.draw();
        if(drawDebug){
            debugRenderer.render(world, debugMatrix);
        }

    }

    public void launch(){
        if(!rocketLaunched){
            Vector2 launchVec = new Vector2(1000000000,1000000000).scl((float) Stats.cannonStrength);
            launchVec.scl((float)(1/Stats.weight));
            rocket.launch(rocket.body, launchVec);
            rocketLaunched=true;
            userInterface.removeLaunchButton();
        }
    }
    public void boost(){
        if(Stats.currentFuel>0) {
            rocket.stateTime+=Gdx.graphics.getDeltaTime();
            float addedVel = 20000*(float)Stats.power;
            Vector2 pos = rocket.body.getPosition();
            Vector2 localDirectionVector = rocket.body.getLocalPoint(new Vector2(pos.x + rocket.sprite.getWidth() / 2, pos.y - rocket.sprite.getHeight() / 2));
            float angle = -1 * (float) Math.toRadians(localDirectionVector.angle());
            rocket.body.setLinearVelocity(rocket.body.getLinearVelocity().add(new Vector2(addedVel*(float)Math.cos(angle),addedVel* (float) Math.sin(angle))));
        }
        float consumption = (float)((Stats.fuel/60)/Stats.efficiency);
        Stats.currentFuel = (Stats.currentFuel-consumption);
        userInterface.updateFuelGauge();
    }
    public void buttonInput(){
        if(userInterface.right.isPressed()){
            rocket.body.setAngularVelocity(-1f);
        }
        if(userInterface.left.isPressed()){
            rocket.body.setAngularVelocity(1f);
        }
    }
    public void updateCam(){
        camera.position.set(rocket.body.getPosition().x, Gdx.graphics.getHeight()/2,0);
        camera.update();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        stage.dispose();
        debugRenderer.dispose();

    }

    public boolean keyDown(int keycode){
        return false;
    }
    public boolean keyUp(int keycode) {return false; }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public class CollisionListener implements ContactListener {
        @Override
        public void beginContact(Contact contact) {
            Body bodyA = contact.getFixtureA().getBody();
            Body bodyB = contact.getFixtureB().getBody();
            TextButton next = new TextButton("Continue", MyGdxGame.gameSkin);
            Label failLabel = new Label("CRASH!", MyGdxGame.gameSkin);
            failLabel.setFontScale(2f);

            ground.body.setLinearVelocity(Vector2.Zero);
            next.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.setScreen(new UpgradeScreen(game));
                    rocketLaunched=false;
                    screenInit=false;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                }
            });
            //Check if one body is kinematic, end game if true
            if (bodyA.getType() == BodyDef.BodyType.KinematicBody || bodyB.getType() == BodyDef.BodyType.KinematicBody){
                if(!changedScreen){
                    changedScreen=true;
                    failLabel.setPosition(rocket.body.getPosition().x,rocket.body.getPosition().y+failLabel.getHeight()+next.getHeight()+50);
                    next.setPosition(failLabel.getX()-(next.getWidth()-failLabel.getWidth())/4, failLabel.getY()-next.getHeight()-10);
                    rocket.body.setType(BodyDef.BodyType.StaticBody);
                    stage.addActor(next);
                    stage.addActor(failLabel);
                    MyGdxGame.setCurrency(MyGdxGame.getCurrency()+1);
                    userInterface.removeControls();
                }



            }

        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

    }


}

