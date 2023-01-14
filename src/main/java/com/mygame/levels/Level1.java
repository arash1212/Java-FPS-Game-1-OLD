/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.levels;

import com.jme3.math.Vector3f;
import com.mygame.entity.interfaces.Actor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arash
 */
public class Level1 implements Level {

    //Level
    private static final String PATH_TO_SCENE = "Scenes/Level1.j3o";
    private final List<Actor> actors = new ArrayList();
    //player
    private static final Vector3f PLAYER_SPAWN_POINT = new Vector3f(0, 0, 0);

    public Level1() {
    }

    @Override
    public void load() {
        this.init();

        this.spawnPlayer();
    }

    @Override
    public void update() {
        for (Actor actor : actors) {
            actor.update();
        }
    }

    @Override
    public String getPathToScene() {
        return Level1.PATH_TO_SCENE;
    }

    @Override
    public List<Actor> getActors() {
        return this.actors;
    }

    @Override
    public Vector3f getPlayerSpawnPoint() {
        return Level1.PLAYER_SPAWN_POINT;
    }

}
