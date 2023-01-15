/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.interfaces;

/**
 *
 * @author Arash
 */
public interface Weapon {

    void select();

    void updateAnimations(EnumActorState state);

    void update();

    void fire();

    boolean isSingleShot();
}
