package utils;

public class Position {
    
    private int row;
    private int column;
    
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return column;
    }
    
    public int getCol() {
        return column;
    }
    
    public void setRow(int row) {
        this.row = row;
    }
    
    public void setColumn(int column) {
        this.column = column;
    }
    
    public String toString() {
        char file = (char) ('A' + column);
        int rank = row + 1;
        return "" + file + rank;
    }
}
