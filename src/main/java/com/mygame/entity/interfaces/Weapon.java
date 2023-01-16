/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.interfaces;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Arash
 */
public interface Weapon {

    void select();

    void updateAnimations(EnumActorState state);

    void update(float tpf);

    void fire();

    boolean isSingleShot();

    void setIsAiming(boolean isAiming);

    default CollisionResults rayTo(Vector3f origin, Vector3f direction, Node node) {
        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        node.collideWith(ray, results);
        return results;
    }
}
