package chess.model.pieces;

import chess.model.*;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private static final long serialVersionUID = 1L;
    private boolean hasMoved = false;

    public King(PieceColor color) {
        super(PieceType.KING, color);
    }

    public boolean hasMoved()          { return hasMoved; }
    public void setHasMoved(boolean b) { hasMoved = b; }

    @Override
    public List<int[]> possibleMoves(int row, int col, Board board) {
        List<int[]> moves = new ArrayList<>();
        int[][] steps = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        for (int[] s : steps) {
            int r = row + s[0], c = col + s[1];
            if (Board.inBounds(r, c)) {
                Piece target = board.getPiece(r, c);
                if (target == null || target.getColor() != getColor())
                    moves.add(new int[]{r, c});
            }
        }

        if (!hasMoved && !board.isInCheck(getColor())) {
            int backRank = getColor() == PieceColor.WHITE ? 7 : 0;

            Piece kRook = board.getPiece(backRank, 7);
            if (kRook instanceof Rook && !((Rook) kRook).hasMoved()
                    && board.getPiece(backRank, 5) == null
                    && board.getPiece(backRank, 6) == null
                    && !board.isUnderAttack(backRank, 5, getColor().opposite())
                    && !board.isUnderAttack(backRank, 6, getColor().opposite())) {
                moves.add(new int[]{backRank, 6});
            }

            Piece qRook = board.getPiece(backRank, 0);
            if (qRook instanceof Rook && !((Rook) qRook).hasMoved()
                    && board.getPiece(backRank, 1) == null
                    && board.getPiece(backRank, 2) == null
                    && board.getPiece(backRank, 3) == null
                    && !board.isUnderAttack(backRank, 3, getColor().opposite())
                    && !board.isUnderAttack(backRank, 2, getColor().opposite())) {
                moves.add(new int[]{backRank, 2});
            }
        }

        return moves;
    }
}
