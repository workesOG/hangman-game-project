import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HangmanGame {
	// Static variables belong to the class itself, not to any specific instance
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
		// A while loop keeps running as long as the condition is true, so while(true)
		// is an infinite loop.
		while (true) {
			String input = scanner.nextLine();
			input = input.toUpperCase();
			if (input.equals("EXIT")) {
				System.out.println(getColorFormattedString("Thank you for playing!", "94"));
				scanner.close();
				// Break stops the loop, instantly exiting it, which is also what justifies the
				// while (true) loop.
				renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet, "exit");
				break;
			}
			else if (input.length() != 1) {
				extraMessage = getColorFormattedString("Please enter a single letter!", "91");
				// Continue skips the rest of the loop, instantly going to the next iteration.
				renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet);
				continue;
			}
			// This checks if the input is in the alphabet array, ensuring the given letter
			// is from the provided alphabet.
			// Arrays.stream() converts array to stream, anyMatch() checks if any element
			// matches the condition
			// input::equals is a method reference - equivalent to (letter) ->
			// input.equals(letter) (above our current level, but it was the easiest way and
			// predicates are the only way to do this in this way)
			else if (!Arrays.stream(alphabet).anyMatch(input::equals)) {
				extraMessage = getColorFormattedString("A letter from the english alphabet please!", "91");
				// Continue skips the rest of the loop, instantly going to the next iteration.
				renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet);
				continue;
			}
			/*
			 * At this point, we have guaranteed that the given input is a single letter
			 * from the english alphabet using the checks above. Therefore we can now safely
			 * treat the variable as such without having to worry about potential errors due
			 * to bad user input.
			 */
			// Add the guessed letter to our list to keep track of what has been guessed
			guessedLetters.add(input);
			// If the word does not contain the input, we increment the incorrect guesses.
			if (!word.contains(input)) {
				incorrectGuesses++;
			}

			// Now we render the screen again with the new information.
			boolean isGameLost = incorrectGuesses >= tries;
			boolean isGameWon = areAllLettersGuessed(word, guessedLetters);

			/*
			 * || means OR - if either condition is true, execute the block The ? : is a
			 * ternary operator - if isGameLost is true, use "lost", otherwise use "won"
			 * Which is the same as: if (isGameLost) { renderScreen(word, guessedLetters,
			 * incorrectGuesses, extraMessage, alphabet, "lost"); } else {
			 * renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet,
			 * "won"); } but taking up significantly less space.
			 */
			if (isGameLost || isGameWon) {
				renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet,
						isGameLost ? "lost" : "won");
				scanner.close();
				break;
			}

			renderScreen(word, guessedLetters, incorrectGuesses, extraMessage, alphabet);
		}
	}

	// This function renders an "image" of the hangman game, ensuring the game
	// window is always the same size and structure.
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

	// Method overloading: same method name but different parameters
	// This version is called when the game is over (won/lost/exit)
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

	// This function clears the console. This has nothing to do with java, but is
	// automatically supported by most terminals just like \n, \t, \r, etc.
	public static void clearConsole() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	// This function returns the ASCII art for the hangman game depending on the
	// stage, from 0 with 0 wrong guesses to 6 (game over) with 6 wrong guesses.
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

	/*
	 * This function returns the alphabet, with guessed letters in magenta and
	 * unguessed letters in white.
	 * 
	 */
	public static String printAlphabet(String[] alphabet, String word, int charsPerRow,
			ArrayList<String> guessedLetters, int spacing, int alternatingLinesOffset) {
		String alphabetDisplay = "";

		// Loop through each letter in the alphabet array
		int numLine = 0;
		for (int i = 0; i < alphabet.length; i++) {
			// Check if this letter has been guessed using the contains() method
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
			// % is the modulo operator - gives the remainder after division
			// This creates a new line every 7 characters (charsPerRow)
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

	/*
	 * This function returns an array of the alphabet letters.
	 * String.valueOf((char)('A' + i)) works, because the ASCII code for A is 65,
	 * and from there until the end of the alphabet, the ASCII code increases by 1
	 * for each letter. (char) is called a type cast, and it converts the integer to
	 * the corresponding character by not using the text value, but the actual ASCII
	 * character code.
	 */
	public static String[] getAlphabetLetters() {
		// Create an array with exactly 26 elements (one for each letter)
		String[] alphabet = new String[26];
		// Generate each letter of the alphabet using ASCII values
		for (int i = 0; i < 26; i++) {
			// 'A' + i: A=65, B=66, C=67, etc. in ASCII
			alphabet[i] = String.valueOf((char) ('A' + i));
		}
		return alphabet;
	}

	// This function returns the word display, with guessed letters in magenta and
	// unguessed letters in white.
	public static String getWordDisplay(String word, ArrayList<String> guessedLetters) {
		String display = "";
		for (int i = 0; i < word.length(); i++) {
			/*
			 * word.charAt(i) gets the character at position i. This works because a
			 * "String" is essentially just an array of characters. This is also the reason
			 * that String starts with an uppercase letter (Because it's a class), while
			 * normal datatypes (int, float etc..) start with a lowercase letter.
			 * String.valueOf() converts the char to a String for comparison.
			 */
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

	// This method returns true if all letters in the word have been guessed
	// This works because we return as soon as we find a letter that hasn't been
	// guessed, meaning that we only make it to the return true statement if all
	// letters have been guessed.
	public static boolean areAllLettersGuessed(String word, ArrayList<String> guessedLetters) {
		for (int i = 0; i < word.length(); i++) {
			// If any letter hasn't been guessed, return false immediately
			if (!guessedLetters.contains(String.valueOf(word.charAt(i)))) {
				return false;
			}
		}
		// If we get here, all letters have been guessed
		return true;
	}

	// Returns a random word from the array of words.
	public static String getRandomWord(String[] words, SecureRandom secureRandom) {
		return words[secureRandom.nextInt(words.length)];
	}

	/*
	 * This function uses ANSI escape codes to color the string and return it. This
	 * has nothing to do with java, but is automatically supported by most terminals
	 * just like \n, \t, \r, etc. Color codes: 30: Black 31: Red 32: Green 33:
	 * Yellow 34: Blue 35: Magenta 36: Cyan 37: White 90: Bright Black 91: Bright
	 * Red 92: Bright Green 93: Bright Yellow 94: Bright Blue 95: Bright Magenta 96:
	 * Bright Cyan 97: Bright White
	 */
	public static String getColorFormattedString(String string, String color) {
		return "\u001B[" + color + "m" + string + "\u001B[0m";
	}
}