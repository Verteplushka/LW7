package mainModules;

import collectionClasses.SpaceMarine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class KeyboardReader implements Runnable {
    private LinkedList<SpaceMarine> spaceMarines;

    public KeyboardReader(LinkedList<SpaceMarine> spaceMarines) {
        this.spaceMarines = spaceMarines;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                if (reader.ready()) {
                    String input = reader.readLine();
                    if (input.equals("exit")) {
                        System.out.println("Программа завершена");
                        System.exit(0);
                    } else {
                        System.out.println("Некорректно введена команда (доступна только команда exit)");
                    }
                }
            }
        } catch (IOException exception) {
            System.out.println("Ошибка ввода с клавиатуры");
        }
    }
}
