package com.geekazodium.unnamedminecraftrpg.dungeons;

import com.geekazodium.unnamedminecraftrpg.util.Grid2;
import com.geekazodium.unnamedminecraftrpg.util.Grid2.Coordinate2i;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.geekazodium.unnamedminecraftrpg.dungeons.DungeonGenerator.State.*;

public abstract class DungeonGenerator {
    protected List<State> allStates = new ArrayList<>();

    protected boolean canCollapseFurther(Grid2<Superposition> grid) {
        for (Superposition superposition:grid.allValues()){
            if(superposition.canCollapse()){
                return true;
            }
        }
        return false;
    }

    protected Pair<Coordinate2i, Superposition> collapseLowestEntropy(Random random, Grid2<Superposition> grid) {
        LinkedList<Pair<Coordinate2i,Superposition>> superpositions = getPositionsWithLowestEntropy(grid);
        Pair<Coordinate2i, Superposition> pair = superpositions.get(random.nextInt(superpositions.size()));
        pair.right().collapse(random);
        return pair;
    }

    protected void propagateCollapse(Grid2<Superposition> grid, Pair<Coordinate2i, Superposition> modified) {
        Coordinate2i startCoordinate = modified.left();
        LinkedList<Pair<Coordinate2i,Superposition>> queuedTiles = new LinkedList<>();
        queuedTiles.add(grid.getOrNull(startCoordinate.up()));
        queuedTiles.add(grid.getOrNull(startCoordinate.left()));
        queuedTiles.add(grid.getOrNull(startCoordinate.down()));
        queuedTiles.add(grid.getOrNull(startCoordinate.right()));
        ArrayList<Coordinate2i> checkedCoordinates = new ArrayList<>();
        checkedCoordinates.add(modified.left());
        while (queuedTiles.contains(null)) {
            queuedTiles.remove(null);
        }
        while (queuedTiles.size()>0){
            Pair<Coordinate2i,Superposition> tile = queuedTiles.pop();
            propagateToTile(tile,checkedCoordinates,queuedTiles,grid);
            while (queuedTiles.contains(null)) {
                queuedTiles.remove(null);
            }
        }
    }

    private void propagateToTile(Pair<Coordinate2i,Superposition> tile, ArrayList<Coordinate2i> checkedCoordinates, LinkedList<Pair<Coordinate2i, Superposition>> queuedTiles, Grid2<Superposition> grid){
        Superposition superposition = tile.right();
        Coordinate2i coordinate = tile.left();

        Coordinate2i up = coordinate.up();
        if(grid.isInGrid(up)) {
            Superposition s = grid.getOrNull(up).right();
            superposition.filterFromConstraints(s.constraints(DOWN), UP);
            if(!checkedCoordinates.contains(up)){
                Pair<Coordinate2i, Superposition> pair = grid.getOrNull(up);
                if(!queuedTiles.contains(pair)) {
                    queuedTiles.add(pair);
                }
            }
        }

        Coordinate2i left = coordinate.left();
        if(grid.isInGrid(left)) {
            Superposition s = grid.getOrNull(left).right();
            superposition.filterFromConstraints(s.constraints(RIGHT), LEFT);
            if(!checkedCoordinates.contains(left)){
                Pair<Coordinate2i, Superposition> pair = grid.getOrNull(left);
                if(!queuedTiles.contains(pair)) {
                    queuedTiles.add(pair);
                }
            }
        }

        Coordinate2i down = coordinate.down();
        if(grid.isInGrid(down)) {
            Superposition s = grid.getOrNull(down).right();
            superposition.filterFromConstraints(s.constraints(UP), DOWN);
            if(!checkedCoordinates.contains(down)){
                Pair<Coordinate2i, Superposition> pair = grid.getOrNull(down);
                if(!queuedTiles.contains(pair)) {
                    queuedTiles.add(pair);
                }
            }
        }

        Coordinate2i right = coordinate.right();
        if(grid.isInGrid(right)) {
            Superposition s = grid.getOrNull(right).right();
            superposition.filterFromConstraints(s.constraints(LEFT), RIGHT);
            if(!checkedCoordinates.contains(right)){
                Pair<Coordinate2i, Superposition> pair = grid.getOrNull(right);
                if(!queuedTiles.contains(pair)) {
                    queuedTiles.add(pair);
                }
            }
        }
        checkedCoordinates.add(coordinate);
    }

