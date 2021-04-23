package com.tomasarleklint.assignment_2.levels;

public abstract class LevelData {
    int[][] _tiles;
    int _height = 0;
    int _width = 0;

    public int getTile(final int x, final int y){
        return _tiles[y][x];
    }

    int[] getRow(final int y){
        return _tiles[y];
    }

    void updateLevelDimensions(){
        _height = _tiles.length;
        _width = _tiles[0].length;
    }

    abstract public String getSpriteName(final int tileType);

}
