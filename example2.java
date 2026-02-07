
//package p1_jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class example2 {
    public static void main (String [] args){
       
        String url = "jdbc:mysql://localhost:3306/iti_lab2";
        String username = "root";
        String password = "math";

          example2 exm = new example2();
          Scanner sc = new Scanner(System.in);
         try {
             
// load the driver into memory:
             DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
         
// establish the connection with database useing url,name,password:
            Connection con = DriverManager.getConnection(url,username,password);

            // use function to get employee id 
            System.out.print("Input your Employee ID: ");
            int id = sc.nextInt();
            exm.getEmployee(id,con);


        } catch (Exception  e) {
            e.printStackTrace();
        }

        
    }

    public void getEmployee(int id,Connection con) throws SQLException{
        String sql="SELECT * FROM employee where ssn=?";
        PreparedStatement pst=con.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                 ResultSet.CONCUR_READ_ONLY);
        pst.setInt(1, id);
        ResultSet rsl=pst.executeQuery();
        if(rsl.first()){
            System.out.println("Employee found");
            System.out.println("ID"+rsl.getInt("ssn"));
            System.out.println("Name"+rsl.getString("fname")+" "+rsl.getString("lname"));
            System.out.println("address"+rsl.getString("address"));
        } else {
            System.out.println("Employee not found");
        }
        pst.close();
        con.close();
    }
    
}
