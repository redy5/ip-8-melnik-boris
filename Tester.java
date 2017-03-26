import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Scanner;

public class Tester {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner s = new Scanner(System.in);
		System.out.println("Dictionary by Melnik Boris");
		Dictionary d = new Dictionary();
		d.BSD();
		d.words();
		
	}
}