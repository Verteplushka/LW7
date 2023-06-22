package mainProgramms;

import collectionClasses.MeleeWeapon;
import collectionClasses.SpaceMarine;
import collectionClasses.Weapon;
import commands.*;

import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;

public class ExecuteRequestObj extends RecursiveTask<ReplyObj> {
    private final LinkedList<SpaceMarine> spaceMarines;
    private final RequestObj recievedObj;

    public ExecuteRequestObj(LinkedList<SpaceMarine> spaceMarines, RequestObj recievedObj){
        this.spaceMarines = spaceMarines;
        this.recievedObj = recievedObj;
    }
    @Override
    public ReplyObj compute() {
        String command = recievedObj.getCommandName();
        ReplyObj prevReplyObj = new ReplyObj();
        try {
            switch (command) {
                case "checkUser":
                    return CheckUser.checkUser(recievedObj.getUser());
                case "help":
                    return Help.help();
                case "info":
                    return Info.info(recievedObj.getUser(), spaceMarines);
                case "show":
                    return Show.show(recievedObj.getUser(), spaceMarines);
                case "head":
                    return Head.head(recievedObj.getUser(), spaceMarines);
                case "add":
                    SpaceMarine addingSpaceMarine = GetSpaceMarineFromUser.get(new Scanner(recievedObj.getAttribute()));
                    return Add.add(recievedObj.getUser(), spaceMarines, addingSpaceMarine);
                case "remove_greater":
                    return RemoveGreater.removeGreater(recievedObj.getUser(), spaceMarines, GetSpaceMarineFromUser.get(new Scanner(recievedObj.getAttribute())));
                case "print_ascending":
                    return PrintAscending.printAscending(recievedObj.getUser(), spaceMarines);
                case "remove_first":
                    return RemoveFirst.removeFirst(recievedObj.getUser(), spaceMarines);
                case "clear":
                    return Clear.clear(recievedObj.getUser(), spaceMarines);
                case "update":
                    return UpdateId.update(recievedObj.getUser(), spaceMarines, GetSpaceMarineFromUser.get(new Scanner(recievedObj.getAttribute())), recievedObj.getId());
                case "remove_by_id":
                    return RemoveById.removeById(recievedObj.getUser(), spaceMarines, recievedObj.getId());
                case "remove_all_by_weapon_type":
                    Weapon weaponType = Weapon.valueOf(recievedObj.getAttribute());
                    return RemoveAllByWeaponType.removeAllByWeaponType(recievedObj.getUser(), spaceMarines, weaponType);
                case "count_by_melee_weapon":
                    MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(recievedObj.getAttribute());
                    return CountByMeleeWeapon.countByMeleeWeapon(recievedObj.getUser(), spaceMarines, meleeWeapon);
            }
        } catch (ReadException exception) {
            prevReplyObj.addString(exception.getMessage());
            return prevReplyObj;
        } catch (DateTimeParseException exception) {
            prevReplyObj.addString("Неверное поле creationDate");
            return prevReplyObj;
        } catch (java.lang.IndexOutOfBoundsException exception) {
            prevReplyObj.addString("В коллекции нет объекта с таким индексом");
            exception.printStackTrace();
            return prevReplyObj;
        } catch (IllegalArgumentException exception) {
            prevReplyObj.addString("Неверно введено поле/команда");
            return prevReplyObj;
        } catch (NullPointerException exception) {
            prevReplyObj.addString("Ошибка");
            exception.printStackTrace();
            return prevReplyObj;
        } catch (NoSuchElementException exception) {
            prevReplyObj.addString("Ошибка поиска элемента/Неправильно задан объект коллекции");
            return prevReplyObj;
        }
        prevReplyObj.addString("Неверная команда на сервере??");
        return prevReplyObj;
    }
}
