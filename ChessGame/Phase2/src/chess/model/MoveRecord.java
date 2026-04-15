package chess.model;

import java.io.Serializable;

public class MoveRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int fromRow, fromCol;
    private final int toRow,   toCol;
    private final Piece movedPiece;
    private final Piece capturedPiece;
    private final Board boardBefore;

    public MoveRecord(int fromRow, int fromCol, int toRow, int toCol,
                      Piece movedPiece, Piece capturedPiece, Board boardBefore) {
        this.fromRow       = fromRow;
        this.fromCol       = fromCol;
        this.toRow         = toRow;
        this.toCol         = toCol;
        this.movedPiece    = movedPiece;
        this.capturedPiece = capturedPiece;
        this.boardBefore   = boardBefore;
    }

    public int getFromRow()         { return fromRow; }
    public int getFromCol()         { return fromCol; }
    public int getToRow()           { return toRow; }
    public int getToCol()           { return toCol; }
    public Piece getMovedPiece()    { return movedPiece; }
    public Piece getCapturedPiece() { return capturedPiece; }
    public Board getBoardBefore()   { return boardBefore; }

    @Override
    public String toString() {
        String cols = "abcdefgh";
        String from = cols.charAt(fromCol) + String.valueOf(8 - fromRow);
        String to   = cols.charAt(toCol)   + String.valueOf(8 - toRow);
        String abbr = movedPiece.getType().getAbbreviation();
        String move = abbr + from + "\u2192" + to;
        if (capturedPiece != null)
            move += " x" + capturedPiece.getSymbol();
        return move;
    }
}
