/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.settings.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author Arash
 */
public class InputSettings {

    private InputManager inputManager;

    private InputState inputState = InputState.getInstance();

    public InputSettings(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public void initInputs() {
        this.inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
        this.inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
        this.inputManager.addMapping("forward", new KeyTrigger(KeyInput.KEY_W));
        this.inputManager.addMapping("backward", new KeyTrigger(KeyInput.KEY_S));
        this.inputManager.addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));

        this.inputManager.addListener(actionListener, "left", "right", "forward", "backward");
    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("left")) {
                inputState.isPressedLeft = isPressed;
            }
            if (name.equals("right")) {
                inputState.isPressedRight = isPressed;
            }
            if (name.equals("forward")) {
                inputState.isPressedForward = isPressed;
            }
            if (name.equals("backward")) {
                inputState.isPressedBackward = isPressed;
            }
            if (name.equals("jump")) {
                inputState.isPressedJump = isPressed;
            }
        }
    };
}
