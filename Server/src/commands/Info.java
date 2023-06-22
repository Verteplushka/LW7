package commands;

import collectionClasses.SpaceMarine;
import mainProgramms.ReplyObj;
import mainProgramms.User;
import mainProgramms.UserCollectionGetter;

import java.util.LinkedList;

public class Info extends Command {
    public static ReplyObj info(User user, LinkedList<SpaceMarine> spaceMarines) {
        ReplyObj replyObj = new ReplyObj();
        replyObj.addString("Тип: LinkedList");
        replyObj.addString("Дата инициализации: " + java.time.ZonedDateTime.now());
        replyObj.addString("Количество объектов всего: " + spaceMarines.size());
        replyObj.addString("Количество объектов пользователя " + user.getUsername() + ": " + UserCollectionGetter.run(user, spaceMarines).size());
        return replyObj;
    }
}
