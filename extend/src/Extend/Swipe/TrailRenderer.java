package Extend.Swipe;

import Extend.Swipe.mesh.SwipeTriStrip;
import GameGDX.GUIData.IImage;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class TrailRenderer{
    public SwipeTriStrip tris = new SwipeTriStrip();
    public Swipe swipe = new Swipe(10);
    private Texture tex = IImage.emptyTexture;
    private boolean start;

    public TrailRenderer(){}
    public TrailRenderer(int maxPoint){
        swipe = new Swipe(maxPoint);
    }
    public void SetTexture(Texture tex)
    {
        this.tex = tex;
    }
    public void Start(float x,float y)
    {
        swipe.Start(x,y);
        start = true;
    }
    public void Update(float x,float y) {
        swipe.Update(x,y);
    }
    public void Draw(Color color, Camera camera) {
        if (!start) return;
        tex.bind();
        //generate the triangle strip from our path
        tris.update(swipe.path());
        //the vertex color for tinting, i.e. for opacity
        tris.color = color;
        //render the triangles to the screen
        tris.draw(camera);
    }
}
