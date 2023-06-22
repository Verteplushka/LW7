package mainProgramms;

import collectionClasses.SpaceMarine;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class UserCollectionGetter {
    public static LinkedList<SpaceMarine> run(User user, LinkedList<SpaceMarine> spaceMarines){
        return spaceMarines.stream().filter(spaceMarine -> spaceMarine.getUser().getUsername().equals(user.getUsername())).collect(Collectors.toCollection(LinkedList::new));
    }
}
