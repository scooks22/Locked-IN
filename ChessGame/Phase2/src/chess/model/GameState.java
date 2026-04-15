package chess.model;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Board board;
    private PieceColor currentTurn;
    private boolean gameOver;
    private PieceColor winner;

    private final List<MoveRecord> moveHistory;
    private final Deque<MoveRecord> undoStack;
    private final List<Piece> capturedByWhite;
    private final List<Piece> capturedByBlack;

    public GameState() {
        board           = new Board();
        currentTurn     = PieceColor.WHITE;
        gameOver        = false;
        winner          = null;
        moveHistory     = new ArrayList<>();
        undoStack       = new ArrayDeque<>();
        capturedByWhite = new ArrayList<>();
        capturedByBlack = new ArrayList<>();
    }

    public Board getBoard()                      { return board; }
    public PieceColor getCurrentTurn()           { return currentTurn; }
    public boolean isGameOver()                  { return gameOver; }
    public PieceColor getWinner()                { return winner; }
    public List<MoveRecord> getMoveHistory()     { return moveHistory; }
    public List<Piece> getCapturedByWhite()      { return capturedByWhite; }
    public List<Piece> getCapturedByBlack()      { return capturedByBlack; }

    public MoveRecord executeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (gameOver) return null;

        Piece moving = board.getPiece(fromRow, fromCol);
        if (moving == null) return null;
        if (moving.getColor() != currentTurn) return null;

        Piece target = board.getPiece(toRow, toCol);
        if (target != null && target.getColor() == currentTurn) return null;

        Board snapshot = new Board(board);
        Piece captured = board.movePiece(fromRow, fromCol, toRow, toCol);

        MoveRecord record = new MoveRecord(fromRow, fromCol, toRow, toCol,
                                           moving, captured, snapshot);
        moveHistory.add(record);
        undoStack.push(record);

        if (captured != null) {
            if (currentTurn == PieceColor.WHITE)
                capturedByWhite.add(captured);
            else
                capturedByBlack.add(captured);

            if (captured.getType() == PieceType.KING) {
                gameOver = true;
                winner   = currentTurn;
            }
        }

        if (!gameOver)
            currentTurn = currentTurn.opposite();

        return record;
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
        gameOver    = false;
        winner      = null;

        return last;
    }

    public void reset() {
        board = new Board();
        currentTurn = PieceColor.WHITE;
        gameOver    = false;
        winner      = null;
        moveHistory.clear();
        undoStack.clear();
        capturedByWhite.clear();
        capturedByBlack.clear();
    }
}
