package commands;

import mainProgramms.BD;
import mainProgramms.ReplyObj;
import mainProgramms.User;

public class CheckUser extends Command{
    public static ReplyObj checkUser(User user){
        if(BD.addUser(user)!=0){
            return new ReplyObj(false);
        }
        return new ReplyObj(true);
    }
}
