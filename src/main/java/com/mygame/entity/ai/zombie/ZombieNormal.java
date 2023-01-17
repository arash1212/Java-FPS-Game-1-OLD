/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.ai.zombie;

import com.jme3.ai.navmesh.NavMesh;
import com.jme3.ai.navmesh.NavMeshPathfinder;
import com.jme3.anim.AnimComposer;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
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
    private static final float HEIGHT = 2.7f;
    private static final String PATH_TO_MODEL = "Models/zombies/zombieNormal/ZombieNormal.j3o";

    //anim constants
    private static final String ANIM_ACTION_IDLE = "Idle";
    private static final String ANIM_ACTION_WALK = "Walk";
    private static final String ANIM_ACTION_RUN = "Run";

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

    //Animatiom
    private AnimComposer animComposer;
    private EnumActorState currentState = EnumActorState.STAND_STILL;
    
    private EnumActorState state = EnumActorState.STAND_STILL;
    
    public ZombieNormal() {
        this.assetManager = Managers.getInstance().getAsseManager();
        this.bullAppState = Managers.getInstance().getBulletAppState();
        this.shootables = Managers.getInstance().getShooteables();
    }
    
    private void init() {
        Spatial model = this.assetManager.loadModel(PATH_TO_MODEL);
        this.animComposer = ((Node) model).getChild(0).getControl(AnimComposer.class);
        
        CapsuleCollisionShape capsule = new CapsuleCollisionShape(1.0f, HEIGHT, 1);
        this.control = new CharacterControl(capsule, 0.01f);
        model.addControl(control);
        model.setLocalRotation(new Quaternion().fromAngles(0, 110, 0));
        
        this.bullAppState.getPhysicsSpace().add(control);
        this.shootables.attachChild(this);
        this.attachChild(model);
        this.addControl(control);
        
        this.animComposer.setCurrentAction("Idle");
    }
    
    @Override
    public void spawn(Vector3f spawnPoint) {
        this.init();
        
        this.control.setPhysicsLocation(spawnPoint);
    }
    
    @Override
    public void update(float tpf) {
        
        this.navigateTo(Managers.getInstance().getPlayer().getPosition());
        
        if (this.pathfinder == null && Managers.getInstance().getCurrentlyLoadedLevel() != null) {
            this.initNavMesh();
            this.targetPosition.set(Managers.getInstance().getPlayer().getPosition());
        }
        
        this.updateActorState();
        
        this.updateAnimations();

        //Test
        lookAtTarget();
        
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
     * ********************************Animation*****************************************
     */
    public void updateAnimations() {
        if (state == EnumActorState.WALKING && this.currentState != EnumActorState.WALKING) {
            System.out.println("zombie is walking");
            this.animComposer.setCurrentAction(ANIM_ACTION_WALK);
            this.currentState = EnumActorState.WALKING;
        } else if (state == EnumActorState.RUNNING && this.currentState != EnumActorState.RUNNING) {
            this.animComposer.setCurrentAction(ANIM_ACTION_RUN);
            this.currentState = EnumActorState.RUNNING;
            System.out.println("zombie is running");
        } else if ((state == EnumActorState.STAND_STILL || state == EnumActorState.IN_AIR) && this.currentState != EnumActorState.STAND_STILL) {
            System.out.println("zombie is standing still");
            this.animComposer.setCurrentAction(ANIM_ACTION_IDLE);
            this.currentState = EnumActorState.STAND_STILL;
        }
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
    
    private void lookAtTarget() {
        Vector3f dir = this.getPosition().subtract(targetPosition);
        this.control.setViewDirection(dir);
    }
    
}
