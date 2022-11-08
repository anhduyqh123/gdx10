package Editor.UITool.Form;

import Editor.JFameUI;

import javax.swing.*;

public class GlyphsForm {
    private JTextArea ta;
    private JPanel panel1;
    public GlyphsForm(String st)
    {
        JFameUI ui = new JFameUI();
        JFrame jFrame = ui.NewJFrame("config",panel1);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ta.setText(st);
    }
}
