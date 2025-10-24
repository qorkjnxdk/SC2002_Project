package entities;

public class CompanyRepCreationReq{
    private String userID;
    private String name;
    private String companyName;
    private String department;
    private String position;
    public CompanyRepCreationReq(String userID, String name, String companyName, String department, String position){
        this.userID = userID;
        this.name = name;
        this.companyName = companyName;
        this.department = department;
        this.position = position;
    }
    public String getUserID(){
        return userID;
    }
    public String getName(){
        return name;
    }
    public String getCompanyName(){
        return companyName;
    }
    public String getDepartment(){
        return department;
    }
    public String getPosition(){
        return position;
    }
}
