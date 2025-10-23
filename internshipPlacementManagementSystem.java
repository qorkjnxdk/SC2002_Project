import util.LoadFiles;

public class internshipPlacementManagementSystem{
    public static void main(String[] args) {
        LoadFiles ld = new LoadFiles();
        ld.loadStudentCSV();
        ld.loadCareerStaffCSV();
        ld.loadCompanyRepCSV();
    }
}