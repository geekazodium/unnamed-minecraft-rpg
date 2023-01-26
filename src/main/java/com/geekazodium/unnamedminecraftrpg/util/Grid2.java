package com.geekazodium.unnamedminecraftrpg.util;

import it.unimi.dsi.fastutil.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Grid2<T extends Grid2.CloneableValue<T>>{
    private final LinkedList<LinkedList<T>> grid = new LinkedList<>();
    private T defaultValue = null;
    public final int length;
    public final int width;

    public Grid2(int x, int y){
        if(y<0||x<0){
            throw new RuntimeException(new Exception("can't create grid of negative size"));
        }
        this.length = y;
        this.width = x;
    }

    public Grid2(int x, int y,T defaultValue) {
        this(x,y);
        this.defaultValue = defaultValue;
    }

    public void initAllValues(){//do not use unless absolutely necessary
        for(int y = 0;y<length;y++){
            for (int x = 0;x<width;x++){
                get(x,y);
            }
        }
    }

    public void set(int x,int y,T value){
        if(x>=width||x<0||y>=length||y<0){
            throw new RuntimeException(new IllegalAccessException("can't access value outside of grid"));
        }
        if(grid.size() <= y){
            for (int i = grid.size();i<=y;i++){
                grid.add(new LinkedList<>());
            }
        }
        set(x,grid.get(y),value);
    }

    private void set(int x,LinkedList<T> row,T value){
        if(row.size() <= x){
            for (int i = row.size();i<=x;i++){
                if(i==x){
                    row.add(value);
                }else {
                    row.add(defaultValue._clone());
                }
            }
        }else {
            row.set(x,value);
        }
    }

    public T get(int x,int y){
        if(x>=width||x<0||y>=length||y<0){
            throw new RuntimeException(new IllegalAccessException("can't access value outside of grid"));
        }
        if(grid.size() <= y){
            set(x,y, defaultValue._clone());
        }
        LinkedList<T> row = grid.get(y);
        if(row.size() <= x){
            set(x,y, defaultValue._clone());
        }
        return row.get(x);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Grid2@");
        stringBuilder.append(hashCode());
        stringBuilder.append("[\n");
        for (int y = 0;y < length;y++){
            for(int x = 0;x < width;x++){
                stringBuilder.append(get(x,y));
                if(x < width-1) {
                    stringBuilder.append(",");
                }
            }
            if(y < length-1){
                stringBuilder.append("\n");
            }else{
                stringBuilder.append("]");
            }
        }
        return stringBuilder.toString();
    }

    public List<T> allValues() {
        if(!allValuesExist()) {
            initAllValues();
        }
        List<T> list = new LinkedList<>();
        for (LinkedList<T> line:grid) {
            list.addAll(line);
        }
        return list;
    }

    public List<Pair<Coordinate2i,T>> allGridValues(){
        ArrayList<Pair<Coordinate2i,T>> list = new ArrayList<>();
        for(int y = 0;y<length;y++){
            for (int x = 0;x<width;x++){
                list.add(Pair.of(new Coordinate2i(x,y),get(x,y)));
            }
        }
        return list;
    }

    private boolean allValuesExist() {
        if(grid.size()<length){
            return false;
        }
        for (LinkedList<T> list : grid) {
            if(list.size()<width){
                return false;
            }
        }
        return true;
    }

    public Pair<Coordinate2i,T> get(Coordinate2i coordinate) {
        return Pair.of(coordinate,get(coordinate.x,coordinate.y));
    }

    public Pair<Coordinate2i,T> getOrNull(Coordinate2i coordinate) {
        if(coordinate.x>=width||coordinate.x<0||coordinate.y>=length||coordinate.y<0){
            return null;
        }
        return Pair.of(coordinate,get(coordinate.x,coordinate.y));
    }

    public Boolean isInGrid(Coordinate2i coordinate){
        return coordinate.x < width && coordinate.x >= 0 && coordinate.y < length && coordinate.y >= 0;
    }

    public interface CloneableValue<T>{
        T _clone();
    }

    public static class Coordinate2i{
        public int y;
        public int x;

        public Coordinate2i(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Coordinate2i c)){
                return false;
            }
            return c.x == x && c.y == y;
        }

        public Coordinate2i up() {
            return new Coordinate2i(x,y-1);
        }
        public Coordinate2i down() {
            return new Coordinate2i(x,y+1);
        }
        public Coordinate2i left() {
            return new Coordinate2i(x-1,y);
        }
        public Coordinate2i right() {
            return new Coordinate2i(x+1,y);
        }
    }
}
