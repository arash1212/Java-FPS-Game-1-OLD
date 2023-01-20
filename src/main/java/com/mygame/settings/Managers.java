/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame.settings;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.system.AppSettings;
import com.mygame.entity.interfaces.Actor;
import com.mygame.levels.Level;
import com.mygame.settings.input.InputState;

/**
 *
 * @author Arash
 */
public class Managers {

    private AssetManager asseManager;
    private InputManager inputManager;
    private AppStateManager stateManager;
    private AppSettings appSettings;
    private AudioRenderer audioRenderer;
    private Node rootNode;
    private Node shooteables;
    private Camera cam;
    private BulletAppState bulletAppState;
    private CameraNode cameraNode;
    private Level currentlyLoadedLevel;
    private Actor player;

    private final static Managers instance = new Managers();

    private Managers() {

    }

    public static Managers getInstance() {
        return instance;
    }

    //Getter/Setters
    public AssetManager getAsseManager() {
        return asseManager;
    }

    public void setAsseManager(AssetManager asseManager) {
        this.asseManager = asseManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public AudioRenderer getAudioRenderer() {
        return audioRenderer;
    }

    public void setAudioRenderer(AudioRenderer audioRenderer) {
        this.audioRenderer = audioRenderer;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public Node getShooteables() {
        return shooteables;
    }

    public void setShooteables(Node shooteables) {
        this.shooteables = shooteables;
    }

    public Camera getCam() {
        return cam;
    }

    public void setCam(Camera cam) {
        this.cam = cam;
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public void setBulletAppState(BulletAppState bulletAppState) {
        this.bulletAppState = bulletAppState;
    }

    public AppStateManager getStateManager() {
        return stateManager;
    }

    public void setStateManager(AppStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public AppSettings getAppSettings() {
        return appSettings;
    }

    public void setAppSettings(AppSettings appSettings) {
        this.appSettings = appSettings;
    }

    public CameraNode getCameraNode() {
        return cameraNode;
    }

    public void setCameraNode(CameraNode cameraNode) {
        this.cameraNode = cameraNode;
        this.cameraNode.setControlDir(CameraControl.ControlDirection.CameraToSpatial);

        this.rootNode.attachChild(Managers.getInstance().getCameraNode());
    }

    public Level getCurrentlyLoadedLevel() {
        return currentlyLoadedLevel;
    }

    public void setCurrentlyLoadedLevel(Level currentlyLoadedLevel) {
        this.currentlyLoadedLevel = currentlyLoadedLevel;
    }

    public Actor getPlayer() {
        return player;
    }

    public void setPlayer(Actor player) {
        this.player = player;
    }

}
