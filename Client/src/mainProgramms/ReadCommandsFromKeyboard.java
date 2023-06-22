package mainProgramms;

import enums.MeleeWeapon;
import enums.Weapon;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.SocketException;
import java.nio.file.NoSuchFileException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ReadCommandsFromKeyboard {
    public static void readCommandFromKeyboard() {
        Scanner scanner = new Scanner(System.in);
        String firstPart = null;
        String line;

        try {
            User user = getUser(scanner);
            while (SendRequest.run(new RequestObj(user, "checkUser"))) {
                System.out.println("Такой пользователь уже существует, и у него другой пароль");
                user = getUser(scanner);
            }
            ;

            System.out.println("Добро пожаловать, " + user.getUsername());

            while (true) {
                if (firstPart == null) {
                    System.out.println("Введите команду:");
                }
                line = scanner.next();
                try {
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
                        case "head":
                            SendRequest.run(new RequestObj(user, "head"));
                            break;
                        case "add":
                            String addingSpaceMarine = GetSpaceMarineFromKeyboard.getSpaceMarineFromKeyboard();
                            SendRequest.run(new RequestObj(user, "add", addingSpaceMarine));
                            break;
                        case "remove_greater":
                            String compSpaceMarine = GetSpaceMarineFromKeyboard.getSpaceMarineFromKeyboard();
                            SendRequest.run(new RequestObj(user, "remove_greater", compSpaceMarine));
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
                                        firstPart = null;
                                        String newSpaceMarine = GetSpaceMarineFromKeyboard.getSpaceMarineFromKeyboard();
                                        SendRequest.run(new RequestObj(user, "update", newSpaceMarine, id));
                                        break;
                                    case "remove_by_id":
                                        id = Integer.parseInt(line);
                                        firstPart = null;
                                        SendRequest.run(new RequestObj(user, "remove_by_id", id));
                                        break;
                                    case "execute_script":
                                        firstPart = null;
                                        ExecuteScript.executeScript(user, line);
                                        //SendRequest.run(new RequestObj("execute_script", line));
                                        break;
                                    case "remove_all_by_weapon_type":
                                        Weapon weaponType = Weapon.valueOf(line);
                                        firstPart = null;
                                        SendRequest.run(new RequestObj(user, "remove_all_by_weapon_type", weaponType.toString()));
                                        break;
                                    case "count_by_melee_weapon":
                                        MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(line);
                                        firstPart = null;
                                        SendRequest.run(new RequestObj(user, "count_by_melee_weapon", meleeWeapon.toString()));
                                        break;
                                }
                            } else if (line.equals("update") | line.equals("remove_by_id") | line.equals("execute_script") | line.equals("remove_all_by_weapon_type") | line.equals("count_by_melee_weapon")) {
                                firstPart = line;
                            } else {
                                firstPart = null;
                                throw new ReadException("Неверно введена команда");
                            }
                    }
                } catch (ReadException exception) {
                    System.out.println(exception.getMessage());
                    firstPart = null;
                } catch (NoSuchFileException exception) {
                    System.out.println("Ошибка чтения файла");
                } catch (NumberFormatException exception) {
                    System.out.println("Неверно введен параметр id");
                } catch (IllegalArgumentException exception) {
                    System.out.println("Неверно введено enum-поле");
                    firstPart = null;
                }
            }
        } catch (NoSuchElementException exception) {
            System.out.println("NoSuchElementException");
        } catch (SocketException exception) {
            System.out.println("Сервер отключен");
        } catch (StreamCorruptedException exception) {
            System.out.println("Получено слишком большое сообщение");
        } catch (IOException exception) {
            System.out.println("Ошибка подключения к серверу");
        }
    }

    private static User getUser(Scanner scanner) {
        System.out.println("Введите имя:");
        String username = scanner.nextLine();
        while(username.contains(" ") | username.equals("")){
            System.out.println("Имя не может содержать пробелов/быть пустой строкой");
            System.out.println("Введите имя:");
            username = scanner.nextLine();
        }
        System.out.println("Введите пароль");
        String password = scanner.nextLine();
        while(password.contains(" ") | password.equals("")){
            System.out.println("Пароль не может содержать пробелов/быть пустой строкой");
            System.out.println("Введите имя:");
            password = scanner.nextLine();
        }
        return new User(username, MD2Hash.getHash(password));
    }
}
