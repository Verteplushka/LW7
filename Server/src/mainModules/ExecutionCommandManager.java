package mainModules;

import collectionClasses.SpaceMarine;
import mainProgramms.ExecuteRequestObj;
import mainProgramms.ReplyObj;
import mainProgramms.RequestObj;

import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;

public class ExecutionCommandManager {
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final ForkJoinPool pool = new ForkJoinPool(THREAD_POOL_SIZE);
    public static ReplyObj execute(LinkedList<SpaceMarine> spaceMarines, RequestObj recievedObj){
        ExecuteRequestObj executeRequestObj = new ExecuteRequestObj(spaceMarines, recievedObj);
        return pool.invoke(executeRequestObj);
    }
}
