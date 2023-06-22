package commands;

import collectionClasses.SpaceMarine;
import mainProgramms.*;

import java.util.LinkedList;

public class UpdateId extends Command {
    public static ReplyObj update(User user, LinkedList<SpaceMarine> spaceMarines, SpaceMarine newSpaceMarine, int id) throws ReadException {

        for (SpaceMarine curSpaceMarine : spaceMarines) {
            if (curSpaceMarine.getId() == id) {
                if (curSpaceMarine.getUser().getUsername().equals(user.getUsername())) {
                    BD.updateId(user, newSpaceMarine, id);
                    newSpaceMarine.setId(id);
                    newSpaceMarine.setUser(user);
                    spaceMarines.set(spaceMarines.indexOf(curSpaceMarine), newSpaceMarine);
                    CollectionManager.setSpaceMarines(spaceMarines);
                    Sort.sort(spaceMarines);
                    return new ReplyObj("Объект c id = " + id + " успешно обновлен");
                }
                throw new ReadException("Объект с id = " + id + " не принадлежит пользователю");
            }
        }
        throw new ReadException("В коллекции нет объекта с id = " + id);
    }
}
