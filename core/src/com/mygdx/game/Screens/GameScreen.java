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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ParallaxBackground;
import com.mygdx.game.Pipe;

public class GameScreen implements Screen, InputProcessor {
    SpriteBatch batch;
    Sprite sprite, topPipeSprite, groundSprite;

    Texture img, groundImg;
    World world;
    Body body;
    public static Body groundBody;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    Texture pipe;
    Sprite pipeSprite;
    int pipeX;

    float torque = 0.0f;
    boolean drawSprite = true;
    boolean drawDebug = true;

    private Stage stage;
    private Game game;
    private OrthographicCamera camera;

    final float PIXEL_TO_METERS = 100f;
    ParallaxBackground parallaxBackground;

    public GameScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        camera = (OrthographicCamera)stage.getViewport().getCamera();

        //PHYSICS
        batch = new SpriteBatch();
        img = new Texture("pixthulhu/raw/touchpad-knob.png");
        pipe = new Texture(Gdx.files.internal("imgs/pipe.png"));

        pipeSprite = new Sprite(pipe);
        topPipeSprite = new Sprite(pipe);
        topPipeSprite.flip(false,true);
        sprite = new Sprite(img);

        sprite.setPosition(Gdx.graphics.getWidth()/2-sprite.getWidth()/2, Gdx.graphics.getHeight()/2-sprite.getHeight()/2);

        world = new World(new Vector2(0, -9.8f), true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(sprite.getX()+sprite.getWidth()/2, sprite.getY()+sprite.getHeight()/2);

        body = world.createBody(bodyDef);
        //GROUND BODY
        groundImg = new Texture("imgs/grass.png");
        groundImg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(groundImg);
        textureRegion.setRegion(10,0,groundImg.getWidth()*100000,groundImg.getHeight());
        groundSprite = new Sprite(textureRegion);

        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.DynamicBody;
        groundDef.position.set(0,0);

        groundBody = world.createBody(groundDef);
        //BALL SHAPE
        CircleShape shape = new CircleShape();
        shape.setRadius(sprite.getWidth()/2+3);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0001f;

        body.createFixture(fixtureDef);

        shape.dispose();
        //GROUND SHAPE
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(groundSprite.getWidth()/2, groundSprite.getHeight()/2-2, new Vector2(0,groundSprite.getY()), 0);

        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;
        groundFixtureDef.density = 1f;

        groundBody.createFixture(groundFixtureDef);
        groundBody.setGravityScale(0);



        //ADDING INPUTS
        InputMultiplexer mult = new InputMultiplexer();
        mult.addProcessor(stage);
        mult.addProcessor(this);
        Gdx.input.setInputProcessor(mult);

        System.out.println(body.getMass());
        debugRenderer = new Box2DDebugRenderer();
        //REST IS IN RENDER

        //ADDING PARALLAX BACKGROUND
        Array<Texture> textures = new Array<Texture>();
        for(int i = 2; i <=6;i++){
            textures.add(new Texture(Gdx.files.internal("parallax/img"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(1);

        stage.addActor(parallaxBackground);

        //ADDING TITLE
        Label title = new Label("Playing Screen", MyGdxGame.gameSkin);
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

        pipeX = (int)(groundBody.getPosition().x);
        int num = (int)(Gdx.graphics.getWidth()*2/pipeSprite.getWidth());
        for(int i = 0 ; i<num; i++){
            addPipe();
        }
        stage.addActor(backButton);
        stage.addActor(title);
    }
    public void addPipe(){
        float randomHeight = ((float)Math.random()*sprite.getHeight()*3/2)-pipeSprite.getHeight()/6;
        Pipe pipeBottom = new Pipe(pipeSprite,new Vector2(pipeX,(int)randomHeight));
        stage.addActor(pipeBottom);
        Pipe pipeTop = new Pipe(topPipeSprite,new Vector2(pipeX,Gdx.graphics.getHeight()-(int)randomHeight));
        stage.addActor(pipeTop);
        pipeX+=pipeSprite.getWidth();
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

        groundSprite.setPosition(groundBody.getPosition().x-groundSprite.getWidth()/2, groundBody.getPosition().y-groundSprite.getHeight()/2);
        groundSprite.setRotation((float)Math.toDegrees(groundBody.getAngle()));

        groundBody.setLinearVelocity(body.linVelLoc.scl(-1f));

        Gdx.gl.glClearColor(135/256f,206/256f,235/256f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(1, 1, 0);

        parallaxBackground.setSpeed((int)body.getLinearVelocity().x);
        stage.act();
        stage.draw();



        batch.begin();
        if (drawSprite) {
            batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
            batch.draw(groundSprite, groundSprite.getX(), groundSprite.getY(), groundSprite.getOriginX(), groundSprite.getOriginY(), groundSprite.getWidth(), groundSprite.getHeight(), groundSprite.getScaleX(), groundSprite.getScaleY(), groundSprite.getRotation());

        }
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
            body.applyForceToCenter(10000f, 0f, true);
        if(keycode == Input.Keys.LEFT)
            body.applyForceToCenter(-10000f, 0f,true);

        if(keycode == Input.Keys.UP)
            body.applyForceToCenter(0f,10f,true);
            addPipe();
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
            body.setLinearVelocity(0f, 0f);
            body.setAngularVelocity(0f);
            torque = 0f;
            sprite.setPosition(Gdx.graphics.getWidth()/2-sprite.getWidth()/2,Gdx.graphics.getHeight()/2-sprite.getHeight()/2);
            body.setTransform((sprite.getX()+sprite.getWidth()/2),(sprite.getY()+sprite.getHeight()/2) ,0f);
            addPipe();
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


}

