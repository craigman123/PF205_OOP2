/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package companyInfo;

/**
 *
 * @author user
 */
public class CompanyInformation {
    private String company_name;
    private String companyDate;
    private String location;
    
    private void Information(){
        company_name = "Armex Legal Guns Industry Inc.";
    }
    
    private void CompanyLocation(){
        location = "Armex Industrial Factory Minglanilla Cebu Philippines";
    }
    
    private void Date(){
        companyDate = "2026-1-23";
    }
    
    public String GetInfo(){
        return company_name;
    }
    
    public String GetDate(){
        return companyDate;
    }
    
    public String GetLocation(){
        return location;
    }
}
