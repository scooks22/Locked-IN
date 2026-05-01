# Chess Game - Phase 3

## Overview

A fully functional GUI-based chess game built with Java Swing. Phase 3 integrates complete backend chess logic with the graphical interface from Phase 2, resulting in a fully playable chess game that enforces all standard chess rules.

## Repository Structure

Phase 3 code is located in the `ChessGame/Phase3` folder.

```
ChessGame/Phase3/
└── src/
    └── chess/
        ├── Main.java
        ├── model/
        │   ├── Piece.java          (abstract base class)
        │   ├── Board.java
        │   ├── GameState.java
        │   ├── MoveValidator.java
        │   ├── MoveRecord.java
        │   ├── PieceColor.java
        │   ├── PieceType.java
        │   └── pieces/
        │       ├── Pawn.java
        │       ├── Rook.java
        │       ├── Knight.java
        │       ├── Bishop.java
        │       ├── Queen.java
        │       └── King.java
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

```
cd ChessGame/Phase3
mkdir out
javac -d out -sourcepath src src\chess\Main.java
java -cp out chess.Main
```

---

## Features Implemented

### Core Requirements
- 8x8 chessboard rendered with Java Swing
- All pieces displayed at starting positions using Unicode symbols
- Click-to-move and drag-and-drop piece movement
- Full legal move validation for all 6 piece types
- Green dot indicators show legal moves when a piece is selected
- Piece capture with correct rules enforcement
- Check detection with red king highlight and popup warning
- Checkmate detection with winner declaration popup
- Stalemate detection
- Castling (kingside and queenside) for both players
- Pawn promotion dialog when a pawn reaches the back rank

### Extra GUI Features
1. Menu bar with New Game, Save Game, and Load Game options using Java serialization
2. Game history panel showing all moves in algebraic notation with captured pieces display
3. Undo button that fully reverts the board including restoring captured pieces
4. Board size customization via the View menu (Small / Medium / Large)

### AI Usage
This project was developed with AI assistance for code generation, debugging, and architecture decisions. All code was reviewed, tested, and verified to compile and run correctly before submission.

---

## Phase Reference

- Phase 1 code: `ChessGame/Board`, `ChessGame/Game`, `ChessGame/Pieces`, `ChessGame/Player`, `ChessGame/utils`
- Phase 2 code: `ChessGame/Phase2`
- Phase 3 code: `ChessGame/Phase3`
