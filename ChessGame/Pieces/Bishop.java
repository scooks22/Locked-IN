package Pieces;

import utils.Position;
import Board.Board;
import java.util.List;
import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        String color = getColor();
        
        // Move diagonally in all 4 directions
        // Up-right
        for (int i = 1; i < 8; i++) {
            int newRow = row + i;
            int newCol = col + i;
            if (!board.isInBounds(newRow, newCol)) break;
            Piece target = board.getPiece(new Position(newRow, newCol));
            if (target == null) {
                moves.add(new Position(newRow, newCol));
            } else {
                if (!target.getColor().equals(color)) {
                    moves.add(new Position(newRow, newCol));
                }
                break;
            }
        }
        
        // Up-left
        for (int i = 1; i < 8; i++) {
            int newRow = row + i;
            int newCol = col - i;
            if (!board.isInBounds(newRow, newCol)) break;
            Piece target = board.getPiece(new Position(newRow, newCol));
            if (target == null) {
                moves.add(new Position(newRow, newCol));
            } else {
                if (!target.getColor().equals(color)) {
                    moves.add(new Position(newRow, newCol));
                }
                break;
            }
        }
        
        // Down-right
        for (int i = 1; i < 8; i++) {
            int newRow = row - i;
            int newCol = col + i;
            if (!board.isInBounds(newRow, newCol)) break;
            Piece target = board.getPiece(new Position(newRow, newCol));
            if (target == null) {
                moves.add(new Position(newRow, newCol));
            } else {
                if (!target.getColor().equals(color)) {
                    moves.add(new Position(newRow, newCol));
                }
                break;
            }
        }
        
        // Down-left
        for (int i = 1; i < 8; i++) {
            int newRow = row - i;
            int newCol = col - i;
            if (!board.isInBounds(newRow, newCol)) break;
            Piece target = board.getPiece(new Position(newRow, newCol));
            if (target == null) {
                moves.add(new Position(newRow, newCol));
            } else {
                if (!target.getColor().equals(color)) {
                    moves.add(new Position(newRow, newCol));
                }
                break;
            }
        }
        
        return moves;
    }

    @Override
    public String getSymbol() {
        if (getColor().equals("white")) {
            return "wB";
        } else {
            return "bB";
        }
    }
    
    @Override
    public Piece copy() {
        return new Bishop(getColor(), new Position(getPosition().getRow(), getPosition().getCol()));
    }
}
