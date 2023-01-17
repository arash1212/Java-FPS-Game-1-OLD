/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.interfaces;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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

    boolean isAiming();

    float getDamage();

    default CollisionResults rayTo(Vector3f origin, Vector3f direction, Node node) {
        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        node.collideWith(ray, results);
        return results;
    }

    default float calculateDamage(Spatial hitObject) {
        if (hitObject.getName().contains("Head")) {
            return this.getDamage() * 4.f;
        } else if (hitObject.getName().contains("Body")) {
            return this.getDamage() * 2.f;
        } else {
            return this.getDamage();
        }
    }

    default void applyDamageToTarget(CollisionResults results) {
        for (CollisionResult result : results) {
            Spatial hitObject = result.getGeometry();
            Spatial actorObject = result.getGeometry();
            while (!(actorObject instanceof Actor)) {
                if (actorObject.getParent() != null) {
                    actorObject = actorObject.getParent();
                } else {
                    break;
                }
            }
            if (actorObject instanceof Actor) {
                ((Actor) actorObject).takeDamage(this.calculateDamage(hitObject));
                break;
            }
        }
    }

    default void updatePosition(float tpf, Spatial model, Vector3f defaultPos, Quaternion defaultRot, Vector3f aimPos, Quaternion aimRot) {
        if (this.isAiming()) {
            model.getLocalTranslation().interpolateLocal(aimPos, tpf * 12);
            model.setLocalRotation(aimRot);
        } else {
            model.getLocalTranslation().interpolateLocal(defaultPos, tpf * 12);
            model.setLocalRotation(defaultRot);
        }
    }
}
