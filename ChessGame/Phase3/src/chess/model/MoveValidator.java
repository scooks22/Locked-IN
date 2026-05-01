package chess.model;

import chess.model.pieces.*;
import java.util.ArrayList;
import java.util.List;

public class MoveValidator {

    private MoveValidator() {}

    public static boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol,
                                       Board board, PieceColor currentTurn) {
        Piece moving = board.getPiece(fromRow, fromCol);
        if (moving == null || moving.getColor() != currentTurn) return false;

        Piece target = board.getPiece(toRow, toCol);
        if (target != null && target.getColor() == currentTurn) return false;

        List<int[]> candidates = moving.possibleMoves(fromRow, fromCol, board);
        boolean inCandidates = false;
        for (int[] m : candidates) {
            if (m[0] == toRow && m[1] == toCol) { inCandidates = true; break; }
        }
        if (!inCandidates) return false;

        Board simulated = new Board(board);
        simulated.movePiece(fromRow, fromCol, toRow, toCol);
        return !isInCheckRaw(simulated, currentTurn);
    }

    private static boolean isInCheckRaw(Board board, PieceColor color) {
        int[] kingPos = board.findKing(color);
        if (kingPos == null) return false;
        PieceColor opp = color.opposite();
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Piece p = board.getPiece(r, c);
                if (p != null && p.getColor() == opp) {
                    for (int[] m : p.possibleMoves(r, c, board)) {
                        if (m[0] == kingPos[0] && m[1] == kingPos[1]) return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<int[]> getLegalMoves(int row, int col, Board board, PieceColor currentTurn) {
        List<int[]> legal = new ArrayList<>();
        Piece p = board.getPiece(row, col);
        if (p == null || p.getColor() != currentTurn) return legal;

        for (int[] m : p.possibleMoves(row, col, board)) {
            if (isLegalMove(row, col, m[0], m[1], board, currentTurn))
                legal.add(m);
        }
        return legal;
    }

    public static boolean hasAnyLegalMove(Board board, PieceColor color) {
        for (int[] pos : board.getPiecePositions(color)) {
            if (!getLegalMoves(pos[0], pos[1], board, color).isEmpty())
                return true;
        }
        return false;
    }

    public static boolean isCheckmate(Board board, PieceColor color) {
        return isInCheckRaw(board, color) && !hasAnyLegalMove(board, color);
    }

    public static boolean isStalemate(Board board, PieceColor color) {
        return !isInCheckRaw(board, color) && !hasAnyLegalMove(board, color);
    }

    public static boolean isCastleMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        Piece p = board.getPiece(fromRow, fromCol);
        if (p == null || p.getType() != PieceType.KING) return false;
        return Math.abs(toCol - fromCol) == 2 && fromRow == toRow;
    }

    public static boolean isPromotion(int fromRow, int fromCol, int toRow, Board board) {
        Piece p = board.getPiece(fromRow, fromCol);
        if (p == null || p.getType() != PieceType.PAWN) return false;
        return (p.getColor() == PieceColor.WHITE && toRow == 0)
            || (p.getColor() == PieceColor.BLACK && toRow == 7);
    }
}
