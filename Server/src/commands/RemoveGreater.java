package commands;

import collectionClasses.SpaceMarine;
import mainProgramms.*;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class RemoveGreater extends Command {
    public static ReplyObj removeGreater(User user, LinkedList<SpaceMarine> spaceMarines, SpaceMarine comparableSpaceMarine) throws ReadException {

        comparableSpaceMarine.setUser(user);
        comparableSpaceMarine.setId(BD.addSpaceMarine(comparableSpaceMarine));
        BD.removeById(user, comparableSpaceMarine.getId());

        int count = BD.removeGreater(user, comparableSpaceMarine);
        if(count > 0) {
            spaceMarines.removeIf(spaceMarine -> spaceMarine.compareTo(comparableSpaceMarine) > 0 & spaceMarine.getUser().getUsername().equals(user.getUsername()));
            Sort.sort(spaceMarines);
        }

        ReplyObj replyObj = new ReplyObj("Сравниваемому объекту присвоено id = " + comparableSpaceMarine.getId());
        replyObj.addString("Успешно удалено объектов: " + count);
        return replyObj;
    }
}
