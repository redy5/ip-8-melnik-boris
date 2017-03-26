/*IP4 - full - Melnik Boris*/
import java.io.Serializable;
import java.util.ArrayList;

public class Token implements Comparable<Token>, Serializable {
	private String text;
	int index;

	ArrayList<Integer> docs = new ArrayList<Integer>();

	Token(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public int compareTo(Token o) {
		return this.text.compareTo(o.text);
	}

	@Override
	public String toString() {
		return text + " " + docs;
	}
	
	public void addDocId(int doc_id){
		docs.add(doc_id);
	}

}