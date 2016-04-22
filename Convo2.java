import java.util.*;
import java.io.*;
import java.lang.*;

public class Convo2 {
	public static final String fileName = "out.txt";

	public static HashMap<String, ArrayList<String>> bot = new HashMap<String, ArrayList<String>>();
	public static ArrayList<String> kv = new ArrayList<String>();
	public static ArrayList<String> keyWords = new ArrayList<String>();
	public static Random seed = new Random();
	public static String botName;
	public static String prevLine;

	public static void type(String s) {
		for (int i = 0; i < s.length(); ++i) {
			System.out.print(s.charAt(i));
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {}
		}
	}

	public static boolean isSentenceEnd(String s) {
		if (s.length() == 0) return true;
		if (s.charAt(s.length() - 1) == '.' ||
			s.charAt(s.length() - 1) == '!' ||
			s.charAt(s.length() - 1) == '?') {
			return true;
		}
		return false;
	}

	public static String getNextWord(String s) {
		if (seed.nextBoolean()) {
			String keyword = "";
			for (int i = 0; i < keyWords.size(); ++i) {
				keyword = keyWords.get(i);
				if (bot.get(s).contains(keyword)) {
					if (seed.nextInt(10) <= 1) {
						return keyword;
					}
				}
			}
		}

		int n = seed.nextInt(bot.get(s).size());
		return bot.get(s).get(n);
	}

	public static String getLongestWord(String s) {
		String[] parts = s.split(" ");
		String longest = parts[0];
		for (int i = 0; i < parts.length; ++i) {
			if (parts[i].length() > longest.length()) {
				longest = parts[i];
			}
		}
		if (longest.length() <= 5) return "";
		return longest;
	}

	public static String getSentence(String prevLine) {
		String longest = getLongestWord(prevLine);
		if (!longest.equals("") && !keyWords.contains(longest)) keyWords.add(0, longest);

		int n = seed.nextInt(kv.size());
		String s = kv.get(n);
		String cur = s;

		if (bot.containsKey(longest)) {
			if (seed.nextBoolean()) {
				//System.out.println("Keyword chosen: longest of previous line; hide " + longest);
				s = "";
				cur = getNextWord(longest);
			} else if (seed.nextBoolean()) {
				n = seed.nextInt(keyWords.size());
				//System.out.println("Keyword chosen: " + keyWords.get(n));
				s = keyWords.get(n);
				cur = s;
			} else if (seed.nextBoolean()) {
				//System.out.println("Keyword chosen: longest of previous line " + longest);
				s = longest;
				cur = s;
			}
		}
		
		while (!isSentenceEnd(cur)) {
			String nxt = getNextWord(cur);
			cur = nxt;
			s += " " + cur;
			if (keyWords.contains(cur)) {
				keyWords.remove(cur);
			}
		}
		s = s.trim();
		if (s.length() == 0) return getSentence(prevLine);
		return s;
	}

	public static void beginConvo() {
		System.out.println("Hey! Who are you talking to?");
		System.out.print("Me: ");
		Scanner scan = new Scanner(System.in);
		botName = scan.nextLine();
		System.out.print(botName + ": ");
		type("Hi. What's up?");
		System.out.println();
		while (true) {
			System.out.print("Me: ");
			String line = scan.nextLine();
			if (line.length() == 0) continue;
			if (line.equals("bye")) break;
			String botLine = getSentence(line);
			botLine = botLine.substring(0, botLine.length() - 1);
			System.out.print(botName + ": ");
			type(botLine);
			System.out.println();
		}
		System.out.print(botName + ": ");
		type("fak u");
		System.out.println("\n");
		System.out.println(botName + " logged off.");
	}

	public static void main(String[] args) {
		String line = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			prevLine = line;
			System.out.println("Give me one second...");
			while((line = bufferedReader.readLine()) != null) {
				line.replace("\"", "");
				line.replace("\'", "");

				// get rid of the 's somehow
				if (line.length() == 0) continue;
				char c = line.charAt(0);
				if (c >= '0' && c <= '9') continue;

				String[] splitAr;
				try {
					splitAr = line.split("\\s+");
					splitAr[splitAr.length - 1] += ".";
					kv.add(splitAr[0]);
				} catch (Exception e) {
					splitAr = new String[0];
				}

				for (int i = 0; i < splitAr.length - 1; ++i) {
					if (!bot.containsKey(splitAr[i])) {
						bot.put(splitAr[i], new ArrayList<String>());
					}
					bot.get(splitAr[i]).add(splitAr[i + 1]);
				}
				prevLine = line;
			}
			bufferedReader.close();
			beginConvo();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file " + fileName);
		} catch (IOException e) {
			System.out.println("Error reading file " + fileName);
		}
	}

}