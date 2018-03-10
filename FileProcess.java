package process_deadlock;

import java.util.*;
import java.io.*;

public class FileProcess {
	 void readTextLineByLine(List<String> data,String file_name) {//This function is not used for this homework

		try {
			FileReader fr = new FileReader(file_name);
			BufferedReader br = new BufferedReader(fr);
			String str;
			while((str = br.readLine()) != null) {
	            data.add(str);
	            //System.out.println(str);
	        }
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
