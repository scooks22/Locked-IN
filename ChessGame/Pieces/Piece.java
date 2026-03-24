package Pieces;

import Utils.Position;
import java.util.List;

public abstract class Piece {
    
    private String color;
    private Position position;
    
    public Piece(String color, Position position) {
        this.color = color;
        this.position = position;
    }
    
    public String getColor() {
        return color;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    public abstract List<Position> possibleMoves();
    
    public abstract String getSymbol();
}