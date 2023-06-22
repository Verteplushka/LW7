package commands;

import collectionClasses.SpaceMarine;
import mainProgramms.BD;
import mainProgramms.ReadException;
import mainProgramms.ReplyObj;
import mainProgramms.User;

import java.util.LinkedList;

public class Clear extends Command {
    public static ReplyObj clear(User user, LinkedList<SpaceMarine> spaceMarines) throws ReadException {
        int count = BD.clear(user);
        if(count > 0){
            spaceMarines.removeIf(spaceMarine -> spaceMarine.getUser().getUsername().equals(user.getUsername()));
            ReplyObj replyObj =  new ReplyObj("Коллекция пользователя " + user.getUsername() + " успешно очищена");
            replyObj.addString("Удалено объектов: " + count);
            return replyObj;
        }
        return new ReplyObj("Коллекция пользователя " + user.getUsername() + " пустая");
    }
}
