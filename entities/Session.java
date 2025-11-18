package entities;
import entities.User.Role;

public class Session {
    private String userID;
    private User user;
    private Role role;
    private Filter filter;
    public Session(String userID, User user, Role role){
        this.userID = userID;
        this.role = role;
        this.user = user;
        this.filter = null;
    }
    public User getUser(){
        return user;
    }
    public String getUserId(){
        return userID;
    }
    public Role getRole(){
        return role;
    }
    public Filter getFilter(){
        return filter;
    }
    public void setFilter(Filter filter){
        this.filter = filter;
    }
}
