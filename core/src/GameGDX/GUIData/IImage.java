package GameGDX.GUIData;

import GameGDX.*;
import GameGDX.GUIData.IChild.IActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class IImage extends IActor {
    public ITexture iTexture = new ITexture();
    @Override
    protected Actor NewActor() {
        return new Image(){
            @Override
            public void act(float delta) {
                super.act(delta);
                Update(delta);
            }
            @Override
            public void draw(Batch batch, float parentAlpha) {
                OnDraw(batch,parentAlpha,()->super.draw(batch, parentAlpha));
            }
        };
    }

    @Override
    public void SetConnect(GDX.Func1 connect) {
        super.SetConnect(connect);
        iSize.getDefaultSize = ()->GetDefaultSize();
    }

    protected Vector2 GetDefaultSize() {
        TextureRegion tr = iTexture.GetTexture();
        return new Vector2(tr.getRegionWidth(),tr.getRegionHeight());
    }

    @Override
    public void RefreshContent() {
        SetDrawable(iTexture.GetDrawable());
    }

    @Override
    public void RefreshLanguage() {
        if (!iTexture.multiLanguage) return;
        SetDrawable(iTexture.GetDrawable());
    }

    @Override
    public void Refresh() {
        super.Refresh();
        RefreshContent();
    }

    public void SetTexture(String name)
    {
        SetTexture(Assets.GetTexture(name));
    }
    public void SetTexture(Texture texture)
    {
        SetTexture(new TextureRegion(texture));
    }
    public void SetTexture(TextureRegion texture)
    {
        SetDrawable(NewDrawable(texture));
    }
    public void SetDrawable(Drawable drawable)
    {
        Image img = GetActor();
        img.setDrawable(drawable);
    }
    protected TextureRegion GetTexture()
    {
        return iTexture.GetTexture();
    }

    //class
    public static class ITexture
    {
        public boolean multiLanguage;
        public String name = "";
        public Drawable GetDrawable()
        {
            return NewDrawable(GetTexture());
        }
        public TextureRegion GetTexture()
        {
            try {
                return Assets.GetTexture(GetName());
            }catch (Exception e)
            {
                return new TextureRegion(emptyTexture);
            }
        }
        protected String GetName()
        {
            return multiLanguage?Translate.i.Get(name):name;
        }
        @Override
        public boolean equals(Object obj) {
            return Reflect.equals(this,obj);
        }
    }
    public static class INinePath extends ITexture
    {
        public int left=10,right=10,top=10,bottom=10;
        @Override
        public Drawable GetDrawable() {
            NinePatch ninePatch = new NinePatch(GetTexture(),left,right,top,bottom);
            return new NinePatchDrawable(ninePatch);
        }
    }

    //static
    public static Texture emptyTexture = NewTexture(Color.WHITE,100,100);
    private static Texture NewTexture(Color color, float width, float height)
    {
        Pixmap pixmap = new Pixmap((int)width, (int)height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, (int)width, (int)height);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        return texture;
    }
    //Drawable
    public static Drawable NewDrawable(Texture texture)
    {
        return NewDrawable(new TextureRegion(texture));
    }
    public static Drawable NewDrawable(TextureRegion textureRegion)
    {
        return new TextureRegionDrawable(textureRegion);
    }
    public static Drawable NewDrawable(NinePatch ninePath)
    {
        return new NinePatchDrawable(ninePath);
    }

    //<editor-fold desc="Image color">
    public static Image NewImage(Color color)
    {
        Image img = new Image(emptyTexture);
        img.setColor(color);
        return img;
    }
    public static Image NewImage(Color color, Group parent)
    {
        return NewImage(color,(int)parent.getWidth(),(int)parent.getHeight(),parent);
    }
    public static Image NewImage(Color color,float width,float height, Group parent)
    {
        return NewImage(color,0,0, Align.bottomLeft,width,height,parent);
    }
    public static Image NewImage(Color color, float x, float y, int align , float width, float height, Group parent)
    {
        Image img = NewImage(color);
        img.setSize(width,height);
        img.setPosition(x,y,align);
        parent.addActor(img);
        return img;
    }
    public static Image NewImage(Color color, Vector2 pos, int align , float width, float height, Group parent)
    {
        return NewImage(color,pos.x,pos.y,align,width,height,parent);
    }
    //</editor-fold>
    //<editor-fold desc="Image Texture">
    public static Image NewImage(String name)
    {
        return NewImage(Assets.GetTexture(name));
    }
    public static Image NewImage(TextureRegion texture)
    {
        return new Image(texture);
    }
    public static Image NewImage(TextureRegion texture, Group parent)
    {
        return NewImage(texture,texture.getRegionWidth(),texture.getRegionHeight(),parent);
    }
    public static Image NewImage(TextureRegion texture,float width,float height, Group parent)
    {
        return NewImage(texture,0,0, Align.bottomLeft,width,height,parent);
    }
    public static Image NewImage(TextureRegion texture,float x,float y,int align, Group parent)
    {
        return NewImage(texture,x,y, align,texture.getRegionWidth(),texture.getRegionHeight(),parent);
    }
    public static Image NewImage(TextureRegion texture, float x, float y, int align , float width, float height, Group parent)
    {
        Image img = NewImage(texture);
        img.setSize(width, height);
        img.setPosition(x,y,align);
        parent.addActor(img);
        return img;
    }
    public static Image NewImage(TextureRegion texture, Vector2 pos, int align , float width, float height, Group parent)
    {
        return NewImage(texture,pos.x,pos.y,align,width,height,parent);
    }
    //</editor-fold>
    //<editor-fold desc="Image Other">
    public static void SetImage(Image img,Color color)
    {
        img.setDrawable(NewDrawable(emptyTexture));
        img.setColor(color);
    }
    //</editor-fold>
}
