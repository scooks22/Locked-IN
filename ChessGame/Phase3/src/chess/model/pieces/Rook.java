package chess.model.pieces;

import chess.model.*;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    private static final long serialVersionUID = 1L;
    private boolean hasMoved = false;

    public Rook(PieceColor color) {
        super(PieceType.ROOK, color);
    }

    public boolean hasMoved()          { return hasMoved; }
    public void setHasMoved(boolean b) { hasMoved = b; }

    @Override
    public List<int[]> possibleMoves(int row, int col, Board board) {
        List<int[]> moves = new ArrayList<>();
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
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
