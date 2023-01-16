/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.weapons.pistol;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.Tween;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.Action;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.mygame.entity.interfaces.Actor;
import com.mygame.entity.interfaces.EnumActorState;
import com.mygame.entity.interfaces.Weapon;
import com.mygame.settings.Managers;
import com.mygame.settings.input.InputState;

/**
 *
 * @author Arash
 */
public class PistolMakarove implements Weapon {

    //constants
    private static final String PATH_TO_MODEL = "Models/weapons/pistols/makarove/pistol_makarove.j3o";
    private static final String PATH_TO_FIRE_SOUND = "Models/weapons/pistols/makarove/sounds/Pistol_Makarove_Fire_Sound.wav";
    private static final float DAMAGE = 25.f;
    private static final Vector3f DEFAULT_POSITION = new Vector3f(0, -0.95f, 0.61f);
    private static final Quaternion DEFAULT_ROTATIN = new Quaternion().fromAngles(0.0f, 39.11f, 0.08f);
    private static final Vector3f AIM_POSITION = new Vector3f(0.2f, -0.807f, 0.67f);
    private static final Quaternion AIM_ROTATIN = new Quaternion().fromAngles(0.0f, 39.096f, -0.00f);

    private boolean isAiming = false;

    //anim constants
    private static final String ANIM_ACTION_IDLE = "Idle";
    private static final String ANIM_ACTION_WALK = "Walk";
    private static final String ANIM_ACTION_RUN = "Run";
    private static final String ANIM_ACTION_FIRE = "Fire";
    private static final String ANIM_ACTION_FIRE_ONCE = "FireOnce";

    private final AssetManager assetManager;
    private final InputState inputState;
    private final Camera cam;
    private final Node shootables;

    //Sounds
    private AudioNode fireSound;

    //animation
    private Spatial model;
    private AnimComposer animComposer;
    private EnumActorState currentState = EnumActorState.STAND_STILL;

    //Actions
    private Action fireOnce;

    private final CameraNode cameraNode;

    //Recoil
    private float recoilAmount = 0.f;
    private Quaternion currentCamRotation;

    public PistolMakarove() {
        this.assetManager = Managers.getInstance().getAsseManager();
        this.cameraNode = Managers.getInstance().getCameraNode();
        this.inputState = InputState.getInstance();
        this.cam = Managers.getInstance().getCam();
        this.shootables = Managers.getInstance().getShooteables();
    }

    private void init() {
        model = this.assetManager.loadModel(PATH_TO_MODEL);
        this.animComposer = ((Node) model).getChild("Armature").getControl(AnimComposer.class);
        model.setLocalTranslation(DEFAULT_POSITION);
        model.setLocalRotation(DEFAULT_ROTATIN);
        animComposer.setCurrentAction(ANIM_ACTION_IDLE);

        this.cameraNode.attachChild(model);

        //sounds
        this.fireSound = new AudioNode(this.assetManager, PATH_TO_FIRE_SOUND, DataType.Buffer);
        this.fireSound.setPositional(false);
    }

    @Override
    public void select() {
        init();
    }

    @Override
    public void updateAnimations(EnumActorState state) {
        if (state == EnumActorState.WALKING && currentState != EnumActorState.WALKING) {
            this.animComposer.setCurrentAction(ANIM_ACTION_WALK);
            this.currentState = EnumActorState.WALKING;
        } else if (state == EnumActorState.RUNNING && currentState != EnumActorState.RUNNING) {
            this.animComposer.setCurrentAction(ANIM_ACTION_RUN);
            this.currentState = EnumActorState.RUNNING;
        } else if ((state == EnumActorState.STAND_STILL || state == EnumActorState.IN_AIR) && currentState != EnumActorState.STAND_STILL) {
            this.animComposer.setCurrentAction(ANIM_ACTION_IDLE);
            this.currentState = EnumActorState.STAND_STILL;
        }

        this.initTweens(state);
    }

    @Override
    public void update(float tpf) {

        this.updatePosition(tpf);

        this.recoil(tpf);
    }

    @Override
    public void fire() {
        this.animComposer.setCurrentAction(ANIM_ACTION_FIRE_ONCE);
        this.fireSound.playInstance();

        CollisionResults results = this.rayTo(this.cam.getLocation(), this.cam.getDirection(), shootables);

        for (CollisionResult result : results) {
            Spatial hitObject = result.getGeometry();
            while (!(hitObject instanceof Actor)) {
                if (hitObject.getParent() != null) {
                    hitObject = hitObject.getParent();
                } else {
                    break;
                }
            }
            if (hitObject instanceof Actor) {
                ((Actor) hitObject).takeDamage(DAMAGE);
                break;
            }
        }

        this.recoilAmount += 1;
    }

    @Override
    public boolean isSingleShot() {
        return true;
    }

    private void initTweens(EnumActorState state) {
        Action fireAction = this.animComposer.action(ANIM_ACTION_FIRE);
        Tween doneTween = doneTween = Tweens.callMethod(this.animComposer, "setCurrentAction", ANIM_ACTION_IDLE);
        if (state == EnumActorState.WALKING) {
            doneTween = Tweens.callMethod(this.animComposer, "setCurrentAction", ANIM_ACTION_WALK);
        } else if (state == EnumActorState.RUNNING) {
            doneTween = Tweens.callMethod(this.animComposer, "setCurrentAction", ANIM_ACTION_RUN);
        }

        fireOnce = this.animComposer.actionSequence(ANIM_ACTION_FIRE_ONCE, fireAction, doneTween);
        fireOnce.setSpeed(1.0f);
    }

    //aim/default
    private void updatePosition(float tpf) {
        if (this.isAiming) {
            model.getLocalTranslation().interpolateLocal(AIM_POSITION, tpf * 12);
            model.setLocalRotation(AIM_ROTATIN);
        } else {
            model.getLocalTranslation().interpolateLocal(DEFAULT_POSITION, tpf * 12);
            model.setLocalRotation(DEFAULT_ROTATIN);
        }
    }

    @Override
    public void setIsAiming(boolean isAiming) {
        this.isAiming = isAiming;
    }

    //TODO : fix beshe
    private void recoil(float tpf) {
        if (recoilAmount > 0) {
           // Vector3f newDir = new Vector3f(this.cam.getDirection().x, this.cam.getDirection().y + 0.003f, this.cam.getDirection().z);
           // this.cam.lookAtDirection(new Vector3f().interpolateLocal(this.cam.getDirection(), newDir, 0.6f * tpf * 30), cam.getUp());
            recoilAmount -= tpf;
        }
    }

}
