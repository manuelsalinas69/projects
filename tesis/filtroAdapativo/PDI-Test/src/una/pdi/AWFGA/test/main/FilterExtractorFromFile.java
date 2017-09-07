package una.pdi.AWFGA.test.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterExtractorFromFile {

	public static void main(String[] args) throws IOException {
		List<String> lines=getLines("/Users/Manuel/Documents/Tesis/output/exec_20170609.txt");
		String regex="(Best\\sResult\\s\\d*:\\sVariables:\\s)(.*)(\\sObjectives.*)";
		Pattern p = Pattern.compile(regex);
		HashMap<String, String> map= new HashMap<String, String>();
		String filter=null;
		for (String _l : lines) {
			Matcher m=p.matcher(_l);
			if (m.matches()) {
				filter=m.group(2);
				if (!map.containsKey(filter)) {
					map.put(filter, filter);
					System.out.println(filter);
				}
				else{
					
				}
				
			}
		}
	}
	
	public static List<String> getLines(String filePath) throws IOException{
		List<String> _return= new ArrayList<String>();
		
		List<String> l=Files.readAllLines(Paths.get(filePath));
		
		for (String _l : l) {
			if (!_return.contains(_l)) {
				_return.add(_l);
			}
		}
		return _return;
		
	}
}
