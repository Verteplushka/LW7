package mainProgramms;

import java.io.Serializable;
import java.sql.Date;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String role;
    private Date creationDate;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password, String role, Date creationDate){
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.creationDate = creationDate;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
