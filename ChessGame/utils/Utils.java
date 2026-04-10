package utils;

public class Utils {
    
    public static boolean isValidPosition(int row, int column) {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }
    
    public static Position parsePosition(String input) {
        char file = input.toUpperCase().charAt(0);
        int rank = Character.getNumericValue(input.charAt(1));
        int column = file - 'A';
        int row = rank - 1;
        return new Position(row, column);
    }
}