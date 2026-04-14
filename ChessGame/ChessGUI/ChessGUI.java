package ChessGUI;

import Game.Game;
import Board.Board;
import Pieces.Piece;
import utils.Position;
import Player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ChessGUI extends JFrame {
    private Game game;
    private JPanel boardPanel;
    private JPanel statusPanel;
    private JPanel controlPanel;
    private JButton[][] squares = new JButton[8][8];
    private JLabel statusLabel;
    private JLabel turnLabel;
    private Position selectedPosition = null;
    private Color lightSquare = new Color(240, 217, 181);
    private Color darkSquare = new Color(181, 136, 99);
    private Color selectedSquare = new Color(255, 255, 0, 128);
    private Color possibleMoveSquare = new Color(0, 255, 0, 64);
    private Color lastMoveSquare = new Color(255, 255, 0, 64);

    public ChessGUI() {
        initializeGame();
        initializeGUI();
        updateBoard();
    }

    private void initializeGame() {
        game = new Game();
    }

    private void initializeGUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        createBoardPanel();
        createStatusPanel();
        createControlPanel();

        add(boardPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
    }

    private void createBoardPanel() {
        boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(60, 60));
                // Try multiple fonts for Unicode chess pieces
                Font[] chessFonts = {
                    new Font("Segoe UI Symbol", Font.BOLD, 36),
                    new Font("Arial Unicode MS", Font.BOLD, 36),
                    new Font("DejaVu Sans", Font.BOLD, 36),
                    new Font("Lucida Sans Unicode", Font.BOLD, 36),
                    new Font("SansSerif", Font.BOLD, 36)
                };
                
                Font chessFont = null;
                for (Font font : chessFonts) {
                    if (font.canDisplay('\u2654')) {
                        chessFont = font;
                        break;
                    }
                }
                
                if (chessFont == null) {
                    chessFont = new Font("SansSerif", Font.BOLD, 36);
                }
                
                square.setFont(chessFont);
                square.setFocusPainted(false);
                square.setContentAreaFilled(false);
                square.setOpaque(true);
                
                final int finalRow = row;
                final int finalCol = col;
                
                square.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleSquareClick(finalRow, finalCol);
                    }
                });

                squares[row][col] = square;
                boardPanel.add(square);
            }
        }
    }

    private void createStatusPanel() {
        statusPanel = new JPanel(new GridLayout(2, 1));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        statusLabel = new JLabel("Chess Game Started!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        turnLabel = new JLabel("White's Turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        statusPanel.add(statusLabel);
        statusPanel.add(turnLabel);
    }

    private void createControlPanel() {
        controlPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton newGameButton = new JButton("New Game");
        JButton resignButton = new JButton("Resign");
        JButton drawButton = new JButton("Offer Draw");
        JButton exitButton = new JButton("Exit");
        
        newGameButton.addActionListener(e -> startNewGame());
        resignButton.addActionListener(e -> handleResign());
        drawButton.addActionListener(e -> handleDraw());
        exitButton.addActionListener(e -> System.exit(0));
        
        controlPanel.add(newGameButton);
        controlPanel.add(resignButton);
        controlPanel.add(drawButton);
        controlPanel.add(exitButton);
    }

    private void handleSquareClick(int row, int col) {
        Position clickedPosition = new Position(row, col);
        
        if (selectedPosition == null) {
            // Select a piece
            Piece piece = game.getBoard().getPiece(clickedPosition);
            if (piece != null && piece.getColor().equals(game.getCurrentTurn())) {
                selectedPosition = clickedPosition;
                updateBoard();
                highlightPossibleMoves(clickedPosition);
            }
        } else {
            // Try to move the selected piece
            if (isValidMove(selectedPosition, clickedPosition)) {
                boolean success = game.getBoard().movePiece(selectedPosition, clickedPosition);
                if (success) {
                    // Switch turns
                    switchTurn();
                    selectedPosition = null;
                    updateBoard();
                    checkGameStatus();
                } else {
                    statusLabel.setText("Invalid move! Try again.");
                    selectedPosition = null;
                    updateBoard();
                }
            } else {
                // Deselect or select a different piece
                Piece piece = game.getBoard().getPiece(clickedPosition);
                if (piece != null && piece.getColor().equals(game.getCurrentTurn())) {
                    selectedPosition = clickedPosition;
                    updateBoard();
                    highlightPossibleMoves(clickedPosition);
                } else {
                    selectedPosition = null;
                    updateBoard();
                }
            }
        }
    }

    private boolean isValidMove(Position from, Position to) {
        Piece piece = game.getBoard().getPiece(from);
        if (piece == null) return false;
        
        List<Position> possibleMoves = piece.possibleMoves(game.getBoard());
        for (Position move : possibleMoves) {
            if (move.getRow() == to.getRow() && move.getCol() == to.getCol()) {
                return true;
            }
        }
        return false;
    }

    private void highlightPossibleMoves(Position position) {
        Piece piece = game.getBoard().getPiece(position);
        if (piece == null) return;
        
        List<Position> possibleMoves = piece.possibleMoves(game.getBoard());
        for (Position move : possibleMoves) {
            JButton square = squares[move.getRow()][move.getCol()];
            square.setBackground(possibleMoveSquare);
        }
    }

    private void updateBoard() {
        Board board = game.getBoard();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = squares[row][col];
                Position position = new Position(row, col);
                Piece piece = board.getPiece(position);
                
                // Set square color
                if ((row + col) % 2 == 0) {
                    square.setBackground(lightSquare);
                } else {
                    square.setBackground(darkSquare);
                }
                
                // Highlight selected square
                if (selectedPosition != null && 
                    selectedPosition.getRow() == row && 
                    selectedPosition.getCol() == col) {
                    square.setBackground(selectedSquare);
                }
                
                // Set piece text
                if (piece != null) {
                    square.setText(getPieceUnicode(piece));
                    square.setForeground(piece.getColor().equals("white") ? Color.WHITE : Color.BLACK);
                } else {
                    square.setText("");
                }
            }
        }
    }

    private String getPieceUnicode(Piece piece) {
        String symbol = piece.getSymbol();
        
        switch (symbol) {
            case "wK": return "\u2654"; // White king
            case "bK": return "\u265A"; // Black king
            case "wQ": return "\u2655"; // White queen
            case "bQ": return "\u265B"; // Black queen
            case "wR": return "\u2656"; // White rook
            case "bR": return "\u265C"; // Black rook
            case "wB": return "\u2657"; // White bishop
            case "bB": return "\u265D"; // Black bishop
            case "wN": return "\u2658"; // White knight
            case "bN": return "\u265E"; // Black knight
            case "wp": return "\u2659"; // White pawn
            case "bp": return "\u265F"; // Black pawn
            default: return "?";
        }
    }

    private void switchTurn() {
        game.switchTurn();
        String currentTurn = game.getCurrentTurn();
        turnLabel.setText(currentTurn.toUpperCase() + "'s Turn");
    }

    private void checkGameStatus() {
        String currentTurn = game.getCurrentTurn();
        
        if (game.getBoard().isCheckmate(currentTurn)) {
            String winner = currentTurn.equals("white") ? "Black" : "White";
            statusLabel.setText("Checkmate! " + winner + " wins!");
            disableBoard();
        } else if (game.getBoard().isCheck(currentTurn)) {
            statusLabel.setText(currentTurn.toUpperCase() + " is in CHECK!");
        } else {
            statusLabel.setText("Game in progress...");
        }
    }

    private void disableBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setEnabled(false);
            }
        }
    }

    private void enableBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setEnabled(true);
            }
        }
    }

    private void startNewGame() {
        initializeGame();
        selectedPosition = null;
        enableBoard();
        updateBoard();
        statusLabel.setText("New game started!");
        turnLabel.setText("White's Turn");
    }

    private void handleResign() {
        String currentTurn = game.getCurrentTurn();
        String winner = currentTurn.equals("white") ? "Black" : "White";
        statusLabel.setText(currentTurn.toUpperCase() + " resigned! " + winner + " wins!");
        disableBoard();
    }

    private void handleDraw() {
        statusLabel.setText("Draw offered by " + game.getCurrentTurn().toUpperCase());
        // In a real implementation, this would wait for the opponent's response
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChessGUI().setVisible(true);
            }
        });
    }
}
