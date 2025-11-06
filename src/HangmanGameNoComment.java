import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HangmanGameNoComment {
	static String[] words = { "apple", "banana", "cherry", "date", "elderberry", "computer", "keyboard", "monitor",
			"mouse", "laptop", "elephant", "giraffe", "penguin", "dolphin", "tiger", "mountain", "ocean", "forest",
			"desert", "valley", "pizza", "hamburger", "sandwich", "pancake", "waffle", "guitar", "piano", "violin",
			"trumpet", "drum", "bicycle", "airplane", "truck", "motorcycle", "helicopter", "rainbow", "thunder",
			"lightning", "snowflake", "sunshine", "library", "hospital", "restaurant", "museum", "theater", "adventure",
			"mystery", "journey", "discovery", "treasure", "butterfly", "dragonfly", "ladybug", "grasshopper",
			"caterpillar", "birthday", "holiday", "vacation", "celebration", "festival", "basketball", "football",
			"baseball", "tennis", "soccer", "chocolate", "vanilla", "strawberry", "cinnamon", "peppermint", "backpack",
			"umbrella", "camera", "telephone", "calendar" };
	static int tries = 6;

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		SecureRandom secureRandom = new SecureRandom();

		String word = getRandomWord(words, secureRandom).toUpperCase();
		String extraMessage = "";
		int incorrectGuesses = 0;
		ArrayList<String> guessedLetters = new ArrayList<>();
		String[] alphabet = getAlphabetLetters();

		renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet);
		while (true) {
			String input = scanner.nextLine();
			input = input.toUpperCase();
			if (input.equals("EXIT")) {
				System.out.println(getColorFormattedString("Thank you for playing!", "94"));
				scanner.close();
				renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet, "exit");
				break;
			}
			else if (input.length() != 1) {
				extraMessage = getColorFormattedString("Please enter a single letter!", "91");
				renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet);
				continue;
			}
			else if (!Arrays.stream(alphabet).anyMatch(input::equals)) {
				extraMessage = getColorFormattedString("A letter from the english alphabet please!", "91");
				renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet);
				continue;
			}
			guessedLetters.add(input);
			if (!word.contains(input)) {
				incorrectGuesses++;
			}

			boolean isGameLost = incorrectGuesses >= tries;
			boolean isGameWon = areAllLettersGuessed(word, guessedLetters);

			if (isGameLost || isGameWon) {
				renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet,
						isGameLost ? "lost" : "won");
				scanner.close();
				break;
			}

			renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet);
		}
	}

	public static void renderScreen(String word, ArrayList<String> guessedLetters, int gameStage, String extraMessage,
			String[] alphabet) {
		clearConsole();
		System.out.println(getColorFormattedString("Welcome to Hangman!", "94"));
		System.out.println(getHangManAsciiArt(gameStage));
		System.out.println(getWordDisplay(word, guessedLetters));
		System.out.println(printAlphabet(alphabet, word, 7, guessedLetters, 3, 2));
		System.out.println(getColorFormattedString("--------------------------------", "34"));
		System.out.println(extraMessage);
		System.out.println(getColorFormattedString("Please enter a letter, or 'exit' to stop playing:", "34"));
	}

	public static void renderScreen(String word, ArrayList<String> guessedLetters, int gameStage, String extraMessage,
			String[] alphabet, String gameState) {
		clearConsole();
		System.out.println(getColorFormattedString("Welcome to Hangman!", "94"));
		System.out.println(getHangManAsciiArt(gameStage));
		System.out.println(getWordDisplay(word, guessedLetters));
		System.out.println(printAlphabet(alphabet, word, 7, guessedLetters, 3, 2));
		System.out.println(getColorFormattedString("--------------------------------", "34"));
		if (gameState.equals("lost")) {
			System.out
					.println(getColorFormattedString("What have you done? You killed him! Game over for you...", "91"));
		}
		else if (gameState.equals("won")) {
			System.out.println(getColorFormattedString("Good job, you won! You're a genius!", "92"));
		}
		else if (gameState.equals("exit")) {
			System.out.println(getColorFormattedString("No more I guess :(... Thank you for playing!", "94"));
		}
	}

	public static void clearConsole() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static String getHangManAsciiArt(int stage) {
		String headLine;
		String bodyLine;
		String legsLine;

		if (stage < 1) {
			headLine = "    |";
		}
		else {
			headLine = String.format(" %s  %s", getColorFormattedString("O", "33"), getColorFormattedString("|", "34"));
		}

		if (stage < 2) {
			bodyLine = "    |";
		}
		else if (stage < 3) {
			bodyLine = String.format(" %s  %s", getColorFormattedString("|", "33"), getColorFormattedString("|", "34"));
		}
		else if (stage < 4) {
			bodyLine = String.format("%s  %s", getColorFormattedString("/|", "33"), getColorFormattedString("|", "34"));
		}
		else {
			bodyLine = String.format("%s %s", getColorFormattedString("/|\\", "33"),
					getColorFormattedString("|", "34"));
		}

		if (stage < 5) {
			legsLine = "    |";
		}
		else if (stage < 6) {
			legsLine = String.format("%s   %s", getColorFormattedString("/", "33"), getColorFormattedString("|", "34"));
		}
		else {
			legsLine = String.format("%s %s", getColorFormattedString("/ \\", "33"),
					getColorFormattedString("|", "34"));
		}

		String asciiArt = "";
		asciiArt += getColorFormattedString(" +--+\n", "34");
		asciiArt += getColorFormattedString(" |  | \n", "34");
		asciiArt += getColorFormattedString(headLine + "\n", "34");
		asciiArt += getColorFormattedString(bodyLine + "\n", "34");
		asciiArt += getColorFormattedString(legsLine + "\n", "34");
		asciiArt += getColorFormattedString("    |\n", "34");
		asciiArt += getColorFormattedString("=====\n", "34");

		return asciiArt;
	}

	public static String printAlphabet(String[] alphabet, String word, int charsPerRow,
			ArrayList<String> guessedLetters, int spacing, int alternatingLinesOffset) {
		String alphabetDisplay = "";

		int numLine = 0;
		for (int i = 0; i < alphabet.length; i++) {
			if (guessedLetters.contains(alphabet[i])) {
				if (word.contains(alphabet[i])) {
					alphabetDisplay += getColorFormattedString(alphabet[i], "32");
				}
				else {
					alphabetDisplay += getColorFormattedString(alphabet[i], "31");
				}
			}
			else {
				alphabetDisplay += alphabet[i];
			}
			if (spacing > 0) {
				for (int j = 0; j < spacing; j++) {
					alphabetDisplay += " ";
				}
			}
			if (i % charsPerRow == charsPerRow - 1) {
				alphabetDisplay += "\n";
				if (numLine % alternatingLinesOffset == 0) {
					for (int j = 0; j < alternatingLinesOffset; j++) {
						alphabetDisplay += " ";
					}
				}
				numLine++;

			}
		}
		return alphabetDisplay;
	}

	public static String[] getAlphabetLetters() {
		String[] alphabet = new String[26];
		for (int i = 0; i < 26; i++) {
			alphabet[i] = String.valueOf((char) ('A' + i));
		}
		return alphabet;
	}

	public static String getWordDisplay(String word, ArrayList<String> guessedLetters) {
		String display = "";
		for (int i = 0; i < word.length(); i++) {
			if (guessedLetters.contains(String.valueOf(word.charAt(i)))) {
				display += getColorFormattedString(String.valueOf(word.charAt(i)), "35");
			}
			else {
				display += "_";
			}
			if (i < word.length() - 1) {
				display += " ";
			}
		}
		return display;
	}

	public static boolean areAllLettersGuessed(String word, ArrayList<String> guessedLetters) {
		for (int i = 0; i < word.length(); i++) {
			if (!guessedLetters.contains(String.valueOf(word.charAt(i)))) {
				return false;
			}
		}
		return true;
	}

	public static String getRandomWord(String[] words, SecureRandom secureRandom) {
		return words[secureRandom.nextInt(words.length)];
	}

	public static String getColorFormattedString(String string, String color) {
		return "\u001B[" + color + "m" + string + "\u001B[0m";
	}
}

