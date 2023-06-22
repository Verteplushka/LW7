package mainModules;

import mainProgramms.CollectionManager;

public class MainModule {

    public static void run() {

        Thread keyboardReader = new Thread(new KeyboardReader(CollectionManager.getSpaceMarines()));
        keyboardReader.start();

        Thread clientProcessingModule = new Thread(new ClientProcessingModule());
        clientProcessingModule.start();
    }
}