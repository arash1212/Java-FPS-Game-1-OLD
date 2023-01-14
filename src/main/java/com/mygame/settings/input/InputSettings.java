/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.settings.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.mygame.settings.Managers;
import java.util.Arrays;

/**
 *
 * @author Arash
 */
public class InputSettings {

    //constants
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String FORWARD = "forward";
    private static final String BACKWARD = "backward";
    private static final String JUMP = "jump";

    //
    private final InputManager inputManager;
    private final InputState inputState = InputState.getInstance();

    public InputSettings() {
        this.inputManager = Managers.getInstance().getInputManager();
    }

    public void initInputs() {
        this.inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
        this.inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
        this.inputManager.addMapping("forward", new KeyTrigger(KeyInput.KEY_W));
        this.inputManager.addMapping("backward", new KeyTrigger(KeyInput.KEY_S));
        this.inputManager.addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));

        this.inputManager.addListener(actionListener, LEFT, RIGHT, FORWARD, BACKWARD, JUMP);
    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals(LEFT)) {
                inputState.isPressedLeft = isPressed;
            }
            if (name.equals(RIGHT)) {
                inputState.isPressedRight = isPressed;
            }
            if (name.equals(FORWARD)) {
                inputState.isPressedForward = isPressed;
            }
            if (name.equals(BACKWARD)) {
                inputState.isPressedBackward = isPressed;
            }
            if (name.equals(JUMP)) {
                inputState.isPressedJump = isPressed;
            }
        }
    };
}
