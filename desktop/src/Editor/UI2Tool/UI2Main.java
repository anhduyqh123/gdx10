package Editor.UI2Tool;

import Editor.UI2Tool.Form.Content2Panel;
import Editor.UITool.Form.Panel.ContentPanel;
import Editor.UITool.MyGame;
import Editor.UITool.UIMain;

import javax.swing.*;

public class UI2Main extends UIMain {
    public static void main (String[] arg) {
        SwingUtilities.invokeLater(()->{
            new UI2Main();
        });
    }
    protected MyGame NewGame(int width, int height, Runnable done)
    {
        return new MyGame2(width, height, done);
    }
    protected void NewContent()
    {
        ContentPanel.i = new Content2Panel();
    }
}
