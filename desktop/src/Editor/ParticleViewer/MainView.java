package Editor.ParticleViewer;

import Editor.JFameUI;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import javax.swing.*;
import java.awt.*;

public class MainView {

    public static void main (String[] arg) {
        SwingUtilities.invokeLater(()->{
            new MainView();
        });
    }

    public MainView()
    {
        JFameUI ui = new JFameUI();
        JFrame frame = ui.NewJFrame("ui editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        InitGame(()->{
            ViewerForm form = new ViewerForm();
            frame.add(form.panel1);
            frame.pack();
        });
    }
    private void InitGame(Runnable done)
    {
        MyGame game = new MyGame(done);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 360;
        config.height = 360;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        config.x = screenSize.width- 360;
        config.y = 0;
        new LwjglApplication(game, config);
    }
}
