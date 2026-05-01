package chess.model;

import chess.model.pieces.*;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status { PLAYING, CHECK, CHECKMATE, STALEMATE }

    private Board board;
    private PieceColor currentTurn;
    private Status status;
    private PieceColor winner;

    private final List<MoveRecord> moveHistory;
    private final Deque<MoveRecord> undoStack;
    private final List<Piece> capturedByWhite;
    private final List<Piece> capturedByBlack;

    public GameState() {
        board           = new Board();
        currentTurn     = PieceColor.WHITE;
        status          = Status.PLAYING;
        winner          = null;
        moveHistory     = new ArrayList<>();
        undoStack       = new ArrayDeque<>();
        capturedByWhite = new ArrayList<>();
        capturedByBlack = new ArrayList<>();
    }

    public Board getBoard()                      { return board; }
    public PieceColor getCurrentTurn()           { return currentTurn; }
    public Status getStatus()                    { return status; }
    public boolean isGameOver()                  { return status == Status.CHECKMATE || status == Status.STALEMATE; }
    public PieceColor getWinner()                { return winner; }
    public List<MoveRecord> getMoveHistory()     { return moveHistory; }
    public List<Piece> getCapturedByWhite()      { return capturedByWhite; }
    public List<Piece> getCapturedByBlack()      { return capturedByBlack; }

    public MoveRecord executeMove(int fromRow, int fromCol, int toRow, int toCol) {
        return executeMove(fromRow, fromCol, toRow, toCol, PieceType.QUEEN);
    }

    public MoveRecord executeMove(int fromRow, int fromCol, int toRow, int toCol,
                                   PieceType promotionChoice) {
        if (isGameOver()) return null;

        if (!MoveValidator.isLegalMove(fromRow, fromCol, toRow, toCol, board, currentTurn))
            return null;

        Piece moving = board.getPiece(fromRow, fromCol);
        Board snapshot = new Board(board);

        boolean isCastle    = MoveValidator.isCastleMove(fromRow, fromCol, toRow, toCol, board);
        boolean isPromotion = MoveValidator.isPromotion(fromRow, fromCol, toRow, board);

        Piece captured = board.movePiece(fromRow, fromCol, toRow, toCol);

        if (moving instanceof Pawn)  ((Pawn) moving).setHasMoved(true);
        if (moving instanceof Rook)  ((Rook) moving).setHasMoved(true);
        if (moving instanceof King)  ((King) moving).setHasMoved(true);

        if (isCastle) {
            int backRank = fromRow;
            if (toCol == 6) {
                Piece rook = board.getPiece(backRank, 7);
                board.setPiece(backRank, 5, rook);
                board.setPiece(backRank, 7, null);
                if (rook instanceof Rook) ((Rook) rook).setHasMoved(true);
            } else {
                Piece rook = board.getPiece(backRank, 0);
                board.setPiece(backRank, 3, rook);
                board.setPiece(backRank, 0, null);
                if (rook instanceof Rook) ((Rook) rook).setHasMoved(true);
            }
        }

        if (isPromotion) {
            Piece promoted = switch (promotionChoice) {
                case QUEEN  -> new Queen(currentTurn);
                case ROOK   -> new Rook(currentTurn);
                case BISHOP -> new Bishop(currentTurn);
                case KNIGHT -> new Knight(currentTurn);
                default     -> new Queen(currentTurn);
            };
            board.setPiece(toRow, toCol, promoted);
        }

        MoveRecord record = new MoveRecord(fromRow, fromCol, toRow, toCol,
                                           moving, captured, snapshot,
                                           isPromotion, isCastle);
        moveHistory.add(record);
        undoStack.push(record);

        if (captured != null) {
            if (currentTurn == PieceColor.WHITE) capturedByWhite.add(captured);
            else capturedByBlack.add(captured);
        }

        currentTurn = currentTurn.opposite();
        updateStatus();

        return record;
    }

    private void updateStatus() {
        if (MoveValidator.isCheckmate(board, currentTurn)) {
            status = Status.CHECKMATE;
            winner = currentTurn.opposite();
        } else if (MoveValidator.isStalemate(board, currentTurn)) {
            status = Status.STALEMATE;
            winner = null;
        } else if (board.isInCheck(currentTurn)) {
            status = Status.CHECK;
        } else {
            status = Status.PLAYING;
        }
    }

    public MoveRecord undoLastMove() {
        if (undoStack.isEmpty()) return null;

        MoveRecord last = undoStack.pop();
        moveHistory.remove(moveHistory.size() - 1);

        if (last.getCapturedPiece() != null) {
            if (last.getMovedPiece().getColor() == PieceColor.WHITE)
                capturedByWhite.remove(capturedByWhite.size() - 1);
            else
                capturedByBlack.remove(capturedByBlack.size() - 1);
        }

        board       = last.getBoardBefore();
        currentTurn = last.getMovedPiece().getColor();
        status      = Status.PLAYING;
        winner      = null;

        updateStatus();
        return last;
    }

    public void reset() {
        board = new Board();
        currentTurn = PieceColor.WHITE;
        status      = Status.PLAYING;
        winner      = null;
        moveHistory.clear();
        undoStack.clear();
        capturedByWhite.clear();
        capturedByBlack.clear();
    }
}
