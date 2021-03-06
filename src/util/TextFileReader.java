package util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * A reader of text files.
 * @author adityasrinivasan
 *
 */
public class TextFileReader {
	
	/**
	 * Reads a text file located by a specified path and read using a specified delimiter.
	 * @param path: the text file path
	 * @param delimiter: the delimiter
	 * @return
	 */
	public static Collection<String> read(String path, String delimiter) {
		Collection<String> strings = new ArrayList<>();
		InputStream input = TextFileReader.class.getResourceAsStream(path);
		try (Scanner reader = new Scanner(input)) {
			reader.useDelimiter(delimiter);
			while (reader.hasNext()) {
				strings.add(reader.next());
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strings;
	}

}
