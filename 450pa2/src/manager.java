import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class manager extends JFrame {
    private static Jdbc jdbc = new Jdbc();
    private static JFrame framelog = new JFrame();
    private static JFrame framelogwrong = new JFrame();
    private static JFrame framemain= new JFrame();
    private static JFrame framesub = new JFrame();
    private static JFrame frameadd = new JFrame();
    private static JFrame framepro = new JFrame();
    private static JFrame framedep = new JFrame();
    private static JFrame frameresult = new JFrame();

    private static JPanel loginpanel;
    private static JPanel mainMenue;
    private static Container subPanel;
    private static Container addemp;
    private static String ssn;
    private static JPanel addpro;
    private static JPanel addepedent;
    private static JTextField userText;
    private static JButton button;
    private static int leftworkHours = 40;
    private static JLabel failureProduct;

    public manager() throws SQLException, ClassNotFoundException {
        jdbc.connect();
        framelog.setTitle("managerSide");
        framelog.setDefaultCloseOperation(EXIT_ON_CLOSE);

        loginpanel = new JPanel();
        loginpanel.setPreferredSize(new Dimension(250,150));

        JLabel label = new JLabel("ssn");
        label.setBounds(40,50,80,30);
        loginpanel.add(label);

        userText = new JTextField(20);
        userText.setBounds(100,50,165,25);
        loginpanel.add(userText);

        button = new JButton("login");
        button.setBounds(100,100,100,20);
        button.addActionListener(e -> {
            try {
                framelog.setVisible(false);

                sqlstuf();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
        loginpanel.add(button);
        framelog.setSize(250,150);
        framelog.setLocationRelativeTo(null);

        framelog.add(loginpanel,BorderLayout.CENTER);



        framelog.pack();
        framelog.setVisible(true);



    }

    public void mainM(){
        framemain = new JFrame();
        mainMenue = new JPanel();
        framemain.setSize(new Dimension(250,150));

        JButton newE = new JButton("new employee");

        newE.setBounds(100,125,165,25);
        newE.addActionListener(e->{
            framemain.setVisible(false);
            addE();
        });
        mainMenue.add(newE);



        framelog.setVisible(false);

        framemain.setLocationRelativeTo(null);
        framemain.add(mainMenue,BorderLayout.CENTER);
        framemain.setVisible(true);

    }

    public void addE(){
        frameadd = new JFrame();
        leftworkHours = 40;
        Hashtable<String,JTextField> table = new Hashtable<>();
        ssn = null;

        frameadd.setSize(new Dimension(300,500));
        SpringLayout layout = new SpringLayout();
        addemp = frameadd.getContentPane();

        addemp.setLayout(layout);

        ArrayList<String> attrs = new ArrayList<>();
        JButton add = new JButton("add");
        JButton addP = new JButton("assign projects");
        JLabel wrongmessage = new JLabel("failed to added the employee");
        wrongmessage.setBounds(100,50*(attrs.size()+3),80,30);
        JCheckBox c1,c2;
        c1 = new JCheckBox("Yes");
        c2 = new JCheckBox("No");

        //add basic information
        attrs.add("fname");
        attrs.add("minit");
        attrs.add("lname");
        attrs.add("ssn");
        attrs.add("bdate");
        attrs.add("address");
        attrs.add("sex");
        attrs.add("salary");
        attrs.add("superssn");
        attrs.add("dno");
        attrs.add("email");
        int numPairs = attrs.size();
        for(int i = 0;i< attrs.size();i++){
            JLabel temp = new JLabel(attrs.get(i));

            JTextField text = new JTextField(20);
            temp.setBounds(40,100*(i+1),80,30);
            text.setBounds(100,100*(i+1),80,30);
            addemp.add(temp);
            addemp.add(text);


            layout.putConstraint(SpringLayout.WEST,temp,5,SpringLayout.WEST,addemp);
            layout.putConstraint(SpringLayout.NORTH,temp,5+30*i,SpringLayout.NORTH,addemp);
            layout.putConstraint(SpringLayout.WEST,text,80,SpringLayout.WEST,temp);
            layout.putConstraint(SpringLayout.NORTH,text,5+30*i,SpringLayout.NORTH,addemp);

            table.put(attrs.get(i),text);
        }


        add.setBounds(100,50*(attrs.size()+4),80,30);
        add.addActionListener(e->{
            try {
                ssn = table.get("ssn").getText();
                boolean result = jdbc.insertemp(table.get("fname").getText(),table.get("minit").getText(),table.get("lname").getText(),table.get("ssn").getText(),table.get("bdate").getText(),table.get("address").getText(),table.get("sex").getText(),table.get("salary").getText(),
                        table.get("superssn").getText(),table.get("dno").getText(),table.get("email").getText());
                if(result == true){
                    frameadd.setVisible(false);
                    System.out.println(c1.isSelected());
                    secondMainPanel(c1.isSelected());

                }else{
                    System.out.println("wrong info");
                    wrongmessage.setVisible(true);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });




        addemp.add(add);
        layout.putConstraint(SpringLayout.WEST,add,125,SpringLayout.WEST,addemp);
        layout.putConstraint(SpringLayout.NORTH,add,395,SpringLayout.NORTH,addemp);

        wrongmessage.setVisible(false);
        addemp.add(wrongmessage);
        layout.putConstraint(SpringLayout.WEST,wrongmessage,125,SpringLayout.WEST,addemp);
        layout.putConstraint(SpringLayout.NORTH,wrongmessage,425,SpringLayout.NORTH,addemp);


        JLabel dep = new JLabel("dependent");
        addemp.add(dep);
        addemp.add(c1);
        addemp.add(c2);
        layout.putConstraint(SpringLayout.WEST,dep,5,SpringLayout.WEST,addemp);
        layout.putConstraint(SpringLayout.NORTH,dep,365,SpringLayout.NORTH,addemp);
        layout.putConstraint(SpringLayout.WEST,c1,120,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,c1,365,SpringLayout.NORTH,addemp);
        layout.putConstraint(SpringLayout.WEST,c2,180,SpringLayout.WEST,addemp);
        layout.putConstraint(SpringLayout.NORTH,c2,365,SpringLayout.NORTH,addemp);

        mainMenue.setVisible(false);

        frameadd.setLocationRelativeTo(null);

        frameadd.setVisible(true);


    }

    public void project(String ssn){
        framepro = new JFrame();
        Container pro = framepro.getContentPane();
        SpringLayout layout = new SpringLayout();
        framepro.setSize(new Dimension(250,500));
        framepro.setLayout(layout);

        JLabel left = new JLabel(String.valueOf(leftworkHours)+"hours left");
        left.setBounds(125,0,80,30);
        pro.add(left);


        JLabel pname = new JLabel("project name");
        pname.setBounds(40,200,80,30);
        JTextField pname_text = new JTextField(20);
        pname_text.setBounds(100,200,80,30);
        JLabel phours = new JLabel("project hours");
        phours.setBounds(40,300,80,30);
        JTextField phours_text = new JTextField(20);
        phours_text.setBounds(100,300,80,30);

        pro.add(pname);
        pro.add(pname_text);
        pro.add(phours);
        pro.add(phours_text);

        JButton assign = new JButton("assign");
        assign.setBounds(125,150,80,30);
        assign.addActionListener(e->{
            try {
                int temp = jdbc.assignProject(ssn,pname_text.getText(),Integer.valueOf(phours_text.getText()));
                failureProduct = new JLabel("failed to assign the job to the employee");

                if( temp == -1){
                    framepro.add(failureProduct);
                    layout.putConstraint(SpringLayout.NORTH,framepro,50,SpringLayout.NORTH,assign);
                }else{
                    failureProduct.setVisible(false);
                    leftworkHours = leftworkHours-temp;

                }
                framepro.dispose();
                framesub.setVisible(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        pro.add(assign);

        layout.putConstraint(SpringLayout.WEST,pname,80,SpringLayout.WEST,pro);
        layout.putConstraint(SpringLayout.NORTH,pname,80,SpringLayout.NORTH ,pro);
        layout.putConstraint(SpringLayout.WEST,pname_text,80,SpringLayout.WEST,pname);
        layout.putConstraint(SpringLayout.NORTH,pname_text,80,SpringLayout.NORTH ,pro);
        layout.putConstraint(SpringLayout.WEST,phours,80,SpringLayout.WEST,pro);
        layout.putConstraint(SpringLayout.NORTH,phours,50,SpringLayout.NORTH ,pname);
        layout.putConstraint(SpringLayout.WEST,phours_text,80,SpringLayout.WEST,phours);
        layout.putConstraint(SpringLayout.NORTH,phours_text,50,SpringLayout.NORTH ,pname_text);
        layout.putConstraint(SpringLayout.WEST,assign,125,SpringLayout.WEST,pro);
        layout.putConstraint(SpringLayout.NORTH,assign,50,SpringLayout.NORTH ,phours_text);


        framepro.setVisible(true);
        framepro.setLocationRelativeTo(null);


    }

    public void setAddepedent(String ssn){
        framedep = new JFrame();
        Container dep = framedep.getContentPane();
        SpringLayout layout = new SpringLayout();
        framedep.setSize(new Dimension(250,600));
        dep.setLayout(layout);


        JLabel essn = new JLabel("essn");
        JTextField essn_t = new JTextField(50);


        JLabel name = new JLabel("name");
        JTextField name_t = new JTextField(50);



        JLabel sex = new JLabel("sex");
        JTextField sex_t = new JTextField(50);


        JLabel bdate = new JLabel("bdate");
        JTextField bdate_t = new JTextField(50);


        JLabel relationship = new JLabel("relationship");
        JTextField relationship_t = new JTextField(50);


        JButton done = new JButton("add");
        done.addActionListener(e->{
            try{
                boolean click = jdbc.addDep(essn_t.getText(),name_t.getText(), sex_t.getText(), bdate_t.getText(),relationship_t.getText());
                if(click == false){
                    JLabel info = new JLabel("wrong information provided");
                    info.setBounds(125,300,100,30);
                    dep.add(info);
                    layout.putConstraint(SpringLayout.WEST,info,100,SpringLayout.WEST,dep);
                    layout.putConstraint(SpringLayout.NORTH,info,50,SpringLayout.NORTH,done);

                }
                framedep.dispose();
                framesub.setVisible(true);
            }catch(SQLException throwables){
                throwables.printStackTrace();
            }
        });


        dep.add(essn);
        dep.add(name);
        dep.add(sex);
        dep.add(bdate);
        dep.add(relationship);
        dep.add(essn_t);
        dep.add(name_t);
        dep.add(sex_t);
        dep.add(bdate_t);
        dep.add(relationship_t);
        dep.add(done);

        layout.putConstraint(SpringLayout.WEST,essn,20,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,essn,5,SpringLayout.NORTH ,dep);

        layout.putConstraint(SpringLayout.WEST,essn_t,100,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,essn_t,5,SpringLayout.NORTH ,dep);

        layout.putConstraint(SpringLayout.WEST,name,20,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,name,55,SpringLayout.NORTH ,dep);

        layout.putConstraint(SpringLayout.WEST,name_t,100,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,name_t,55,SpringLayout.NORTH ,dep);

        layout.putConstraint(SpringLayout.WEST,sex,20,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,sex,105,SpringLayout.NORTH ,dep);

        layout.putConstraint(SpringLayout.WEST,sex_t,100,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,sex_t,105,SpringLayout.NORTH ,dep);

        layout.putConstraint(SpringLayout.WEST,bdate,20,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,bdate,155,SpringLayout.NORTH ,dep);
        layout.putConstraint(SpringLayout.WEST,bdate_t,100,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,bdate_t,155,SpringLayout.NORTH ,dep);

        layout.putConstraint(SpringLayout.WEST,relationship,20,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,relationship,205,SpringLayout.NORTH ,dep);
        layout.putConstraint(SpringLayout.WEST,relationship_t,100,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,relationship_t,205,SpringLayout.NORTH ,dep);

        layout.putConstraint(SpringLayout.WEST,done,100,SpringLayout.WEST,dep);
        layout.putConstraint(SpringLayout.NORTH,done,255,SpringLayout.NORTH,dep);




        framedep.setLocationRelativeTo(null);
        framedep.setVisible(true);


    }

    public void secondMainPanel(boolean dependent){
        framesub = new JFrame();
        framesub.setSize(new Dimension(300,400));
        subPanel = framesub.getContentPane();
        SpringLayout layout = new SpringLayout();
        subPanel.setLayout(layout);
        failureProduct = new JLabel("Failed to assign the projects");
        failureProduct.setBounds(125,200,80,30);

        JButton assignP = new JButton("assign project");
        JButton addDep = new JButton("add dependent");

        assignP.setBounds(125,60,80,30);
        addDep.setBounds(125,120,80,30);


        assignP.addActionListener(e->{
            framesub.setVisible(false);
            project(ssn);
        });



        subPanel.add(assignP);
        if(dependent){
            subPanel.add(addDep);
            layout.putConstraint(SpringLayout.WEST,addDep,150,SpringLayout.WEST,subPanel);
            layout.putConstraint(SpringLayout.NORTH,addDep,50,SpringLayout.NORTH,subPanel);
        }
        addDep.addActionListener(e->{
            framesub.setVisible(false);
            setAddepedent(ssn);
        });

        failureProduct.setVisible(false);
        subPanel.add(failureProduct);

        layout.putConstraint(SpringLayout.WEST,assignP,50,SpringLayout.WEST,subPanel);
        layout.putConstraint(SpringLayout.NORTH,assignP,5,SpringLayout.NORTH,subPanel);

        layout.putConstraint(SpringLayout.NORTH,failureProduct,80,SpringLayout.NORTH,subPanel);

        JButton finish = new JButton("finished");
        finish.addActionListener(e->{
            try {
                framesub.setVisible(false);
                result(dependent);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        subPanel.add(finish);
        layout.putConstraint(SpringLayout.WEST,addDep,150,SpringLayout.WEST,subPanel);
        layout.putConstraint(SpringLayout.NORTH,finish,100,SpringLayout.NORTH,assignP);


        framesub.setLocationRelativeTo(null);
        framesub.setVisible(true);


    }
    public static void result(boolean dependent) throws SQLException {
        frameresult = new JFrame();
        Container rp = frameresult.getContentPane();

        frameresult.setSize(new Dimension(600,600));
        SpringLayout layout = new SpringLayout();
        rp.setLayout(layout);


        if(dependent==true){
            String r1 = jdbc.getIE(ssn);
            String r2 = jdbc.getIW(ssn);
            String r3 = jdbc.getID(ssn);
            JLabel emp = new JLabel("employee information");
            JTextArea emp_t = new JTextArea(r1);
            JLabel work = new JLabel("works_on");
            JTextArea work_t = new JTextArea(r2);
            JLabel dep = new JLabel("dependent");
            JTextArea dep_t = new JTextArea(r3);

            //JLabel works_on = new JLabel("works information");
            //JTextArea works_t = new JTextArea(r1);
            rp.add(emp);
            rp.add(emp_t);
            rp.add(work);
            rp.add(work_t);
            rp.add(dep);
            rp.add(dep_t);
            layout.putConstraint(SpringLayout.WEST,emp,5,SpringLayout.WEST,rp);
            layout.putConstraint(SpringLayout.NORTH,emp,5,SpringLayout.NORTH,rp);
            layout.putConstraint(SpringLayout.WEST,emp_t,205,SpringLayout.WEST,rp);
            layout.putConstraint(SpringLayout.NORTH,emp_t,5,SpringLayout.NORTH,rp);

            layout.putConstraint(SpringLayout.WEST,work,5,SpringLayout.WEST,rp);
            layout.putConstraint(SpringLayout.NORTH,work,200,SpringLayout.NORTH,rp);
            layout.putConstraint(SpringLayout.WEST,work_t,205,SpringLayout.WEST,rp);
            layout.putConstraint(SpringLayout.NORTH,work_t,200,SpringLayout.NORTH,rp);

            layout.putConstraint(SpringLayout.WEST,dep,5,SpringLayout.WEST,rp);
            layout.putConstraint(SpringLayout.NORTH,dep,50,SpringLayout.NORTH,work);
            layout.putConstraint(SpringLayout.WEST,dep_t,205,SpringLayout.WEST,rp);
            layout.putConstraint(SpringLayout.NORTH,dep_t,50,SpringLayout.NORTH,work_t);

        }else{
            String r1 = jdbc.getIE(ssn);
            String r2 = jdbc.getIW(ssn);

            JLabel emp = new JLabel("employee information");
            JTextArea emp_t = new JTextArea(r1);
            JLabel work = new JLabel("works_on");
            JTextArea work_t = new JTextArea(r2);



            rp.add(emp);
            rp.add(emp_t);
            rp.add(work);
            rp.add(work_t);

            layout.putConstraint(SpringLayout.WEST,emp,5,SpringLayout.WEST,rp);
            layout.putConstraint(SpringLayout.NORTH,emp,5,SpringLayout.NORTH,rp);
            layout.putConstraint(SpringLayout.WEST,emp_t,200,SpringLayout.WEST,emp);
            layout.putConstraint(SpringLayout.NORTH,emp_t,5,SpringLayout.NORTH,rp);

            layout.putConstraint(SpringLayout.WEST,work,5,SpringLayout.WEST,rp);
            layout.putConstraint(SpringLayout.NORTH,work,20,SpringLayout.NORTH,emp_t);
            layout.putConstraint(SpringLayout.WEST,work_t,200,SpringLayout.WEST,work);
            layout.putConstraint(SpringLayout.NORTH,work_t,20,SpringLayout.NORTH,emp_t);

        }
        frameresult.setLocationRelativeTo(null);
        frameresult.setVisible(true);
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        new manager();
    }

    public ActionListener sqlstuf() throws SQLException, ClassNotFoundException {

        boolean result = jdbc.checkManager(userText.getText());
        if(result == true){
            mainM();
        }else{
            framelogwrong = new JFrame();
            framelogwrong.setSize(new Dimension(200,200));
            Container wrong = framelogwrong.getContentPane();
            JLabel wrongssn = new JLabel("wrong manager ssn");
            JButton returnLogin = new JButton("retry");
            SpringLayout layout = new SpringLayout();
            wrong.setLayout(layout);

            wrong.setPreferredSize(new Dimension(250,150));

            wrongssn.setBounds(100,50,165,25);
            returnLogin.setBounds(100,100,165,25);



            returnLogin.addActionListener(e ->{
                framelog.setVisible(true);
                framelogwrong.setVisible(false);
            });
            wrong.add(wrongssn);
            wrong.add(returnLogin);
            layout.putConstraint(SpringLayout.WEST,wrongssn,50,SpringLayout.WEST,wrong);
            layout.putConstraint(SpringLayout.NORTH,wrongssn,50,SpringLayout.NORTH,wrong);
            layout.putConstraint(SpringLayout.WEST,returnLogin,50,SpringLayout.WEST,wrong);
            layout.putConstraint(SpringLayout.NORTH,returnLogin,50,SpringLayout.NORTH,wrongssn);

            framelog.setVisible(false);
            framelogwrong.setVisible(true);
            framelogwrong.setLocationRelativeTo(null);
        }
        return null;
    }
}
