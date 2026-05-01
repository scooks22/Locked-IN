package chess.model;

import java.io.Serializable;
import java.util.List;

public abstract class Piece implements Serializable {
    private static final long serialVersionUID = 1L;

    private final PieceType type;
    private final PieceColor color;

    public Piece(PieceType type, PieceColor color) {
        this.type  = type;
        this.color = color;
    }

    public PieceType getType()   { return type; }
    public PieceColor getColor() { return color; }
    public char getSymbol()      { return type.getSymbol(color); }

    public abstract List<int[]> possibleMoves(int row, int col, Board board);

    @Override
    public String toString() {
        return color + " " + type;
    }
}
