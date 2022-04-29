package components;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class RigidBody extends Component {

    private int colliderType = 0;
    private float friction = 0.5f;
    public Vector3f velocity = new Vector3f(0f, 0.8f, 0f);
    public transient Vector4f tmp = new Vector4f(0,0,0,0);
}
