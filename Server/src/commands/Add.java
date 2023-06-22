package commands;

import collectionClasses.SpaceMarine;
import mainProgramms.*;

import java.util.LinkedList;

public class Add extends Command {
    public static ReplyObj add(User user, LinkedList<SpaceMarine> spaceMarines, SpaceMarine newSpaceMarine) throws ReadException {
        newSpaceMarine.setUser(user);
        newSpaceMarine.setId(BD.addSpaceMarine(newSpaceMarine));
        spaceMarines.add(newSpaceMarine);
        Sort.sort(spaceMarines);
        return new ReplyObj( "Объект успешно добавлен");
    }
}
