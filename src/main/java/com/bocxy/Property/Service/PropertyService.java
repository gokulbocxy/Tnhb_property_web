package com.bocxy.Property.Service;
import com.bocxy.Property.Entity.*;
import com.bocxy.Property.Model.CombinedDataModel;
import com.bocxy.Property.Model.WebsiteModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PropertyService {

    List<Allottee> saveAllottees(List<Allottee> allottees);

    List<Allottee> getAllotteesBySchemeId(Long schemeId);


    List<SchemeData>getAllSchemeData();

    List<SchemeData> saveSchemeData(List<SchemeData> schemeData);

    SchemeData getSchemeData(Long id);

    String getDivisionCode(String divisionName);
    int getDivSchemeCount(String divName);
    List<String> getAllDivision();

    List<UnitData> saveUnitData(List<UnitData> unitData);

    List<UnitData> getUnits(Long nSchemeId);

    void deleteSchemeData(Long id);

    void deleteAllotteeData(Long id);

    void deleteUnitData(Long id);

    List<SalesDeedData> saveSalesDeed(List<SalesDeedData> salesdeed);

    List<SalesDeedData> getAllSalesDeeds(Long nSchemeId);


    // ALLOTTEE DASHBOARD
    boolean emailExists(String email);

    void sendOtpToAllottee(String email);

    List<Allottee> verifyOtpAndGetAllottee(String email, String otp);

    List<Allottee> getAllotteebyId(Long id);

    CombinedDataModel getDetailsById(Long id);

    List<SalesDeedData> getSalesDeedDatabyId(Long id);


    //WEBSITE MODULE
    List<WebsiteData> saveWebsiteData(List<WebsiteData> websiteDataList);

    List<WebsiteData>getWebsiteData();


    List<WebsiteModel>getAllWebsiteData();

    Map<String, Object> getSchemeDataBySchemeId(Long schemeId);

    void deleteWebsiteData(Long id);

    //Enquiry
    String sendMailToCustomer(Enquiry enquiry, String vPocEmail, String vEmail);

    List<Residential>getResidential();

}
