package chess.gui;

import chess.model.*;
import chess.util.SaveLoadUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

public class MainWindow extends JFrame {

    private GameState    gameState;
    private BoardPanel   boardPanel;
    private HistoryPanel historyPanel;
    private StatusBar    statusBar;

    public MainWindow() {
        super("Chess");
        gameState = new GameState();
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        setJMenuBar(buildMenuBar());

        boardPanel   = new BoardPanel(gameState);
        historyPanel = new HistoryPanel();
        statusBar    = new StatusBar();

        historyPanel.getUndoButton().addActionListener(e -> onUndo());
        boardPanel.addPropertyChangeListener("moveMade", evt -> onMoveMade((MoveRecord) evt.getNewValue()));

        JPanel center = new JPanel(new BorderLayout(6, 0));
        center.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        center.add(boardPanel,   BorderLayout.CENTER);
        center.add(historyPanel, BorderLayout.EAST);

        add(center,    BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        refreshAll();
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');

        JMenuItem newItem  = new JMenuItem("New Game");
        JMenuItem saveItem = new JMenuItem("Save Game");
        JMenuItem loadItem = new JMenuItem("Load Game");
        JMenuItem exitItem = new JMenuItem("Exit");

        newItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        loadItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));

        newItem.addActionListener(e  -> onNewGame());
        saveItem.addActionListener(e -> onSaveGame());
        loadItem.addActionListener(e -> onLoadGame());
        exitItem.addActionListener(e -> System.exit(0));

        gameMenu.add(newItem);
        gameMenu.addSeparator();
        gameMenu.add(saveItem);
        gameMenu.add(loadItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');

        JMenuItem smallBoard  = new JMenuItem("Small Board (60px)");
        JMenuItem mediumBoard = new JMenuItem("Medium Board (80px)");
        JMenuItem largeBoard  = new JMenuItem("Large Board (100px)");

        smallBoard.addActionListener(e  -> resizeBoard(60));
        mediumBoard.addActionListener(e -> resizeBoard(80));
        largeBoard.addActionListener(e  -> resizeBoard(100));

        viewMenu.add(smallBoard);
        viewMenu.add(mediumBoard);
        viewMenu.add(largeBoard);

        menuBar.add(gameMenu);
        menuBar.add(viewMenu);
        return menuBar;
    }

    private void onMoveMade(MoveRecord record) {
        refreshAll();
        if (gameState.isGameOver()) {
            String winner = gameState.getWinner() == PieceColor.WHITE ? "White" : "Black";
            JOptionPane.showMessageDialog(
                this,
                winner + " captures the King — " + winner + " wins!",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void onUndo() {
        gameState.undoLastMove();
        boardPanel.setLastMove(-1, -1, -1, -1);
        if (!gameState.getMoveHistory().isEmpty()) {
            MoveRecord last = gameState.getMoveHistory().get(gameState.getMoveHistory().size() - 1);
            boardPanel.setLastMove(last.getFromRow(), last.getFromCol(),
                                   last.getToRow(),   last.getToCol());
        }
        boardPanel.refresh();
        refreshAll();
    }

    private void onNewGame() {
        int choice = JOptionPane.showConfirmDialog(
            this, "Start a new game? Current progress will be lost.",
            "New Game", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            gameState.reset();
            boardPanel.setLastMove(-1, -1, -1, -1);
            boardPanel.refresh();
            refreshAll();
        }
    }

    private void onSaveGame() {
        JFileChooser chooser = createChessFileChooser();
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        if (!file.getName().endsWith(".chess")) file = new File(file.getPath() + ".chess");

        try {
            SaveLoadUtil.save(gameState, Path.of(file.getPath()));
            JOptionPane.showMessageDialog(this, "Game saved to:\n" + file.getName(),
                                          "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to save: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLoadGame() {
        JFileChooser chooser = createChessFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        try {
            gameState = SaveLoadUtil.load(Path.of(file.getPath()));
            boardPanel = new BoardPanel(gameState);
            boardPanel.addPropertyChangeListener("moveMade",
                evt -> onMoveMade((MoveRecord) evt.getNewValue()));

            Container content = getContentPane();
            content.removeAll();
            JPanel center = new JPanel(new BorderLayout(6, 0));
            center.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            center.add(boardPanel,   BorderLayout.CENTER);
            center.add(historyPanel, BorderLayout.EAST);
            content.add(center,    BorderLayout.CENTER);
            content.add(statusBar, BorderLayout.SOUTH);

            refreshAll();
            pack();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resizeBoard(int squareSize) {
        boardPanel.setSquareSize(squareSize);
        pack();
    }

    private void refreshAll() {
        historyPanel.refresh(gameState);
        statusBar.refresh(gameState);
        boardPanel.repaint();
    }

    private JFileChooser createChessFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Chess saves (*.chess)", "chess"));
        return chooser;
    }
}
