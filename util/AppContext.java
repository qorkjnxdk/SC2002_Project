package util;

import controllers.AuthController;
import entities.Session;

public class AppContext {
    private AuthController authController;
    private Session session;
    public AppContext(AuthController authController){
        this.authController = authController;
    }
    public AuthController getAuthController(){
        return authController;
    }
    public Session getSession(){
        return session;
    }
    public void setSession(Session session){
        this.session = session;
    }
    public void clearSession(){
        this.session = null;
    }
}
