/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.ai.zombie;

import com.jme3.ai.navmesh.NavMesh;
import com.jme3.ai.navmesh.NavMeshPathfinder;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.mygame.entity.interfaces.AIControllable;
import com.mygame.entity.interfaces.Actor;
import com.mygame.entity.interfaces.EnumActorState;
import com.mygame.settings.GeneralConstants;
import com.mygame.settings.Managers;

/**
 *
 * @author Arash
 */
public class ZombieNormal extends Node implements AIControllable {

    //constants
    private static final float HEIGHT = 1.8f;

    //Health
    private float health = 100;
    private Actor attacker;

    //Managers
    private final AssetManager assetManager;
    private final BulletAppState bullAppState;
    private final Node shootables;

    //Navigation
    private CharacterControl control;
    private NavMeshPathfinder pathfinder;
    private Vector3f targetPosition = new Vector3f(0, 0, 0);

    private EnumActorState state = EnumActorState.STAND_STILL;

    public ZombieNormal() {
        this.assetManager = Managers.getInstance().getAsseManager();
        this.bullAppState = Managers.getInstance().getBulletAppState();
        this.shootables = Managers.getInstance().getShooteables();
    }

    private void init() {
        Box box = new Box(1, HEIGHT, 1);
        Geometry geom = new Geometry("ZombieNormal", box);

        Material mat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        geom.setMaterial(mat);

        CapsuleCollisionShape capsule = new CapsuleCollisionShape(1.3f, HEIGHT, 1);
        this.control = new CharacterControl(capsule, 0.01f);

        this.bullAppState.getPhysicsSpace().add(control);
        this.shootables.attachChild(this);
        this.attachChild(geom);
        this.addControl(control);

    }

    @Override
    public void spawn(Vector3f spawnPoint) {
        this.init();

        this.control.setPhysicsLocation(spawnPoint);
    }

    @Override
    public void update() {

        this.navigateTo(Managers.getInstance().getPlayer().getPosition());

        if (this.pathfinder == null && Managers.getInstance().getCurrentlyLoadedLevel() != null) {
            this.initNavMesh();
            this.targetPosition.set(Managers.getInstance().getPlayer().getPosition());
        }

        this.die();
    }

    @Override
    public EnumActorState getState() {
        return this.state;
    }

    @Override
    public void setState(EnumActorState state) {
        this.state = state;
    }

    @Override
    public CharacterControl getControl() {
        return this.control;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    /**
     * ********************************Navigation*****************************************
     */
    @Override
    public void initNavMesh() {
        Spatial level = this.assetManager.loadModel(Managers.getInstance().getCurrentlyLoadedLevel().getPathToScene());
        Geometry navMeshGeom = (Geometry) (((Node) level).getChild(GeneralConstants.NAV_MESH_NAME));
        NavMesh navMesh = new NavMesh(navMeshGeom.getMesh());
        this.pathfinder = new NavMeshPathfinder(navMesh);

        if (targetPosition == null) {
            this.targetPosition = this.getPosition();
        }
        this.navigateTo(this.getTargetPosition());
        System.out.println("init done");
    }

    @Override
    public NavMeshPathfinder getPathfinder() {
        return this.pathfinder;
    }

    @Override
    public Vector3f getTargetPosition() {
        return this.targetPosition;
    }

    @Override
    public void setTargetPosition(Vector3f pos) {
        this.targetPosition.set(pos);
    }

    @Override
    public void takeDamage(float damage) {
        this.health -= damage;
    }

    @Override
    public void die() {
        if (this.health <= 0) {
            this.bullAppState.getPhysicsSpace().remove(this.control);
            this.shootables.detachChild(this);
        }
    }

    @Override
    public float getHealth() {
        return this.health;
    }

}
