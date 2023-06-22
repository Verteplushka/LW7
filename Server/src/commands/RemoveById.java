package commands;

import collectionClasses.SpaceMarine;
import mainProgramms.BD;
import mainProgramms.ReadException;
import mainProgramms.ReplyObj;
import mainProgramms.User;

import java.util.LinkedList;

public class RemoveById extends Command {
    public static ReplyObj removeById(User user, LinkedList<SpaceMarine> spaceMarines, int id) throws ReadException {
        for (SpaceMarine curSpaceMarine : spaceMarines) {
            if (curSpaceMarine.getId() == id) {
                if(curSpaceMarine.getUser().getUsername().equals(user.getUsername())) {
                    BD.removeById(user, id);
                    spaceMarines.remove(curSpaceMarine);
                    return new ReplyObj("Объект c id = " + id + " успешно удален");
                }
                throw new ReadException("Объект с id = " + id + "есть в коллекции, но не принадлежит пользователю " + user.getUsername());
            }
        }
            throw new ReadException("В коллекции нет объекта с id = " + id);
    }
}
