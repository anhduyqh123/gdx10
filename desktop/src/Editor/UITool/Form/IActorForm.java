package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.Form.Panel.ContentPanel;
import Editor.UITool.Form.Panel.IPosPanel;
import GameGDX.GUIData.IChild.IActor;

import javax.swing.*;

public class IActorForm {
    public JPanel panel1;
    private JButton btRefresh;
    private JPanel pnContent;
    private JPanel pInfo;
    private JPanel pSize;
    private JPanel pPos;

    private Runnable onRefresh;
    private JFameUI ui = new JFameUI();
    public IActorForm()
    {
        btRefresh.addActionListener(e->onRefresh.run());
    }
    public void SetData(IActor iActor)
    {
        pInfo.removeAll();
        pSize.removeAll();
        pPos.removeAll();

        ContentPanel.i.SetContent(pInfo, iActor);
        ui.InitComponents(iActor.iSize,pSize);
        if (iActor.GetActor()!=null)
            ui.NewCheckBox("debug", iActor.GetActor().getDebug(), pSize,
                vl-> iActor.GetActor().setDebug(vl));

        new IPosPanel(iActor.iPos,pPos);

        onRefresh = ()->{
            iActor.Refresh();
            SetData(iActor);
        };

        ui.Repaint(panel1);
    }
}
