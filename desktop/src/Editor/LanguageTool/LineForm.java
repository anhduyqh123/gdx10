package Editor.LanguageTool;

import GameGDX.GDX;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LineForm {
    public JPanel panel1;
    private JLabel label;
    private JTextField textField;
    private JButton button;

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
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER)
                    onChange.Run(textField.getText());
            }
        });
    }
    public void SetText(String text)
    {
        textField.setText(text);
    }
    public void SetText(String text, GDX.Runnable<String> onChange)
    {
        SetText(text);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onChange.Run(textField.getText());
            }
        });
    }
}
