package chess.model;

public enum PieceType {
    KING   ('\u2654', '\u265A'),
    QUEEN  ('\u2655', '\u265B'),
    ROOK   ('\u2656', '\u265C'),
    BISHOP ('\u2657', '\u265D'),
    KNIGHT ('\u2658', '\u265E'),
    PAWN   ('\u2659', '\u265F');

    private final char whiteSymbol;
    private final char blackSymbol;

    PieceType(char whiteSymbol, char blackSymbol) {
        this.whiteSymbol = whiteSymbol;
        this.blackSymbol = blackSymbol;
    }

    public char getSymbol(PieceColor color) {
        return color == PieceColor.WHITE ? whiteSymbol : blackSymbol;
    }

    public String getAbbreviation() {
        return switch (this) {
            case KING   -> "K";
            case QUEEN  -> "Q";
            case ROOK   -> "R";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case PAWN   -> "";
        };
    }
}
