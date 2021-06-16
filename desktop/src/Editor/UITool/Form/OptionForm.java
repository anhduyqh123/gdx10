package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.LanguageTool.LanguageMain;
import GameGDX.Assets;
import GameGDX.GDX;
import GameGDX.Language;
import GameGDX.Scene;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

import javax.swing.*;
import java.util.List;

public class OptionForm {
    private JButton BGColorButton;
    public JPanel panel1;
    private JButton configButton;
    private JComboBox cbCode;
    private JButton btEdit;

    public GDX.Func<List<String>> getObjects;
    public Runnable onClosePack;

    private JFameUI ui = new JFameUI();

    public OptionForm(){

        configButton.addActionListener(e->new ConfigForm());
        BGColorButton.addActionListener(e->{
            ui.NewColorChooserWindow(Color.WHITE,hex->{
                Actor bg = Scene.background.getChild(0);
                bg.setColor(Color.valueOf(hex));
            });
        });

        try {
            InitLanguage();
        }catch (Exception e){}
    }
    private void InitLanguage()
    {
        String url = Assets.GetNode("translate").url;
        Language language = Language.NewLanguage(GDX.GetString(url));
        language.SetCode(0);
        InitLanguage(language);
        btEdit.addActionListener(e->{
            ui.NewJFrame("Language", LanguageMain.New(language,
                    ()->GDX.WriteToFile(url,language.ToJsonData())),()->InitLanguage(language));
        });
    }
    private void InitLanguage(Language language)
    {
        String[] codes = language.GetCodes().toArray(new String[0]);
        if (codes.length<=0) return;
        String code = Language.GetCode();
        ui.ComboBox(cbCode,codes,code,vl->language.SetCode(vl));
    }
}
