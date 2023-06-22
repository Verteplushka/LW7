package commands;

import collectionClasses.SpaceMarine;
import mainProgramms.ReplyObj;
import mainProgramms.Sort;
import mainProgramms.User;

import java.util.LinkedList;

public class PrintAscending extends Command {
    public static ReplyObj printAscending(User user, LinkedList<SpaceMarine> spaceMarines) {
        Sort.sort(spaceMarines);
        return Show.show(user, spaceMarines);
    }
}
