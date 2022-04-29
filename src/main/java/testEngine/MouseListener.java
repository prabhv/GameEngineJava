package testEngine;

import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener instance;
    private double scrollx, scrolly;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollx = 0.0;
        this.scrolly = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get(){
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }
    //will be called anytime the cursor is moved
    public static void mousePosCallback(long window, double xPos, double yPos){
        get().lastY = get().xPos;
        get().lastX = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action == GLFW_PRESS){
            if (button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE){
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrolly = yOffset;
        get().scrollx = xOffset;
    }

    public static void endFrame(){
        get().scrolly = 0;
        get().scrollx = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX(){
        return (float)get().xPos;
    }

    public static float getY(){
        return (float)get().yPos;
    }

    public static float getOrthoX(){
        float currentX = getX();
        currentX = (currentX / (float) Window.getWidth()) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(currentX, 0, 0, 1);
        temp.mul(Window.getScene().camera().getInverseProjectionMatrix().mul(Window.getScene().camera().getInverseViewMatrix()));
        currentX = temp.x;

        return currentX;
    }

    public static float getOrthoY(){
        float currentY = Window.getHeight() - getY();
        currentY = (currentY / (float) Window.getHeight()) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(0, currentY, 0, 1);
        temp.mul(Window.getScene().camera().getInverseProjectionMatrix().mul(Window.getScene().camera().getInverseViewMatrix()));
        currentY = temp.y;

        return currentY;

    }

    public static float getDx(){
        return ((float)get().lastX - (float)get().xPos);
    }

    public static float getDy(){
        return ((float)get().lastY - (float)get().yPos);
    }

    public static float getScrollX(){
        return (float)get().scrollx;
    }

    public static float getScrollY(){
        return (float)get().scrolly;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if (button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }


}
