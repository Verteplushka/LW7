package commands;

import collectionClasses.MeleeWeapon;
import collectionClasses.SpaceMarine;
import mainProgramms.ReplyObj;
import mainProgramms.User;
import mainProgramms.UserCollectionGetter;

import java.util.LinkedList;

public class CountByMeleeWeapon extends Command {
    public static ReplyObj countByMeleeWeapon(User user, LinkedList<SpaceMarine> spaceMarines, MeleeWeapon comparableMeleeType) {
        long count = spaceMarines.stream().filter(spaceMarine -> spaceMarine.getMeleeWeapon().equals(comparableMeleeType)).count();
        long userCount = UserCollectionGetter.run(user, spaceMarines).stream().filter(spaceMarine -> spaceMarine.getMeleeWeapon().equals(comparableMeleeType)).count();
        ReplyObj replyObj= new ReplyObj("Найдено объектов всего: " + count);
        replyObj.addString("Из которых объектов пользователя: " + userCount);
        return replyObj;
    }
}
