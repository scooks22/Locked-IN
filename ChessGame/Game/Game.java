package game;
 
import board.Board;
import player.Player;
import utils.Position;
 
import java.util.Scanner;
 
public class Game {
 
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private String currentTurn;
    private boolean running;
 
    public Game() {
        board = new Board();
        whitePlayer = new Player("white");
        blackPlayer = new Player("black");
        currentTurn = "white";
        running = false;
    }
 
    public void start() {
        running = true;
        System.out.println("Chess Game Started!");
        System.out.println("Enter moves in the format: E2 E4");
        System.out.println("Type 'quit' to exit.\n");
        play();
    }
 
    public void end(String winner) {
        running = false;
        if (winner == null) {
            System.out.println("Game over. It's a draw (stalemate)!");
        } else {
            System.out.println("Checkmate! " + winner + " wins!");
        }
    }
 
    public void play() {
        Scanner scanner = new Scanner(System.in);
 
        while (running) {
            board.display();
            System.out.println("\n" + currentTurn.toUpperCase() + "'s turn.");
 
            if (board.isCheckmate(currentTurn)) {
                String winner = currentTurn.equals("white") ? "Black" : "White";
                end(winner);
                break;
            }
 
            if (board.isCheck(currentTurn)) {
                System.out.println("Warning: " + currentTurn.toUpperCase() + " is in CHECK!");
            }
 
            System.out.print("Enter move: ");
            String input = scanner.nextLine().trim();
 
            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Game ended by player.");
                running = false;
                break;
            }
 
            if (!isValidFormat(input)) {
                System.out.println("Invalid format. Please use format like 'E2 E4'.\n");
                continue;
            }
 
            String[] parts = input.toUpperCase().split(" ");
            Position from = parsePosition(parts[0]);
            Position to = parsePosition(parts[1]);
 
            if (from == null || to == null) {
                System.out.println("Invalid position entered. Try again.\n");
                continue;
            }
 
            pieces.Piece selectedPiece = board.getPiece(from);
            if (selectedPiece == null) {
                System.out.println("No piece at " + parts[0] + ". Try again.\n");
                continue;
            }
 
            if (!selectedPiece.getColor().equals(currentTurn)) {
                System.out.println("That piece belongs to your opponent. Try again.\n");
                continue;
            }
 
            boolean success = board.movePiece(from, to);
            if (!success) {
                System.out.println("Illegal move. Try again.\n");
                continue;
            }
 
            currentTurn = currentTurn.equals("white") ? "black" : "white";
        }
 
        scanner.close();
    }
 
    private boolean isValidFormat(String input) {
        if (input == null || input.isEmpty()) return false;
        String[] parts = input.toUpperCase().split(" ");
        if (parts.length != 2) return false;
        return parts[0].matches("[A-H][1-8]") && parts[1].matches("[A-H][1-8]");
    }
 
    private Position parsePosition(String notation) {
        if (notation == null || notation.length() != 2) return null;
        char fileLetter = notation.charAt(0);
        char rankChar = notation.charAt(1);
        int col = fileLetter - 'A';
        int row = rankChar - '1';
        if (row < 0 || row > 7 || col < 0 || col > 7) return null;
        return new Position(row, col);
    }
 
    public Board getBoard() {
        return board;
    }
 
    public String getCurrentTurn() {
        return currentTurn;
    }
}
