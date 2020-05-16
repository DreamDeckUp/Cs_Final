package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Actors.Rocket;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Actors.ParallaxBackground;

public class GameScreen implements Screen, InputProcessor {
    SpriteBatch batch;
    Sprite sprite, topPipeSprite, groundSprite, cannonSprite;
    Texture img, groundImg, cannonSheet;
    Texture pipe;
    Sprite pipeSprite;
    Label title;
    Texture rocketSheet;

    World world;
    public static Body groundBody;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;

    float torque = 0.0f;
    public static boolean drawSprite = true;
    boolean drawDebug = false;
    boolean changedScreen;


    private Stage stage;
    Game game;
    private OrthographicCamera camera;

    final float PIXEL_TO_METERS = 100f;
    ParallaxBackground parallaxBackground;



    Animation<TextureRegion> cannonAnimation;
    float stateTime;


    public GameScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        camera = (OrthographicCamera)stage.getViewport().getCamera();
        changedScreen=false;
        

        //PHYSICS
        batch = new SpriteBatch();
        img = new Texture("imgs/rocket.png");
        pipe = new Texture(Gdx.files.internal("imgs/pipe.png"));
        cannonSheet = new Texture(Gdx.files.internal("imgs/cannonSheet.png"));

        pipeSprite = new Sprite(pipe);
        topPipeSprite = new Sprite(pipe);
        topPipeSprite.flip(false,true);


        rocketSheet = new Texture(Gdx.files.internal("imgs/rocket.png"));




