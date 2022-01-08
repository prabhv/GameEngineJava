package testEngine;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListeners {
    private static KeyListeners instance;
    private boolean keyPressed[] = new boolean[350];

    private KeyListeners(){

    }

    public static KeyListeners get(){
        if (KeyListeners.instance == null){
            KeyListeners.instance = new KeyListeners();
        }
        return KeyListeners.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if (action == GLFW_PRESS){
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }

}
