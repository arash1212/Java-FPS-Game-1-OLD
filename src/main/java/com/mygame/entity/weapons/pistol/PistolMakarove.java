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
    private static final Vector3f DEFAULT_POSITION = new Vector3f(0, -1.05f, 1);
    private static final Quaternion DEFAULT_ROTATIN = new Quaternion().fromAngles(0.0f, 39.15f, 0.08f);
    private static final float DAMAGE = 25.f;

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
    private AnimComposer animComposer;
    private EnumActorState currentState = EnumActorState.STAND_STILL;

    //Actions
    private Action fireOnce;

    private final CameraNode cameraNode;

    public PistolMakarove() {
        this.assetManager = Managers.getInstance().getAsseManager();
        this.cameraNode = Managers.getInstance().getCameraNode();
        this.inputState = InputState.getInstance();
        this.cam = Managers.getInstance().getCam();
        this.shootables = Managers.getInstance().getShooteables();
    }

    private void init() {
        Spatial model = this.assetManager.loadModel(PATH_TO_MODEL);
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
    public void update() {
        System.out.println("size : " + this.shootables.getChildren().size());
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
                System.out.println("hit actor : apply damage");
                break;
            }
        }
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
        fireOnce.setSpeed(1.3f);
    }

}
