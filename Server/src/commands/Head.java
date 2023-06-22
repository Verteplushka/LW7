package commands;

import collectionClasses.SpaceMarine;
import mainProgramms.ReplyObj;
import mainProgramms.Sort;
import mainProgramms.User;
import mainProgramms.UserCollectionGetter;

import java.util.LinkedList;
import java.util.Optional;

public class Head extends Command {
    public static ReplyObj head(User user, LinkedList<SpaceMarine> spaceMarines) {
        Optional<SpaceMarine> firstElement = spaceMarines.stream().findFirst();
        Optional<SpaceMarine> firstElementUser = Sort.sort(UserCollectionGetter.run(user, spaceMarines)).stream().findFirst();
        if(firstElement.isPresent()){
            ReplyObj replyObj = new ReplyObj("Первый объект в общей коллекции: " + firstElement.get());
            if(firstElementUser.isPresent()){
                replyObj.addString("Первый объект пользователя: " + firstElementUser.get());
            }
            return replyObj;
        }
        return new ReplyObj("Коллекция пустая");
    }
}
