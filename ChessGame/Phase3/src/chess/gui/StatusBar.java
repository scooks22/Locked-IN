package chess.gui;

import chess.model.*;
import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {

    private final JLabel label;

    public StatusBar() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLoweredBevelBorder());
        label = new JLabel("  White's turn");
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(label, BorderLayout.WEST);
    }

    public void refresh(GameState state) {
        switch (state.getStatus()) {
            case CHECK -> {
                String turn = state.getCurrentTurn() == PieceColor.WHITE ? "White" : "Black";
                label.setText("  \u26A0  " + turn + " is in CHECK!");
                label.setForeground(new Color(180, 80, 0));
            }
            case CHECKMATE -> {
                String winner = state.getWinner() == PieceColor.WHITE ? "White" : "Black";
                label.setText("  \u265A  Checkmate — " + winner + " wins!");
                label.setForeground(new Color(180, 30, 30));
            }
            case STALEMATE -> {
                label.setText("  Stalemate — it's a draw!");
                label.setForeground(Color.GRAY);
            }
            default -> {
                String turn = state.getCurrentTurn() == PieceColor.WHITE ? "White" : "Black";
                label.setText("  " + turn + "'s turn");
                label.setForeground(UIManager.getColor("Label.foreground"));
            }
        }
    }
}
