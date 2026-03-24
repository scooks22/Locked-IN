package ChessGame.pieces;

import ChessGame.utils.Position;
import java.util.List;
import java.util.ArrayList;

public class Knight extends Piece {

    public Knight(String color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        return new ArrayList<>();
    }

    @Override
    public String getSymbol() {
        if (getColor().equals("white")) {
            return "wN";
        } else {
            return "bN";
        }
    }
}
