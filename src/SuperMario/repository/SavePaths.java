package SuperMario.repository;

public class SavePaths {
    public static final String[] filePaths = {
            "data/data-game-1.txt",
            "data/data-game-2.txt",
            "data/data-game-3.txt"
    };

    public static boolean isFileIdValid(int fileId) {
        return filePaths.length <= fileId || fileId < 0;
    }

}
