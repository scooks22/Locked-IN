package chess.gui;

import chess.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardPanel extends JPanel {

    private static final int DEFAULT_SQUARE_SIZE = 80;

    private static final Color LIGHT_SQUARE      = new Color(240, 217, 181);
    private static final Color DARK_SQUARE       = new Color(181, 136,  99);
    private static final Color HIGHLIGHT_SEL     = new Color(100, 180, 100, 160);
    private static final Color HIGHLIGHT_DST     = new Color(100, 180, 100,  90);
    private static final Color HIGHLIGHT_LAST_FROM = new Color(255, 210, 60, 120);
    private static final Color HIGHLIGHT_LAST_TO   = new Color(255, 210, 60, 160);

    private final GameState gameState;
    private int squareSize = DEFAULT_SQUARE_SIZE;

    private int selectedRow = -1;
    private int selectedCol = -1;

    private boolean isDragging = false;
    private int dragRow        = -1;
    private int dragCol        = -1;
    private int dragCursorX    = 0;
    private int dragCursorY    = 0;

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

    public void setLastMove(int fromRow, int fromCol, int toRow, int toCol) {
        lastFromRow = fromRow; lastFromCol = fromCol;
        lastToRow   = toRow;   lastToCol   = toCol;
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
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Color base = ((r + c) % 2 == 0) ? LIGHT_SQUARE : DARK_SQUARE;
                g2.setColor(base);
                g2.fillRect(c * squareSize, r * squareSize, squareSize, squareSize);

                if (r == lastFromRow && c == lastFromCol)
                    fillOverlay(g2, r, c, HIGHLIGHT_LAST_FROM);
                if (r == lastToRow && c == lastToCol)
                    fillOverlay(g2, r, c, HIGHLIGHT_LAST_TO);
                if (r == selectedRow && c == selectedCol)
                    fillOverlay(g2, r, c, HIGHLIGHT_SEL);

                if (isDragging) {
                    int hoverRow = dragCursorY / squareSize;
                    int hoverCol = dragCursorX / squareSize;
                    if (r == hoverRow && c == hoverCol && !(r == dragRow && c == dragCol))
                        fillOverlay(g2, r, c, HIGHLIGHT_DST);
                }
            }
        }
    }

    private void fillOverlay(Graphics2D g2, int row, int col, Color color) {
        g2.setColor(color);
        g2.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
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
        int x = dragCursorX - squareSize / 2;
        int y = dragCursorY - squareSize / 2;
        drawPieceAt(g2, fm, piece, x, y);
    }

    private void drawPieceAt(Graphics2D g2, FontMetrics fm, Piece piece, int px, int py) {
        String symbol = String.valueOf(piece.getSymbol());
        g2.setColor(new Color(0, 0, 0, 80));
        int tx = px + (squareSize - fm.stringWidth(symbol)) / 2 + 1;
        int ty = py + (squareSize + fm.getAscent() - fm.getDescent()) / 2 + 1;
        g2.drawString(symbol, tx, ty);
        g2.setColor(piece.getColor() == PieceColor.WHITE
                    ? new Color(255, 255, 255)
                    : new Color(20, 20, 20));
        g2.drawString(symbol, tx - 1, ty - 1);
    }

    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameState.isGameOver()) return;
                int row = e.getY() / squareSize;
                int col = e.getX() / squareSize;
                if (!Board.inBounds(row, col)) return;

                Piece clicked = gameState.getBoard().getPiece(row, col);

                if (clicked != null && clicked.getColor() == gameState.getCurrentTurn()) {
                    isDragging  = true;
                    dragRow     = row;
                    dragCol     = col;
                    dragCursorX = e.getX();
                    dragCursorY = e.getY();
                    selectedRow = row;
                    selectedCol = col;
                    repaint();
                } else if (selectedRow >= 0) {
                    tryMove(selectedRow, selectedCol, row, col);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isDragging) return;
                int row = e.getY() / squareSize;
                int col = e.getX() / squareSize;
                isDragging = false;
                if (Board.inBounds(row, col) && !(row == dragRow && col == dragCol)) {
                    tryMove(dragRow, dragCol, row, col);
                } else {
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    dragCursorX = e.getX();
                    dragCursorY = e.getY();
                    repaint();
                }
            }
        });
    }

    private void tryMove(int fromRow, int fromCol, int toRow, int toCol) {
        MoveRecord record = gameState.executeMove(fromRow, fromCol, toRow, toCol);
        if (record != null) {
            setLastMove(fromRow, fromCol, toRow, toCol);
            clearSelection();
            firePropertyChange("moveMade", null, record);
        } else {
            Piece target = gameState.getBoard().getPiece(toRow, toCol);
            if (target != null && target.getColor() == gameState.getCurrentTurn()) {
                selectedRow = toRow;
                selectedCol = toCol;
            } else {
                clearSelection();
            }
        }
        repaint();
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
    }
}