    protected LinkedList<Pair<Coordinate2i,Superposition>> getPositionsWithLowestEntropy(Grid2<Superposition> grid){
        int lowestEntropy = allStates.size();
        LinkedList<Pair<Coordinate2i,Superposition>> superpositions = new LinkedList<>();
        for (Pair<Coordinate2i,Superposition> pair:grid.allGridValues()) {
            Superposition superposition = pair.right();
            if(superposition.canCollapse()){
                if(superposition.entropy()==lowestEntropy){
                    superpositions.add(pair);
                }else if(superposition.entropy()<lowestEntropy){
                    lowestEntropy = superposition.entropy();
                    superpositions = new LinkedList<>();
                    superpositions.add(pair);
                }
            }
        }
        return superpositions;
    }

    public DungeonGenerator(){
        allStates.add(
                new State("empty",State.UP,
                        getConnectorId("none", true, false),
                        getConnectorId("none", true, false),
                        getConnectorId("none", true, false),
                        getConnectorId("none", true, false)
                )
        );
    }

    public abstract void generate(Location location);

    public static String getConnectorId(String name,boolean symmetric,boolean mirrored){
        return (symmetric?"s_":(mirrored?"m_":"nm"))+"_"+name;
    }

    public static String getOtherConnector(String name){
        if(name.charAt(0)=='n'){
            return name.replaceFirst("nm_","m__");
        }else if(name.charAt(0)=='m'){
            return name.replaceFirst("m__","nm_");
        }
        return name;
    }

    protected class Superposition implements Grid2.CloneableValue<Superposition>, Cloneable{
        public List<State> possibleStates;
        public Superposition(List<State> allStates){
            possibleStates = new ArrayList<>(allStates);
        }

        public boolean canCollapse() {
            return possibleStates.size()>1;
        }

        public int entropy(){
            return possibleStates.size();
        }

        public void collapse(Random random) {
            State state = possibleStates.get(random.nextInt(possibleStates.size()));
            possibleStates.clear();
            possibleStates.add(state);
        }
        @Override
        public Superposition _clone() {
            return new Superposition(possibleStates);
        }

        @Override
        public String toString() {
            if(possibleStates.size() == 1){
                return possibleStates.get(0).toString();
            }
            return "{ "+possibleStates.size()+" }";
            /*return "Superposition@"+hashCode()+"[" +
                    "possibleStates=" + possibleStates.size() +
                    ']';*/
        }

        public List<String> constraints(int d) {
            List<String> constraints = new ArrayList<>();
            for (State state:possibleStates){
                String otherConnector = getOtherConnector(state.directions.get(d));
                if(!constraints.contains(otherConnector)) {
                    constraints.add(otherConnector);
                }
            }
            return constraints;
        }

        public void filterFromConstraints(List<String> constraints,int d) {
            if(possibleStates.size() == 1){
                return;
            }
            List<State> states = new ArrayList<>();
            for (State state:possibleStates) {
                if(constraints.contains(state.directions.get(d))){
                    states.add(state);
                }
            }
            possibleStates = states;
        }
    }

    protected class State{
        public final String id;
        public final int rotation;
        public final String up;
        public final String left;
        public final String down;
        public final String right;

        public final LinkedList<String> directions;

        public static final int UP = 0;
        public static final int LEFT = 1;
        public static final int DOWN = 2;
        public static final int RIGHT = 3;

        protected State(String id, int rotation, String up, String left, String down, String right) {
            this.id = id;
            this.rotation = rotation%4;
            this.up = up;
            this.left = left;
            this.down = down;
            this.right = right;
            this.directions = new LinkedList<>();
            this.directions.add(up);
            this.directions.add(left);
            this.directions.add(down);
            this.directions.add(right);
        }


        public State[] getAllRotations(){
            return new State[]{
                    new State(id,rotation,up,left,down,right),
                    new State(id,rotation+1,left,down,right,up),
                    new State(id,rotation+2,down,right,up,left),
                    new State(id,rotation+3,right,up,left,down)
            };
        }

        @Override
        public String toString() {
            return "{" + id +"_"+ rotation+"}";
        }
    }
}
