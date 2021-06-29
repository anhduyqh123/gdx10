package GameGDX.GUIData;

import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.Language;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
    public float sizeScale = 1f;
    public boolean multiLanguage;

    @Override
    protected Actor NewActor() {
        return new Image();
    }

    @Override
    public void SetConnect(GDX.Func1 connect) {
        super.SetConnect(connect);
        iSize.getDefaultSize = ()->GetDefaultSize();
    }

    protected Vector2 GetDefaultSize() {
        Vector2 size = new Vector2();
        TextureRegion tr = iTexture.GetTexture(GetExtend());
        size.set(tr.getRegionWidth(),tr.getRegionHeight());
        return size;
    }

    @Override
    public Vector2 GetSize() {
        Vector2 size = super.GetSize();
        return size.scl(sizeScale);
    }

    @Override
    public void RefreshContent() {
        SetTexture(iTexture.GetTexture(GetExtend()));
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
        Image img = GetActor();
        img.setDrawable(NewDrawable(texture));
    }
    private String GetExtend()
    {
        if (multiLanguage) return Language.GetCode();
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IImage)) return false;
        if (!super.equals(o)) return false;
        IImage iImage = (IImage) o;
        return Float.compare(iImage.sizeScale, sizeScale) == 0 && multiLanguage == iImage.multiLanguage && iTexture.equals(iImage.iTexture);
    }

    //class
    public static class ITexture
    {
        public String name = "";
        public Drawable GetDrawable(String extend)
        {
            return NewDrawable(GetTexture(extend));
        }
        public TextureRegion GetTexture(String extend)
        {
            try {
                return Assets.GetTexture(name+extend);
            }catch (Exception e)
            {
                return new TextureRegion(emptyTexture);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ITexture)) return false;
            ITexture iTexture = (ITexture) o;
            return name.equals(iTexture.name);
        }
    }
    public static class INinePath extends ITexture
    {
        public int left=10,right=10,top=10,bottom=10;
        @Override
        public Drawable GetDrawable(String extend) {
            NinePatch ninePatch = new NinePatch(GetTexture(extend),left,right,top,bottom);
            return new NinePatchDrawable(ninePatch);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof INinePath)) return false;
            if (!super.equals(o)) return false;
            INinePath iNinePath = (INinePath) o;
            return left == iNinePath.left && right == iNinePath.right && top == iNinePath.top && bottom == iNinePath.bottom;
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
