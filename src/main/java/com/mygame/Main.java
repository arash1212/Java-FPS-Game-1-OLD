package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.mygame.settings.Managers;

public class Main extends SimpleApplication {
    
    private BulletAppState bulletAppState = new BulletAppState();
    
    private final Node shootables = new Node();
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        this.stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(true);
        
        initManagers();
        
        initNodes();
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    //inits
    private void initManagers() {
        Managers.getInstance().setAsseManager(this.assetManager);
        Managers.getInstance().setAudioRenderer(this.audioRenderer);
        Managers.getInstance().setInputManager(this.inputManager);
        Managers.getInstance().setRootNode(this.rootNode);
        Managers.getInstance().setShooteables(this.shootables);
        Managers.getInstance().setCam(this.cam);
        Managers.getInstance().setBulletAppState(this.bulletAppState);
        Managers.getInstance().setStateManager(this.stateManager);
        Managers.getInstance().setAppSettings(this.settings);
    }
    
    private void initNodes() {
        rootNode.attachChild(shootables);
    }
}
