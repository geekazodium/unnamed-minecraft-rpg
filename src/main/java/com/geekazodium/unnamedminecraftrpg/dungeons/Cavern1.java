package com.geekazodium.unnamedminecraftrpg.dungeons;

import com.geekazodium.unnamedminecraftrpg.util.AreaSelection;
import com.geekazodium.unnamedminecraftrpg.util.Grid2;
import com.geekazodium.unnamedminecraftrpg.util.Grid2.Coordinate2i;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;
import java.util.Random;

public class Cavern1 extends DungeonGenerator{
    public Cavern1(){
        allStates.addAll(
                List.of(new State(
                        "bend",
                        State.UP,
                        getConnectorId("door", true, false),
                        getConnectorId("door", true, false),
                        getConnectorId("none", true, false),
                        getConnectorId("none", true, false)
                        //new AreaSelection(Bukkit.getWorld("minecraft:overworld"),0,56,0,9,56,9)
                ).getAllRotations())
        );
        allStates.addAll(
                List.of(new State(
                        "end",
                        State.UP,
                        getConnectorId("door", true, false),
                        getConnectorId("none", true, false),
                        getConnectorId("none", true, false),
                        getConnectorId("none", true, false)
                        //new AreaSelection(Bukkit.getWorld("minecraft:overworld"),0,56,0,9,56,9)
                ).getAllRotations())
        );
        allStates.add(
                new State(
                        "line",
                        State.UP,
                        getConnectorId("door", true, false),
                        getConnectorId("none", true, false),
                        getConnectorId("door", true, false),
                        getConnectorId("none", true, false)
                        //new AreaSelection(Bukkit.getWorld("minecraft:overworld"),0,56,0,9,56,9)
                )
        );
        allStates.add(
                new State(
                        "line",
                        State.UP,
                        getConnectorId("none", true, false),
                        getConnectorId("door", true, false),
                        getConnectorId("none", true, false),
                        getConnectorId("door", true, false)
                        //new AreaSelection(Bukkit.getWorld("minecraft:overworld"),0,56,0,9,56,9)
                )
        );
        allStates.add(
                new State(
                        "x",
                        State.UP,
                        getConnectorId("door", true, false),
                        getConnectorId("door", true, false),
                        getConnectorId("door", true, false),
                        getConnectorId("door", true, false)
                        //new AreaSelection(Bukkit.getWorld("minecraft:overworld"),0,56,0,9,56,9)
                )
        );
        allStates.addAll(
                List.of(new State(
                        "3way",
                        State.UP,
                        getConnectorId("door", true, false),
                        getConnectorId("door", true, false),
                        getConnectorId("door", true, false),
                        getConnectorId("none", true, false)
                ).getAllRotations())
        );
    }
    @Override
    public void generate(Location location) {
        Grid2<Superposition> grid = generate();
        placeGrid(location,grid);
    }

    private void placeGrid(Location location, Grid2<Superposition> grid) {
        int blockX = (int) location.getX();
        int blockY = (int) location.getY();
        int blockZ = (int) location.getZ();
        for (int y = 0;y<grid.length;y++){
            for (int x = 0;x<grid.width;x++){
                Superposition superposition = grid.get(x,y);
                State state = superposition.possibleStates.get(0);
                AreaSelection selection = new AreaSelection(Bukkit.getWorld("minecraft:overworld"),0,56,0,9,56,9);//TODO add area copy code
            }
        }
    }

    public Grid2<Superposition> generate() {
        Random random = new Random();
        Grid2<Superposition> grid = new Grid2<>(5,5,new Superposition(allStates));
        while (canCollapseFurther(grid)){
            System.out.println(grid);
            Pair<Coordinate2i, Superposition> modified = collapseLowestEntropy(random,grid);
            propagateCollapse(grid,modified);
        }
        System.out.println(grid);
        return grid;
    }
}
