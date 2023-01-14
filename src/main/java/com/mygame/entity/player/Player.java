/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.player;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.mygame.entity.interfaces.Actor;
import com.mygame.settings.Managers;
import com.mygame.settings.input.InputSettings;
import com.mygame.settings.input.InputState;

/**
 *
 * @author Arash
 */
public class Player extends Node implements Actor {

    //Managers
    private final InputState inputState;
    private final AssetManager assetManager;
    private final Camera cam;
    private final BulletAppState bulletAppSate;

    //actor specifics
    private CharacterControl control;

    //Movement
    private float movementSpeed = 6;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    
    public Player() {
        this.inputState = InputState.getInstance();
        this.assetManager = Managers.getInstance().getAsseManager();
        this.cam = Managers.getInstance().getCam();
        this.bulletAppSate = Managers.getInstance().getBulletAppState();
    }
    
    private void init() {
        CapsuleCollisionShape capsule = new CapsuleCollisionShape(1.5f, 4f, 1);
        control = new CharacterControl(capsule, 0.001f);
        this.bulletAppSate.getPhysicsSpace().add(control);
    }
    
    @Override
    public void spawn(Vector3f spawnPoint) {
        this.init();
        
        this.control.setPhysicsLocation(spawnPoint);
    }
    
    @Override
    public void update() {
        updateMovements();
    }
    
    private void updateMovements() {
        this.camDir.set(cam.getDirection());
        this.camLeft.set(cam.getLeft());
        
        walkDirection.set(0, 0, 0);
        if (inputState.isPressedLeft) {
            walkDirection.add(camLeft);
        }
        if (inputState.isPressedRight) {
            walkDirection.add(camLeft.negate());
        }
        if (inputState.isPressedForward) {
            walkDirection.add(camDir);
        }
        if (inputState.isPressedBackward) {
            walkDirection.add(camDir);
        }
        
        this.control.setWalkDirection(this.walkDirection);
        this.cam.setLocation(control.getPhysicsLocation());
    }
}
