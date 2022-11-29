package Editor;

import GameGDX.GDX;
import GameGDX.Reflect;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class JFameUI {

    //<editor-fold desc="Object">
    public List<String> GetDeclaredFields(Class type)
    {
        List<String> list = new ArrayList<>();
        for(Field f : Reflect.GetDeclaredFields(type))
            if (Reflect.IsValidField(f)) list.add(f.getName());
        return list;
    }
    public List<String> GetDeclaredFields(Object object)
    {
        return GetDeclaredFields(object.getClass());
    }
    public List<String> GetFields(Object object)
    {
        List<String> list = new ArrayList<>();
        for(Field f : Reflect.GetFields(object.getClass()).values())
            list.add(f.getName());
        return list;
    }
    public void InitComponents(Object object,JPanel parent)
    {
        for(Field f : Reflect.GetFields(object.getClass()).values())
            NewComponent(f,object,parent);
    }
    public void InitComponents(List<String> fieldNames, Object object, JPanel parent)
    {
        for(String name : fieldNames)
            NewComponent(Reflect.GetFields(object.getClass()).get(name),object,parent);
    }
    public void NewComponent(String name,Object object, JPanel parent)
    {
        NewComponent(Reflect.GetField(object.getClass(),name),object,parent);
    }
    public void NewComponent(Field field, Object object, JPanel parent)
    {
        Class type = field.getType();
        if (type.isInterface()) return;
        String name = field.getName();
        try
        {
            Object value = field.get(object);
            if (value==null) return;
            if (type == boolean.class) {
                NewCheckBox(name,(boolean)value,parent,result-> SetField(field,object,result));
                return;
            }
            if (type.isEnum()){
                Enum[] constants = (Enum[])type.getEnumConstants();
                NewComboBox(name,constants,value,parent,vl->SetField(field,object,vl));
                return;
            }
            if (type.equals(String.class))
            {
                NewTextArea(name,value,80,parent,st->SetField(field,object,st));
                return;
            }
            if (Reflect.IsBaseType(type))
            {
                int w = 50;
                if (type == String.class) w = 80;
                NewTextField(name,value,w,parent,st->SetField(field,object,st));
                return;
            }
            InitComponents(value, NewPanel(name,parent));

        }catch (ReflectionException e){e.printStackTrace();}
    }
    public JTextArea NewTextArea(String name,Object value,int width,JPanel parent,GDX.Runnable<String> onChange)
    {
        return NewTextArea(name,value.toString(), width,20,parent,onChange);
    }
    public JTextArea NewTextArea(String name,Object value,int width,int height,JPanel parent,GDX.Runnable<String> onChange)
    {
        JTextArea textField = NewTextArea(value.toString(), width,height);
        LabelComponent(name,textField,parent);
        if (onChange!=null)
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        onChange.Run(textField.getText());
                    }catch (Exception x){}
                }
            });
        return textField;
    }
    private JTextArea NewTextArea(String value,int width,int height)
    {
        JTextArea textField = new JTextArea(value);
        SetSize(textField,width,height);
        return textField;
    }

    public void SetField(Field field,Object object,Object value)
    {
        Class<?> type = field.getType();
        if (type == int.class) {
            Reflect.SetValue(field,object,Integer.valueOf(value.toString()));
            return;
        }
        if (type == float.class) {
            Reflect.SetValue(field,object,Float.valueOf(value.toString()));
            return;
        }
        Reflect.SetValue(field,object,value);
    }
    //</editor-fold>
    //<editor-fold desc="Panel">
    public void AutoFixSize(JPanel panel)
    {
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
    }

    public JPanel NewPanel(JPanel parent)
    {
        JPanel panel = new JPanel();
        parent.add(panel);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setMaximumSize(new Dimension(parent.getWidth(),1000));
        panel.setLayout(new WrapLayout());
        return panel;
    }
    public JPanel NewPanel(String name,JPanel parent)
    {
        JPanel pn = NewPanel(parent);
        pn.setBorder(BorderFactory.createTitledBorder(pn.getBorder(),name));
        return pn;
    }
    public JPanel NewPanel(int width, int height)
    {
        return NewPanel(new FlowLayout(),width,height);
    }
    public JPanel NewPanel(LayoutManager layout, int width, int height)
    {
        JPanel jPanel = new JPanel(layout);
        SetSize(jPanel,width,height);
        return jPanel;
    }
    public JPanel NewPanel(LayoutManager layout,int width,int height,JPanel parent)
    {
        JPanel jPanel = NewPanel(layout, width, height);
        parent.add(jPanel);
        return jPanel;
    }
    public JPanel NewPanel(int width,int height,JPanel parent)
    {
        return NewPanel(new FlowLayout(FlowLayout.LEFT),width, height, parent);
    }
    public JPanel NewBorderPanel(int width,int height,JPanel parent)
    {
        JPanel jPanel = NewPanel(width, height, parent);
        jPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return jPanel;
    }
    public JPanel NewBigPanel(String name,int width,int height,JPanel parent)
    {
        JPanel jPanel = NewPanel(new FlowLayout(FlowLayout.CENTER),width, height, parent);
        SetBorder(name,jPanel);
        return jPanel;
    }
    public void SetBorder(String name, JPanel panel)
    {
        panel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(1, 0, 1, 1),
                BorderFactory.createTitledBorder(name)));
    }
    public void SetBorder(JPanel panel)
    {
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    //</editor-fold>
    //<editor-fold desc="Label">
    public JLabel NewLabel(String name,JPanel parent)
    {
        JLabel label = new JLabel(name);
        parent.add(label);
        return label;
    }
    //</editor-fold>
    //<editor-fold desc="TextField">
    public void TextField(JTextComponent textField, String value, GDX.Runnable<String> onChange)
    {
        textField.setText(value);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    onChange.Run(textField.getText());
                }catch (Exception ex){}
            }
        });
    }
    public void TextField(JTextField textField, String value, GDX.Runnable<String> onChange)
    {
        textField.setText(value);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    onChange.Run(textField.getText());
                }catch (Exception ex){}
            }
        });
    }

    private JTextField NewTextField(String value,int width,int height)
    {
        JTextField textField = new JTextField(value);
        SetSize(textField,width,height);
        return textField;
    }
    public JTextField NewTextField(String name,Object value,int width,int height,JPanel parent,GDX.Runnable<String> onChange)
    {
        JTextField textField = NewTextField(value.toString(), width,height);
        LabelComponent(name,textField,parent);
        if (onChange!=null)
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        onChange.Run(textField.getText());
                    }catch (Exception x){}
                }
            });
        return textField;
    }
    public JTextField NewTextField(String name,Object value,int width,JPanel parent,GDX.Runnable<String> onChange)
    {
        return NewTextField(name,value.toString(), width,20,parent,onChange);
    }
    public JTextField NewTextField(String name,Object value,int width,JPanel parent)
    {
        return NewTextField(name,value.toString(), width,parent,null);
    }
    public JTextField NewTextField(String name,Object value,JPanel parent)
    {
        return NewTextField(name, value, parent,null);
    }
    public JTextField NewTextField(String name, Object value, JPanel parent, GDX.Runnable<String> onChange)
    {
        return NewTextField(name, value, 80, parent,onChange);
    }
    //</editor-fold>

    //<editor-fold desc="Button">
    public JButton NewButton(String name,int width,int height,JPanel parent)
    {
        JButton button = NewButton(name,parent);
        SetSize(button,width,height);
        return button;
    }
    public JButton NewButton(String name,int width,int height,JPanel parent,Runnable onClick)
    {
        JButton button = NewButton(name, width, height, parent);
        button.addActionListener(e->onClick.run());
        return button;
    }
    public JButton NewButton(String name,int width,JPanel parent,Runnable onClick)
    {
        return NewButton(name,width,30,parent,onClick);
    }
    public JButton NewButton(String name,int width,JPanel parent)
    {
        JButton button = NewButton(name,width,30,parent);
        return button;
    }
    public JButton NewButton(String name,JPanel parent)
    {
        JButton button = new JButton(name);
        parent.add(button);
        return button;
    }
    public JButton NewButton(String name,JPanel parent,Runnable onClick)
    {
        JButton button = new JButton(name);
        button.addActionListener(e -> onClick.run());
        parent.add(button);
        return button;
    }
    //</editor-fold>
    //<editor-fold desc="ScrollPane">
    public JScrollPane NewScrollPane(Component view,int width,int height)
    {
        JScrollPane scrollPane = new JScrollPane(view);
        SetSize(scrollPane,width,height);
        return scrollPane;
    }
    public JScrollPane NewScrollPane(Component view,int width,int height,JPanel parent)
    {
        JScrollPane scrollPane = NewScrollPane(view, width, height);
        parent.add(scrollPane);
        return scrollPane;
    }
    public JList NewList(GDX.Runnable<String> onSelect)
    {
        JList list = new JList<>();
        list.addListSelectionListener(e-> {
            if(e.getValueIsAdjusting()) return;
            if (list.getSelectedValue()==null) return;
            String data = list.getSelectedValue().toString();
            onSelect.Run(data);
        });
        return list;
    }
    public void ListSetModel(JList list, List<String> data)
    {
        DefaultListModel l = new DefaultListModel<>();
        for(String key : data)
            l.addElement(key);
        list.setModel(l);
    }
    //</editor-fold>
    //<editor-fold desc="CheckBox">
    public JCheckBox NewCheckBox(String name, boolean value, JPanel parent, GDX.Runnable<Boolean> onChange)
    {
        JCheckBox checkBox = NewCheckBox(value, parent,onChange);
        checkBox.setText(name);
        return checkBox;
    }
    public JCheckBox NewCheckBox(String name,boolean value,JPanel parent)
    {
        JCheckBox checkBox = NewCheckBox(value, parent);
        checkBox.setText(name);
        return checkBox;
    }
    public JCheckBox NewCheckBox(boolean value,JPanel parent,GDX.Runnable<Boolean> onChange)
    {
        JCheckBox checkBox = NewCheckBox(value, parent);
        checkBox.addActionListener(e->onChange.Run(checkBox.isSelected()));
        return checkBox;
    }
    public JCheckBox NewCheckBox(boolean value,JPanel parent)
    {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(value);
        parent.add(checkBox);
        return checkBox;
    }
    public JCheckBox NewCheckBox(boolean value)
    {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(value);
        return checkBox;
    }
    //</editor-fold>
    //<editor-fold desc="ComboBox">
    public <T> void ComboBox(JComboBox cb,T[] items)
    {
        ComboBox(cb,items,items[0]);
    }
    public <T> void ComboBox(JComboBox cb,T[] items, T value)
    {
        cb.setModel(new DefaultComboBoxModel(items));
        cb.setSelectedItem(value);
    }
    public <T> void ComboBox(JComboBox cb,T[] items, T value,GDX.Runnable<T> onChange)
    {
        ComboBox(cb,items,value);
        for (ActionListener e : cb.getActionListeners())
            cb.removeActionListener(e);
        cb.addActionListener(e->onChange.Run((T)cb.getSelectedItem()));
        cb.setSelectedItem(value);
        //onChange.Run(value);
    }
    public <T> JComboBox NewComboBox(T[] items, T value, JPanel parent,GDX.Runnable<T> onChange)
    {
        JComboBox cb = NewComboBox(items,value,parent);
        cb.addActionListener(e->onChange.Run((T)cb.getSelectedItem()));
        return cb;
    }
    public <T> JComboBox NewComboBox(T[] items, T value, int width,int height)
    {
        JComboBox comboBox = new JComboBox(items);
        comboBox.setSelectedItem(value);
        SetSize(comboBox,width,height);
        return comboBox;
    }
    public <T> JComboBox NewComboBox(T[] items, T value, JPanel parent)
    {
        return NewComboBox(items,value,120,parent);
    }
    public <T> JComboBox NewComboBox(T[] items, T value, int width, JPanel parent)
    {
        return NewComboBox(items, value, width,20, parent);
    }
    public <T> JComboBox NewComboBox(T[] items, T value, int width,int height, JPanel parent)
    {
        JComboBox comboBox = NewComboBox(items,value,width,height);
        parent.add(comboBox);
        return comboBox;
    }
    public <T> JComboBox NewComboBox(String name, T[] items, T value, int width,int height, JPanel parent)
    {
        JComboBox comboBox = NewComboBox(items,value,width,height);
        LabelComponent(name,comboBox,parent);
        return comboBox;
    }
    public <T> JComboBox NewComboBox(String name, T[] items, T value, JPanel parent)
    {
        return NewComboBox(name,items,value,120,parent);
    }
    public <T> JComboBox NewComboBox(String name, T[] items, T value, int width, JPanel parent)
    {
        return NewComboBox(name,items,value,width,20,parent);
    }
    public <T> JComboBox NewComboBox(String name, T[] items, T value, JPanel parent, GDX.Runnable<T> onChange)
    {
        JComboBox cb = NewComboBox(name,items,value,parent);
        cb.addActionListener(e->onChange.Run((T)cb.getSelectedItem()));
        return cb;
    }
    //</editor-fold>
    //<editor-fold desc="Other">
    private void LabelComponent(String text, JComponent component, JPanel parent)
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lb = new JLabel(text);
        panel.add(lb);
        panel.add(component);
        parent.add(panel);
    }
    public void SetGap(JComponent panel,int h,int v)
    {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(v);
        flowLayout.setHgap(h);
        panel.setLayout(flowLayout);
    }
    public void SetSize(JComponent component,int width,int height)
    {
        component.setPreferredSize(new Dimension(width,height));
    }
    public void NewDialog(String log,JPanel panel)
    {
        JOptionPane.showMessageDialog(panel,log);
    }
    public void Repaint(JPanel panel)
    {
        panel.revalidate();
        panel.repaint();
    }
    public void ClearPanel(JPanel panel)
    {
        panel.removeAll();
        Repaint(panel);
    }
    //</editor-fold>

    //<editor-fold desc="ColorChooser">
    private Color GDXColorToColor(com.badlogic.gdx.graphics.Color cl)
    {
        return new Color(cl.r,cl.g,cl.b,cl.a);
    }
    private com.badlogic.gdx.graphics.Color ColorToGDXColor(Color cl)
    {
        return new com.badlogic.gdx.graphics.Color(
                cl.getRed()/255f,cl.getGreen()/255f,cl.getBlue()/255f,cl.getAlpha()/255f);
    }
    public void NewColorChooserWindow(String hex, GDX.Runnable<String> onClose)
    {
        NewColorChooserWindow(com.badlogic.gdx.graphics.Color.valueOf(hex),onClose);
    }
    public void NewColorChooserWindow(com.badlogic.gdx.graphics.Color color, GDX.Runnable<String> onClose)
    {
        JColorChooser chooser = new JColorChooser();
        chooser.setColor(GDXColorToColor(color));
        NewJFrame("Color",chooser,()->onClose.Run(ColorToGDXColor(chooser.getColor()).toString()));
    }
    public void NewColorPicker(JPanel panel,String hexColor ,GDX.Runnable<String> onChance)
    {
        NewButton("Color",panel,()->NewColorChooserWindow(hexColor,onChance));
    }
    //</editor-fold>
    //<editor-fold desc="ColorChooser">
    public void NewWindow(JComponent content,int width,int height,Runnable onClosed)
    {
        EventQueue.invokeLater(()->{
            JFrame frame = NewJFrame("Test",width,height);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(content);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    onClosed.run();
                }
            });
        });
    }
    public JFrame NewWindow(String name,JComponent content,int width,int height,Runnable onClosed)
    {
        JFrame frame = NewWindow(name,content,width,height);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                onClosed.run();
            }
        });
        return frame;
    }
    public JFrame NewWindow(String name,JComponent content,int width,int height)
    {
        JFrame frame = NewJFrame(name,width,height);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(content);
        frame.pack();
        return frame;
    }
    public JFrame NewJFrame(String name,JComponent content,Runnable onClosed)
    {
        JFrame frame = NewJFrame(name,content);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                onClosed.run();
            }
        });
        return frame;
    }
    public JFrame NewJFrame(String name,JComponent content)
    {
        JFrame frame = NewJFrame(name);
        frame.add(content);
        frame.pack();
        frame.setLocationRelativeTo(null);
        return frame;
    }
    public JFrame NewJFrame(String name)
    {
        JFrame frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        return frame;
    }
    public JFrame NewJFrame(String name,int width,int height)
    {
        JFrame frame = NewJFrame(name);
        frame.setSize(width, height);
        return frame;
    }

    public void Try(Runnable run,JPanel panel)
    {
        try {
            run.run();
        }catch (Exception e)
        {
            e.printStackTrace();
            NewDialog(e.getMessage(),panel);
        }
    }
    public String[] ClassToName(Class[] arr)
    {
        String[] names = new String[arr.length];
        for(int i=0;i<arr.length;i++)
            names[i] = arr[i].getSimpleName();
        return names;
    }
    //</editor-fold>

    //new
    public void Button(JButton bt,Runnable cb)
    {
        for (ActionListener e : bt.getActionListeners())
            bt.removeActionListener(e);
        bt.addActionListener(e->cb.run());
    }
    public void JListClearListener(JList jList)
    {
        for (ListSelectionListener e : jList.getListSelectionListeners())
            jList.removeListSelectionListener(e);
    }
    public int GetInt(JTextField tf)
    {
        return Integer.parseInt(tf.getText());
    }
    public float GetFloat(JTextField tf)
    {
        return Float.parseFloat(tf.getText());
    }
}
