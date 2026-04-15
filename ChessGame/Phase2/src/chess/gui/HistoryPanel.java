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
    private final JButton                  undoButton;

    public HistoryPanel() {
        setLayout(new BorderLayout(0, 6));
        setPreferredSize(new Dimension(200, 0));
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
        capturedByWhiteLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        capturedByBlackLabel = new JLabel("Black took: ");
        capturedByBlackLabel.setFont(new Font("Serif", Font.PLAIN, 14));

        capturePanel.add(capturedByWhiteLabel);
        capturePanel.add(capturedByBlackLabel);

        undoButton = new JButton("\u21A9 Undo");
        undoButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        undoButton.setFocusPainted(false);

        add(scroll,       BorderLayout.CENTER);
        add(capturePanel, BorderLayout.SOUTH);
        add(undoButton,   BorderLayout.NORTH);
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

        undoButton.setEnabled(!state.getMoveHistory().isEmpty());
    }

    public JButton getUndoButton() { return undoButton; }

    private String buildCaptureString(List<Piece> pieces) {
        if (pieces.isEmpty()) return "\u2014";
        StringBuilder sb = new StringBuilder();
        for (Piece p : pieces) sb.append(p.getSymbol());
        return sb.toString();
    }
}
