/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.settings.input;

/**
 *
 * @author Arash
 */
public class InputState {

    private static final InputState instance = new InputState();

    private InputState() {
    }

    //Movement
    public boolean isPressedLeft, isPressedRight, isPressedForward, isPressedBackward, isPressedJump;

    public static InputState getInstance() {
        return instance;
    }
}
