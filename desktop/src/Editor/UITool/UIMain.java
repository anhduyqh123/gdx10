package Editor.UITool;

import Editor.JFameUI;
import Editor.UITool.Form.MainForm;
import Editor.UITool.Form.Panel.ContentPanel;
import GameGDX.GUIData.GUIData;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTFrame;

import javax.swing.*;
import java.awt.*;

public class UIMain {
    public static void main (String[] arg) {
        SwingUtilities.invokeLater(()->{
            new UIMain();
        });
    }

    private JFameUI ui = new JFameUI();
    public UIMain()
    {
        JFrame frame = ui.NewJFrame("ui editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(0,0);

        NewContent();

        Config config = Config.NewConfig();
        MyGame game = NewGame(config.game_width,config.game_height,()->{
            new GUIData();
            MainForm main = new MainForm();
            frame.add(main.pnMain);
            frame.pack();
        });
        JFrame gameFrame = new LwjglAWTFrame(game,"game",config.screen_width,config.screen_height);
        gameFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gameFrame.setLocation(screenSize.width-config.screen_width,0);
        gameFrame.setAlwaysOnTop(true);
    }
    protected MyGame NewGame(int width, int height, Runnable done)
    {
        return new MyGame(width, height, done);
    }
    protected void NewContent()
    {
        ContentPanel.i = new ContentPanel();
    }
}
