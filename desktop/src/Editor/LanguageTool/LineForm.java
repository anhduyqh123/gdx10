package Editor.LanguageTool;

import GameGDX.GDX;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LineForm {
    public JPanel panel1;
    private JLabel label;
    private JButton button;
    private JTextArea textArea;

    public LineForm(String name)
    {
        label.setText(name);
    }
    public void SetButton(String name,Runnable run)
    {
        button.setText(name);
        button.addActionListener(e->run.run());
    }
    public void SetKey(String key,GDX.Runnable<String> onChange)
    {
        SetText(key);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER)
                    onChange.Run(textArea.getText());
            }
        });
    }
    public void SetText(String text)
    {
        textArea.setText(text);
    }
    public void SetText(String text, GDX.Runnable<String> onChange)
    {
        SetText(text);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onChange.Run(textArea.getText());
            }
        });
    }
}
