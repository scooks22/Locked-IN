package chess.model.pieces;

import chess.model.*;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    private static final long serialVersionUID = 1L;

    public Bishop(PieceColor color) {
        super(PieceType.BISHOP, color);
    }

    @Override
    public List<int[]> possibleMoves(int row, int col, Board board) {
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{-1,-1},{-1,1},{1,-1},{1,1}};
        for (int[] d : directions) {
            int r = row + d[0], c = col + d[1];
            while (Board.inBounds(r, c)) {
                Piece target = board.getPiece(r, c);
                if (target == null) {
                    moves.add(new int[]{r, c});
                } else {
                    if (target.getColor() != getColor()) moves.add(new int[]{r, c});
                    break;
                }
                r += d[0]; c += d[1];
            }
        }
        return moves;
    }
}
