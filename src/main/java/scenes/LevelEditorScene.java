package scenes;


import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;
import scenes.Scene;
import testEngine.Camera;
import testEngine.GameObject;
import testEngine.Prefabs;
import testEngine.Transform;
import utils.AssetPool;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private SpriteSheet sprites;

    private MouseControls mouseControls = new MouseControls();

    public LevelEditorScene(){

    }

    @Override
    public void init(){

        loadResources();
        this.camera = new Camera(new Vector2f());
        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet/decorationsAndBlocks.png");

        if (levelLoaded){
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        obj1 = new GameObject("Object1", new Transform(new Vector2f(300, 100), new Vector2f(200, 200)), 2);
        SpriteRenderer obj1SpriteRenderer = new SpriteRenderer();
        obj1SpriteRenderer.setColor(new Vector4f(1,0,0,1));
        obj1.addComponent(obj1SpriteRenderer);
        obj1.addComponent(new RigidBody());
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object2", new Transform(new Vector2f(400, 100), new Vector2f(200, 200)), -2);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
        obj1SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);

    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheet/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet/decorationsAndBlocks.png"), 16, 16, 81, 0));
        AssetPool.getTexture("assets/images/blendImage2.png");
    }

    @Override
    public void update(float dt) {
        mouseControls.update(dt);
//        System.out.println("FPS :" + 1.0f/dt);
        for (GameObject go : this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.begin("Sprite Window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < sprites.size(); i++){
            Sprite sprite = sprites.getSprite(i);
            float spriteHeight = sprite.getHeight() * 4;
            float spriteWidth = sprite.getWidth() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if( ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)){
                GameObject object = Prefabs.generateSpriteObjects(sprite, spriteWidth, spriteHeight);

                mouseControls.pickUpObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPressed = new ImVec2();
            ImGui.getItemRectMax(lastButtonPressed);
            float lastButtonX2 = lastButtonPressed.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if ( i+1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
