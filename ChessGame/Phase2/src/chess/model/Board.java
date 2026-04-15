package chess.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int SIZE = 8;
    private Piece[][] grid;

    public Board() {
        grid = new Piece[SIZE][SIZE];
        setupInitialPosition();
    }

    public Board(Board other) {
        grid = new Piece[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                grid[r][c] = other.grid[r][c];
    }

    public void setupInitialPosition() {
        grid = new Piece[SIZE][SIZE];
        PieceType[] backRank = {
            PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP,
            PieceType.QUEEN, PieceType.KING,
            PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
        };
        for (int c = 0; c < SIZE; c++) {
            grid[0][c] = new Piece(backRank[c], PieceColor.BLACK);
            grid[1][c] = new Piece(PieceType.PAWN, PieceColor.BLACK);
            grid[6][c] = new Piece(PieceType.PAWN, PieceColor.WHITE);
            grid[7][c] = new Piece(backRank[c], PieceColor.WHITE);
        }
    }

    public Piece getPiece(int row, int col) {
        return grid[row][col];
    }

    public void setPiece(int row, int col, Piece piece) {
        grid[row][col] = piece;
    }

    public Piece movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece captured = grid[toRow][toCol];
        grid[toRow][toCol] = grid[fromRow][fromCol];
        grid[fromRow][fromCol] = null;
        return captured;
    }

    public static boolean inBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public int[] findKing(PieceColor color) {
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getType() == PieceType.KING && p.getColor() == color)
                    return new int[]{r, c};
            }
        return null;
    }

    public List<int[]> getPiecePositions(PieceColor color) {
        List<int[]> positions = new ArrayList<>();
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getColor() == color)
                    positions.add(new int[]{r, c});
            }
        return positions;
    }
}
