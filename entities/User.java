package entities;

public abstract class User {
    public enum Role {STUDENT,COMPANY_REP,CAREER_STAFF};
    protected Role role;
    private String userID;
    private String name;
    private String password;
    public User(String userID, String name, String password){
        this.userID = userID;
        this.name = name;
        this.password = password;
    }
    public void logIn(String password){
        
    }
    public void logOut(){

    }
    public Role getRole(){
        return role;
    }
    public String getUserId(){
        return userID;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public void changePassword(String password){
        this.password = password;
    }
}
