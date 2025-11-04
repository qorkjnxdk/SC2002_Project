package entities;

public class CompanyRep extends User{
    private String companyName;
    private String department;
    private String position;
    public CompanyRep(String userID, String name, String companyName, String department, String position, String password){
        super(userID, name, password);
        this.role = Role.COMPANY_REP;
        this.companyName = companyName;
        this.department = department;
        this.position = position;
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
