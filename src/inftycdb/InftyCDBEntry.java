package inftycdb;

public class InftyCDBEntry{

	public int 		baseline, italicFlag, boldFlag, syllabeAfter,
					charID, journalID, sheetID, width, height, 
					parentCharID, left, top, right, bottom, wordID, 
					wordLeft, wordTop, wordRight, wordBottom;
	public String 	type, code, entity, region, quality, 
					link, imageName, wordMathML, wordTeX, wordIML;

	protected InftyCDBEntry(){};
	
	public static InftyCDBEntry getInftyCDBEntry(String line){
		InftyCDBEntry e = new InftyCDBEntry();
		String[] parts = splitCSV(line);
		e.charID 		= Integer.parseInt(parts[0]) + 1;
		e.journalID 	= Integer.parseInt(parts[1]);
		e.sheetID 		= Integer.parseInt(parts[2]);
		e.type			= parts[3].substring(1,parts[3].length()-1);
		e.code			= parts[4].substring(1,parts[4].length()-1);
		e.entity		= parts[5].substring(1,parts[5].length()-1);
		e.region		= parts[6].substring(1,parts[6].length()-1);
		e.baseline 		= Integer.parseInt(parts[7]);
		e.italicFlag	= Integer.parseInt(parts[8]);
		e.boldFlag 		= Integer.parseInt(parts[9]);
		e.quality		= parts[10].substring(1,parts[10].length()-1);
		e.width			= Integer.parseInt(parts[11]);
		e.height		= Integer.parseInt(parts[12]);
		e.parentCharID	= Integer.parseInt(parts[13]);
		e.link			= parts[14].substring(1,parts[14].length()-1);
		e.imageName		= parts[15].substring(1,parts[15].length()-1);
		e.left			= Integer.parseInt(parts[16]);
		e.top			= Integer.parseInt(parts[17]);
		e.right			= Integer.parseInt(parts[18]);
		e.bottom		= Integer.parseInt(parts[19]);
		e.wordID		= Integer.parseInt(parts[20]);
		e.wordMathML	= parts[21].substring(1,parts[21].length()-1);
		e.wordTeX		= parts[22].substring(1,parts[22].length()-1);
		e.wordIML		= parts[23].substring(1,parts[23].length()-1);
		e.wordLeft		= Integer.parseInt(parts[24]);
		e.wordTop		= Integer.parseInt(parts[25]);
		e.wordRight		= Integer.parseInt(parts[26]);
		e.wordBottom	= Integer.parseInt(parts[27]);
		e.syllabeAfter 	= Integer.parseInt(parts[28]);
		return e;
	}

	public String getMySQLInsertStatement(String tableName){
		return "INSERT IGNORE INTO "+tableName+" VALUES ('"+
				charID +"','"+ journalID +"','"+ sheetID +"','"+ 
				type +"','"+ code +"','"+ entity +"','"+ region +"','"+ 
				baseline +"','"+ italicFlag +"','"+ boldFlag +"','"+ 
				quality +"','"+ width +"','"+ height +"','"+ 
				parentCharID +"','"+ escapeString(link) +"','"+ escapeString(imageName) +"','"+ 
				left +"','"+ top +"','"+ right +"','"+ bottom +"','"+ 
				wordID +"','"+ escapeString(wordMathML) +"','"+ escapeString(wordTeX) +"','"+ 
				escapeString(wordIML) +"','"+ wordLeft +"','"+ wordTop +"','"+ 
				wordRight +"','"+ wordBottom +"','"+ syllabeAfter+"');";
	}
	
	public static String[] splitCSV(String line){
		int li = 0;
		boolean inQuotes = false;
		int length = line.length();
		java.util.ArrayList<String> strings = new java.util.ArrayList<>();
		for(int c = 0; c<length; c++){
			if(line.charAt(c)=='"'){
				inQuotes = !inQuotes;
			}
			if (!inQuotes) {
				if(line.charAt(c) == ','){
					strings.add(line.substring(li, c));
					li = c + 1;
				}
				if(c==length-1){
					strings.add(line.substring(li, c+1));
				}
			}
		}
		return strings.toArray(new String[strings.size()]);
	}
	
	public static String escapeString(String s){
		int length = s.length();
		char x;
		boolean escape = false;
		for(int c = 0; c<length; c++){
			x = s.charAt(c);
			escape = false;
			switch(x){
			case '\'':escape=true;break;
			case '\\':escape=true;break;
			}
			if(escape){
				s = s.substring(0, c)+"\\"+s.substring(c, length);
				length = s.length();
				c++;
			}
		}
		return s;
	}
}
