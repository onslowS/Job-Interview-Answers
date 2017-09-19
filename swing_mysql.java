package swing_mysql;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * 
 * @author Sean Onslow
 *
 */
public class swing_mysql{
		private Connection con;
		private Statement st;
		private ResultSet rs;
		private String url;
		private String user;
		private String password;
		private String[] departments;
		private String[] pay_type;
		private String[] education_level;
		private JComboBox departmentCombo;
		private JComboBox payCombo;
		private JComboBox educationCombo;
		private JButton query;
		private JTable data_table;
		private DefaultTableModel model;
		
		private JFrame mainFrame;
		private JPanel dataPanel;
		private JPanel controlPanel;
		private JScrollPane scrollPane;
		
	public swing_mysql() throws SQLException{
		con = null;
	    st = null;
	    rs = null;
	    url = "jdbc:mysql://localhost:3306/foodmart";
	    //use your username and password that can access this particular database
	    user = "root";
	    password = "password";
	    departments = new String[countRecords("department_id", "department")];
	    departments = getRecords(departments.length, "department_description", "department");
	    pay_type = new String[countRecords("pay_type", "position")];
	    pay_type = getRecords(pay_type.length, "pay_type", "position");
	    education_level = new String[countRecords("education_level", "employee")];
	    education_level = getRecords(education_level.length, "education_level", "employee");
	    prepareGUI();
	}
	public String[] getRecords(int length, String column_name, String table_name){
		String[] records = new String[length];
		int counter = 0;
		try{
			con = DriverManager.getConnection(url, user, password);
	        st = con.createStatement();
	        String query = "SELECT DISTINCT " + column_name + " FROM " + table_name + ";";
	        rs = st.executeQuery(query);
	        while (rs.next()) {
	        	System.out.println(rs.getString(1));
	        	records[counter] = rs.getString(1);
	        	counter += 1;
	        }
		} catch (SQLException ex) {
		} finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                
                if (st != null) {
                    st.close();
                }
                
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
            }
        }
		return records;
	}
	public int countRecords(String column_name, String table_name){
		int result = 0;
		try{
			con = DriverManager.getConnection(url, user, password);
	        st = con.createStatement();
	        rs = st.executeQuery("SELECT Count(DISTINCT "+ column_name + ") AS NumberOfRecords FROM " + table_name + ";");
	        if (rs.next()) {
	        	result = Integer.parseInt(rs.getString(1));
	        }
		} catch (SQLException ex) {
		} finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                
                if (st != null) {
                    st.close();
                }
                
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
            }
        }
		System.out.println(result);
        return result;
	}
	private void prepareGUI(){
	      mainFrame = new JFrame("Java Swing Examples");
	      mainFrame.setSize(400,400);
	      mainFrame.setLayout(new GridLayout(2, 0));
	      
	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });
	      data_table = new JTable();
	      model = (DefaultTableModel) data_table.getModel();
	      model.addColumn("Full name");
	      model.addColumn("Birth Date");
	      model.addColumn("Hire date");
	      model.addColumn("Gender");
	      JScrollPane scrollPane = new JScrollPane(data_table);
	      controlPanel = new JPanel();
	      controlPanel.setLayout(new FlowLayout());
	      controlPanel.setSize(400, 100);
	      departmentCombo = new JComboBox(departments);
	      controlPanel.add(departmentCombo);
	      payCombo = new JComboBox(pay_type);
	      controlPanel.add(payCombo);
	      educationCombo = new JComboBox(education_level);
	      controlPanel.add(educationCombo);
	      query = new JButton("Query");
	      controlPanel.add(query);
	      query.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	 Object[] row = new Object[4];
	        	 model.setRowCount(0);
	        	 String query = "SELECT employee.full_name, employee.birth_date, employee.hire_date, employee.gender FROM employee INNER JOIN position on employee.position_title = position.position_title INNER JOIN department on department.department_id = employee.department_id WHERE department.department_description = '" + (String)departmentCombo.getSelectedItem() + "' AND employee.education_level = '" + (String)educationCombo.getSelectedItem() + "' AND position.pay_type = '" +  (String)payCombo.getSelectedItem() + "' ORDER BY employee.full_name;";
	        	 try{
	     			con = DriverManager.getConnection(url, user, password);
	     	        st = con.createStatement();
	     	        rs = st.executeQuery(query);
	     	        while (rs.next()) {
	     	        	 row[0] = rs.getString(1);
	     	        	 row[1] = rs.getString(2);
	     	        	 row[2] = rs.getString(3);
	     	        	 row[3] = rs.getString(4);
	     	        	 model.addRow(row);
	     	        }
	     		} catch (SQLException ex) {
	     		} finally {
	                 try {
	                     if (rs != null) {
	                         rs.close();
	                     }
	                     
	                     if (st != null) {
	                         st.close();
	                     }
	                     
	                     if (con != null) {
	                         con.close();
	                     }

	                 } catch (SQLException ex) {
	                 }
	             }
	          }          
	       });
	      dataPanel = new JPanel();
	      dataPanel.setLayout(new BorderLayout());
	      //dataPanel.add(data_table);
	      scrollPane = new JScrollPane(data_table);
	      dataPanel.add(scrollPane, BorderLayout.CENTER);
	      //dataPanel.add(data_table, BorderLayout.CENTER);
	      dataPanel.add(data_table.getTableHeader(), BorderLayout.NORTH);
	      mainFrame.add(controlPanel);
	      mainFrame.add(dataPanel);
	      mainFrame.setVisible(true);  
	   }
	public static void main(String[] args) throws SQLException{
		swing_mysql swing_mysql = new swing_mysql();
	}
}