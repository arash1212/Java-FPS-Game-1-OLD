/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.interfaces;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;

/**
 *
 * @author Arash
 */
public interface Actor {

    void spawn(Vector3f spawnPoint);

    void update();

    default boolean canJump() {
        return this.getState() != EnumActorState.IN_AIR;
    }

    EnumActorState getState();

    void setState(EnumActorState state);

    CharacterControl getControl();

    boolean isRunning();

    default void updateActorState() {
        if (this.getControl().onGround()) {
            if (!this.getControl().getWalkDirection().equals(Vector3f.ZERO)) {
                if (!this.isRunning()) {
                    this.setState(EnumActorState.WALKING);
                } else {
                    this.setState(EnumActorState.RUNNING);
                }
            } else if (this.getControl().getWalkDirection().equals(Vector3f.ZERO)) {
                this.setState(EnumActorState.STAND_STILL);
            }
        } else if (this.getControl().onGround() == false) {
            this.setState(EnumActorState.IN_AIR);
            System.out.println("in air ?");
        }
    }
}
