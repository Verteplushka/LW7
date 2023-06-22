package mainProgramms;

import mainModules.MainModule;

public class Main {
    public static void main(String[] args) throws ReadException{
        BD.readHostData(".pgpass");
        BD.connectToBD();
        CollectionManager.setSpaceMarines(Sort.sort(BD.readCollection()));

        MainModule.run();
    }
}