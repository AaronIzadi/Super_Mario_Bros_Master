package SuperMario.repository;

public class SavePaths {
    public static final String[] filePaths = {
            "src/data/data-game-1.txt",
            "src/data/data-game-2.txt",
            "src/data/data-game-3.txt",
    };

    public static boolean isFileIdValid(int fileId) {
        return fileId < 0 || filePaths.length >= fileId;
    }

}
