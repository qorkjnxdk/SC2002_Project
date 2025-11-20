package repositories;
import java.util.ArrayList;
import entities.CompanyRepCreationReq;
import entities.InternshipWithdrawalReq;

/**
 * Repository class responsible for storing and managing request-related data,
 * including:
 * <ul>
 *     <li>{@link CompanyRepCreationReq} – Requests for new company representative accounts</li>
 *     <li>{@link InternshipWithdrawalReq} – Requests from students to withdraw their internship applications</li>
 * </ul>
 *
 * <p>This repository provides methods to retrieve, add, and delete these requests.
 * It is used by controllers such as:</p>
 * <ul>
 *     <li>{@link controllers.CareerStaffController}</li>
 *     <li>{@link controllers.CompanyRepController}</li>
 *     <li>{@link controllers.StudentController}</li>
 * </ul>
 *
 * <p>Internally, the repository uses {@code ArrayList} to store the two types of requests.</p>
 */
public class RequestRepository {
    private ArrayList<CompanyRepCreationReq> companyRepCreationReqList = new ArrayList<>();
    private ArrayList<InternshipWithdrawalReq> internshipWithdrawalReqList = new ArrayList<>();
    public RequestRepository(){}
    /**
     * Returns all pending company representative creation requests.
     *
     * @return a list of company representative account creation requests
     */
    public ArrayList<CompanyRepCreationReq> getAllCompanyRepCreationReq(){
        return companyRepCreationReqList;
    }

     /**
     * Returns all pending internship withdrawal requests submitted by students.
     *
     * @return a list of internship withdrawal requests
     */
    public ArrayList<InternshipWithdrawalReq> getInternshipWithdrawalReqList(){
        return internshipWithdrawalReqList;
    }

    /**
     * Adds a new company representative account creation request.
     *
     * @param companyRepCreationReq the request to add
     */
    public void addCompanyReqCreationReq(CompanyRepCreationReq companyRepCreationReq){
        companyRepCreationReqList.add(companyRepCreationReq);
    }

      /**
     * Removes an existing company representative account creation request.
     *
     * @param companyRepCreationReq the request to remove
     */
    public void deleteCompanyReqCreationReq(CompanyRepCreationReq companyRepCreationReq){
        companyRepCreationReqList.remove(companyRepCreationReq);
    }

    /**
     * Adds a new internship withdrawal request.
     *
     * @param internshipWithdrawalReq the withdrawal request to add
     */
    public void addInternshipWithdrawalReq(InternshipWithdrawalReq internshipWithdrawalReq){
        internshipWithdrawalReqList.add(internshipWithdrawalReq);
    }

    /**
     * Removes an existing internship withdrawal request.
     *
     * @param internshipWithdrawalReq the request to remove
     */
    public void deleteInternshipWithdrawalReq(InternshipWithdrawalReq internshipWithdrawalReq){
        internshipWithdrawalReqList.remove(internshipWithdrawalReq);
    }
}
