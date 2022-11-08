package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.LanguageTool.GlyphsForm;
import Editor.UITool.MyGame;
import Extend.Box2d.GBox2d;
import Extend.GShape.GShapeRenderer;
import GameGDX.*;
import GameGDX.AssetLoading.AssetNode;
import GameGDX.GUIData.ILabel;
import com.badlogic.gdx.graphics.Color;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class OptionForm {
    private JButton BGColorButton;
    public JPanel panel1;
    private JButton configButton;
    private JComboBox cbCode;
    private JButton btGlyphs;
    private JComboBox cbFont;
    private JCheckBox cbPhysics;
    private JCheckBox cbDrag;
    private JCheckBox cbBox2d;

    public GDX.Func<List<String>> getObjects;
    public Runnable onClosePack;

    private GShapeRenderer shapeRenderer = new GShapeRenderer(Scene.ui);

    private JFameUI ui = new JFameUI();

    public OptionForm(){

        configButton.addActionListener(e->new ConfigForm());
        BGColorButton.addActionListener(e->{
            ui.NewColorChooserWindow(Color.WHITE,hex->{
                MyGame.i.bg.set(Color.valueOf(hex));
            });
        });
        try {
            InitTranslate();
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
    private void InitTranslate()
    {
        Translate translate = GetTranslate();
        if (translate.codes.size()>0)
        {
            translate.SetCode(1);
            InitTranslate(translate);
        }
        btGlyphs.addActionListener(e-> Glyphs());
    }
    private Translate GetTranslate()
    {
        try {
            return new Translate(Assets.GetNode("translate").url);
        }catch (Exception e){}
        return new Translate();
    }
    private void InitTranslate(Translate translate)
    {
        String[] codes = translate.codes.toArray(new String[0]);
        if (codes.length<=0) return;
        String code = translate.code;
        ui.ComboBox(cbCode,codes,code, translate::SetCode);
    }

    private void Glyphs()
    {
        String code = (String) cbCode.getSelectedItem();
        HashSet<Character> hashSet = new HashSet<>();
        Json.Foreach(Translate.i.GetData(),i->{
            String st = i.getString(code);
            for (char c : st.toCharArray())
                hashSet.add(c);
        });
        String s0 = "";
        for (Character c : hashSet)
            s0+=c;
        new GlyphsForm(s0);
    }
}
