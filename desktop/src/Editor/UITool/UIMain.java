package Editor.UITool;

import Editor.JFameUI;
import Editor.UITool.Form.MainForm;
import Editor.UITool.Form.Panel.ContentPanel;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

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

        UIConfig uiConfig = UIConfig.NewConfig();
        MyGame game = NewGame(uiConfig.game_width, uiConfig.game_height,()->{
            MainForm main = new MainForm();
            frame.add(main.pnMain);
            frame.pack();
        });
//        JFrame gameFrame = new LwjglAWTFrame(game,"game", uiConfig.screen_width, uiConfig.screen_height);
//        gameFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        gameFrame.setLocation(screenSize.width- uiConfig.screen_width,0);
//        gameFrame.setAlwaysOnTop(true);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = uiConfig.screen_width;
		config.height = uiConfig.screen_height;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        config.x = screenSize.width- uiConfig.screen_width;
        config.y = 0;
        LwjglApplication gameFrame = new LwjglApplication(game, config);
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
