package Pieces;

import utils.Position;
import Board.Board;
import java.util.List;
import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        String color = getColor();
        
        // Queen moves like a rook + bishop combined
        
        // Rook-like moves (horizontal and vertical)
        // Up
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
        
        // Down
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
        
        // Right
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
        
        // Left
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
        
        // Bishop-like moves (diagonal)
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
            return "wQ";
        } else {
            return "bQ";
        }
    }
    
    @Override
    public Piece copy() {
        return new Queen(getColor(), new Position(getPosition().getRow(), getPosition().getCol()));
    }
}
