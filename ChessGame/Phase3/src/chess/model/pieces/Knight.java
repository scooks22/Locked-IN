package chess.model.pieces;

import chess.model.*;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    private static final long serialVersionUID = 1L;

    public Knight(PieceColor color) {
        super(PieceType.KNIGHT, color);
    }

    @Override
    public List<int[]> possibleMoves(int row, int col, Board board) {
        List<int[]> moves = new ArrayList<>();
        int[][] jumps = {{-2,-1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}};
        for (int[] j : jumps) {
            int r = row + j[0], c = col + j[1];
            if (Board.inBounds(r, c)) {
                Piece target = board.getPiece(r, c);
                if (target == null || target.getColor() != getColor())
                    moves.add(new int[]{r, c});
            }
        }
        return moves;
    }
}
