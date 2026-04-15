# Chess Game - Phase 2

## Repository Structure

Phase 2 code is located in the `ChessGame/Phase2` folder.

```
ChessGame/Phase2/
└── src/
    └── chess/
        ├── Main.java
        ├── model/
        │   ├── Board.java
        │   ├── GameState.java
        │   ├── MoveRecord.java
        │   ├── Piece.java
        │   ├── PieceColor.java
        │   └── PieceType.java
        ├── gui/
        │   ├── BoardPanel.java
        │   ├── HistoryPanel.java
        │   ├── MainWindow.java
        │   └── StatusBar.java
        └── util/
            └── SaveLoadUtil.java
```

---

## How to Compile and Run

Requirements: Java JDK 17 or higher

Navigate to the Phase2 folder in your terminal:

```
cd ChessGame/Phase2
mkdir out
javac -d out -sourcepath src src/chess/Main.java
java -cp out chess.Main
```

---

## Features Implemented

**Core Requirements:**
- 8x8 chessboard using Java Swing
- All pieces displayed at starting positions using Unicode symbols
- Click-to-move and drag-and-drop piece movement
- Piece capture — opponent pieces are removed from the board
- King capture triggers a Game Over popup declaring the winner

**Extra GUI Features (3 implemented):**

1. **Menu Bar with Game Controls** — New Game, Save Game, and Load Game options. Save and Load use Java serialization so the exact game state is preserved.

2. **Game History Panel with Undo** — A sidebar panel displays every move made in algebraic notation, shows captured pieces for each player, and includes an Undo button that reverts the board to the previous state including restoring any captured pieces.

3. **Board Size Customization** — A View menu allows the player to switch between Small (60px), Medium (80px), and Large (100px) square sizes, resizing the board in real time.
