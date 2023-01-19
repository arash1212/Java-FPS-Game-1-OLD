/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.player;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.mygame.entity.interfaces.Actor;
import com.mygame.entity.interfaces.EnumActorState;
import com.mygame.entity.interfaces.Weapon;
import com.mygame.entity.weapons.pistol.PistolMakarove;
import com.mygame.settings.Managers;
import com.mygame.settings.input.InputState;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arash
 */
public class Player extends Node implements Actor {

    //constants
    private static final float MOVEMENT_SPEED = 6;
    private static final float RUN_SPEED = 3;
    private static final float GRAVITY_SPEED = 20;
    private static final float JUMP_SPEED = 10;
    private static final float HEIGHT = 1.8f;
    
    private float currentSpeed = 6;
    private float currentFov = 50f;

    //Managers
    private final InputState inputState;
    private final AssetManager assetManager;
    private final Camera cam;
    private final BulletAppState bulletAppSate;
    private final Node rootNode;
    private final CameraNode cameraNode;
    private Node shootables;

    //actor specifics
    private CharacterControl control;

    //Movement
    private final Vector3f camDir = new Vector3f();
    private final Vector3f camLeft = new Vector3f();
    private final Vector3f walkDirection = new Vector3f();
    private EnumActorState state = EnumActorState.STAND_STILL;
    private Vector3f camPosition = new Vector3f(0, 0, 0);
    private final float mouseSensitivity = 0.001f;

    //Weapons
    private List<Weapon> weapons = new ArrayList(3);
    private Weapon selectedWeapon;

    //Health
    private float health = 100;
    private Actor attacker;
    
    public Player() {
        this.inputState = InputState.getInstance();
        this.assetManager = Managers.getInstance().getAsseManager();
        this.cam = Managers.getInstance().getCam();
        this.bulletAppSate = Managers.getInstance().getBulletAppState();
        this.rootNode = Managers.getInstance().getRootNode();
        this.cameraNode = Managers.getInstance().getCameraNode();
        this.shootables = Managers.getInstance().getShooteables();
    }
    
    private void init() {
        
        CapsuleCollisionShape capsule = new CapsuleCollisionShape(1.3f, HEIGHT, 1);
        control = new CharacterControl(capsule, 1f);
        control.setSpatial(this);
        this.bulletAppSate.getPhysicsSpace().add(control);
        
        control.setGravity(GRAVITY_SPEED);
        control.setJumpSpeed(JUMP_SPEED);

        //rootNode.attachChild(cameraNode);
        this.attachChild(this.cameraNode);
        this.shootables.attachChild(this);
    }
    
    @Override
    public void spawn(Vector3f spawnPoint) {
        this.init();
        
        this.control.setPhysicsLocation(spawnPoint);
        
        initWeapons();
    }
    
    @Override
    public void update(float tpf) {
        
        this.cam.setFov(currentFov);

        // updateCamera();
        updateMovements();
        
        updateActorState();
        
        if (selectedWeapon != null) {
            selectedWeapon.update(tpf);
            selectedWeapon.updateAnimations(this.state);
        }
        
        this.fire();
        
        this.aim(tpf);
    }
    
    private void setCurrentSpeed() {
        if (this.state.equals(EnumActorState.WALKING) && currentSpeed < MOVEMENT_SPEED) {
            currentSpeed += 0.05f;
        } else if (this.state.equals(EnumActorState.RUNNING) && currentSpeed > RUN_SPEED) {
            currentSpeed -= 0.3f;
        } else if (this.state.equals(EnumActorState.STAND_STILL)) {
            currentSpeed = MOVEMENT_SPEED;
        }
    }
    
    private void updateCamera() {
        //lock cursor inside window
        Managers.getInstance().getInputManager().setCursorVisible(false);
        //camPosition
        this.camPosition.set(this.getPosition().x, this.getPosition().y + HEIGHT, this.getPosition().z);
        // this.cam.setLocation(this.camPosition);

        if (inputState.mouseDeltaXY == null) {
            return;
        }
        
        if (inputState.mouseDeltaXY.y != 0.0f) {
            this.cameraNode.rotate(-inputState.mouseDeltaXY.y * mouseSensitivity, 0, 0);
        }
        if (inputState.mouseDeltaXY.x != 0.0f) {
            this.rotate(0, -inputState.mouseDeltaXY.x * mouseSensitivity, 0);
        }
        
    }
    
    private void updateMovements() {
        setCurrentSpeed();
        
        this.camPosition.set(this.getPosition().x, this.getPosition().y + HEIGHT, this.getPosition().z);
        
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
            //todo fix (backward run ?)
            this.walkDirection.addLocal(camDir.negate());
        }
        if (inputState.isPressedJump && this.canJump()) {
            this.control.jump();
        }
        
        this.walkDirection.y = 0;
        this.control.setWalkDirection(this.walkDirection.normalizeLocal().divide(currentSpeed));
        this.cam.setLocation(this.camPosition);
    }

    //weapons
    private void initWeapons() {
        this.weapons.add(new PistolMakarove());
        
        this.selectedWeapon = this.weapons.get(0);
        this.selectedWeapon.select();
        this.selectedWeapon.setOwner(this);
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
    
    @Override
    public boolean isRunning() {
        return !this.control.getWalkDirection().equals(Vector3f.ZERO)
                && !this.inputState.isPressedBackward
                && this.inputState.isPressedForward
                && inputState.isPressedRun
                && !inputState.isPressedAim;
    }
    
    private void fire() {
        if (this.inputState.isPressedFire) {
            this.selectedWeapon.fire();
            if (this.selectedWeapon.isSingleShot()) {
                this.inputState.isPressedFire = false;
            }
        }
    }
    
    private void aim(float tpf) {
        if (this.selectedWeapon != null) {
            if (this.inputState.isPressedAim) {
                if (this.currentFov > 40) {
                    this.currentFov -= 40.4f * tpf;
                }
                this.selectedWeapon.setIsAiming(true);
            } else {
                if (this.currentFov < 50) {
                    this.currentFov += 40.4f * tpf;
                }
                this.selectedWeapon.setIsAiming(false);
            }
        }
    }
    
    @Override
    public void takeDamage(float damage, Actor attackers) {
        this.health -= damage;
    }
    
    @Override
    public void die() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public float getHealth() {
        return this.health;
    }
    
}
