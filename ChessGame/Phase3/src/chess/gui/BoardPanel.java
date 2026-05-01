package chess.gui;

import chess.model.*;
import chess.model.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BoardPanel extends JPanel {

    private static final int DEFAULT_SQUARE_SIZE = 80;

    private static final Color LIGHT_SQUARE        = new Color(240, 217, 181);
    private static final Color DARK_SQUARE         = new Color(181, 136,  99);
    private static final Color HIGHLIGHT_SEL       = new Color( 80, 160,  80, 180);
    private static final Color HIGHLIGHT_LEGAL     = new Color( 80, 160,  80,  80);
    private static final Color HIGHLIGHT_LAST_FROM = new Color(255, 210,  60, 120);
    private static final Color HIGHLIGHT_LAST_TO   = new Color(255, 210,  60, 160);
    private static final Color HIGHLIGHT_CHECK     = new Color(220,  50,  50, 180);
    private static final Color HIGHLIGHT_DST       = new Color( 80, 160,  80, 110);

    private final GameState gameState;
    private int squareSize = DEFAULT_SQUARE_SIZE;

    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<int[]> legalMovesForSelected = null;

    private boolean isDragging = false;
    private int dragRow = -1, dragCol = -1;
    private int dragCursorX = 0, dragCursorY = 0;

    private int lastFromRow = -1, lastFromCol = -1;
    private int lastToRow   = -1, lastToCol   = -1;

    public BoardPanel(GameState gameState) {
        this.gameState = gameState;
        setPreferredSize(new Dimension(squareSize * Board.SIZE, squareSize * Board.SIZE));
        setFont(new Font("Serif", Font.PLAIN, (int)(squareSize * 0.72)));
        addMouseListeners();
    }

    public void refresh() {
        clearSelection();
        repaint();
    }

    public void setSquareSize(int size) {
        this.squareSize = size;
        setPreferredSize(new Dimension(size * Board.SIZE, size * Board.SIZE));
        setFont(new Font("Serif", Font.PLAIN, (int)(size * 0.72)));
        revalidate();
        repaint();
    }

    public void setLastMove(int fr, int fc, int tr, int tc) {
        lastFromRow = fr; lastFromCol = fc;
        lastToRow   = tr; lastToCol   = tc;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawSquares(g2);
        drawPieces(g2);
        if (isDragging) drawDragGhost(g2);
    }

    private void drawSquares(Graphics2D g2) {
        int[] kingInCheck = null;
        if (gameState.getStatus() == GameState.Status.CHECK
                || gameState.getStatus() == GameState.Status.CHECKMATE) {
            kingInCheck = gameState.getBoard().findKing(gameState.getCurrentTurn());
        }

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                g2.setColor(((r + c) % 2 == 0) ? LIGHT_SQUARE : DARK_SQUARE);
                g2.fillRect(c * squareSize, r * squareSize, squareSize, squareSize);

                if (r == lastFromRow && c == lastFromCol) fillOverlay(g2, r, c, HIGHLIGHT_LAST_FROM);
                if (r == lastToRow   && c == lastToCol)   fillOverlay(g2, r, c, HIGHLIGHT_LAST_TO);

                if (kingInCheck != null && r == kingInCheck[0] && c == kingInCheck[1])
                    fillOverlay(g2, r, c, HIGHLIGHT_CHECK);

                if (r == selectedRow && c == selectedCol)
                    fillOverlay(g2, r, c, HIGHLIGHT_SEL);

                if (legalMovesForSelected != null) {
                    for (int[] m : legalMovesForSelected) {
                        if (m[0] == r && m[1] == c) {
                            fillOverlay(g2, r, c, HIGHLIGHT_LEGAL);
                            Piece t = gameState.getBoard().getPiece(r, c);
                            if (t != null) drawCaptureRing(g2, r, c);
                        }
                    }
                }

                if (isDragging) {
                    int hr = dragCursorY / squareSize, hc = dragCursorX / squareSize;
                    if (r == hr && c == hc && !(r == dragRow && c == dragCol))
                        fillOverlay(g2, r, c, HIGHLIGHT_DST);
                }
            }
        }
    }

    private void fillOverlay(Graphics2D g2, int row, int col, Color color) {
        g2.setColor(color);
        g2.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
    }

    private void drawCaptureRing(Graphics2D g2, int row, int col) {
        g2.setColor(new Color(80, 160, 80, 140));
        g2.setStroke(new BasicStroke(3));
        int margin = 4;
        g2.drawOval(col * squareSize + margin, row * squareSize + margin,
                    squareSize - margin * 2, squareSize - margin * 2);
        g2.setStroke(new BasicStroke(1));
    }

    private void drawPieces(Graphics2D g2) {
        FontMetrics fm = g2.getFontMetrics();
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                if (isDragging && r == dragRow && c == dragCol) continue;
                Piece piece = gameState.getBoard().getPiece(r, c);
                if (piece != null) drawPieceAt(g2, fm, piece, c * squareSize, r * squareSize);
            }
        }
    }

    private void drawDragGhost(Graphics2D g2) {
        Piece piece = gameState.getBoard().getPiece(dragRow, dragCol);
        if (piece == null) return;
        FontMetrics fm = g2.getFontMetrics();
        drawPieceAt(g2, fm, piece, dragCursorX - squareSize / 2, dragCursorY - squareSize / 2);
    }

    private void drawPieceAt(Graphics2D g2, FontMetrics fm, Piece piece, int px, int py) {
        String symbol = String.valueOf(piece.getSymbol());
        int tx = px + (squareSize - fm.stringWidth(symbol)) / 2 + 1;
        int ty = py + (squareSize + fm.getAscent() - fm.getDescent()) / 2 + 1;
        g2.setColor(new Color(0, 0, 0, 80));
        g2.drawString(symbol, tx, ty);
        g2.setColor(piece.getColor() == PieceColor.WHITE
                    ? new Color(255, 255, 255) : new Color(20, 20, 20));
        g2.drawString(symbol, tx - 1, ty - 1);
    }

    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameState.isGameOver()) return;
                int row = e.getY() / squareSize, col = e.getX() / squareSize;
                if (!Board.inBounds(row, col)) return;

                Piece clicked = gameState.getBoard().getPiece(row, col);

                if (clicked != null && clicked.getColor() == gameState.getCurrentTurn()) {
                    isDragging  = true;
                    dragRow     = row; dragCol = col;
                    dragCursorX = e.getX(); dragCursorY = e.getY();
                    selectedRow = row; selectedCol = col;
                    legalMovesForSelected = MoveValidator.getLegalMoves(row, col,
                                               gameState.getBoard(), gameState.getCurrentTurn());
                    repaint();
                } else if (selectedRow >= 0) {
                    tryMove(selectedRow, selectedCol, row, col);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isDragging) return;
                int row = e.getY() / squareSize, col = e.getX() / squareSize;
                isDragging = false;
                if (Board.inBounds(row, col) && !(row == dragRow && col == dragCol))
                    tryMove(dragRow, dragCol, row, col);
                else
                    repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    dragCursorX = e.getX(); dragCursorY = e.getY();
                    repaint();
                }
            }
        });
    }

    private void tryMove(int fromRow, int fromCol, int toRow, int toCol) {
        PieceType promotionChoice = PieceType.QUEEN;

        if (MoveValidator.isPromotion(fromRow, fromCol, toRow, gameState.getBoard())) {
            promotionChoice = askPromotion();
        }

        MoveRecord record = gameState.executeMove(fromRow, fromCol, toRow, toCol, promotionChoice);
        if (record != null) {
            setLastMove(fromRow, fromCol, toRow, toCol);
            clearSelection();
            firePropertyChange("moveMade", null, record);
        } else {
            Piece target = gameState.getBoard().getPiece(toRow, toCol);
            if (target != null && target.getColor() == gameState.getCurrentTurn()) {
                selectedRow = toRow; selectedCol = toCol;
                legalMovesForSelected = MoveValidator.getLegalMoves(toRow, toCol,
                                           gameState.getBoard(), gameState.getCurrentTurn());
            } else {
                clearSelection();
            }
        }
        repaint();
    }

    private PieceType askPromotion() {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(this,
            "Promote pawn to:", "Pawn Promotion",
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
            null, options, options[0]);
        return switch (choice) {
            case 1 -> PieceType.ROOK;
            case 2 -> PieceType.BISHOP;
            case 3 -> PieceType.KNIGHT;
            default -> PieceType.QUEEN;
        };
    }

    private void clearSelection() {
        selectedRow = -1; selectedCol = -1;
        legalMovesForSelected = null;
    }
}
