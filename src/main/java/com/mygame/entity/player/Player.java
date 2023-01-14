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
import com.mygame.entity.interfaces.EnumActorState;
import com.mygame.settings.Managers;
import com.mygame.settings.input.InputState;

/**
 *
 * @author Arash
 */
public class Player extends Node implements Actor {

    //constants
    private static final float MOVEMENT_SPEED = 6;
    private static final float GRAVITY_SPEED = 20;
    private static final float JUMP_SPEED = 10;

    //Managers
    private final InputState inputState;
    private final AssetManager assetManager;
    private final Camera cam;
    private final BulletAppState bulletAppSate;

    //actor specifics
    private CharacterControl control;

    //Movement
    private final Vector3f camDir = new Vector3f();
    private final Vector3f camLeft = new Vector3f();
    private final Vector3f walkDirection = new Vector3f();
    private EnumActorState state = EnumActorState.STAND_STILL;

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

        control.setGravity(GRAVITY_SPEED);
        control.setJumpSpeed(JUMP_SPEED);
    }

    @Override
    public void spawn(Vector3f spawnPoint) {
        this.init();

        this.control.setPhysicsLocation(spawnPoint);
    }

    @Override
    public void update() {

        updateMovements();

        updateActorState();
    }

    private void updateMovements() {
        this.camDir.set(cam.getDirection());
        this.camLeft.set(cam.getLeft());

        this.walkDirection.set(0, 0, 0);
        if (inputState.isPressedLeft) {
            this.walkDirection.addLocal(camLeft);
        }
        if (this.inputState.isPressedRight) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (inputState.isPressedForward) {
            this.walkDirection.addLocal(camDir);
        }
        if (inputState.isPressedBackward) {
            this.walkDirection.addLocal(camDir.negate());
        }
        if (inputState.isPressedJump && this.canJump()) {
            this.control.jump();
        }

        this.walkDirection.y = 0;
        this.control.setWalkDirection(this.walkDirection.divide(MOVEMENT_SPEED));
        this.cam.setLocation(control.getPhysicsLocation());
    }

    @Override
    public EnumActorState getState() {
        return state;
    }

    @Override
    public void setState(EnumActorState state) {
        this.state = state;
    }

    @Override
    public CharacterControl getControl() {
        return control;
    }

}
