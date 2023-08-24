import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
class PersonalInfo {
    String name;
    String address;
    String phoneNumber;

    PersonalInfo(String name, String address, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    void display() {
        JOptionPane.showMessageDialog(null, "Name: " + name + "\nAddress: " + address + "\nPhone no: " + phoneNumber);
    }
}

class Address_Book {
    ArrayList persons;
    Address_Book() {
        persons = new ArrayList();
        loadPersons();
    }
    void show(String s)
    {

        JOptionPane.showMessageDialog(null,s);
    }
    boolean isNumeric(String s)
    {
        int a;
        if(s==null||s.equals(""))
        {
            show("enter number");
            return false;
        }
        try
        {
            a=Integer.parseInt(s);
            return true;
        }
        catch(NumberFormatException e){}
        show("enter number");
        return false;
    }
    void addPerson()
    {
        JPanel panel=new JPanel(new GridLayout(5,3));
        JTextField tname=new JTextField(10);
        JTextField tadd=new JTextField(10);
        JTextField tpnum=new JTextField(10);
        JLabel lname=new JLabel("Enter Name");
        JLabel ladd=new JLabel("Enter Address");
        JLabel lpnum=new JLabel("Enter Phone Number");
        panel.add(lname);
        panel.add(tname);
        panel.add(ladd);
        panel.add(tadd);
        panel.add(lpnum);
        panel.add(tpnum);
        JOptionPane.showMessageDialog(null,panel);
        String name,add,pNum;
        name=tname.getText();
        add=tadd.getText();
        pNum=tpnum.getText();
        if((name.equals(""))||name==null)
        {
            show("name is mandatory");
            return;
        }
        if(isNumeric(pNum)){

            contactinfo p = new contactinfo(name, add, pNum);
            persons.add(p);
        }
    }

     void searchPerson(String n) {
        int i;
        for (i = 0; i < persons.size(); i++) {
            contactinfo p = (contactinfo) persons.get(i);
            if (n.equals(p.name)) {
                p.display();break;
            }
        }
        if(i==persons.size())
        {
            show(n+"\'s contact is not found");
        }
    }
    void showAll()
    {
        new secondframe(persons);
    }

    // deleting a person
    void deletePerson(String n) {
        int i;
        int flag=0;
        for (i = 0; i < persons.size(); i++) {
            contactinfo p = (contactinfo) persons.get(i);
            if (n.equals(p.name)) {
                persons.remove(i);flag=1;i--;
            }
        }
        if(flag==1)
        {
            show("contact with name "+n+" is deleted");
        }
        else
        {
            show("contact with name "+n+" not found");
        }
    }


    void savePersons() {
        try {
            contactinfo p;
            String line;
            FileWriter fw = new FileWriter("persons.txt");
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < persons.size(); i++) {
                p = (contactinfo) persons.get(i);
                line = p.name + "," + p.address + "," + p.phoneNumber;
                // write line to persons.txt
                pw.println(line);
            }
            pw.flush();
            pw.close();
            fw.close();
        } catch(IOException ioEx) {
            System.out.println(ioEx);
        }
    }

    void loadPersons() {
        String tokens[] = null;
        String name, add, ph;
        try {
            FileReader fr = new FileReader("persons.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                tokens = line.split(",");
                name = tokens[0];
                add = tokens[1];
                ph = tokens[2];
                contactinfo p = new contactinfo(name, add, ph);

                persons.add(p);
                line = br.readLine();
            }
            br.close();
            fr.close();
        } catch(IOException ioEx) {
            System.out.println(ioEx);
        }
    }

}

class secondframe extends JFrame {

    public secondframe(ArrayList persons) {
        String[][] data = new String[persons.size()][3];
        for (int i = 0; i < persons.size(); i++) {
            PersonInfo p;
            p = (PersonInfo) persons.get(i);
            data[i][0] = p.name;
            data[i][1] = p.address;
            data[i][2] = p.phoneNumber;
        }
        String[] heading = {"NAME", "ADDRESS", "PHONE NUMBER"};
        JTable table = new JTable(data, heading);
        add(new JScrollPane(table));
        JButton b = new JButton("Close");
        b.addActionListener(e ->
        {
            new firstframe();
            dispose();
        });
        add(b);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        setVisible(true);
        setSize(500, 530);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
public class phonebook
{
    public static void main(String args[])
    {
        firstframe cl=new firstframe();
    }
}
class firstframe extends JFrame
{
    public firstframe()
    {
        AddressBook ab = new AddressBook();
        setLayout(new FlowLayout(FlowLayout.CENTER,10,15));
        setVisible(true);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent windowEvent)
            {
                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    ab.savePersons();
                    dispose();
                }
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(300,220));
        JButton addContact=new JButton("Add New Contact");
        JButton search=new JButton("Search For a Contact");
        JButton delete=new JButton("Delete a Contact");
        JButton showAll=new JButton("Show All The Contacts");
        add(addContact);
        add(search);
        add(delete);
        add(showAll);
        addContact.addActionListener(e->
        {
            ab.addPerson();
            ab.savePersons();
        });
        search.addActionListener(e->
        {

            String s = JOptionPane.showInputDialog("Enter name to search:");
            ab.searchPerson(s);
        });
        delete.addActionListener(e->
        {
            String s = JOptionPane.showInputDialog("Enter name to delete:");
            ab.deletePerson(s);
            ab.savePersons();

        });
        showAll.addActionListener(e->
        {
            ab.showAll();
            dispose();
        });
    }
}
