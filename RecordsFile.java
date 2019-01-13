import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RecordsFile {
	boolean fileChanged = false;
	static File file = new File("C:\\Users\\Stambek\\Desktop\\Дося проекты\\programming01\\Records.txt");
	public static void assignNewRecord(int record) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);

		writer.print(record + "\n");
		writer.close();
	}

	public static int getRecord() throws NumberFormatException, IOException {
		String text = null;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int record = Integer.parseInt(reader.readLine());
		return record;
	}




	
}
