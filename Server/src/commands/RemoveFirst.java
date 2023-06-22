package commands;

import collectionClasses.SpaceMarine;
import mainProgramms.*;

import java.util.LinkedList;
import java.util.Optional;

import static mainProgramms.BD.removeById;

public class RemoveFirst extends Command {
    public static ReplyObj removeFirst(User user, LinkedList<SpaceMarine> spaceMarines) throws ReadException{
        Optional<SpaceMarine> spaceMarineToDel = Sort.sort(UserCollectionGetter.run(user, spaceMarines)).stream().findFirst();
        if(spaceMarineToDel.isPresent()){
            BD.removeById(user, spaceMarineToDel.get().getId());
            spaceMarines.removeIf(spaceMarine -> spaceMarine.getId().equals(spaceMarineToDel.get().getId()));
            return new ReplyObj("Первый элемент коллекции пользователя " + user.getUsername() + " успешно удален");
        }
        throw new ReadException("Коллекция пользователя " + user.getUsername() + " пустая");
    }
}
