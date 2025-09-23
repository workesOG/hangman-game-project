import java.util.Scanner;

public class App {
	static String[] words = { "apple", "banana", "cherry", "date", "elderberry", };
	static int tries = 6;

	public static void main(String[] args) throws Exception {
		clearConsole();

		Scanner scanner = new Scanner(System.in);
		String word = words[0];
		renderScreen();
		while (true) {
			String input = scanner.nextLine();

		}
	}

	public static void renderScreen() throws InterruptedException {
		System.out.println("\u001B[31mHello, World!\u001B[0m");
		System.out.println("\u001B[32mHello, World!\u001B[0m");
		System.out.println(getHangManAsciiArt(0));
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
			headLine = " O  |";
		}

		if (stage < 2) {
			bodyLine = "    |";
		}
		else if (stage < 3) {
			bodyLine = " |  |";
		}
		else if (stage < 4) {
			bodyLine = "/|  |";
		}
		else {
			bodyLine = "/|\\ |";
		}

		if (stage < 5) {
			legsLine = "    |";
		}
		else if (stage < 6) {
			legsLine = "/   |";
		}
		else {
			legsLine = "/ \\ |";
		}

		String asciiArt = "";
		asciiArt += " +--+\n";
		asciiArt += " |  | \n";
		asciiArt += headLine + "\n";
		asciiArt += bodyLine + "\n";
		asciiArt += legsLine + "\n";
		asciiArt += "    |\n";
		asciiArt += "=====\n";

		return asciiArt;
	}
}
