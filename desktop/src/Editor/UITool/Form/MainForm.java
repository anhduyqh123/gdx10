package Editor.UITool.Form;

import GameGDX.GUIData.GUIData;

import javax.swing.*;

public class MainForm {
    public JPanel pnMain;
    private JPanel pnTop;
    private JPanel pnBot;
    private JTabbedPane tabbedPane;

    public MainForm()
    {
        DataForm dataForm = new DataForm();
        IActorForm iActorForm = new IActorForm();
        ActionForm actionForm = new ActionForm();

        dataForm.onSelect = (iActor,list)->{
            iActorForm.SetData(iActor,list);

            actionForm.SetData(iActor.acList);
            actionForm.onRun = name->{
                iActor.StopAction();
                iActor.RunAction(name);
            };
            actionForm.onStop = iActor::StopAction;
        };

        OptionForm optionForm = new OptionForm();
        optionForm.getObjects = GUIData.i::GetAll;
        optionForm.onClosePack = dataForm::RefreshData;

        iActorForm.isDrag = optionForm::IsDrag;

        pnTop.add(dataForm.panel1);
        pnTop.add(optionForm.panel1);

        tabbedPane.add("Info", iActorForm.panel1);
        tabbedPane.add("Action",actionForm.panel1);
    }
}
