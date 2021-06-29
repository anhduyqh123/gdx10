package Editor.UITool.Form;

import Editor.JFameUI;
import Editor.UITool.Form.Panel.ContentPanel;
import Editor.UITool.Form.Panel.IPosPanel;
import GameGDX.GUIData.IChild.IActor;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

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
    public void SetData(IActor iActor, List<String> list)
    {
        pInfo.removeAll();
        pSize.removeAll();
        pPos.removeAll();

        ContentPanel.i.SetContent(pInfo, iActor);
        ui.InitComponents(Arrays.asList("width","height","origin","scale","rotate","extendScreen","fillW","fillH")
                ,iActor.iSize,pSize);
        if (iActor.GetActor()!=null)
            ui.NewCheckBox("debug", iActor.GetActor().getDebug(), pSize,
                vl-> iActor.GetActor().setDebug(vl));

        new IPosPanel(list,iActor.iPos,pPos);

        onRefresh = ()->{
            iActor.Refresh();
            SetData(iActor,list);
        };

        ui.Repaint(panel1);
    }
}
