package Editor.LanguageTool;

import Editor.JFameUI;
import GameGDX.Language;
import com.badlogic.gdx.files.FileHandle;

import javax.swing.*;

public class LanguageMain {
    public static void main (String[] arg) {
        SwingUtilities.invokeLater(()->{
            new LanguageMain();
        });
    }

    public static JPanel New(Language language,Runnable onSave)
    {
        return new MainForm(language,onSave).panel1;
    }
    private Language language;
    public LanguageMain()
    {
        JFameUI ui = new JFameUI();
        language = GetLanguage();
        ui.NewJFrame("Language",New(language,this::Save)).
                setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    private Language GetLanguage()
    {
        try {
            String data = new FileHandle("translate.txt").readString();
            return Language.NewLanguage(data);
        }catch (Exception e){}
        return new Language();
    }
    private void Save()
    {
        String stData = language.ToJsonData();
        FileHandle file = new FileHandle("translate.txt");
        file.writeString(stData,false);
    }
}
