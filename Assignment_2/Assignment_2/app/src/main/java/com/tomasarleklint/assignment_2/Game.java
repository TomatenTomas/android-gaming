package com.tomasarleklint.assignment_2;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.tomasarleklint.assignment_2.entities.Entity;
import com.tomasarleklint.assignment_2.input.InputManager;
import com.tomasarleklint.assignment_2.levels.LevelManager;
import com.tomasarleklint.assignment_2.utilities.BitmapPool;

import java.util.ArrayList;

public class Game extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private static String TAG = ""; //TODO STRING

    private Thread _gameThread = null;
    private volatile boolean _isRunning = false;

    private SurfaceHolder _holder = null;
    private final Paint _paint = new Paint();
    private Canvas _canvas = null;
    private final Matrix _transform = new Matrix();

    private LevelManager _level = null;
    private InputManager _controls = new InputManager();
    private Viewport _camera = null;
    public final ArrayList<Entity> _visibleEntities = new ArrayList<>();
    public BitmapPool _pool = null;
    public Jukebox _jukebox = null;

    private TextView _tv_health;
    private TextView _tv_coins;
    private TextView _tv_victory;

    public boolean _invulnerable = false;
    public boolean _invulnframe = false;
    public int _player_health = Config.PLAYER_START_HEALTH;
    public int _coins_taken = 0;
    public boolean _game_over = false;
    private TypedArray _levelData;

    public Game(Context context) {
        super(context);
        init(context);
    }

    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Game(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Game(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        TAG = getResources().getString(R.string.TAG_GAME);
        final int actualHeight = getScreenHeight();
        final float ratio = (Config.TARGET_HEIGHT >= actualHeight) ? 1 : (float) Config.TARGET_HEIGHT / actualHeight;
        final int STAGE_WIDTH = (int) (ratio * getScreenWidth());
        final int STAGE_HEIGHT = Config.TARGET_HEIGHT;
        _camera = new Viewport(STAGE_WIDTH, STAGE_HEIGHT, Config.METERS_TO_SHOW_X, Config.METERS_TO_SHOW_Y);
        Entity._game = this;
        Log.d(TAG, _camera.toString());

        _jukebox = new Jukebox(context);
        _pool = new BitmapPool(this);
        _levelData = getResources().obtainTypedArray(R.array.level_1);
        _level = new LevelManager(_levelData, _pool);
        _holder = getHolder();
        _holder.addCallback(this);
        _holder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT);

        RectF bounds = new RectF(0, 0, _level._levelWidth, _level._levelHeight);
        _camera.setBounds(bounds);
    }

    public void setTextviews(final TextView health, final TextView coins, final TextView victory){
        _tv_health = health;
        _tv_coins = coins;
        _tv_victory = victory;
    }

    public InputManager getControls(){
        return _controls;
    }
    public void setControls(final InputManager controls){
        _controls.onPause();
        _controls.onStop();
        _controls = controls;
    }

    public float getWorldHeight() { return _level._levelHeight; }
    public float getWorldWidth() { return _level._levelWidth; }
    public int worldToScreenX(float worldDistance){
        return (int) (worldDistance * _camera.getPixelsPerMeterX());
    }
    public int worldToScreenY(float worldDistance){
        return (int) (worldDistance * _camera.getPixelsPerMeterY());
    }
    public float screenToWorldX(float pixelDistance){
        return (float) (pixelDistance / _camera.getPixelsPerMeterX());
    }
    public float screenToWorldY(float pixelDistance){
        return (float) (pixelDistance / _camera.getPixelsPerMeterY());
    }

    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    public static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void restart(){
        _levelData = getResources().obtainTypedArray(R.array.level_1);
        _player_health = Config.PLAYER_START_HEALTH;
        _coins_taken = 0;
        _game_over = false;
        _level = new LevelManager(_levelData, _pool);
    }

    @Override
    public void run() {
        long lastFrame = System.nanoTime();
        while(_isRunning) {
            final double deltaTime = (System.nanoTime() - lastFrame) * Config.NANOS_TO_SECONDS;
            lastFrame = System.nanoTime();
            update(deltaTime);
            buildVisibleSet();
            render(_camera, _visibleEntities);
            if(_game_over){
                restart();
            }
        }
    }

    private void update(final double dt) {
        _camera.lookAt(_level._player); //TODO: to not jank, set to % of distance between camera and , lookup easing, lurp, slurp
        _level.update(dt);
        _tv_health.setText(getResources().getString(R.string.HEALTH, _player_health));
        _tv_coins.setText(getResources().getString(R.string.COINS_FOUND, _coins_taken, _level._coins_created));
        if(_coins_taken == _level._coins_created){
            _tv_victory.setText(getResources().getString(R.string.GAME_DONE));
        }
    }

    private void buildVisibleSet(){
        _visibleEntities.clear();
        for(final Entity e : _level._entities){
            if(_camera.inView(e))
                _visibleEntities.add(e);
        }
    }

    private static final Point _position = new Point();
    private void render(final Viewport camera, final ArrayList<Entity> visibleEntities){
        if (!lockCanvas()){
            return;
        }
        try{
            _canvas.drawColor(Config.BG_COLOR);

            for(final Entity e : visibleEntities){
                _transform.reset();
                camera.worldToScreen(e, _position);
                _transform.postTranslate(_position.x, _position.y);
                e.render(_canvas, _transform, _paint);
            }
        }finally{
            _holder.unlockCanvasAndPost(_canvas);
        }
    }

    private boolean lockCanvas(){
        if (!_holder.getSurface().isValid()) {
            return false;
        }
        _canvas = _holder.lockCanvas();
        return (_canvas != null);
    }

    //Below here is executing on UI thread
    protected void onResume(){
        Log.d(TAG, getResources().getString(R.string.RESUME));
        _isRunning = true;
        _controls.onResume();
        _gameThread = new Thread(this);
        _jukebox.mp.start();
    }

    protected void onPause(){
        Log.d(TAG, getResources().getString(R.string.PAUSE));
        _isRunning = false;
        _controls.onPause();
        _jukebox.mp.pause();
        while(_gameThread.getState() != Thread.State.TERMINATED){
            try{
                _gameThread.join();
                return;
            }catch (InterruptedException e) {
                Log.d(TAG, Log.getStackTraceString(e.getCause()));
            }
        }
    }

    protected void onDestroy(){
        Log.d(TAG, getResources().getString(R.string.DESTROY));
        _gameThread = null;
        if(_level != null){
            _level.destroy();   //should empty the bitmap pool
            _level = null;
        }
        _controls = null;
        Entity._game = null;
        if(_pool != null) {
            _pool.empty(); //safe, but redundant, the LevelManager empties the pool as well
        }
        _jukebox.destroy();
        _holder.removeCallback(this);
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        Log.d(TAG, getResources().getString(R.string.SURF_CREATED));
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
        Log.d(TAG, getResources().getString(R.string.SURF_CHANGED));
        if(_gameThread != null && _isRunning) {
            Log.d(TAG, getResources().getString(R.string.GTHREAD_START));
            _gameThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {
        Log.d(TAG, getResources().getString(R.string.SURF_DESTROYED));
    }
}
