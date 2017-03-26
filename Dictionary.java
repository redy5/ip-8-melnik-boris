
/*Melnik Boris
 * IP
 * Practice-3
 * */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Dictionary implements Serializable {
	public final static double TITLE_W = 0.4;
	public final static double AUTHOR_W = 0.3;
	public final static double GENRE_W = 0.2;
	public final static double TEXT_W = 0.1;

	HashMap<String, Boolean> dict = new HashMap<String, Boolean>();
	Token[] tokens = new Token[100];
	Book[] books = new Book[100];
	int book_count = 0;
	int docnum = 0;
	HashMap<Integer, String> documents = new HashMap<Integer, String>();

	int tokenCount;
	int wordCount;

	int prev_word_id = -1;

	int current_coordinate = 0;

	public void addDocument(File file) {
		try {
			StreamTokenizer st = new StreamTokenizer(new BufferedReader(new FileReader(file)));

			System.out.println("Beginning to index " + file);

			st.lowerCaseMode(true);
			st.whitespaceChars(',', ',');
			st.whitespaceChars('-', '-');
			st.whitespaceChars('.', '.');

			docnum++;
			documents.put(docnum, file.getName());

			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				if (st.sval == null)
					continue;
				String current_word = clear(st.sval);
				if (isWord(current_word)) {
					current_coordinate++;
					wordCount++;
					consideration(current_word, docnum);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("No such file!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		prev_word_id = -1;
		current_coordinate = 0;
		System.out.println("Words added.");
		Arrays.sort(tokens, 0, tokenCount);
		System.out.println("Dictionary sorted.");
		for (int i = 0; i < tokenCount; i++)
			tokens[i].index = i;
		System.out.println("Tokens indexed.");
	}

	private boolean isWord(String s) {
		for (int i = 0; i < s.length(); i++)
			if (Character.isAlphabetic(s.charAt(i)))
				return true;
		return false;
	}

	private void consideration(String s, int doc_id) {
		if (s == null)
			return;
		if (tokens.length == tokenCount)
			resizeToken();
		if (dict.get(s) != null) {
			for (int i = 0; i < tokenCount; i++) {
				if (tokens[i].getText().equals(s) == true) {
					if (!tokens[i].docs.contains((Integer) doc_id))
						tokens[i].docs.add(doc_id);
					prev_word_id = i;
					break;
				}
			}
		} else {
			Token n = new Token(s);
			n.docs.add(doc_id);
			prev_word_id = tokenCount;
			tokens[tokenCount++] = n;
			dict.put(s, true);
		}
	}

	private void resizeToken() {
		Token[] temp = new Token[tokens.length * 2];
		for (int i = 0; i < tokens.length; i++) {
			temp[i] = tokens[i];
		}
		tokens = temp;
	}

	private String clear(String initial) {
		char[] r = new char[initial.length()];
		int count = 0;
		for (int i = 0; i < initial.length(); i++) {
			char c = initial.charAt(i);
			if (Character.isLetter(c))
				r[count++] = c;
		}
		char[] re = new char[count];
		for (int i = 0; i < count; i++)
			re[i] = r[i]; // this way i dont += to String and take much less
							// memory!
		String res = new String(re);
		return res;
	}

	//
	// public boolean[][] buildIncident() {
	// boolean[][] incident = new boolean[tokenCount][docnum];
	// for (int i = 0; i < tokenCount; i++) {
	// ArrayList<Integer> al = (ArrayList<Integer>) tokens[i].docs.clone();
	// while (!al.isEmpty())
	// incident[i][al.remove(0) - 1] = true;
	// }
	// return incident;
	// }
	//
	public void statistics() {
		// System.out.println("Total word count: " + wordCount);
		System.out.println("Unique word count: " + tokenCount);
	}

	//
	public void words() {
		System.out.println("Word list:");
		for (int i = 0; i < tokenCount; i++) {
			System.out.println(tokens[i]);
			System.out.println(tokens[i].docs);
		}
	}

	// public void dwords() {
	// System.out.println("Double Word list:");
	// for (int i = 0; i < tokenCount; i++)
	// for (int j = 0; j < tokens[i].dt.size(); j++)
	// System.out.println(tokens[i].getText() + " " +
	// tokens[i].dt.get(j).secondWord + ", in doc "
	// + documents.get(tokens[i].dt.get(j).doc_id));
	//
	// }

	// public void trigrams(){ //shows trigram list
	// System.out.println("Trigram Word list:");
	// for (int i = 0; i < tokenCount; i++)
	// System.out.println(tokens[i].trigram);
	// }
	//
	// public void shuffles(){ //shows shuufles list (with $)
	// System.out.println("Shuffle Word list:");
	// for (int i = 0; i < tokenCount; i++)
	// System.out.println(tokens[i].shuffle);
	// }
	//
	// public ArrayList<Integer> booleanSearch(String request) { // returns list
	// of doc #'s
	// ArrayList<Integer> res = new ArrayList<Integer>();
	// StringTokenizer st = new StringTokenizer(request);
	// ArrayList<String> toks = new ArrayList<String>();
	// while (st.hasMoreTokens())
	// toks.add(st.nextToken("& "));
	// ArrayList<Integer>[] bools = (ArrayList<Integer>[]) new
	// ArrayList[toks.size()];
	//
	// for (int i = 0; i < toks.size(); i++) {
	// if (toks.get(i).charAt(0) == '!') {
	// toks.set(i, toks.get(i).substring(1, toks.get(i).length()));
	// bools[i] = inverted(findToken(toks.get(i)).docs, docnum);
	// } else
	// bools[i] = findToken(toks.get(i)).docs;
	// }
	// res.addAll(bools[0]);
	// for (int i = 1; i < toks.size(); i++)
	// res = cross(res, bools[i], docnum);
	// return res;
	// }
	//
	// private Token findToken(String s) { // return token by word
	// for (int i = 0; i < tokenCount; i++) {
	// if (tokens[i].getText().equals(s) == true)
	// return tokens[i];
	// }
	// return null;
	// }
	//
	// private ArrayList<Integer> inverted(ArrayList<Integer> base, int max) {
	// // returns inverted collection
	// ArrayList<Integer> bs = base;
	// ArrayList<Integer> res = new ArrayList<Integer>();
	// for (int i = 1; i <= max; i++)
	// if (!bs.contains((Integer) i))
	// res.add(i);
	// return res;
	// }
	//
	// private ArrayList<Integer> cross(ArrayList<Integer> f, ArrayList<Integer>
	// s, int max) { // returns the crossing of 2 collections
	// ArrayList<Integer> res = new ArrayList<Integer>();
	// for (int i = 0; i <= max; i++) {
	// if (f.contains((Integer) i) && s.contains((Integer) i))
	// res.add(i);
	// }
	// return res;
	// }

	// public ArrayList<Integer> phraseSearch(String w1, String w2) {
	// ArrayList<Integer> result = new ArrayList<Integer>();
	// Token word = findToken(w1);
	// for (int j = 0; j < word.dt.size(); j++) {
	// if (word.dt.get(j).secondWord.equals(w2) && !result.contains((Integer)
	// word.dt.get(j).doc_id))
	// result.add(word.dt.get(j).doc_id);
	// }
	// return result;
	// }
	//
	// public ArrayList<Integer> coordSearch(int k, String[] words) {
	// ArrayList<Integer> result = new ArrayList<Integer>();
	// int n = words.length;
	// ArrayList<Integer>[][] mat = (ArrayList<Integer>[][]) new
	// ArrayList[n][docnum+1];
	//
	// for(int i=0;i<n;i++){
	// Token t = findToken(words[i]);
	// if(t==null)
	// return result;
	// for(int j=0;j<t.coords.size();j++){
	// //System.out.println("word "+i+" "+ words[i]+" doc id
	// "+t.coords.get(j).doc_id+" coords "+t.coords.get(j).places);
	// mat[i][t.coords.get(j).doc_id] =
	// (ArrayList<Integer>)t.coords.get(j).places.clone();
	// }
	// }
	//
	// for(int i=1;i<=docnum;i++){
	// boolean doc_match=true;
	// for(int j=0;j<n;j++)
	// if(mat[j][i]==null)
	// doc_match=false;
	// if(!doc_match)
	// continue;
	// doc_match=true;
	// for(int f=0;f<n-1;f++)
	// if(!inRange(mat[f][i],mat[f+1][i],k))
	// doc_match=false;
	// if(!doc_match)
	// continue;
	// result.add(i);
	// }
	// return result;
	// }
	//
	// private boolean inRange(ArrayList<Integer> rr1,ArrayList<Integer> rr2,
	// int range){
	// ArrayList<Integer> r1 = (ArrayList<Integer>) rr1.clone();
	// ArrayList<Integer> r2 = (ArrayList<Integer>) rr2.clone();
	// int rem1 = r1.remove(0);
	// int rem2 = r2.remove(0);
	// if(Math.abs(rem1-rem2)<=range)
	// return true;
	// while(!(r1.isEmpty()&&r2.isEmpty())){
	// if(r1.isEmpty())
	// rem2=r2.remove(0);
	// else if(r2.isEmpty())
	// rem1=r1.remove(0);
	// else if(rem1>=rem2)
	// rem2=r2.remove(0);
	// else
	// rem1=r1.remove(0);
	// if(Math.abs(rem1-rem2)<=range)
	// return true;
	// }
	// return false;
	// }
	//
	// public ArrayList<Token> findIncompleteToken(String s){ //returns token
	// from a request with joker/wildcard/*
	// ArrayList<Token> results = new ArrayList<Token>();
	// if(s.charAt(0)=='*'){
	// String request=backward(s).substring(0, s.length()-1);
	// ArrayList<String> r = backwardBTree.query(request);
	// for(int i=0;i<r.size();i++)
	// results.add(findToken(backward(r.get(i))));
	// }
	// else if(s.charAt(s.length()-1)=='*'){
	// String request=s.substring(0, s.length()-1);
	// ArrayList<String> r = forwardBTree.query(request);
	// for(int i=0;i<r.size();i++)
	// results.add(findToken(r.get(i)));
	// }
	// else{
	// if(!s.contains("*")){
	// return null;
	// }
	// String[] ws = s.split("\\*");
	// String part1=ws[0];
	// String part2=backward(ws[1]);
	// ArrayList<String> r1 = forwardBTree.query(part1);
	// ArrayList<String> r2 = backwardBTree.query(part2);
	// for(int i=0;i<r2.size();i++)
	// r2.set(i, backward(r2.get(i)));
	// ArrayList<String> r = crossing(r1,r2);
	// for(int i=0;i<r.size();i++)
	// results.add(findToken(r.get(i)));
	// }
	// return results;
	// }
	//
	// private String backward(String s){
	// char[] mat = new char[s.length()];
	// for(int i=0;i<s.length();i++){
	// mat[i]=s.charAt(s.length()-1-i);
	// }
	// return new String(mat);
	// }
	//
	// private ArrayList<String> crossing(ArrayList<String> a1,
	// ArrayList<String> a2){
	// ArrayList<String> res = new ArrayList<String>();
	// for(int i=0;i<a1.size();i++){
	// String word = a1.get(i);
	// for(int j=0;j<a2.size();j++)
	// if(word.equals(a2.get(j)))
	// res.add(word);
	// }
	// return res;
	// }

	public CompressedDictionary compression() {
		CompressedDictionary cd = new CompressedDictionary();
		for (int i = 0; i < tokenCount; i++)
			cd.addWord(tokens[i]);
		return cd;
	}

	ArrayList<Book> al = new ArrayList<Book>();

	public void BSD() {
		Pair[] block = new Pair[50000];
		File folder = new File("result");
		File[] listOfFiles = folder.listFiles();
		folder.listFiles();
		int n = listOfFiles.length;
		for (int i = 1; i <= 10; i++)
			indexBook(i, listOfFiles[i]);
		for (int i = 1; i < 11; i++)
			System.out.println(books[i]);

	}

	private void indexBook(int doc_id, File file) {
		StreamTokenizer st;
		Pair[] f = new Pair[5000000];
		int pcounter = 0;
		try {
			st = new StreamTokenizer(new BufferedReader(new FileReader(file)));
			st.whitespaceChars('<', '>');
			st.whitespaceChars('/', '/');
			st.whitespaceChars(',', ',');
			st.whitespaceChars('.', '.');
			st.whitespaceChars('"', '"');
			st.whitespaceChars('_', '_');
			boolean lfauthor = true;
			boolean lftitle = true;
			String author = "";
			String title = "";
			ArrayList<String> genres = new ArrayList<String>();
			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				if (st.sval != null && st.sval.equals("genre")) {
					st.nextToken();
					String cur = "";
					while (st.sval != null && !st.sval.equals("genre")) {
						cur += st.sval + " ";
						st.nextToken();
					}
					genres.add(cur);
				}
				if (lfauthor && st.sval != null && st.sval.equals("first-name")) {
					st.nextToken();
					author += st.sval;
					st.nextToken();
					st.nextToken();
					st.nextToken();
					author += " " + st.sval;
					lfauthor = false;
				}
				if (lftitle && st.sval != null && st.sval.equals("book-title")) {
					st.nextToken();
					while (st.sval != null && !st.sval.equals("book-title")) {
						title += st.sval + " ";
						st.nextToken();
					}
					lftitle = false;
				}
				if (st.sval != null && st.sval.equals("body")) {
					break;
				}

			}
			String[] r = new String[genres.size()];
			for (int i = 0; i < genres.size(); i++)
				r[i] = genres.get(i);
			Book b = new Book(doc_id, title, author, r);
			books[doc_id] = b;
			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				if (st.sval != null && st.sval.equals("body"))
					break;
				if (st.sval != null) {
					String current_word = clear(st.sval.toLowerCase());
					Pair p = new Pair(current_word, doc_id);
					f[pcounter++] = p;
				}
				// if (isWord(current_word))
				// consideration(current_word,docnum);
				// consideration(st.sval,doc_id);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Book indexed" + doc_id);
		writeBlock(f, 1, pcounter);
	}

	private void writeBlock(Pair[] p, int k, int pcount) {
		try {
			PrintWriter writer = new PrintWriter("block" + k, "windows-1251");
			for (int i = 0; i < pcount; i++) {
				if (!(p[i].word.equals("emphasis") || p[i].word.equals("section") || p[i].word.equals("title")
						|| p[i].word.equals("p")))
					writer.println(p[i].word + " " + p[i].doc_id);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reworkBlock(int block_id) {
		int tcount = 0;
		Token[] toks = new Token[5000000];
		try {
			BufferedReader br = new BufferedReader(new FileReader("block"+block_id));
			while (br.ready()) {
				String tok = br.readLine();
				String[] f = tok.split(" ");
				String word = f[0];
				boolean contained = false;
				int doc_id = Integer.parseInt(f[1]);
				for (int i = 0; i < tcount; i++) {
					if (toks[i].getText().equals(word))
						if (!toks[i].docs.contains(doc_id)) {
							toks[i].addDocId(doc_id);
							contained = true;
						}
				}
				if (!contained) {
					Token t = new Token(word);
					t.addDocId(doc_id);
					toks[tcount++] = t;
				}
			}
			Token[] writable = new Token[tcount];
			for(int i=0;i<tcount;i++){
				writable[i]=toks[i];
			}
			FileOutputStream fos = new FileOutputStream("reworked"+block_id);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(writable);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int rangeSearch(String s) { // return best doc_id
		int k = -1;
		int coef = 0;
		ArrayList<Integer> contained_docs = new ArrayList<Integer>();
		// search text first
		for(int j=0;j<tokenCount;j++)
			if(s.contains(tokens[j].getText()))
				contained_docs.addAll(tokens[j].docs);
		// then search by metadata to find best variant
		for (int i = 0; i < book_count; i++) {
			Book t = books[i];
			int cur_coef = 0;
			if (t.author.contains(s))
				cur_coef += AUTHOR_W;
			if (t.bookTitle.contains(s))
				cur_coef += TITLE_W;
			for (int j = 0; j < t.genres.length; j++) {
				if (t.genres[j].contains(s)) {
					cur_coef += GENRE_W;
					break;
				}
			}
			if(contained_docs.contains(i))
				cur_coef+=TEXT_W;
			if (cur_coef > coef)
				k = i;
		}
		return k;
	}

}

class Pair implements Serializable {
	int doc_id;
	String word;

	Pair(String word, int doc_id) {
		this.word = word;
		this.doc_id = doc_id;
	}
}