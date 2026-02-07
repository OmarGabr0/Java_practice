

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;


// Project pname  pnum plocation city dnum 
class ProjectPojo {
    private String pname ; 
    private int pnum ; 
    private String plocation;
    private String city; 
    private int dnum; 

    public ProjectPojo (String pname , int pnum , String plocation , 
            String city , int dnum
    ) { 
        this.pname=pname;
        this.pnum=pnum;
        this.plocation=plocation;
        this.city=city;
        this.dnum=dnum;
    }
    public String getCity() {
        return city;
    }
    public int getDnum() {
        return dnum;
    }
    public String getPlocation() {
        return plocation;
    }
    public String getPname() {
        return pname;
    }
    public int getPnum() {
        return pnum;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setDnum(int dnum) {
        this.dnum = dnum;
    }
    public void setPlocation(String plocation) {
        this.plocation = plocation;
    }
    public void setPname(String pname) {
        this.pname = pname;
    }
    public void setPnum(int pnum) {
        this.pnum = pnum;
    }
}

class ProjectDAO{

    private String DB_url = "jdbc:mysql://localhost:3306/iti_lab2";
    private String USER = "root";
    private String PASS = "math";
    private Connection con=null;
    private boolean conflag= false; 
    private preparedStatement pst=null;

    public ProjectDAO { 
        if(Connect()){
            System.out.println("Connection established");
        } else {
            System.out.println("Connection failed");
        }

    }
   private boolean Connect (){

        try{
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            con = DriverManager.getConnection(DB_url,USER,PASS);
            conflag=true;
            return true;
        } catch(SQLException e){
            e.printStackTrace();
            conflag=false;
            return false;
        }
   }
   void closeConn(){
    try{
        if(con!=null){
            con.close();
            conflag=false;
        }
    } catch(SQLException e){
        e.printStackTrace();
    }
   }

   private ProjectPojo createProjectElement(ResultSet rsl){
        try {
          
                String pname = rsl.getString("pname");
                int pnum = rsl.getInt("pnum");
                String plocation = rsl.getString("plocation");
                String city = rsl.getString("city");
                int dnum = rsl.getInt("dnum");
                return new ProjectPojo(pname, pnum, plocation, city, dnum);
 
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
                try {
                    rsl.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    }
   public void createProjectTable(){
    String SQL="CREATE TABLE project (pname string,pnum integer, plocation string , plocation string city string , dnum integer)";
    try{

        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        PreparedStatement pstmt = con.prepareStatement(SQL);
        pstmt.executeUpdate();
        System.out.println("Project table created/verified");

    } catch(SQLException e){
        e.printStackTrace();
    } finally{
        try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }


    }
   public List<ProjectPojo> getProjects(){
    String SQL = "SELECT * FROM project";
    List<ProjectPojo> list= new ArrayList<ProjectPojo>();
    try{
        PreparedStatement pstmt= con.prepareStatement(SQL);
        ResultSet rsl=pstmt.executeQuery();
        while(rsl.next()){
                list.add(createProjectElement(rsl));
        }
        return list;
    
    } catch (SQLException e ){
        e.printStackTrace();
        return list; // empty
    }finally{
            try {
                if (rsl != null) rsl.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }



   }
   public  List<ProjectPojo> getProjectForName(String name){
    String SQL = "SELECT * FROM project WHERE pname=?";
    List<ProjectPojo> list = new ArrayList<>();

    try{
        PreparedStatement pstmt= con.prepareStatement(SQL);
        pstmt.setString(1, name);
        ResultSet rsl=pstmt.executeQuery();
        while  ( rsl.next() ) {
            list.add(createProjectElement(rsl));
        }
         return list;
    
    } catch (  Exception e  ) { 
        e.printStackTrace();
        return list; //empty

    } finally{
        try{
        if(rsl != null) rsl.close();
        if(pstmt != null)  pstmt.close();
        } catch(SQLException e){
            e.printStackTrace();;
        }
    }

   }

    public ProjectPojo getProjectByNumber(int pnum) {
        String SQL = "SELECT * FROM project WHERE pnum = ?";
        
        PreparedStatement pstmt = null;
        ResultSet rsl = null;
        
        try {
            pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, pnum);
            rsl = pstmt.executeQuery();
            
            if (rsl.next()) {
                return createProjectElement(rsl);
            } else {
                return null; // Not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rsl != null) rsl.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
   public void insertProject(ProjectPojo p){
    if(!conflag) return ; 

          String sql = "INSERT INTO project (pname, pnum, plocation, city, dnum) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try  {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, p.getPname());
            pstmt.setInt(2, p.getPnum());
            pstmt.setString(3, p.getPlocation());   
            pstmt.setString(4, p.getCity());
            pstmt.setInt(5, p.getDnum());
            int rows = pstmt.executeUpdate();
            System.out.println(rows + " project(s) inserted");
           
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
                

   }


}

public class Exercise {
    public static void main(String[] args) {
        ProjectDAO dao = new ProjectDAO();
        Scanner scanner = new Scanner(System.in);
        
        try {
            // 1. Create table
            dao.createProjectTable();
            
            // 2. Insert sample projects
            System.out.println("\n=== Inserting Sample Projects ===");
            dao.insertProject(new ProjectPojo("Website Redesign", 101, "Cairo", "Giza", 10));
            dao.insertProject(new ProjectPojo("Mobile App", 102, "Smart Village", "Giza", 20));
            dao.insertProject(new ProjectPojo("Database Migration", 103, "Alexandria", "Alex", 10));
            
            // 3. Get all projects
            System.out.println("\n=== All Projects ===");
            List<ProjectPojo> allProjects = dao.getProjects();
            for (ProjectPojo p : allProjects) {
                System.out.println(p.getPname() + " | " + p.getPnum() + " | " + 
                                 p.getCity() + " | Dept: " + p.getDnum());
            }
            
            // 4. Search by name
            System.out.println("\n=== Search Projects ===");
            System.out.print("Enter project name to search: ");
            String searchName = scanner.nextLine();
            
            List<ProjectPojo> found = dao.getProjectForName(searchName);
            if (found.isEmpty()) {
                System.out.println("No projects found with name: " + searchName);
            } else {
                System.out.println("Found " + found.size() + " project(s):");
                for (ProjectPojo p : found) {
                    System.out.println("- " + p.getPname() + " in " + p.getCity());
                }
            }
            
            // 5. Get by project number
            System.out.println("\n=== Get Project by Number ===");
            System.out.print("Enter project number: ");
            int projNum = scanner.nextInt();
            
            ProjectPojo project = dao.getProjectByNumber(projNum);
            if (project != null) {
                System.out.println("Found: " + project.getPname() + 
                                 ", Location: " + project.getPlocation());
            } else {
                System.out.println("Project #" + projNum + " not found");
            }
            
        } finally {
            dao.closeConn();
            scanner.close();
        }
    }
}