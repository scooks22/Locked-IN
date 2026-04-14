package Pieces;

import utils.Position;
import Board.Board;
import java.util.List;
import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        String color = getColor();
        
        // Knight moves in L-shape: 2 squares in one direction, 1 in perpendicular
        int[][] knightMoves = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        
        for (int[] move : knightMoves) {
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
            return "wN";
        } else {
            return "bN";
        }
    }
    
    @Override
    public Piece copy() {
        return new Knight(getColor(), new Position(getPosition().getRow(), getPosition().getCol()));
    }
}