        TextureRegion[][] tmp2 = TextureRegion.split(cannonSheet,
                cannonSheet.getWidth()/2,
                cannonSheet.getHeight()/4);
        TextureRegion[] cannonFrames = new TextureRegion[9];
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                cannonFrames[index++] = tmp2[i][j];
            }
        }

        cannonAnimation = new Animation<TextureRegion>(0.025f, cannonFrames);
        cannonSprite = new Sprite(cannonFrames[0]);

        world = new World(new Vector2(0, -98f), true);
        world.setContactListener(new CollisionListener());

        Rocket rocket = new Rocket(rocketSheet, world);

        //GROUND BODY
        groundImg = new Texture("imgs/grass.png");
        groundImg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(groundImg);
        textureRegion.setRegion(10,0,groundImg.getWidth()*100000,groundImg.getHeight());
        groundSprite = new Sprite(textureRegion);
        cannonSprite.setPosition(groundSprite.getHeight(),cannonFrames[0].getRegionWidth());

        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.KinematicBody;
        groundDef.position.set(0,groundSprite.getHeight()/2);

        groundBody = world.createBody(groundDef);


        //GROUND SHAPE
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(groundSprite.getWidth()/2, groundSprite.getHeight()/2-15, new Vector2(0,groundSprite.getY()), 0);

        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;
        groundFixtureDef.density = 1f;

        groundBody.createFixture(groundFixtureDef);



        //ADDING INPUTS
        InputMultiplexer mult = new InputMultiplexer();
        mult.addProcessor(stage);
        mult.addProcessor(this);
        Gdx.input.setInputProcessor(mult);


        debugRenderer = new Box2DDebugRenderer();
        //REST IS IN RENDER

        //ADDING PARALLAX BACKGROUND
        Array<Texture> textures = new Array<>();
        for(int i = 2; i <=6;i++){
            textures.add(new Texture(Gdx.files.internal("parallax/img"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(1);

        stage.addActor(parallaxBackground);
        stage.addActor(rocket);

        //ADDING TITLE
        title = new Label("Welcome to AI launcher", MyGdxGame.gameSkin);
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight()*2/3);
        title.setWidth(Gdx.graphics.getWidth());


        //ADDING BACK BUTTON
        TextButton backButton = new TextButton("Back",MyGdxGame.gameSkin);
        backButton.setWidth(Gdx.graphics.getWidth()/5);
        backButton.setPosition(Gdx.graphics.getWidth()-(backButton.getWidth()+10),Gdx.graphics.getHeight()-(backButton.getHeight()-20));
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(backButton);
        stage.addActor(title);
    }


    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
    }

    @Override
    public void render(float delta) {
        camera.update();

        world.step(1/60f, 6, 2);




        title.setX(groundBody.getPosition().x);

        groundSprite.setPosition(groundBody.getPosition().x-groundSprite.getWidth()/2, groundBody.getPosition().y-groundSprite.getHeight()/2);
        groundSprite.setRotation((float)Math.toDegrees(groundBody.getAngle()));


        Gdx.gl.glClearColor(135/256f,206/256f,235/256f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(1, 1, 0);

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            Rocket.stateTime+= Gdx.graphics.getDeltaTime();
            Rocket.body.setLinearVelocity(Rocket.body.getLinearVelocity().add(new Vector2(2000,2000)));
        }else{
            stateTime=0;
        }

        TextureRegion currentCannonFrame = cannonSprite;

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            //body.setFixedRotation(false);
            Rocket.body.setAngularVelocity(-1f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            //body.setFixedRotation(false);
            Rocket.body.setAngularVelocity(1f);
        }
        //parallaxBackground.setSpeed((int)body.getLinearVelocity().x);
        stage.act(1/60f);
        stage.draw();



        batch.begin();
        if (drawSprite) {
            batch.draw(groundSprite, groundSprite.getX(), groundSprite.getY(), groundSprite.getOriginX(), groundSprite.getOriginY(), groundSprite.getWidth(), groundSprite.getHeight(), groundSprite.getScaleX(), groundSprite.getScaleY(), groundSprite.getRotation());

        }

        batch.draw(currentCannonFrame, cannonSprite.getX(), cannonSprite.getY(), cannonSprite.getOriginX(), cannonSprite.getOriginY(), cannonSprite.getWidth(), cannonSprite.getHeight(), cannonSprite.getScaleX(), cannonSprite.getScaleY(), cannonSprite.getRotation());

        batch.end();
        if(drawDebug){
            debugRenderer.render(world, debugMatrix);
        }

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
        img.dispose();
        world.dispose();
        stage.dispose();

    }

    public boolean keyDown(int keycode){
        return false;
    }
    public boolean keyUp(int keycode){

        // Remove the torque using backslash /
        if(keycode == Input.Keys.BACKSLASH)
            torque = 0.0f;


        if(keycode == Input.Keys.SPACE) {
            Rocket.launch(Rocket.body, new Vector2(1000000,1000000));
        }

        // The ESC key toggles the visibility of the sprite allow user to see physics debug info
        if(keycode == Input.Keys.ESCAPE)
            drawSprite = !drawSprite;


        return true;
    }
    public boolean keyTyped(char character) {
        return false;
    }


    // On touch we apply force from the direction of the users touch.
    // This could result in the object "spinning"
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //body.applyForceToCenter(0f,0f,true);
        //body.applyTorque(0.4f,true);
        //game.setScreen(new TitleScreen(game));
        return true;

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
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.zoom+=amount;
        return true;
    }

    public class CollisionListener implements ContactListener {
        @Override
        public void beginContact(Contact contact) {
            Body bodyA = contact.getFixtureA().getBody();
            Body bodyB = contact.getFixtureB().getBody();
            TextButton next = new TextButton("Continue", MyGdxGame.gameSkin);
            groundBody.setLinearVelocity(Vector2.Zero);
            next.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.setScreen(new UpgradeScreen(game));

                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                }
            });
            if (bodyA.getType() == BodyDef.BodyType.KinematicBody || bodyB.getType() == BodyDef.BodyType.KinematicBody){
                //Window fail = new Window("You failed", MyGdxGame.gameSkin);
                if(!changedScreen){
                    changedScreen=true;
                    Table table = new Table();
                    table.setFillParent(true);
                    table.setSkin(MyGdxGame.gameSkin);
                    table.center().center();
                    Label failLabel = new Label("Lol you died", MyGdxGame.gameSkin);
                    failLabel.setFontScale(2f);
                    table.add(failLabel).center();
                    table.row();
                    table.add(next).center();
                    table.setDebug(drawDebug);
                    stage.addActor(table);

                }
                System.out.println(changedScreen);



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

