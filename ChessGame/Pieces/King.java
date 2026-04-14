package Pieces;

import utils.Position;
import Board.Board;
import java.util.List;
import java.util.ArrayList;

public class King extends Piece {

    public King(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        String color = getColor();
        
        // King moves one square in any direction
        int[][] kingMoves = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };
        
        for (int[] move : kingMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            
            if (board.isInBounds(newRow, newCol)) {
                Piece target = board.getPiece(new Position(newRow, newCol));
                if (target == null || !target.getColor().equals(color)) {
                    moves.add(new Position(newRow, newCol));
                }
            }
        }
        
        return moves;
    }

    @Override
    public String getSymbol() {
        if (getColor().equals("white")) {
            return "wK";
        } else {
            return "bK";
        }
    }
    
    @Override
    public Piece copy() {
        return new King(getColor(), new Position(getPosition().getRow(), getPosition().getCol()));
    }
}
