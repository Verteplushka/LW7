package commands;

import collectionClasses.SpaceMarine;
import collectionClasses.Weapon;
import mainProgramms.*;

import java.util.LinkedList;

public class RemoveAllByWeaponType extends Command {
    public static ReplyObj removeAllByWeaponType(User user, LinkedList<SpaceMarine> spaceMarines, Weapon comparableWeaponType) throws ReadException {
        int count = BD.removeAllByWeaponType(user, comparableWeaponType);
        if(count > 0){
            spaceMarines.removeIf(spaceMarine -> spaceMarine.getWeaponType().equals(comparableWeaponType) & spaceMarine.getUser().getUsername().equals(user.getUsername()));
            return new ReplyObj("Успешно удалено объектов: " + count);
        }
        return new ReplyObj("Не удалось найти объекты с weaponType = " + comparableWeaponType + " у пользователя " + user.getUsername());
    }
}
