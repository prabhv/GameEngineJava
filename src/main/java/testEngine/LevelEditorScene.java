package testEngine;


import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import utils.AssetPool;

public class LevelEditorScene extends Scene{

    public LevelEditorScene(){

    }

    @Override
    public void init(){

        loadResources();
        this.camera = new Camera(new Vector2f());

        SpriteSheet sprites = AssetPool.getSpriteSheet("assets/images/spritesheet/spriteSheet.png");

        GameObject obj1 = new GameObject("Object1", new Transform(new Vector2f(100, 100), new Vector2f(100, 100)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object2", new Transform(new Vector2f(400, 100), new Vector2f(100, 100)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(5)));
        this.addGameObjectToScene(obj2);

    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet/spriteSheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet/spriteSheet.png"), 16, 16, 26, 0));
    }

    @Override
    public void update(float dt) {
        System.out.println("FPS :" + 1.0f/dt);
        for (GameObject go : this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }
}
