package com.tomasarleklint.assignment_3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.tomasarleklint.assignment_3.entities.Asteroid;
import com.tomasarleklint.assignment_3.entities.Border;
import com.tomasarleklint.assignment_3.entities.Bullet;
import com.tomasarleklint.assignment_3.entities.Flame;
import com.tomasarleklint.assignment_3.entities.GLEntity;
import com.tomasarleklint.assignment_3.entities.Player;
import com.tomasarleklint.assignment_3.entities.Star;
import com.tomasarleklint.assignment_3.graphics.GLManager;
import com.tomasarleklint.assignment_3.input.InputManager;
import com.tomasarleklint.assignment_3.utilities.Utils;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Game extends GLSurfaceView implements GLSurfaceView.Renderer {
    Bullet[] _bullets = new Bullet[Config.BULLET_COUNT];

    private ArrayList<Star> _stars = new ArrayList<>();
    private ArrayList<Asteroid> _asteroids = new ArrayList<>();
    private ArrayList<Asteroid> _temp_asteroids = new ArrayList<>();
    public InputManager _inputs = new InputManager(); //empty but valid default

    private Border _border = null;
    public Player _player = null;
    private Flame _flame = null;
    private double _fpsPreviousTime = 0;
    private int _tempFps = 0;
    private Camera _camera = null;
    private HUD _HUD = null;
    private boolean _boostActive = false;
    private boolean _fire = false;
    private double _fireOldTime = 0f;
    private double _fireCurrentTime = 0f;
    public int _fps = 0;
    public int _level = 1;
    public int _score = 0;
    public int _health = Config.STARTING_HEALTH;
    public boolean _gameOver = false;
    public boolean _victory = false;

    public float _width = Config.WORLD_WIDTH;
    public float _height = 0;

    public Game(Context context) {
        super(context);
        init();
    }
    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        GLEntity._game = this;
        HUD._game = this;
        final float ratio = (float)getScreenHeight()/getScreenWidth();
        _height = Math.round(_width*ratio);

        setEGLContextClientVersion(2); //select OpenGL ES 2.0
        setPreserveEGLContextOnPause(true); //context *may* be preserved and thus *may* avoid slow reloads when switching apps.
        // we always re-create the OpenGL context in onSurfaceCreated, so we're safe either way.

        for(int i = 0; i < Config.BULLET_COUNT; i++){
            _bullets[i] = new Bullet();
        }

        setRenderer(this);
        _fpsPreviousTime = System.nanoTime()*Config.NANOSECONDS_TO_SECONDS;
    }

    public void setControls(final InputManager input){
        _inputs = input;
    }

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig eglConfig) {
        float red = Color.red(Config.BG_COLOR) / 255f;
        float green = Color.green(Config.BG_COLOR) / 255f;
        float blue = Color.blue(Config.BG_COLOR) / 255f;
        float alpha = 1f;
        GLManager.buildProgram(getContext()); //compile, link and upload our GL program
        GLES20.glClearColor(red, green, blue, alpha);

        // center the player in the world.
        _player = new Player(_width/2, _height/2);
        _flame = new Flame(_width/2, _height/2);

        //Keep the Border inside the game view
        _border = new Border(_width/2, _height/2, _width, _height);

        _camera = new Camera(_width, _height);
        _HUD = new HUD();

        //Stars
        Random r = new Random();
        for(int i = 0; i < Config.STAR_COUNT; i++){
            _stars.add(new Star(r.nextInt((int)_width), r.nextInt((int)_height)));
        }

        //Asteroids
        for(int i = 0; i < Config.ASTEROID_COUNT; i++){
            int asteroid_points = (int)Utils.between(Config.ASTEROID_POINTS_MIN, Config.ASTEROID_POINTS_MAX);
            _asteroids.add(new Asteroid(r.nextInt((int)_width), r.nextInt((int)_height), asteroid_points, Config.ASTEROID_LARGE));
        }
    }

    @Override
    public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(final GL10 unused) {
        update();
        render();
    }

    //trying a fixed time-step with accumulator, courtesy of
    //   https://gafferongames.com/post/fix_your_timestep/
    final double dt = 0.01;
    double accumulator = 0.0;
    double currentTime = System.nanoTime()*Config.NANOSECONDS_TO_SECONDS;
    private void update(){
        final double newTime = System.nanoTime()*Config.NANOSECONDS_TO_SECONDS;
        final double frameTime = newTime - currentTime;
        currentTime = newTime;
        accumulator += frameTime;

        while(accumulator >= dt){
            for(final Asteroid a : _asteroids){
                a.update(dt);
            }
            for(final Bullet b : _bullets) {
                if (b.isDead()) { continue; } //skip
                b.update(dt);
            }

            collisionDetection();
            updateAsteroidsList();
            removeDeadEntities();

            if(!_gameOver){
                _player.update(dt);
            }
            checkVictory();
            accumulator -= dt;
        }
    }

    private void render(){
        getFps();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); //clear buffer to background color

        final float[] viewportMatrix = _camera.getViewportMatrix();

        _border.render(viewportMatrix);
        for(final Asteroid a : _asteroids){
            a.render(viewportMatrix);
        }
        for(final Star s : _stars){
            s.render(viewportMatrix);
        }
        if(!_gameOver){
            _player.render(viewportMatrix);
            _flame.render(viewportMatrix);
        }
        _HUD.render(viewportMatrix);

        for(final Bullet b : _bullets){
            if(b.isDead()){ continue; } //skip
            b.render(viewportMatrix);
        }
    }

    public void getFps(){
        final double newTime = System.nanoTime()*Config.NANOSECONDS_TO_SECONDS;
        if (newTime >= _fpsPreviousTime + 1){
            _fps = _tempFps;
            _tempFps = 0;
            _fpsPreviousTime = newTime;
        } else {
            _tempFps++;
        }
    }

    public boolean maybeFireBullet(final GLEntity source){
        for(final Bullet b : _bullets) {
            if(b.isDead()) {
                b.fireFrom(source);
                return true;
            }
        }
        return false;
    }

    public void attachFlame(final GLEntity source){
        _flame.attachToSource(source);
    }

    public boolean maybeFireBoost(){
        if(!_boostActive){
            if(_inputs._pressingB){
                _fire = !_fire;
                _boostActive = true;
                _fireOldTime = System.nanoTime()*Config.NANOSECONDS_TO_SECONDS;
            }
        }else{
            if(!_inputs._pressingB){
                _boostActive = false;
                _fire = false;
            }else{
                _fireCurrentTime = System.nanoTime()*Config.NANOSECONDS_TO_SECONDS;
                if((_fireCurrentTime-_fireOldTime) > Config.FLAME_RENDER_TIMER){
                    _fireOldTime = _fireCurrentTime;
                    _fire = !_fire;
                }
            }
        }
        return _fire;
    }

    private void collisionDetection(){
        for(final Asteroid a : _asteroids) {
            if(a.isDead()){continue;}
            if(_player.isColliding(a)){
                _player.onCollision(a);
                a.onCollision(false);
            }
            for(final Bullet b : _bullets){
                if(b.isDead()){ continue; } //skip dead bullets
                if(b.isColliding(a)){
                    b.onCollision(a);
                    a.onCollision(true);
                    _score += 100;
                }
            }
        }
    }

    public void updateAsteroidsList(){
        for(final Asteroid a : _asteroids){
            if(a.isDead() && a._size > 0){
                int asteroid_points = (int)Utils.between(Config.ASTEROID_POINTS_MIN, Config.ASTEROID_POINTS_MAX);
                _temp_asteroids.add(new Asteroid(a.centerX(), a.centerY(), asteroid_points, a._size));
                _temp_asteroids.add(new Asteroid(a.centerX(), a.centerY(), asteroid_points, a._size));
                a._size --;
            }
        }
        _asteroids.addAll(_temp_asteroids);
        _temp_asteroids.clear();
    }

    public void removeDeadEntities(){
        Asteroid temp;
        final int count = _asteroids.size();
        for(int i = count-1; i >= 0; i--){
            temp = _asteroids.get(i);
            if(temp.isDead()){
                _asteroids.remove(i);
            }
        }
    }

    private void checkVictory(){
        if (_asteroids.size() == 0){
            _victory = true;
        }
    }

    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
