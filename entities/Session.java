package entities;
import entities.User.Role;

public class Session {
    private String userID;
    private Role role;
    public Session(String userID, Role role){
        this.userID = userID;
        this.role = role;
    }
    public String getUserId(){
        return userID;
    }
    public Role getRole(){
        return role;
    }
}
