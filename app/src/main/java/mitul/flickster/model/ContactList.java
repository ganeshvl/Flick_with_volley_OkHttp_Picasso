package mitul.flickster.model;

/**
 * Created by mitul on 28/09/15.
 */
public class ContactList {
    String DisplayName;
    String MobileNumber;
    String HomeNumber;
    String WorkNumber;
    String emailID;
    String company;
    String jobTitle;
    public ContactList( String DisplayName, String MobileNumber, String HomeNumber, String WorkNumber, String emailID, String company, String jobTitle){
        this.DisplayName = DisplayName;
        this.MobileNumber=MobileNumber;
        this.HomeNumber = HomeNumber;
        this.WorkNumber = WorkNumber;
        this.emailID = emailID;
        this.company = company;
        this.jobTitle = jobTitle;
    }

    public String getDisplayName(){
        return this.DisplayName;
    }
    public String getMobileNumber(){
        return this.MobileNumber;
    }
    public String getHomeNumber(){
        return HomeNumber;
    }
    public String getWorkNumber(){
        return WorkNumber;
    }
    public String getEmailID(){
        return emailID;
    }
    public String getCompany(){
        return company;
    }
    public String getJobTitle(){
        return jobTitle;
    }
}

