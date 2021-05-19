import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.proxy.annotation.Pre;
import oracle.sql.DATE;

import javax.xml.transform.Result;
import java.sql.Date;
import java.util.*;
import java.io.*;

public class Jdbc {
    private static ResultSet r;
    private static ResultSet r2;

    private static ResultSet rs = null;
    static Statement stmt = null;
    private static Connection conn = null;
    public static void connect() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String dbURL = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
        conn = DriverManager.getConnection(dbURL, "ading", "dynsoaha");
        stmt = conn.createStatement();
    }

    public static void delete(String ssn) throws SQLException, ClassNotFoundException{
        String query =  "DELETE FROM works_on " +
                "WHERE essn = "+ssn;
        String query2 =  "DELETE FROM employee " +
                "WHERE ssn = "+ssn;
        String query3 = "DELETE FROM dependent " +
                "WHERE essn = "+ssn;
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
        stmt.executeUpdate(query3);
        stmt.executeUpdate(query2);


    }

    public static void printR() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String dbURL = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
            conn = DriverManager.getConnection(dbURL, "ading", "dynsoaha");
            System.out.println("question1");
            stmt = conn.createStatement();


            String stmt1 = "select Lname, ssn from EMPLOYEE, DEPARTMENT where dname='Research' AND dno = dnumber" ;
            String stmt2 = "select lname, ssn, hours from EMPLOYEE,PROJECT,WORKS_ON,DEPT_LOCATIONS where dlocation='Houston' and pname='ProductZ' and dnumber = dno and pno = pnumber and employee.ssn= works_on.essn";
            PreparedStatement p = conn.prepareStatement(stmt1) ;
            p.clearParameters();
            //p.setString(1,"12345");
            r = p.executeQuery();
            String lname;
            String ssn;
            System.out.println("employees work in Research department");
            while(r.next()){
                lname = r.getString(1) ;
                ssn = r.getString(2) ;
                System.out.println("last name:"+lname +" ssn:"+ ssn) ;
            }
            System.out.println("question2");
            PreparedStatement p2 = conn.prepareStatement(stmt2) ;
            p2.clearParameters();
            //p.setString(1,"12345");
            r2 = p2.executeQuery();
            String lname2;
            String ssn2;
            Double hours;
            System.out.println("employees who work in departments located in Houston and work on the project ‘ProductZ’");
            while(r2.next()){
                lname2 = r2.getString(1) ;
                ssn2 = r2.getString(2) ;
                hours = r2.getDouble(3);
                System.out.println("last name: "+lname2 +" ssn:"+ ssn2+ " works hours:" + String.valueOf(hours) );
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean checkManager(String manager_ssn) throws SQLException {
        String stmt = "select mgrssn from department";
        PreparedStatement p = conn.prepareStatement(stmt);
        p.clearParameters();
        ResultSet mssn = p.executeQuery();
        while(mssn.next()){
            System.out.println(mssn.getString(1));
            if(mssn.getString(1).equals(manager_ssn)){
                return true;
            }
        }
        return false;
    }

    public static boolean insertemp (String fname, String minit, String lname, String ssn,
                                     String bdate, String address,String sex,
                                    String salary,String superssn,String dno,
                                     String email) throws SQLException{
        Date date;
        try{
            date = Date.valueOf(bdate);
        }catch (Exception e){
            return false;
        }

        String query = "INSERT INTO employee VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1,fname);
        pstmt.setString(2,minit);
        pstmt.setString(3,lname);
        pstmt.setString(4,ssn);
        pstmt.setDate(5,date);
        pstmt.setString(6,address);
        pstmt.setString(7,sex);
        pstmt.setInt(8,Integer.valueOf(salary));
        pstmt.setString(9,superssn);
        pstmt.setInt(10,Integer.valueOf(dno));
        pstmt.setString(11,email);
        Statement stmt = conn.createStatement();
        try {
            pstmt.executeUpdate();
        }catch (SQLException throwables){
            return false;
        }
        return true;



    }
    public static int assignProject(String ssn,String pro,int workhours) throws SQLException{
        //use the name to find the number of the project
        String stmt1 = "select pnumber from project where pname="+"'"+pro+"'";
        PreparedStatement p3 = conn.prepareStatement(stmt1) ;
        p3.clearParameters();
        ResultSet r3 = p3.executeQuery();
        int pnumber = 0;
        while(r3.next()){
            pnumber = r3.getInt(1);
        }
        String query = "INSERT INTO works_on VALUES(?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1,ssn);
        pstmt.setInt(2,pnumber);
        pstmt.setInt(3,workhours);
        try{
            Statement stmt = conn.createStatement();
            pstmt.executeUpdate();
        }catch (SQLException throwables){
            System.out.println("Failed to assign the projects");
            return -1;
        }
        return workhours;
    }
    public static int checknumP(String ssn) throws SQLException {
        int numP = 0;
        String stmt1 = "select pno from wroks_on,project,employee where essn="+"'"+ssn+"'"+" and pno = pnumber and dnum = dno";
        PreparedStatement p4 = conn.prepareStatement(stmt1) ;
        p4.clearParameters();
        ResultSet r4 = p4.executeQuery();
        while(r4.next()){
            numP ++;
        }
        return numP;
    }
    public static boolean addDep(String ssn, String name, String sex, String bdate, String relationship) throws SQLException {
        String query = "INSERT INTO dependent VALUES(?,?,?,?,?)";
        Date date;
        try{
            date = Date.valueOf(bdate);
        }catch (Exception e){
            return false;
        }
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1,ssn);
        pstmt.setString(2,name);
        pstmt.setString(3, sex);
        pstmt.setDate(4,date);
        pstmt.setString(5,relationship);
        Statement stmt = conn.createStatement();
        try {

            pstmt.executeUpdate();
            return true;
        }catch (SQLException throwables){
            return false;
        }

    }
    public static String getIE(String ossn) throws SQLException {
        String result="";
        String stmt1 = "select * from employee where ssn ="+"'"+ossn+"'";
        PreparedStatement p = conn.prepareStatement(stmt1) ;
        p.clearParameters();
        ResultSet res = p.executeQuery();
        if(res.next()){
            String fname  = res.getString(1);
            String minit = res.getString(2);
            String lname = res.getString(3);
            String ssn= res.getString(4);
            Date bdate= res.getDate(5);
            String address= res.getString(6);
            String sex= res.getString(7);
            int salary= res.getInt(8);
            String superssn= res.getString(9);
            int dno= res.getInt(10);
            String email= res.getString(11);

            result =result+ "fname:"+fname + "\n" + "minit:"+minit + "\n"+
                    "lname:"+lname+"\n"+"ssn:"+ssn+"\n"+"birth date:"+bdate.toString()+"\n"+
                    "address"+address+"\n"+"sex:"+sex+"\n"+"salary:"+salary+"\n"+"superssn:"+superssn+"\n"
                    +"dno:"+dno+"\n"+"email:"+email+"\n";

        }
        return result;
    }
    public static String getIW(String ssn) throws SQLException {
        String result="";
        String stmt1 = "select * from works_on where essn ="+"'"+ssn+"'";
        PreparedStatement p = conn.prepareStatement(stmt1) ;
        p.clearParameters();
        ResultSet res = p.executeQuery();
        String essn;
        int pno;
        double hours;
        while(res.next()){
            essn = res.getString(1);
            pno = res.getInt(2);
            hours = res.getDouble(3);
            result = result+"essn: "+essn+"  pno:"+pno+"  hours:"+hours+"\n";
        }
        return result;
    }
    public static String getID(String ssn) throws SQLException {
        String result = "";
        String stmt1 = "select * from dependent where essn ="+"'"+ssn+"'";
        PreparedStatement p = conn.prepareStatement(stmt1) ;
        p.clearParameters();
        ResultSet res = p.executeQuery();
        String essn;
        String dependent_name;
        String sex;
        Date bdate;
        String relationship;
        while(res.next()){
            essn = res.getString(1);
            dependent_name = res.getString(2);
            sex = res.getString(3);
            bdate = res.getDate(4);
            relationship = res.getString(5);
            result = result+"essn: "+essn+"  dependent_name:"+dependent_name+"  sex:"+sex+
                    "  bdate:"+bdate+"  relationship:"+relationship+"\n";
        }
        return result;
    }
    public static void main(String [] args) throws SQLException, ClassNotFoundException {
        connect();
        printR();
    }
}
