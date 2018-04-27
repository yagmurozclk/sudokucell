import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.xml.soap.Node;

public class ReadFile {

	public List<List<Integer>> read(File file) {
		try {

			String line;
			int line_count = 0;

			// File file = new File(fileName);

			BufferedReader reader = new BufferedReader(new FileReader(file));

			List<List<String>> inputList = new ArrayList<List<String>>();
			List<List<Integer>> convList = new ArrayList<List<Integer>>();

			while ((line = reader.readLine()) != null) {
				List<String> temp = new ArrayList<String>();
				temp = Arrays.asList(line.split(""));

				convList.add(new ArrayList<Integer>());

				for (int j = 0; j < temp.size(); j++) {
					String value = temp.get(j);
					if (value.equals("*")) {
						convList.get(line_count).add(0);
					} else {
						convList.get(line_count).add(Integer.parseInt(value));
					}
				}
				inputList.add(temp);
				line_count++;
			}
			// String [] array =new String [temp.size()];
			/*
			 * for(List<String> l1 : inputList){ System.out.println(l1);
			 * 
			 * } System.out.println();
			 * 
			 * for(List<Integer> l : convList){ System.out.println(l);
			 * 
			 * }
			 */
			reader.close();

			return convList;

		} catch (Exception ex) {
			return null;
		}

	}
}