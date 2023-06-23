package SuperMario.repository;

public class SavePaths {
    public static final String[] filePaths = {
            "src/SuperMario/data/data-game-1.txt",
            "src/SuperMario/data/data-game-2.txt",
            "src/SuperMario/data/data-game-3.txt",
            "src/SuperMario/data/config-ap.json"
    };

    public static boolean isFileIdValid(int fileId) {
        return fileId < 0 || filePaths.length >= fileId;
    }

}
