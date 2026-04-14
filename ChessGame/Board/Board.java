package Board;

import Pieces.*;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private Piece[][] grid;
    private List<Piece> capturedPieces;

    public Board() {
        grid = new Piece[8][8];
        capturedPieces = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        grid[7][0] = new Rook("black", new Position(7, 0));
        grid[7][1] = new Knight("black", new Position(7, 1));
        grid[7][2] = new Bishop("black", new Position(7, 2));
        grid[7][3] = new Queen("black", new Position(7, 3));
        grid[7][4] = new King("black", new Position(7, 4));
        grid[7][5] = new Bishop("black", new Position(7, 5));
        grid[7][6] = new Knight("black", new Position(7, 6));
        grid[7][7] = new Rook("black", new Position(7, 7));
        for (int col = 0; col < 8; col++) {
            grid[6][col] = new Pawn("black", new Position(6, col));
        }

        grid[0][0] = new Rook("white", new Position(0, 0));
        grid[0][1] = new Knight("white", new Position(0, 1));
        grid[0][2] = new Bishop("white", new Position(0, 2));
        grid[0][3] = new Queen("white", new Position(0, 3));
        grid[0][4] = new King("white", new Position(0, 4));
        grid[0][5] = new Bishop("white", new Position(0, 5));
        grid[0][6] = new Knight("white", new Position(0, 6));
        grid[0][7] = new Rook("white", new Position(0, 7));
        for (int col = 0; col < 8; col++) {
            grid[1][col] = new Pawn("white", new Position(1, col));
        }
    }

    public Piece getPiece(Position position) {
        return grid[position.getRow()][position.getCol()];
    }

    public boolean movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        if (piece == null) {
            return false;
        }

        List<Position> moves = piece.possibleMoves(this);
        boolean validMove = false;
        for (Position move : moves) {
            if (move.getRow() == to.getRow() && move.getCol() == to.getCol()) {
                validMove = true;
                break;
            }
        }
        if (!validMove) {
            return false;
        }

        Piece target = getPiece(to);
        if (target != null) {
            capturedPieces.add(target);
        }

        grid[to.getRow()][to.getCol()] = piece;
        grid[from.getRow()][from.getCol()] = null;
        piece.setPosition(to);

        return true;
    }

    public boolean isCheck(String color) {
        Position kingPos = findKing(color);
        if (kingPos == null) return false;

        String opponentColor = color.equals("white") ? "black" : "white";
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = grid[row][col];
                if (piece != null && piece.getColor().equals(opponentColor)) {
                    List<Position> moves = piece.possibleMoves(this);
                    for (Position move : moves) {
                        if (move.getRow() == kingPos.getRow() && move.getCol() == kingPos.getCol()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(String color) {
        if (!isCheck(color)) return false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = grid[row][col];
                if (piece != null && piece.getColor().equals(color)) {
                    List<Position> moves = piece.possibleMoves(this);
                    for (Position move : moves) {
                        Board copy = copyBoard();
                        copy.movePieceRaw(new Position(row, col), move);
                        if (!copy.isCheck(color)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void movePieceRaw(Position from, Position to) {
        Piece piece = grid[from.getRow()][from.getCol()];
        if (piece == null) return;
        Piece target = grid[to.getRow()][to.getCol()];
        if (target != null) capturedPieces.add(target);
        grid[to.getRow()][to.getCol()] = piece;
        grid[from.getRow()][from.getCol()] = null;
        piece.setPosition(to);
    }

    private Board copyBoard() {
        Board copy = new Board();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                copy.grid[row][col] = null;
            }
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = grid[row][col];
                if (p != null) {
                    copy.grid[row][col] = p.copy();
                }
            }
        }
        return copy;
    }

    private Position findKing(String color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = grid[row][col];
                if (p instanceof King && p.getColor().equals(color)) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public void display() {
        System.out.println("  A  B  C  D  E  F  G  H");
        for (int row = 7; row >= 0; row--) {
            System.out.print((row + 1) + " ");
            for (int col = 0; col < 8; col++) {
                Piece piece = grid[row][col];
                if (piece == null) {
                    System.out.print("## ");
                } else {
                    System.out.print(piece.getSymbol() + " ");
                }
            }
            System.out.println();
        }
    }

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }

    public Piece[][] getGrid() {
        return grid;
    }
}
