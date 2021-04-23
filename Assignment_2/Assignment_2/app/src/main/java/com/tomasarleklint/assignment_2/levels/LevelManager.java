package com.tomasarleklint.assignment_2.levels;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.util.SparseArray;

import com.tomasarleklint.assignment_2.Config;
import com.tomasarleklint.assignment_2.entities.Coin;
import com.tomasarleklint.assignment_2.entities.Entity;
import com.tomasarleklint.assignment_2.entities.Player;
import com.tomasarleklint.assignment_2.entities.Spear;
import com.tomasarleklint.assignment_2.entities.StaticEntity;
import com.tomasarleklint.assignment_2.utilities.BitmapPool;

import java.util.ArrayList;

public class LevelManager extends LevelData{
    public int _levelHeight = 0;
    public int _levelWidth = 0;
    public final ArrayList<Entity> _entities = new ArrayList<>();
    private final ArrayList<Entity> _entitiesToAdd = new ArrayList<>();
    public static final ArrayList<Entity> _entitiesToRemove = new ArrayList<>();
    public Player _player = null;
    private BitmapPool _pool = null;
    public int _coins_created = 0;
    private final SparseArray<String> _tileIdToSpriteName = new SparseArray<>();

    public LevelManager(final TypedArray levelData, final BitmapPool pool){
        _pool = pool;
        _tileIdToSpriteName.put(0, Config.NULLSPRITE);
        _tileIdToSpriteName.put(1, Config.PLAYER);
        _tileIdToSpriteName.put(2, "ground_square");
        _tileIdToSpriteName.put(3, "ground_round");
        _tileIdToSpriteName.put(4, "ground_left");
        _tileIdToSpriteName.put(5, "ground_left_corner");
        _tileIdToSpriteName.put(6, "ground_right");
        _tileIdToSpriteName.put(7, "ground_right_corner");
        _tileIdToSpriteName.put(8, Config.SPEAR);
        _tileIdToSpriteName.put(9, Config.COIN);
        _tileIdToSpriteName.put(10, "mud_square");
        loadLevel(levelData);
    }

    @SuppressLint("ResourceType")
    private void loadLevel(final TypedArray levelData){
        _levelHeight = levelData.getInt(0, 0);
        _levelWidth = levelData.getInt(1, 0);
        int[][] _tempTiles = new int[_levelHeight][_levelWidth];

        for(int i = 0; i < _levelHeight; i++){
            String[] spriteNumber = levelData.getString(i + 2).split(",");
            for(int j = 0; j < _levelWidth; j++){
                final int tileID = Integer.parseInt(spriteNumber[j]);
                _tempTiles[i][j] = tileID;
                if(tileID == Config.NO_TILE){ continue; }
                final String spriteName = getSpriteName(tileID);
                createEntity(spriteName, j, i);
            }
        }
        levelData.recycle();
        _tiles = _tempTiles;

    }

    private void checkCollisions(){
        final int count = _entities.size();
        Entity a, b;
        for(int i = 0; i < count-1; i++){
            a = _entities.get(i);
            for(int j = i+1; j < count; j++){
                b =_entities.get(j);
                if(a.isColliding(b)){
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }
    }

    public void update(final double dt) {
        for(Entity e : _entities){
            e.update(dt);
        }
        checkCollisions();
        addAndRemoveEntities();
    }

    private void createEntity(final String spriteName, final int xpos, final int ypos){
        Entity e = null;
        if(spriteName.equalsIgnoreCase(Config.PLAYER)) {
            e = new Player(spriteName, xpos, ypos);
            if(_player == null){
                _player = (Player) e;
            }
        }else if(spriteName.equalsIgnoreCase(Config.SPEAR)) {
            e = new Spear(spriteName, xpos, ypos);

        }else if(spriteName.equalsIgnoreCase(Config.COIN)){
            e = new Coin(spriteName, xpos, ypos);
            _coins_created++;
        }

        else{
            e = new StaticEntity(spriteName, xpos, ypos);
        }
        addEntity(e);
    }

    private void addAndRemoveEntities(){
        for(Entity e : _entitiesToRemove){
            _entities.remove(e);
        }
        _entities.addAll(_entitiesToAdd);
        _entitiesToRemove.clear();
        _entitiesToAdd.clear();
    }

    public void addEntity(final Entity e){
        if(e != null){ _entitiesToAdd.add(e); }
    }

    public static void removeEntity(final Entity e){
        if(e != null){ _entitiesToRemove.add(e); }
    }

    private void cleanup(){
        addAndRemoveEntities();
        for(Entity e : _entities){
            e.destroy();
        }
        _entities.clear();
        _player = null;
        _pool.empty();
        //TODO move construction of bitmappool into levelManager and somehow make avaialble to entities anyway
    }

    public void destroy(){
        cleanup();
    }

    @Override
    public String getSpriteName(int tileType) {
        final String fileName = _tileIdToSpriteName.get(tileType);
        if(fileName != null){
            return fileName;
        }
        return Config.NULLSPRITE;
    }
}
