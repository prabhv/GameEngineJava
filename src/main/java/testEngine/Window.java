package testEngine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow = NULL;

    private static Window window = null;

    private static Scene currentScene;

    public float r, g, b, a;
    private boolean fadetoBlack = false;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Window";
        this.r = 1;
        this.g = 1;
        this.b = 1;
        this.a = 1;
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false: "Unknown Scene '" + newScene + "'";
                break;
        }
    }

    public static Window get(){
        if (Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    public static Scene getScene(){
        return get().currentScene;
    }
    public void run(){
        System.out.println("Hello version lwjgl: " + Version.getVersion());

        init();
        loop();

        //Free mem as these are C++ bindings and needs mem destroy.

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //free GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        // error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialise GLFW");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //CREATE WINDOW
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListeners::keyCallback);

        //OPenGL context current
        glfwMakeContextCurrent(glfwWindow);

        //Enable V sync
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop(){
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)){
            //poll events

            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if ( dt >= 0 ){
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
