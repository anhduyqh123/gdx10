package GameGDX.GUIData;

import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.GUIData.IChild.IActor;
import GameGDX.GUIData.IChild.IAlign;
import GameGDX.Language;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.List;

public class ILabel extends IActor {

    public String font = "";
    public String text = "text";
    public IAlign alignment = IAlign.center;
    public float fontScale = 1f;
    public boolean bestFix,wrap,multiLanguage;

    @Override
    protected Actor NewActor() {
        return New(text,GetFontName());
    }

    public String GetText()
    {
        if (multiLanguage) return GetTranslate(text);
        return text;
    }
    private BitmapFont GetFont()
    {
        return Assets.GetFont(GetFontName());
    }
    public String GetFontName()
    {
        if (font.equals("")) return gFont;
        return font;
    }
    public void SetFont(String fontName)
    {
        if (fontName.equals(gFont)) font="";
        else font = fontName;
        Label lb = GetActor();
        lb.setStyle(new Label.LabelStyle(GetFont(), Color.WHITE));
    }
    public void SetText(Object text)
    {
        Label lb = GetActor();
        lb.setText(text+"");
        if (bestFix) BestFix(lb);
    }
    public void ReplaceText(Object value)//default %
    {
        ReplaceText(value,"%");
    }
    public void ReplaceText(Object value,String target)//target = %
    {
        String txt = GetText();
        Label lb = GetActor();
        lb.setText(txt.replace(target,value+""));
    }

    @Override
    public void SetConnect(GDX.Func1 connect) {
        super.SetConnect(connect);
        iSize.getDefaultSize = () ->{
            Label lb = GetActor();
            return new Vector2(lb.getPrefWidth(),lb.getPrefHeight());
        };
    }

    @Override
    public void RefreshContent() {
        Label lb = GetActor();
        lb.setText(GetText());
        lb.setStyle(new Label.LabelStyle(GetFont(), Color.WHITE));
        lb.setWrap(wrap);
        if (bestFix) BestFix(GetActor());
    }

    @Override
    public void Refresh() {
        InitActor();
        Label lb = GetActor();
        lb.setText(GetText());
        lb.setFontScale(fontScale);
        lb.setAlignment(alignment.value);
        lb.setStyle(new Label.LabelStyle(GetFont(), Color.WHITE));
        lb.setWrap(false);
        super.Refresh();

        lb.setWrap(wrap);
        if (bestFix) BestFix(GetActor());
    }

    @Override
    public void StopAction() {
        super.StopAction();
        Label lb = GetActor();
        String text = GetText();
        lb.setText(text);
    }

    //static
    //Multi Language
    public static String GetTranslate(String text)//{key}->en-hello,vi->xin chao;
    {
        List<String> keys = GetKeys(text);
        if (keys.size()<=0) return Language.GetContent(text);
        String txt = text;
        for(String key : keys) txt = txt.replace(key,Language.GetContent(GetValue(key)));
        return txt;
    }
    private static String GetValue(String value)
    {
        return value.substring(1,value.length()-1);
    }
    private static List<String> GetKeys(String data)
    {
        List<String> list = new ArrayList<>();
        while (true)
        {
            String key = GetKey(data);
            if (key==null) return list;
            list.add(key);
            data = data.replace(key,"");
        }
    }
    private static String GetKey(String data)
    {
        try {
            int s = data.indexOf("{");
            int e = data.indexOf("}");
            if (s==-1 || e==-1) return null;
            return data.substring(s,e+1);
        }catch (Exception ignored){}
        return null;
    }

    //other

    public static String gFont = "font";
    private static void BestFix(Label label)
    {
        if (label.getWidth()==0){
            GDX.Error(label.getText()+":can't fix cause width=0");
            return;
        }
        float scale = label.getWidth()/label.getPrefWidth();
        if (label.getWrap())
        {
            label.validate();
            float w = label.getGlyphLayout().width,h=label.getGlyphLayout().height,w0=label.getWidth(),h0=label.getHeight();
            scale = (float) Math.sqrt((w0*h0)/(w*h));
            if (scale>1) scale = Math.min(w0/w,h0/h);
        }
        if (scale>1) scale = 1;
        label.setFontScale(scale*label.getFontScaleX());
    }
    public static Label New(String content,String font)
    {
        return new Label(content,new Label.LabelStyle(Assets.GetFont(font),Color.WHITE));
    }
    public static Label New(String content)
    {
        return New(content,gFont);
    }
}
