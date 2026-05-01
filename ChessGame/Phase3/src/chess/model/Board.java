package chess.model;

import chess.model.pieces.*;
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

        grid[0][0] = new Rook(PieceColor.BLACK);
        grid[0][1] = new Knight(PieceColor.BLACK);
        grid[0][2] = new Bishop(PieceColor.BLACK);
        grid[0][3] = new Queen(PieceColor.BLACK);
        grid[0][4] = new King(PieceColor.BLACK);
        grid[0][5] = new Bishop(PieceColor.BLACK);
        grid[0][6] = new Knight(PieceColor.BLACK);
        grid[0][7] = new Rook(PieceColor.BLACK);

        grid[7][0] = new Rook(PieceColor.WHITE);
        grid[7][1] = new Knight(PieceColor.WHITE);
        grid[7][2] = new Bishop(PieceColor.WHITE);
        grid[7][3] = new Queen(PieceColor.WHITE);
        grid[7][4] = new King(PieceColor.WHITE);
        grid[7][5] = new Bishop(PieceColor.WHITE);
        grid[7][6] = new Knight(PieceColor.WHITE);
        grid[7][7] = new Rook(PieceColor.WHITE);

        for (int c = 0; c < SIZE; c++) {
            grid[1][c] = new Pawn(PieceColor.BLACK);
            grid[6][c] = new Pawn(PieceColor.WHITE);
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

    public boolean isUnderAttack(int row, int col, PieceColor byColor) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getColor() == byColor) {
                    if (p.getType() == PieceType.KING) {
                        int dr = Math.abs(r - row), dc = Math.abs(c - col);
                        if (dr <= 1 && dc <= 1 && (dr + dc > 0)) return true;
                        continue;
                    }
                    List<int[]> moves = p.possibleMoves(r, c, this);
                    for (int[] m : moves) {
                        if (m[0] == row && m[1] == col) return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(PieceColor color) {
        int[] kingPos = findKing(color);
        if (kingPos == null) return false;
        return isUnderAttack(kingPos[0], kingPos[1], color.opposite());
    }
}
