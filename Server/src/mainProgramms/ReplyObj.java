package mainProgramms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

public class ReplyObj implements Serializable {
    private static final Gson MyGson = new GsonBuilder().setPrettyPrinting().create();
    private final LinkedList<String> stringList = new LinkedList<String>();
    private boolean isCorrectUser;

    public ReplyObj() {
    }

    public ReplyObj(String inputString) {
        this.addString(inputString);
    }
    public ReplyObj(boolean isCorrectUser){
        this.isCorrectUser = isCorrectUser;
    }

    public void addString(String addingString) {
        this.stringList.add(addingString);
    }
    public void setCorrectUser(boolean bool){
        this.isCorrectUser = bool;
    }

    public boolean isCorrectUser() {
        return isCorrectUser;
    }

    public LinkedList<String> getStringList() {
        return stringList;
    }

    public String getJson() {
        return MyGson.toJson(this).replace("\n", "");
    }

    @Override
    public String toString() {
        if(stringList.size() == 0){
            return null;
        }
        String outputString = "";
        for (int i = 0; i < this.stringList.size()-1; i++) {
            outputString = outputString + this.stringList.get(i) + "\n";
        }
        outputString = outputString + this.stringList.getLast();
        return outputString;
    }
}
