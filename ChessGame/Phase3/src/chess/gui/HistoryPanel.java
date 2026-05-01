package chess.gui;

import chess.model.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {

    private final DefaultListModel<String> historyModel;
    private final JList<String>            historyList;
    private final JLabel                   capturedByWhiteLabel;
    private final JLabel                   capturedByBlackLabel;
    private final JLabel                   statusLabel;
    private final JButton                  undoButton;

    public HistoryPanel() {
        setLayout(new BorderLayout(0, 6));
        setPreferredSize(new Dimension(210, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        historyModel = new DefaultListModel<>();
        historyList  = new JList<>(historyModel);
        historyList.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyList.setFixedCellHeight(22);

        JScrollPane scroll = new JScrollPane(historyList);
        scroll.setBorder(new TitledBorder("Move History"));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel capturePanel = new JPanel(new GridLayout(2, 1, 4, 4));
        capturePanel.setBorder(new TitledBorder("Captured"));
        capturedByWhiteLabel = new JLabel("White took: ");
        capturedByWhiteLabel.setFont(new Font("Serif", Font.PLAIN, 13));
        capturedByBlackLabel = new JLabel("Black took: ");
        capturedByBlackLabel.setFont(new Font("Serif", Font.PLAIN, 13));
        capturePanel.add(capturedByWhiteLabel);
        capturePanel.add(capturedByBlackLabel);

        statusLabel = new JLabel("  White's turn");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        statusLabel.setBorder(BorderFactory.createEtchedBorder());

        undoButton = new JButton("\u21A9 Undo");
        undoButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        undoButton.setFocusPainted(false);

        JPanel top = new JPanel(new BorderLayout(0, 4));
        top.add(undoButton,   BorderLayout.NORTH);
        top.add(statusLabel,  BorderLayout.SOUTH);

        add(top,          BorderLayout.NORTH);
        add(scroll,       BorderLayout.CENTER);
        add(capturePanel, BorderLayout.SOUTH);
    }

    public void refresh(GameState state) {
        historyModel.clear();
        List<MoveRecord> history = state.getMoveHistory();
        for (int i = 0; i < history.size(); i++) {
            int moveNum = i / 2 + 1;
            String prefix = (i % 2 == 0) ? moveNum + ". " : "   ";
            historyModel.addElement(prefix + history.get(i).toString());
        }
        if (!historyModel.isEmpty())
            historyList.ensureIndexIsVisible(historyModel.size() - 1);

        capturedByWhiteLabel.setText("White took: " + buildCaptureString(state.getCapturedByWhite()));
        capturedByBlackLabel.setText("Black took: " + buildCaptureString(state.getCapturedByBlack()));

        undoButton.setEnabled(!state.getMoveHistory().isEmpty() && !state.isGameOver());

        switch (state.getStatus()) {
            case CHECK     -> { statusLabel.setText("  \u26A0 CHECK!"); statusLabel.setForeground(new Color(180, 80, 0)); }
            case CHECKMATE -> { statusLabel.setText("  \u265A CHECKMATE!"); statusLabel.setForeground(new Color(180, 30, 30)); }
            case STALEMATE -> { statusLabel.setText("  Stalemate"); statusLabel.setForeground(Color.GRAY); }
            default -> {
                String turn = state.getCurrentTurn() == PieceColor.WHITE ? "White" : "Black";
                statusLabel.setText("  " + turn + "'s turn");
                statusLabel.setForeground(UIManager.getColor("Label.foreground"));
            }
        }
    }

    public JButton getUndoButton() { return undoButton; }

    private String buildCaptureString(List<Piece> pieces) {
        if (pieces.isEmpty()) return "\u2014";
        StringBuilder sb = new StringBuilder();
        for (Piece p : pieces) sb.append(p.getSymbol());
        return sb.toString();
    }
}
