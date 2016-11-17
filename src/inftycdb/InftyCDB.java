package inftycdb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class InftyCDB {
	private static String schema = "InftyCDB";
	private static String tableName = schema+".InftyCDB";
	
	private Connection dbConnection;	
	
	private InftyCDB(){}
	
	static{
		// This will load the MySQL driver, each DB has its own driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static InftyCDB getDatabaseInstance(String user, String pass){
		InftyCDB dbI = new InftyCDB();
		try {
			// Setup the connection with the DB
			dbI.dbConnection = DriverManager
					.getConnection("jdbc:mysql://192.168.0.2:3306/"+schema+"?"
							+ "user="+user+"&password="+pass);
			return dbI;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean loadDBTable(String filePath){
		try{
			// Setup the connection with the DB
			Connection connect = DriverManager
					.getConnection("jdbc:mysql://192.168.0.2:3306/InftyCDB?"
							+ "user=admin&password=elechar");

			// Statements allow to issue SQL queries to the database
			Statement statement = connect.createStatement();
			
			BufferedReader input = new BufferedReader(new FileReader(filePath));
			while(input.ready()){
				InftyCDBEntry e = InftyCDBEntry.getInftyCDBEntry(input.readLine());
				String q = e.getMySQLInsertStatement(tableName);
				System.out.println(q);
				statement.executeUpdate(q);
			}
			input.close();
			connect.close();
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
		
		return false;
	}
	
	public List<InftyCDBEntry> getCharacter(char c, int quantity){
		ArrayList<InftyCDBEntry>  list = new ArrayList<>();
		try {
			// Statements allow to issue SQL queries to the database
			Statement statement = dbConnection.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "
					+ "type='Roman' and entity like \"%"+c+"%\" LIMIT "+quantity);
			while(results.next()){
				list.add(getEntryFromResult(results));
			};
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<InftyCDBEntry> getCharactersFromImage(String imgName){
		ArrayList<InftyCDBEntry>  list = new ArrayList<>();
		try {
			// Statements allow to issue SQL queries to the database
			Statement statement = dbConnection.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "
					+ "type='Roman' and imageName='"+imgName+"' and region='text';");
			while(results.next()){
				list.add(getEntryFromResult(results));
			};
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<InftyCDBEntry> getCharacterFromImage(String imgName, char c, int limit){
		ArrayList<InftyCDBEntry>  list = new ArrayList<>();
		try {
			// Statements allow to issue SQL queries to the database
			Statement statement = dbConnection.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "
					+ "type='Roman' and imageName='"+imgName+"' and entity like \"%"+c+"%\" LIMIT "+limit);
			while(results.next()){
				list.add(getEntryFromResult(results));
			};
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private InftyCDBEntry getEntryFromResult(ResultSet r){
		InftyCDBEntry e = new InftyCDBEntry();
		try {
			e.charID 	= r.getInt(1);
			e.journalID = r.getInt(2);
			e.sheetID	= r.getInt(3);
			e.type		= r.getString(4);
			e.code		= r.getString(5);
			e.entity	= r.getString(6);
			e.region	= r.getString(7);
			e.baseline	= r.getInt(8);
			e.italicFlag= r.getInt(9);
			e.boldFlag	= r.getInt(10);
			e.quality	= r.getString(11);
			e.width		= r.getInt(12);
			e.height	= r.getInt(13);
			e.parentCharID=r.getInt(14);
			e.link		= r.getString(15);
			e.imageName = r.getString(16);
			e.left		= r.getInt(17);
			e.top		= r.getInt(18);
			e.right		= r.getInt(19);
			e.bottom	= r.getInt(20);
			e.wordID	= r.getInt(21);
			e.wordMathML= r.getString(22);
			e.wordTeX	= r.getString(23);
			e.wordIML	= r.getString(24);
			e.wordLeft	= r.getInt(25);
			e.wordTop	= r.getInt(26);
			e.wordRight	= r.getInt(27);
			e.wordBottom= r.getInt(28);
			e.syllabeAfter=r.getInt(29);
			return e;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> getPages(){
		String query = "select distinct imageName from "+schema+";";
		ArrayList<String>  pages = new ArrayList<>();
		try {
			// Statements allow to issue SQL queries to the database
			Statement statement = dbConnection.createStatement();
			ResultSet results = statement.executeQuery(query);
			while(results.next()){
				pages.add(results.getString("imageName"));
			};
			return pages;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	public TreeMap<String, ArrayList<InftyCDBEntry>> getCharacter2PageMap(char c, int quantity){
		TreeMap<String, ArrayList<InftyCDBEntry>>  map = new TreeMap<>();
		try {
			// Statements allow to issue SQL queries to the database
			Statement statement = dbConnection.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM "+tableName+" WHERE "
					+ "type='Roman' and entity like \"%"+c+"%\" and region='text' order by imageName ASC, charID ASC LIMIT "+quantity);
			while(results.next()){
				InftyCDBEntry e = getEntryFromResult(results);
				ArrayList<InftyCDBEntry>  list = map.get(e.imageName);
				if(list==null) list = new ArrayList<>();
				list.add(e);
				map.put(e.imageName, list);
			};
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Integer> getUniqueIDList(){
		String query = "SELECT DISTINCT charID FROM "+tableName+";";
		ArrayList<Integer>  charIDs = new ArrayList<>();
		try {
			// Statements allow to issue SQL queries to the database
			Statement statement = dbConnection.createStatement();
			ResultSet results = statement.executeQuery(query);
			while(results.next()){
				charIDs.add(Integer.parseInt(results.getString("charID")));
			};
			return charIDs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Integer> getUniqueCharIDList(){
		String query = "SELECT DISTINCT charID FROM "+tableName+" where type = 'Roman';";
		ArrayList<Integer>  charIDs = new ArrayList<>();
		try {
			// Statements allow to issue SQL queries to the database
			Statement statement = dbConnection.createStatement();
			ResultSet results = statement.executeQuery(query);
			while(results.next()){
				charIDs.add(Integer.parseInt(results.getString("charID")));
			};
			return charIDs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public InftyCDBEntry getEntryByID(int id){
		String query = "SELECT * FROM "+tableName+" where charID = "+id+";";
		try {
			// Statements allow to issue SQL queries to the database
			Statement statement = dbConnection.createStatement();
			ResultSet results = statement.executeQuery(query);
			results.next();
			return getEntryFromResult(results);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static void main(String[] args){
////		loadDBTable("E:/School/Research/BookReader/software/character DB/InftyCDB-1/InftyCDB-1/InftyCDB-1.csv");
////		InftyCDB db = InftyCDB.getDatabaseInstance("admin", "elechar");
////		db.getCharacter('c', 10);
//	}

}
