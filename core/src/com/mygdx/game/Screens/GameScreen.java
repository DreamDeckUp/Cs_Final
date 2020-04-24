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
import com.badlogic.gdx.physics.box2d.CircleShape;
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
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ParallaxBackground;
import com.mygdx.game.Pipe;

public class GameScreen implements Screen, InputProcessor {
    SpriteBatch batch;
    Sprite sprite, topPipeSprite, groundSprite, cannonSprite;
    Texture img, groundImg, cannonSheet;
    Texture pipe;
    Sprite pipeSprite;
    Label title;
    Texture rocketSheet;

    World world;
    Body body;
    public static Body groundBody;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;

    int pipeX;
    float torque = 0.0f;
    boolean drawSprite = true;
    boolean drawDebug = false;
    boolean changedScreen;
    boolean rocketBoosting = false;
    boolean drawRocket =false;

    private Stage stage;
    Game game;
    private OrthographicCamera camera;

    final float PIXEL_TO_METERS = 100f;
    ParallaxBackground parallaxBackground;

    private static final int FRAME_COLS = 4, FRAME_ROWS = 1;

    Animation<TextureRegion> rocketAnimation, cannonAnimation;
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

        TextureRegion[][] tmp = TextureRegion.split(rocketSheet,
                        rocketSheet.getWidth() / FRAME_COLS,
                        rocketSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] rocketFrames = new TextureRegion[FRAME_ROWS*FRAME_COLS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                rocketFrames[index++] = tmp[i][j];
            }
        }

        rocketAnimation = new Animation<TextureRegion>(0.025f, rocketFrames);
        stateTime = 0f;
        sprite = new Sprite(rocketFrames[0]);
        TextureRegion[][] tmp2 = TextureRegion.split(cannonSheet,
                cannonSheet.getWidth()/2,
                cannonSheet.getHeight()/4);
        TextureRegion[] cannonFrames = new TextureRegion[9];
        index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                cannonFrames[index++] = tmp2[i][j];
            }
        }

        cannonAnimation = new Animation<TextureRegion>(0.025f, cannonFrames);
        cannonSprite = new Sprite(cannonFrames[0]);

        world = new World(new Vector2(0, -98f), true);
        world.setContactListener(new CollisionListener());

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(sprite.getX()+sprite.getWidth()/2, sprite.getY()+sprite.getHeight()/2);

        body = world.createBody(bodyDef);
        body.setGravityScale(0);
        body.setTransform(75,200,-45);
        body.setFixedRotation(true);

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
        //Rocket SHAPE
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, 68, new Vector2(sprite.getX(),sprite.getY()+34     ), 0);

        System.out.println(sprite.getHeight());
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.001f;

        body.createFixture(fixtureDef);


        shape.dispose();
        //GROUND SHAPE
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(groundSprite.getWidth()/2, groundSprite.getHeight()/2-15, new Vector2(0,groundSprite.getY()), 0);

        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;
        groundFixtureDef.density = 1f;

        groundBody.createFixture(groundFixtureDef);
        //groundBody.setLinearVelocity(new Vector2(-1000f,0));



        //ADDING INPUTS
        InputMultiplexer mult = new InputMultiplexer();
        mult.addProcessor(stage);
        mult.addProcessor(this);
        Gdx.input.setInputProcessor(mult);

        System.out.println(body.getMass());
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

        /*pipeX = (int)(groundBody.getPosition().x);
        int num = (int)(Gdx.graphics.getWidth()*2/pipeSprite.getWidth());
        for(int i = 0 ; i<num; i++){
            addPipe();
        }*/
        stage.addActor(backButton);
        stage.addActor(title);
    }
    public void addPipe(){
        float randomHeight1 = ((float)Math.random()*Gdx.graphics.getHeight()/5)-pipeSprite.getHeight()/6;
        float randomHeight2 = ((float)Math.random()*Gdx.graphics.getHeight()/5)-pipeSprite.getHeight()/6;

        Pipe pipeBottom = new Pipe(pipeSprite,new Vector2(pipeX,(int)randomHeight1));
        stage.addActor(pipeBottom);
        Pipe pipeTop = new Pipe(topPipeSprite,new Vector2(pipeX,Gdx.graphics.getHeight()-(int)randomHeight2));
        stage.addActor(pipeTop);
        pipeX+=pipeSprite.getWidth();
    }
    public void launch(Body body,Vector2 direction){
        drawRocket = true;
        body.setGravityScale(1);
        body.applyLinearImpulse(direction,Vector2.Zero, true);

    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
    }

    @Override
    public void render(float delta) {
        camera.update();

        world.step(1/60f, 6, 2);

        body.applyTorque(torque, true);

        sprite.setPosition(body.getPosition().x-sprite.getWidth()/2, body.getPosition().y-sprite.getHeight()/2);
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));
        title.setX(groundBody.getPosition().x);

        groundSprite.setPosition(groundBody.getPosition().x-groundSprite.getWidth()/2, groundBody.getPosition().y-groundSprite.getHeight()/2);
        groundSprite.setRotation((float)Math.toDegrees(groundBody.getAngle()));


        Gdx.gl.glClearColor(135/256f,206/256f,235/256f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(1, 1, 0);

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            stateTime+= Gdx.graphics.getDeltaTime();
            body.setLinearVelocity(body.getLinearVelocity().add(new Vector2(2000,2000)));
        }else{
            stateTime=0;
        }
        TextureRegion currentRocketFrame = rocketAnimation.getKeyFrame(stateTime,true);
        TextureRegion currentCannonFrame = cannonSprite;

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            //body.setFixedRotation(false);
            body.setAngularVelocity(-1f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            //body.setFixedRotation(false);
            body.setAngularVelocity(1f);
        }
        //parallaxBackground.setSpeed((int)body.getLinearVelocity().x);
        stage.act(1/60f);
        stage.draw();



        batch.begin();
        if (drawSprite) {
            batch.draw(groundSprite, groundSprite.getX(), groundSprite.getY(), groundSprite.getOriginX(), groundSprite.getOriginY(), groundSprite.getWidth(), groundSprite.getHeight(), groundSprite.getScaleX(), groundSprite.getScaleY(), groundSprite.getRotation());

        }
        if(drawRocket){
            batch.draw(currentRocketFrame, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
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
        if(keycode == Input.Keys.RIGHT)
            //body.applyForceToCenter(10000f, 0f, true);
        if(keycode == Input.Keys.LEFT)
            //body.applyForceToCenter(-10000f, 0f,true);

        if(keycode == Input.Keys.UP)
            body.applyForce(new Vector2(0,1000f), body.getPosition(),true);
            //2addPipe();
        if(keycode == Input.Keys.DOWN)
            body.applyForceToCenter(0f, -10f, true);

        // On brackets ( A, S ) apply torque, either clock or counterclockwise
        if(keycode == Input.Keys.A)
            torque += 0.1f;
        if(keycode == Input.Keys.S)
            torque -= 0.1f;
        if(keycode == Input.Keys.L)
            drawDebug=!drawDebug;

        // Remove the torque using backslash /
        if(keycode == Input.Keys.BACKSLASH)
            torque = 0.0f;

        // If user hits spacebar, reset everything back to normal
        if(keycode == Input.Keys.SPACE) {
            //body.setLinearVelocity(0f, 0f);
            //body.setAngularVelocity(0f);
            //torque = 0f;
            //sprite.setPosition(Gdx.graphics.getWidth()/2-sprite.getWidth()/2,Gdx.graphics.getHeight()/2-sprite.getHeight()/2);
            //body.setTransform((sprite.getX()+sprite.getWidth()/2),(sprite.getY()+sprite.getHeight()/2) ,0f);
            //addPipe();
            launch(body, new Vector2(1000000,1000000));
        }

        // The ESC key toggles the visibility of the sprite allow user to see physics debug info
        if(keycode == Input.Keys.ESCAPE)
            drawSprite = !drawSprite;
        if(keycode == Input.Keys.K){
            body.setLinearVelocity(new Vector2(1f,2f));
        }

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

