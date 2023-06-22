package commands;

import collectionClasses.SpaceMarine;
import mainProgramms.ReplyObj;
import mainProgramms.User;
import mainProgramms.UserCollectionGetter;

import java.util.LinkedList;

public class Show extends Command {
    public static ReplyObj show(User user, LinkedList<SpaceMarine> spaceMarines) {
        if (spaceMarines.size() == 0) {
            return new ReplyObj("Коллекция пустая");
        }
        ReplyObj replyObj = new ReplyObj("Все объекты: ");
        spaceMarines.stream().map(SpaceMarine :: toString).forEach(replyObj::addString);

        replyObj.addString("Объекты пользователя " + user.getUsername() + ":");
        UserCollectionGetter.run(user, spaceMarines).stream().map(SpaceMarine :: toString).forEach(replyObj::addString);

        return replyObj;
    }
}
