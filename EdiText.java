import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class EdiText extends JFrame implements ActionListener{
    JFrame frame;
    JTextArea area;
    JTextField field;
    JMenuBar mb;
    JLabel label;
    JPanel panel;
    JTextField find;
    JTextField replace;
    JButton button;
    Undo undoAction = new Undo();
    Boolean isChecked = false;
    StateChain chain = new StateChain();         // will use for state pattern
    ModeFactory modes = new ModeFactory();       // will use for Factory method pattern
    Boolean isClicked = false;

    public EdiText() {
        frame = new JFrame();
        frame.setTitle("EdiText");
        frame.setResizable(false);         // for screen coulnd't enlarge

        area = new JTextArea();
        area.setLineWrap(true);            // for move to bottom line
        area.setBounds(50, 50, 200,200);

        mb = new JMenuBar();
        mb.setBackground(Color.GRAY);

        field = new JTextField();
        field.setLocation(100,100);

        JMenu fileMenu = new JMenu("File");       // creating file menu
        JMenuItem m1 = new JMenuItem("New");
        JMenuItem m2 = new JMenuItem("Open");
        JMenuItem m3 = new JMenuItem("Save");
        JMenuItem m4 = new JMenuItem("Close");

        m1.addActionListener(this);       // add actions to menu items
        m2.addActionListener(this);
        m3.addActionListener(this);
        m4.addActionListener(this);

        fileMenu.add(m1);
        fileMenu.add(m2);
        fileMenu.add(m3);
        fileMenu.add(m4);

        JMenu editMenu = new JMenu("Edit");          // creating edit menu
        JMenuItem m5 = new JMenuItem("Cut");
        JMenuItem m6 = new JMenuItem("Copy");
        JMenuItem m7 = new JMenuItem("Paste");

        m5.addActionListener(this);          // add actions to menu items
        m6.addActionListener(this);
        m7.addActionListener(this);

        editMenu.add(m5);
        editMenu.add(m6);
        editMenu.add(m7);

        JMenu checkMenu = new JMenu("Check");        // creating check menu
        JMenuItem m8 = new JMenuItem("Fix All");

        m8.addActionListener(this);                // add actions to menu items
        checkMenu.add(m8);

        JMenu undoMenu = new JMenu("Undo");         // creating undo menu
        JMenuItem m9 = new JMenuItem("Undo");

        m9.addActionListener(this);            // add actions to menu items
        undoMenu.add(m9);

        JMenu colorMenu = new JMenu("Design");     // creating design menu
        JMenuItem m10 = new JMenuItem("Change Background");
        JMenuItem m11 = new JMenuItem("Change Menu Color");
        JMenuItem m12 = new JMenuItem("Change Font Color");

        m10.addActionListener(this);          // add actions to menu items
        m11.addActionListener(this);
        m12.addActionListener(this);

        colorMenu.add(m10);
        colorMenu.add(m11);
        colorMenu.add(m12);

        JMenu modeMenu = new JMenu("Mode");           // creating mode menu

        JMenuItem darkMode = new JMenuItem("Dark Mode");
        JMenuItem lightMode = new JMenuItem("Light Mode");
        JMenuItem vincentMode = new JMenuItem("Vincent Mode");
        JMenuItem natureMode = new JMenuItem("Nature Mode");

        darkMode.addActionListener(this);             // add actions to menu items
        lightMode.addActionListener(this);
        vincentMode.addActionListener(this);
        natureMode.addActionListener(this);

        modeMenu.add(darkMode);
        modeMenu.add(lightMode);
        modeMenu.add(vincentMode);
        modeMenu.add(natureMode);
/*

        JMenu findReplaceMenu = new JMenu("Find & Replace");

        find = new JTextField("Find");
        replace = new JTextField("Replace");


        find.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!isClicked){
                    find.setText("");
                    replace.setText("");
                }
                isClicked = true;
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        button = new JButton("*");
        button.addActionListener(this);

        findReplaceMenu.add(find);
        findReplaceMenu.add(replace);
        findReplaceMenu.add(button);
*/
        JMenu findMenu = new JMenu("Find & Replace");
        JMenuItem m13 = new JMenuItem("Find-Replace");
        m13.addActionListener(this);
        findMenu.add(m13);

        panel = new JPanel();
        panel.setBounds(0, 410, 500, 30);
        label = new JLabel(" ");


        area.addKeyListener(new KeyListener() {          // while taking a key from keyboard
            @Override
            public void keyTyped(KeyEvent e) {
                undoAction.execute(area.getText());      // through Command pattern, text in the area adding to Command.stack

                // if text is checked, label text will update and current state will pass to next state
                if(isChecked){
                    chain.doAction();
                    label.setText(chain.getName());
                    isChecked = false;       // variable isChecked is update
                }
            }

            @Override
            public void keyPressed(KeyEvent e) { }

            @Override
            public void keyReleased(KeyEvent e) { }
        });
        panel.add(label);


        mb.add(fileMenu);     // menus are added to menubar
        mb.add(editMenu);
        mb.add(undoMenu);
        mb.add(checkMenu);
        mb.add(colorMenu);
        mb.add(modeMenu);
//        mb.add(findReplaceMenu);
        mb.add(findMenu);

        frame.add(panel);
        frame.setJMenuBar(mb);     // panel, text area and menubar are added to frame
        frame.add(area);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      // when the user click on close button, the EdiText is stopped
        frame.setSize(500,500);
        frame.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();

        if (command.equals("Cut")){       // for cut the choosen text
            area.cut();
        }
        else if (command.equals("Copy")){   // for copy the choosen text
            area.copy();
        }
        else if (command.equals("Paste")){    // for paste already copied or cut text
            area.paste();
        }
        else if (command.equals("New")){      // for create new page
            area.setText("");
        }
        else if (command.equals("Open")){     // for open already exist file

            JFileChooser fc = new JFileChooser("f:");
            int r = fc.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                File file = new File(fc.getSelectedFile().getAbsolutePath());
                try {
                    String s1 = "", sline = "";
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    sline = br.readLine();
                    while ((s1 = br.readLine()) != null) {
                        sline = sline + "\n" + s1;
                    }
                    area.setText(sline);
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(frame, evt.getMessage());
                }
            }

            else
                JOptionPane.showMessageDialog(frame, "You Cancelled The Operation!");
        }
        else if (command.equals("Save")){      // for save all changes
            JFileChooser fc = new JFileChooser("f:");
            int r = fc.showSaveDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {
                File file = new File(fc.getSelectedFile().getAbsolutePath());

                try {
                    FileWriter wr = new FileWriter(file, false);
                    BufferedWriter w = new BufferedWriter(wr);

                    w.write(area.getText());

                    w.flush();
                    w.close();
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(frame, evt.getMessage());
                }
            }
            else
                JOptionPane.showMessageDialog(frame, "You Cancelled The Operation!");
        }
        else if (command.equals("Close")){        // for close the file
            frame.setVisible(false);

        }
        else if (command.equals("Fix All")){         // for fixing wrong written words
            // we are created an object from TextClass and used check() method
            // and then set the fixed text.

            String newText = TextClass.check(area.getText().toLowerCase());
            area.setText(newText);

            chain.doAction();                // after check process, current state pass the next state
            label.setText(chain.getName());
            isChecked = true;

        }
        else if (command.equals("Undo")){       // according to cursor location, the transaction made is undone.
            area.setText(undoAction.undo());     // calling undo() method from the object of Undo class.

        }
        else if (command.equals("Change Background")) {     // for changing background color
            Color c=JColorChooser.showDialog(this,"Choose",Color.LIGHT_GRAY);
            area.setBackground(c);
        }
        else if (command.equals("Change Menu Color")) {       // for changing menu color
            Color c=JColorChooser.showDialog(this,"Choose",Color.GRAY);
            mb.setBackground(c);
        }
        else if (command.equals("Change Font Color")){          // for changing font color
            Color c=JColorChooser.showDialog(this,"Choose",Color.BLACK);
            area.setForeground(c);
        }
        else if (command.equals("Dark Mode")) {         // for setting dark mode
            modes.createMode("dark mode");           // calling createMode() method from object of ModeFactory class
        }
        else if (command.equals("Light Mode")) {        // for setting light mode
            modes.createMode("light mode");          // calling createMode() method from object of ModeFactory class
        }
        else if (command.equals("Vincent Mode")) {      // for setting vincent mode
            modes.createMode("vincent mode");        // calling createMode() method from object of ModeFactory class
        }
        else if (command.equals("Nature Mode")) {       // for setting nature mode
            modes.createMode("nature mode");         // calling createMode() method from object of ModeFactory class
        }
/*        else if (command.equals("*")){
            TextClass.findReplace(area, find.getText(), replace.getText());
            find.setText("Find");
            replace.setText("Replace");
            isClicked=false;
        }*/
        else if (command.equals("Find-Replace")) {
            String findW = JOptionPane.showInputDialog(frame, "Find Word: ");
            String replaceW = JOptionPane.showInputDialog(frame, "Replace Word: ");

            String newText =TextClass.findReplace(area.getText(), findW, replaceW);
            area.setText(newText);
        }
    }
}
