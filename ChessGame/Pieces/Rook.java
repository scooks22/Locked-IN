package Pieces;

import utils.Position;
import Board.Board;
import java.util.List;
import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        String color = getColor();
        
        // Move up
        for (int r = row + 1; r < 8; r++) {
            if (!board.isInBounds(r, col)) break;
            Piece target = board.getPiece(new Position(r, col));
            if (target == null) {
                moves.add(new Position(r, col));
            } else {
                if (!target.getColor().equals(color)) {
                    moves.add(new Position(r, col));
                }
                break;
            }
        }
        
        // Move down
        for (int r = row - 1; r >= 0; r--) {
            if (!board.isInBounds(r, col)) break;
            Piece target = board.getPiece(new Position(r, col));
            if (target == null) {
                moves.add(new Position(r, col));
            } else {
                if (!target.getColor().equals(color)) {
                    moves.add(new Position(r, col));
                }
                break;
            }
        }
        
        // Move right
        for (int c = col + 1; c < 8; c++) {
            if (!board.isInBounds(row, c)) break;
            Piece target = board.getPiece(new Position(row, c));
            if (target == null) {
                moves.add(new Position(row, c));
            } else {
                if (!target.getColor().equals(color)) {
                    moves.add(new Position(row, c));
                }
                break;
            }
        }
        
        // Move left
        for (int c = col - 1; c >= 0; c--) {
            if (!board.isInBounds(row, c)) break;
            Piece target = board.getPiece(new Position(row, c));
            if (target == null) {
                moves.add(new Position(row, c));
            } else {
                if (!target.getColor().equals(color)) {
                    moves.add(new Position(row, c));
                }
                break;
            }
        }
        
        return moves;
    }

    @Override
    public String getSymbol() {
        if (getColor().equals("white")) {
            return "wR";
        } else {
            return "bR";
        }
    }
    
    @Override
    public Piece copy() {
        return new Rook(getColor(), new Position(getPosition().getRow(), getPosition().getCol()));
    }
}
