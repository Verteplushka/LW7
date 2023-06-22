package mainProgramms;

import enums.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ReadCommadsFromFile {
    public static void readCommandsFromFile(User user, String nameOfFile) throws IOException, ReadException {
        Path path = Paths.get(nameOfFile);
        String line;
        String firstPart = null;
        Scanner scanner = new Scanner(path);

        while (scanner.hasNext()) {
            line = scanner.next();
            switch (line) {
                case "help":
                    SendRequest.run(new RequestObj(user, "help"));
                    break;
                case "info":
                    SendRequest.run(new RequestObj(user, "info"));
                    break;
                case "show":
                    SendRequest.run(new RequestObj(user, "show"));
                    break;
                case "add":
                    String curSpaceMarine = GetSpaceMarineFromFileEnter.getSpaceMarineFromFileEnter(scanner);
                    if (curSpaceMarine != null) {
                        SendRequest.run(new RequestObj(user, "add", curSpaceMarine));
                        break;
                    } else {
                        throw new ReadException("Неверно задан объект коллекции");
                    }
                case "remove_greater":
                    SendRequest.run(new RequestObj(user, "remove_greater"));
                    break;
                case "print_ascending":
                    SendRequest.run(new RequestObj(user, "print_ascending"));
                    break;
                case "remove_first":
                    SendRequest.run(new RequestObj(user, "remove_first"));
                    break;
                case "clear":
                    SendRequest.run(new RequestObj(user, "clear"));
                    break;
                default:
                    int id;
                    if (firstPart != null) {
                        switch (firstPart) {
                            case "update":
                                id = Integer.parseInt(line);
                                SendRequest.run(new RequestObj(user, "update", id));
                                firstPart = null;
                                break;
                            case "remove_by_id":
                                id = Integer.parseInt(line);
                                SendRequest.run(new RequestObj(user, "remove_by_id", id));
                                firstPart = null;
                                break;
                            case "execute_script":
                                ExecuteScript.executeScript(user, line);
                                firstPart = null;
                                break;
                            case "remove_all_by_weapon_type":
                                Weapon weaponType = Weapon.valueOf(line);
                                SendRequest.run(new RequestObj(user, "remove_all_by_weapon_type", weaponType.toString()));
                                firstPart = null;
                                break;
                            case "count_by_melee_weapon":
                                MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(line);
                                SendRequest.run(new RequestObj(user, "count_by_melee_weapon", meleeWeapon.toString()));
                                firstPart = null;
                                break;
                        }
                    } else if (line.equals("update") | line.equals("remove_by_id") | line.equals("execute_script") | line.equals("remove_all_by_weapon_type") | line.equals("count_by_melee_weapon")) {
                        firstPart = line;
                    } else {
                        firstPart = null;
                        throw new ReadException("Неверно введена команда");
                    }
            }

            //MainModuleForEx.run(spaceMarines);
        }
    }
}

