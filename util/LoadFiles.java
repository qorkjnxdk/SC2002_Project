package util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadFiles {
    public void loadStudentCSV(){
        String studentListCSVFilePath = "data/sample_student_list.csv";
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(studentListCSVFilePath))){
            while ((line = br.readLine())!=null){
                System.out.println(line);
                System.out.println();
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }
    public void loadCareerStaffCSV(){
        String staffListCSVFilePath = "data/sample_staff_list.csv";
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(staffListCSVFilePath))){
            while ((line = br.readLine())!=null){
                System.out.println(line);
                System.out.println();
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }
    public void loadCompanyRepCSV(){
        String companyRepListCSVFilePath = "data/sample_company_representative_list.csv";
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(companyRepListCSVFilePath))){
            while ((line = br.readLine())!=null){
                System.out.println(line);
                System.out.println();
            }
        } catch (IOException e){
            e.printStackTrace();
        } 
    }
}
