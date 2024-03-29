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
        ParamForm paramForm = new ParamForm();
        FuncForm funcForm = new FuncForm();
        funcForm.refresh = dataForm::Refresh;

        dataForm.onSelect = (iActor,list)->{
            iActorForm.SetData(iActor,list);

            actionForm.SetData(iActor,iActor.acList);
            actionForm.onRun = name->{
                //iActor.StopAction();
                //iActor.acList.Init(iActor);
                iActor.RunAction(name);
            };
            actionForm.onStop = iActor::Refresh;

            paramForm.SetData(iActor.GetParamMap());
            funcForm.SetIActor(iActor);
        };

        OptionForm optionForm = new OptionForm();
        optionForm.getObjects = GUIData.i::GetAll;
        optionForm.onClosePack = dataForm::RefreshData;

        iActorForm.isDrag = optionForm::IsDrag;

        pnTop.add(dataForm.panel1);
        pnTop.add(optionForm.panel1);
        pnTop.add(paramForm.panel1);

        tabbedPane.add("Info", iActorForm.panel1);
        tabbedPane.add("Action",actionForm.panel1);
        tabbedPane.add("Param",paramForm.panel1);
        tabbedPane.add("Func",funcForm.panel1);
    }
}
