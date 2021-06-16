package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.Config;

import javax.swing.*;
import java.util.Arrays;

public class ConfigForm {
    private JPanel panel1;
    private JPanel pnConfig;
    private JButton btSave;
    private JButton btLoad;

    private JFameUI ui = new JFameUI();

    public ConfigForm()
    {
        JFrame jFrame = ui.NewJFrame("config",panel1);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        ui.InitComponents(Arrays.asList("screen_width","screen_height",
                "game_width","game_height"),Config.i,pnConfig);

        Click(btSave,()->{
            Config.Save();
            ui.NewDialog("Save Success!",panel1);
        });
        Click(btLoad,()->Config.i.Reload());
    }
    private void Click(JButton bt,Runnable run)
    {
        bt.addActionListener(e->ui.Try(run,panel1));
    }
}
