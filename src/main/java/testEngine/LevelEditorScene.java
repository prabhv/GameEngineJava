package testEngine;


import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import utils.AssetPool;

public class LevelEditorScene extends Scene{

    private GameObject obj1;
    private SpriteSheet sprites;

    public LevelEditorScene(){

    }

    @Override
    public void init(){

        loadResources();
        this.camera = new Camera(new Vector2f());

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet/spriteSheet.png");

        obj1 = new GameObject("Object1", new Transform(new Vector2f(100, 100), new Vector2f(100, 100)));
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

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;
    @Override
    public void update(float dt) {
        spriteFlipTimeLeft -= dt;
        if (spriteFlipTimeLeft <= 0){
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 4){
                spriteIndex = 0;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }

        System.out.println("FPS :" + 1.0f/dt);
        for (GameObject go : this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }
}
