import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LineEditor {
	private List<String> lines;
	private String filePath;

	public LineEditor(String filePath) throws IOException {
		this.filePath = filePath;
		loadFile();
	}

	// Load the file into memory
	private void loadFile() throws IOException {
		lines = new ArrayList<>(Files.readAllLines(Paths.get(filePath)));
	}

	// List all lines
	private void listLines() {
		for (int i = 0; i < lines.size(); i++) {
			System.out.println((i + 1) + ": " + lines.get(i));
		}
	}

	// Delete a line
	private void deleteLine(int lineNumber) {
		if (lineNumber < 1 || lineNumber > lines.size()) {
			System.out.println("Invalid line number.");
		} else {
			lines.remove(lineNumber - 1);
		}
	}

	// Insert a line
	private void insertLine(int lineNumber, String text) {
		if (lineNumber < 1 || lineNumber > lines.size() + 1) {
			System.out.println("Invalid line number.");
		} else {
			lines.add(lineNumber - 1, text);
		}
	}

	// Save the file
	private void saveFile() throws IOException {
		Files.write(Paths.get(filePath), lines, StandardOpenOption.TRUNCATE_EXISTING);
		System.out.println("File saved.");
	}

	// Main loop for user input
	public void start() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.print(">> ");
			String command = scanner.nextLine().trim();

			if (command.equals("list")) {
				listLines();

			} else if (command.startsWith("del ")) {
				try {
					int lineNumber = Integer.parseInt(command.substring(4).trim());
					deleteLine(lineNumber);
				} catch (NumberFormatException e) {
					System.out.println("Invalid command format. Use 'del n' where n is a line number.");
				}

			} else if (command.startsWith("ins ")) {
				try {
					int lineNumber = Integer.parseInt(command.substring(4).trim());
					System.out.print("Enter text: ");
					String text = scanner.nextLine();
					insertLine(lineNumber, text);
				} catch (NumberFormatException e) {
					System.out.println("Invalid command format. Use 'ins n' where n is a line number.");
				}

			} else if (command.equals("save")) {
				try {
					saveFile();
				} catch (IOException e) {
					System.out.println("Error saving file: " + e.getMessage());
				}

			} else if (command.equals("quit")) {
				System.out.println("Exiting the editor.");
				break;

			} else {
				System.out.println("Unknown command. Available commands: list, del n, ins n, save, quit.");
			}
		}

		scanner.close();
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java LineEditor <filename>");
			return;
		}

		try {
			LineEditor editor = new LineEditor(args[0]);
			editor.start();
		} catch (IOException e) {
			System.out.println("Error loading file: " + e.getMessage());
		}
	}
}
