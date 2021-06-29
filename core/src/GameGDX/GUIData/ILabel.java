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

    //Multi Language
    private String GetValue(String value)
    {
        return value.substring(1,value.length()-1);
    }
    private List<String> GetKeys(String data)
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
    private String GetKey(String data)
    {
        try {
            int s = data.indexOf("{");
            int e = data.indexOf("}");
            if (s==-1 || e==-1) return null;
            return data.substring(s,e+1);
        }catch (Exception ignored){}
        return null;
    }

    public void SetNumber(Object value) //count %->count 1000
    {
        SetNumber(value,GetText());
    }
    public void SetNumber(Object value,String text) //count %->count 1000
    {
        Label lb = GetActor();
        if (text.contains("%")) lb.setText(text.replace("%",value+""));
        else lb.setText(value+"");
    }
    public String GetText()
    {
        if (multiLanguage){
            List<String> keys = GetKeys(text);
            if (keys.size()<=0) return Language.GetContent(text);
            String txt = text;
            for(String key : keys) txt = txt.replace(key,Language.GetContent(GetValue(key)));
            return txt;
        }
        return text;
    }
    @Override
    protected Actor NewActor() {
        return New(text,GetFontName());
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ILabel)) return false;
        if (!super.equals(object)) return false;
        ILabel iLabel = (ILabel) object;
        return Float.compare(iLabel.fontScale, fontScale) == 0 &&
                bestFix == iLabel.bestFix &&
                wrap == iLabel.wrap &&
                multiLanguage == iLabel.multiLanguage &&
                font.equals(iLabel.font) &&
                text.equals(iLabel.text) &&
                alignment == iLabel.alignment;
    }

    //static
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
            float area = label.getGlyphLayout().width*label.getGlyphLayout().height;
            float fixArea = label.getWidth()*label.getHeight();
            scale = (float) Math.sqrt(fixArea/area);
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
