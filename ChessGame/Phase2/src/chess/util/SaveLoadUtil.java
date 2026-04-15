package chess.util;

import chess.model.GameState;

import java.io.*;
import java.nio.file.Path;

public class SaveLoadUtil {

    private SaveLoadUtil() {}

    public static void save(GameState state, Path filePath) throws IOException {
        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(state);
        }
    }

    public static GameState load(Path filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois =
                 new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (GameState) ois.readObject();
        }
    }
}
