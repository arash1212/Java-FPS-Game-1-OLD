/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.weapons.pistol;

import com.jme3.anim.AnimComposer;
import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.mygame.entity.interfaces.EnumActorState;
import com.mygame.entity.interfaces.Weapon;
import com.mygame.settings.Managers;

/**
 *
 * @author Arash
 */
public class PistolMakarove implements Weapon {

    //constants
    private static final String PATH_TO_MODEL = "Models/weapons/pistols/makarove/pistol_makarove.j3o";
    private static final Vector3f DEFAULT_POSITION = new Vector3f(0, -1.05f, 1);
    private static final Quaternion DEFAULT_ROTATIN = new Quaternion().fromAngles(0.0f, 39.15f, 0.08f);

    //anim constants
    private static final String ANIM_ACTION_IDLE = "Idle";
    private static final String ANIM_ACTION_WALK = "Walk";
    private static final String ANIM_ACTION_RUN = "Run";

    private AssetManager assetManager;

    //animation
    private AnimComposer animComposer;
    private EnumActorState currentState = EnumActorState.STAND_STILL;

    private final CameraNode cameraNode;

    public PistolMakarove() {
        this.assetManager = Managers.getInstance().getAsseManager();
        this.cameraNode = Managers.getInstance().getCameraNode();
    }

    private void init() {
        Spatial model = this.assetManager.loadModel(PATH_TO_MODEL);
        this.animComposer = ((Node) model).getChild("Armature").getControl(AnimComposer.class);
        model.setLocalTranslation(DEFAULT_POSITION);
        model.setLocalRotation(DEFAULT_ROTATIN);
        animComposer.setCurrentAction(ANIM_ACTION_IDLE);

        this.cameraNode.attachChild(model);
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
            //todo fix
        }
    }

    @Override
    public void update() {

    }

}
