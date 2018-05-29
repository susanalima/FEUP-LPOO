package com.gdx.game.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.gdx.game.BodyInstance;

public class MapView{

    private static MapView instance = null;

    private ColorAttribute ambientLigth;
    private Array<DirectionalLight> directLigths;
    private Array<BodyInstance> instances;
    private Environment environment;
    private ModelBatch modelBatch;

    private SpriteBatch spriteBatch;

    private int score;
    private String scoreText;
    BitmapFont scoreFont;

    private  Stage stage;
    private Label text;
    private Label.LabelStyle textStyle;

    private TextButton button;
    private TextButton.TextButtonStyle textButtonStyle;
    BitmapFont buttonFont;

    private FreeTypeFontGenerator generator;

    private MapView() {
        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();

        environment = new Environment();

        ambientLigth = new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,0.4f,0.4f,1f);

        directLigths = new Array<DirectionalLight>();
        directLigths.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));

        addLigthToEnvironment();

        instances = new Array<BodyInstance>();

        score = 0;
        scoreText = "score: 0";

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterScore = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterScore.size = 45;
        scoreFont = generator.generateFont(parameterScore);
        scoreFont.setUseIntegerPositions(false);

        /*****/
        stage = new Stage();
        stage = new Stage(new ExtendViewport(800, 1200));

        Gdx.input.setInputProcessor(stage);

        textStyle = new Label.LabelStyle();
        textStyle.font = scoreFont;

        text = new Label("score: 0",textStyle);
        text.setBounds(75,365f,100,1500);
        text.setFontScale(2f,2f);
        stage.addActor(text);

        FreeTypeFontGenerator.FreeTypeFontParameter parameterButton = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterButton.size = 45;
        buttonFont = generator.generateFont(parameterButton);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = buttonFont;
        button = new TextButton("EXIT", textButtonStyle);
        button.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("MAP Over");
                //notificar o mapController nao sei bem como e terminar/ir para outro menu

            }
        } );
        button.setBounds(50,-735f,100,1650/1.5f);
        button.setTransform(true);
        button.setScale(1.5f);
        stage.addActor(button);


        /****/

    }

    private void addLigthToEnvironment() {
        environment.set(ambientLigth);

        for (DirectionalLight dl : directLigths) {
            environment.add(dl);
        }
    }

   public void addInstance(PlainView pv) {
        instances.add(pv.getModelInstance());
    }

    public void addInstance(BallView bv) {
        instances.add(bv.getModelInstance());
    }

    public void addInstance(BonusView bv) {
        instances.add(bv.getModelInstance());
    }

    public void render(PerspectiveCamera camera) {
        clearScreen();
        scoreText = "score: " + score;
        modelBatch.begin(camera);

        modelBatch.render(instances, environment);
        modelBatch.end();

        spriteBatch.begin();

        stage.draw();
        //scoreFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
       // scoreFont.draw(spriteBatch, scoreText, 550, 450);
        text.setText(scoreText);
        spriteBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
        spriteBatch.dispose();
        stage.dispose();
        generator.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    public ColorAttribute getAmbientLigth() {
        return ambientLigth;
    }

    public Array<DirectionalLight> getDirectLigths() {
        return directLigths;
    }

    public Array<BodyInstance> getInstances() {
        return instances;
    }

    public ModelBatch getModelBatch() {
        return modelBatch;
    }

    public static MapView getInstance() {
        if(instance == null)
            instance = new MapView();
        return instance;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
