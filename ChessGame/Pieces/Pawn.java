package Pieces;

import utils.Position;
import Board.Board;
import java.util.List;
import java.util.ArrayList;

public class Pawn extends Piece {

    public Pawn(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        String color = getColor();
        
        int direction = color.equals("white") ? 1 : -1;
        int startRow = color.equals("white") ? 1 : 6;
        
        // Move forward one square
        if (board.isInBounds(row + direction, col)) {
            if (board.getPiece(new Position(row + direction, col)) == null) {
                moves.add(new Position(row + direction, col));
                
                // Move forward two squares from starting position
                if (row == startRow && board.getPiece(new Position(row + 2 * direction, col)) == null) {
                    moves.add(new Position(row + 2 * direction, col));
                }
            }
        }
        
        // Capture diagonally
        if (board.isInBounds(row + direction, col - 1)) {
            Piece target = board.getPiece(new Position(row + direction, col - 1));
            if (target != null && !target.getColor().equals(color)) {
                moves.add(new Position(row + direction, col - 1));
            }
        }
        
        if (board.isInBounds(row + direction, col + 1)) {
            Piece target = board.getPiece(new Position(row + direction, col + 1));
            if (target != null && !target.getColor().equals(color)) {
                moves.add(new Position(row + direction, col + 1));
            }
        }
        
        return moves;
    }

    @Override
    public String getSymbol() {
        if (getColor().equals("white")) {
            return "wp";
        } else {
            return "bp";
        }
    }
    
    @Override
    public Piece copy() {
        return new Pawn(getColor(), new Position(getPosition().getRow(), getPosition().getCol()));
    }
}
