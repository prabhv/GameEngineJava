package components;

import testEngine.GameObject;
import testEngine.MouseListener;
import testEngine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null;

    public void pickUpObject(GameObject gameObject){
        this.holdingObject = gameObject;
        Window.getScene().addGameObjectToScene(gameObject);
    }

    public void place(){
        this.holdingObject = null;
    }

    @Override
    public void update(float dt) {
        if( holdingObject != null ){
            holdingObject.transform.position.x = MouseListener.getOrthoX() - 16;
            holdingObject.transform.position.y = MouseListener.getOrthoY() - 16;

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }
    }
}