package mainProgramms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

public class RequestObj implements Serializable {
    private User user;
    private static final Gson MyGson = new GsonBuilder().setPrettyPrinting().create();
    private String commandName;
    private String attribute = null;
    private int id;
    public RequestObj(User user, String commandName, String attribute, int id){
        this.user = user;
        this.commandName = commandName;
        this.attribute = attribute;
        this.id = id;
    }
    public RequestObj(User user, String commandName, String attribute){
        this.user = user;
        this.commandName = commandName;
        this.attribute = attribute;
    }
    public RequestObj(User user, String commandName){
        this.user = user;
        this.commandName = commandName;
    }
    public RequestObj(User user, String commandName, int id){
        this.user = user;
        this.commandName = commandName;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getCommandName() {
        return commandName;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return MyGson.toJson(this).replace("\n", "");
    }
}
