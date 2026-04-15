package chess.gui;

import chess.model.GameState;
import chess.model.PieceColor;

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
        if (state.isGameOver()) {
            String winner = state.getWinner() == PieceColor.WHITE ? "White" : "Black";
            label.setText("  " + winner + " wins!");
            label.setForeground(new Color(180, 50, 50));
        } else {
            String turn = state.getCurrentTurn() == PieceColor.WHITE ? "White" : "Black";
            label.setText("  " + turn + "'s turn");
            label.setForeground(UIManager.getColor("Label.foreground"));
        }
    }
}
