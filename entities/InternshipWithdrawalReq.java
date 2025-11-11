package entities;

public class InternshipWithdrawalReq{
    private String userID;
    private String internshipTitle;
    private String companyName;
    private String withdrawalReason;
    public InternshipWithdrawalReq(String userID, String internshipTitle, String companyName, String withdrawalReason){
        this.userID = userID;
        this.internshipTitle = internshipTitle;
        this.companyName = companyName;
        this.withdrawalReason = withdrawalReason;
    }
    public String getUserID(){
        return userID;
    }
    public String getInternshipTitle(){
        return internshipTitle;
    }
    public String getCompanyName(){
        return companyName;
    }
    public String getWithdrawalReason(){
        return withdrawalReason;
    }
}
