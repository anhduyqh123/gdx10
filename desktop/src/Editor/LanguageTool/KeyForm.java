package Editor.LanguageTool;

import GameGDX.GDX;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyForm {
    private JLabel label;
    private JButton button;
    private JTextField textField1;
    public JPanel panel1;

    public KeyForm()
    {

    }
    public void SetButton(Runnable run)
    {
        button.addActionListener(e->run.run());
    }
    public void SetKey(String key, GDX.Runnable<String> onChange)
    {
        SetText(key);
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER)
                    onChange.Run(textField1.getText());
            }
        });
    }
    public void SetText(String text)
    {
        textField1.setText(text);
    }
    public void SetText(String text, GDX.Runnable<String> onChange)
    {
        SetText(text);
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onChange.Run(textField1.getText());
            }
        });
    }
}
