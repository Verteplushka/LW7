package mainProgramms;

import collectionClasses.SpaceMarine;

import java.util.LinkedList;

public class CollectionManager {
    private static LinkedList<SpaceMarine> spaceMarines;

    public static LinkedList<SpaceMarine> getSpaceMarines() {
        return spaceMarines;
    }

    public static void setSpaceMarines(LinkedList<SpaceMarine> spaceMarines) {
        CollectionManager.spaceMarines = spaceMarines;
    }
}
