package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.LanguageTool.LanguageMain;
import Editor.UITool.MyGame;
import Extend.Box2d.GBox2d;
import Extend.GShape.GShapeRenderer;
import GameGDX.*;
import GameGDX.AssetLoading.AssetNode;
import GameGDX.GUIData.ILabel;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class OptionForm {
    private JButton BGColorButton;
    public JPanel panel1;
    private JButton configButton;
    private JComboBox cbCode;
    private JButton btEdit;
    private JComboBox cbFont;
    private JCheckBox cbPhysics;
    private JCheckBox cbDrag;
    private JCheckBox cbBox2d;

    public GDX.Func<List<String>> getObjects;
    public Runnable onClosePack;

    private GShapeRenderer shapeRenderer = new GShapeRenderer(Scene.GetUICamera(),Scene.ui);

    private JFameUI ui = new JFameUI();

    public OptionForm(){

        configButton.addActionListener(e->new ConfigForm());
        BGColorButton.addActionListener(e->{
            ui.NewColorChooserWindow(Color.WHITE,hex->{
                MyGame.i.bg.set(Color.valueOf(hex));
            });
        });
        try {
            InitLanguage();
            InitSetFont();
        }catch (Exception e){}


        cbPhysics.setSelected(GBox2d.GetActive());
        cbPhysics.addActionListener(e->{
            //GBox2d.active = cbPhysics.isSelected();
            GBox2d.SetActive(cbPhysics.isSelected());
        });
        cbBox2d.setSelected(GBox2d.i.getDebug());
        cbBox2d.addActionListener(e->{
            GBox2d.i.setDebug(cbBox2d.isSelected());
        });
    }
    public boolean IsDrag()
    {
        return cbDrag.isSelected();
    }
    private void InitSetFont()
    {
        List<String> fonts = new ArrayList<>();
        for (AssetNode n : Assets.Get(AssetNode.Kind.BitmapFont))
            fonts.add(n.name);
        ui.ComboBox(cbFont,fonts.toArray(new String[0]), ILabel.gFont,vl->ILabel.gFont = vl);
    }
    private void InitLanguage()
    {
        String url = Assets.GetNode("translate").url;
        Language language = GetLanguage(url);
        if (language.GetCodes().size()>0)
        {
            language.SetCode(0);
            InitLanguage(language);
        }
        btEdit.addActionListener(e->{
            ui.NewJFrame("Language", LanguageMain.New(language, ()->GDX.WriteToFile(url,language.ToJsonData())),
                    ()->InitLanguage(language));
        });
    }
    private Language GetLanguage(String url)
    {
        try {
            return Language.NewLanguage(GDX.GetString(url));
        }catch (Exception e){}
        return Language.NewLanguage();
    }
    private void InitLanguage(Language language)
    {
        String[] codes = language.GetCodes().toArray(new String[0]);
        if (codes.length<=0) return;
        String code = Language.GetCode();
        ui.ComboBox(cbCode,codes,code,vl->language.SetCode(vl));
    }
    private void InitGrid()
    {
//        Ref<Float> size = new Ref<>(50f);
//        tfSize.setText(size.Get()+"");
//        ui.TextField(tfSize,size.Get()+"",vl->size.Set(Float.parseFloat(vl)));
//
//        btShow.addActionListener(e->{
//            if (shapeRenderer.Size()>0) HideGrid();
//            else ShowGrid(size.Get());
//        });
    }
    private void HideGrid()
    {
        shapeRenderer.Clear();
    }
    private void ShowGrid(float size)
    {
        float len = 100000;
        int column = 100,row = 100;
        for(int i=0;i<column;i++)
        {
            Vector2 pos1 = new Vector2(i*size,0);
            Vector2 pos2 = new Vector2(i*size,len);
            shapeRenderer.NewLine(pos1,pos2);
        }
        for(int i=0;i<row;i++)
        {
            Vector2 pos1 = new Vector2(0,i*size);
            Vector2 pos2 = new Vector2(len,i*size);
            shapeRenderer.NewLine(pos1,pos2);
        }
    }
}
