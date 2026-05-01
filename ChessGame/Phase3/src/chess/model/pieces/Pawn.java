package chess.model.pieces;

import chess.model.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private static final long serialVersionUID = 1L;
    private boolean hasMoved = false;

    public Pawn(PieceColor color) {
        super(PieceType.PAWN, color);
    }

    public boolean hasMoved()          { return hasMoved; }
    public void setHasMoved(boolean b) { hasMoved = b; }

    @Override
    public List<int[]> possibleMoves(int row, int col, Board board) {
        List<int[]> moves = new ArrayList<>();
        int dir = getColor() == PieceColor.WHITE ? -1 : 1;

        int oneStep = row + dir;
        if (Board.inBounds(oneStep, col) && board.getPiece(oneStep, col) == null) {
            moves.add(new int[]{oneStep, col});
            int twoStep = row + 2 * dir;
            if (!hasMoved && Board.inBounds(twoStep, col) && board.getPiece(twoStep, col) == null)
                moves.add(new int[]{twoStep, col});
        }

        for (int dc : new int[]{-1, 1}) {
            int nc = col + dc;
            if (Board.inBounds(oneStep, nc)) {
                Piece target = board.getPiece(oneStep, nc);
                if (target != null && target.getColor() != getColor())
                    moves.add(new int[]{oneStep, nc});
            }
        }

        return moves;
    }
}
