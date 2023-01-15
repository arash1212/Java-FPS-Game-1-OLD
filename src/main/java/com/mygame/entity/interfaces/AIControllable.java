/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.entity.interfaces;

import com.jme3.ai.navmesh.NavMeshPathfinder;
import com.jme3.ai.navmesh.Path.Waypoint;
import com.jme3.math.Vector3f;

/**
 *
 * @author Arash
 */
public interface AIControllable extends Actor {

    void initNavMesh();

    NavMeshPathfinder getPathfinder();

    Vector3f getTargetPosition();

    void setTargetPosition(Vector3f pos);

    default void navigateTo(Vector3f position) {
        if (this.getTargetPosition() != null) {
            if (this.getPathfinder() == null) {
                System.out.println("This Actor Have No Pathfinder assigned.");
                return;
            }

            this.getPathfinder().setPosition(this.getPosition());
            if (!position.equals(this.getTargetPosition())) {
                this.setTargetPosition(position);
                this.getPathfinder().computePath(this.getTargetPosition());
            }

            this.getControl().setWalkDirection(Vector3f.ZERO);
            Waypoint waypoint = this.getPathfinder().getNextWaypoint();
            if (waypoint == null) {
                return;
            }

            Vector3f wayPointDirection = waypoint.getPosition().subtract(this.getPosition());
            this.getControl().setWalkDirection(wayPointDirection.normalize().divide(10));

            if (waypoint.getPosition().distance(this.getPosition()) < 4 && !this.getPathfinder().isAtGoalWaypoint()) {
                this.getPathfinder().goToNextWaypoint();
            }

            if (this.getPathfinder().isAtGoalWaypoint()) {
                this.getPathfinder().clearPath();
            }
        } else {
            System.out.println("This Actor Have No TargetPosition.Setting Target Position to currentPosition");
        }

    }
}
