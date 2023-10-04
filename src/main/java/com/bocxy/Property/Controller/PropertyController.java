package com.bocxy.Property.Controller;

import com.bocxy.Property.Entity.*;
import com.bocxy.Property.Model.CombinedDataModel;
import com.bocxy.Property.Model.WebsiteModel;
import com.bocxy.Property.Repository.AllotteeRepo;
import com.bocxy.Property.Repository.SchemeDataRepo;
import com.bocxy.Property.Repository.UnitDataRepo;
import com.bocxy.Property.Repository.WebsiteDataRepo;
import com.bocxy.Property.Service.PropertyService;
import com.bocxy.Property.common.ResponseDo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4300"}, allowedHeaders = "*")
public class PropertyController {

    private final PropertyService propertyService;
    private final AllotteeRepo allotteeRepo;
    private final SchemeDataRepo schemeDataRepo;
    private final UnitDataRepo UnitDataRepo;
    private final ResponseDo responseDo;
    private final WebsiteDataRepo websiteDataRepo;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public PropertyController(PropertyService propertyService,  AllotteeRepo allotteeRepo,
                              SchemeDataRepo schemeDataRepo, UnitDataRepo unitDataRepo,
                              ResponseDo responseDo,WebsiteDataRepo websiteDataRepo,JavaMailSender javaMailSender) {
        this.propertyService = propertyService;
        this.allotteeRepo = allotteeRepo;
        this.schemeDataRepo = schemeDataRepo;
        this.UnitDataRepo = unitDataRepo;
        this.responseDo = responseDo;
        this.websiteDataRepo = websiteDataRepo;
        this.javaMailSender = javaMailSender;
    }

    //Schemes

    //All Schemes for List

    @PostMapping("/getAllSchemes")
    public ResponseDo getAllScheme(@RequestBody JSONObject json,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        Long id = json.getAsNumber("id").longValue();
        try {
            List<SchemeData> allScheme = propertyService.getAllSchemeData();

            if (allScheme != null) {
                return responseDo.setSuccessResponse(allScheme);
            } else {
                return responseDo.setSuccessResponse("No Data Found", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("An error occurred"); // Return a failure response in case of exception
        }
    }

    //Create/Edit Single Scheme
    @PostMapping("/saveSchemeData")
    public ResponseDo saveSchemeData(@RequestBody List<SchemeData> schemeData) {
        try {
            List<SchemeData> savedSchemeData = propertyService.saveSchemeData(schemeData);

            if (savedSchemeData != null) {
                return responseDo.setSuccessResponse(savedSchemeData);
            } else {
                return responseDo.setFailureResponse("Failed to save SchemeData.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("An error occurred while saving SchemeData.");
        }
    }

    //Get Single Scheme
    @PostMapping("/getSchemeData")
    public ResponseDo getSchemeData(@RequestBody JSONObject json,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        Long id = json.getAsNumber("id").longValue();
        try {
            SchemeData schemeData = propertyService.getSchemeData(id);

            if (schemeData != null) {
                return responseDo.setSuccessResponse(schemeData);
            } else {
                return responseDo.setFailureResponse("Division Office not found for the provided ID.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("An error occurred"); // Return a failure response in case of exception
        }
    }

    // Delete Single Scheme
    @DeleteMapping("/deleteScheme/{id}")
    public ResponseDo deleteScheme(@PathVariable("id") Long id){
        try{
            propertyService.deleteSchemeData(id);
            return responseDo.setSuccessResponse("Successfully deleted SchemeData :"+id);
        }catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("Failed to delete SchemeData");
        }
    }

    //AutoCalculate Scheme Code
    @PostMapping("/getSchemeCode/{division}")
    public ResponseEntity<ResponseDo> getSchemeCode(@PathVariable("division") String division){
        try{
            String code = propertyService.getDivisionCode(division);
            int count = propertyService.getDivSchemeCount(division);
            String newCount = String.format("%02d",count);
            return ResponseEntity.ok(responseDo.setSuccessResponse("Successfully generated Scheme Code",code+newCount));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseDo.setFailureResponse("Failed to generate Scheme code."));
        }
    }

    //Get Division List
    @PostMapping("/getAllDivision")
    public ResponseEntity<ResponseDo> getAllDivision(){
        try{
            List<String> divisions = propertyService.getAllDivision();
            return ResponseEntity.ok(responseDo.setSuccessResponse(divisions));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseDo.setFailureResponse("Failed to retrieve division list."));
        }
    }

    //Allottee

    @PostMapping("/getAllotteesBySchemeId")
    public ResponseEntity<ResponseDo> getAllotteesBySchemeId(@RequestBody JSONObject json) {
        Long schemeId = json.getAsNumber("nSchemeId").longValue();

        try {
            List<Allottee> allottees = propertyService.getAllotteesBySchemeId(schemeId);

            if (allottees != null && !allottees.isEmpty()) {
                return ResponseEntity.ok(responseDo.setSuccessResponse(allottees));
            } else {
                return ResponseEntity.ok(responseDo.setSuccessResponse("No Allottees Found for Scheme ID: " + schemeId, null));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseDo.setFailureResponse("An error occurred"));
        }
    }

    //Create/Edit Allottee
    @PostMapping("/saveAllottees")
    public ResponseEntity<ResponseDo> saveAllottees(@RequestBody List<Allottee> allottees) {
        try {
            List<Allottee> savedAllottees = propertyService.saveAllottees(allottees);

            if (!savedAllottees.isEmpty()) {
                ResponseDo response = new ResponseDo();
                response.setSuccessResponse(true);
                response.setData(savedAllottees);
                return ResponseEntity.ok(response);
            } else {
                ResponseDo response = new ResponseDo();
                response.setSuccessResponse(false);
                response.setFailureResponse("Failed to save Allottees.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseDo response = new ResponseDo();
            response.setSuccessResponse(false);
            response.setFailureResponse("An error occurred while saving Allottees.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //Delete Single Allottee
    @DeleteMapping("/deleteAllottee/{id}")
    public ResponseDo deleteAllottee(@PathVariable("id") Long id){
        try{
            propertyService.deleteAllotteeData(id);
            return responseDo.setSuccessResponse("Successfully deleted Allottee :"+id);
        }catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("Failed to delete Allottee Data");
        }
    }

    //UnitData

    //Create/Edit UnitData
    @PostMapping("/saveUnitData")
    public ResponseDo saveunitdata(@RequestBody List<UnitData> unitData) {
        try {
            List<UnitData> savedUnitData = propertyService.saveUnitData(unitData);

            if (!savedUnitData.isEmpty()) {
                return responseDo.setSuccessResponse(savedUnitData);
            } else {
                return responseDo.setFailureResponse("Failed to save Allottees.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("An error occurred while saving Allottees.");
        }
    }

    //Get UnitData by Scheme id
    @PostMapping("/getUnitOfOneScheme")
    public ResponseDo getUnitOfScheme(@RequestBody JSONObject json,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        Long nSchemeId = json.getAsNumber("id").longValue();
        try {
            List<UnitData> unitData = propertyService.getUnits(nSchemeId);
            return responseDo.setSuccessResponse(unitData);
        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("Failed to list Unit Data");
        }

    }

    //Delete Single UnitData
    @DeleteMapping("/deleteUnit/{id}")
    public ResponseDo deleteUnit(@PathVariable("id") Long id){
        try{
            propertyService.deleteUnitData(id);
            return responseDo.setSuccessResponse("Successfully deleted Unit Data :"+id);
        }catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("Failed to delete Unit Data");
        }
    }

    //SaleDeed Document

    @PostMapping("/saveSalesDeed")
    public ResponseEntity<ResponseDo> saveSalesDeed(@RequestBody List<SalesDeedData> salesdeeddatas) {
        try {
            List<SalesDeedData> savedSalesDeedDatas = propertyService.saveSalesDeed(salesdeeddatas);

            if (!savedSalesDeedDatas.isEmpty()) {
                ResponseDo response = new ResponseDo();
                response.setSuccessResponse(true);
                response.setData(savedSalesDeedDatas);
                return ResponseEntity.ok(response);
            } else {
                ResponseDo response = new ResponseDo();
                response.setSuccessResponse(false);
                response.setFailureResponse("Failed to save SalesDeed Data.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseDo response = new ResponseDo();
            response.setSuccessResponse(false);
            response.setFailureResponse("An error occurred while saving SalesDeed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

   //Get Sales Deed Documents by Scheme ID

    @PostMapping("/getAllSalesDeed")
    public ResponseDo getAllSalesDeed(@RequestBody JSONObject json,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        Long nSchemeId = json.getAsNumber("id").longValue();
        try {
            List<SalesDeedData> allSalesDeeds = propertyService.getAllSalesDeeds(nSchemeId);

            if (allSalesDeeds != null) {
                return responseDo.setSuccessResponse(allSalesDeeds);
            } else {
                return responseDo.setSuccessResponse("No Data Found", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("An error occurred"); // Return a failure response in case of exception
        }
    }

    //ALLOTTEE DASHBOARD API

    //Allotte OTP for Login
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtpToAllottee(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        if (propertyService.emailExists(email)) {
            propertyService.sendOtpToAllottee(email);

            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP sent successfully.");

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid email.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    //Allottee OTP verification
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String otp = requestBody.get("otp");
        List<Allottee> response = propertyService.verifyOtpAndGetAllottee(email, otp);

        if (!response.isEmpty()) {

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP.");
        }
    }

    //Get Single Allottee by ID
    @PostMapping("/getAllotteebyid")
    public ResponseDo getAllotteebyid(@RequestBody JSONObject json,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        Long id = json.getAsNumber("id").longValue();
        try {
            List<Allottee> allotteeData = propertyService.getAllotteebyId(id);
            return responseDo.setSuccessResponse(allotteeData);
        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("Failed to list Allottee Data");
        }

    }

    //Combined Allotte Details

    @PostMapping("/getDetailsById")
    public ResponseDo getDetailsById(@RequestBody JSONObject json) {
        Long id = json.getAsNumber("id").longValue();
        try {
            CombinedDataModel details = propertyService.getDetailsById(id);
            return responseDo.setSuccessResponse(details); // Assuming ResponseDo constructor accepts status and data

        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("Failed to list Combined Data");
        }
    }

    //Get Single Allotee Sales Deed Document by ID
    @PostMapping("/getDocumentsbyid")
    public ResponseDo getDocumentsbyid(@RequestBody JSONObject json,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        Long id = json.getAsNumber("id").longValue();
        try {
            List<SalesDeedData> salesdeedData = propertyService.getSalesDeedDatabyId(id);
            return responseDo.setSuccessResponse(salesdeedData);
        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("Failed to list SalesDeed Data");
        }

    }

    //Website Module

    //Create/Edit Single WebsiteScheme
    @PostMapping("/saveWebsiteData")
    public ResponseEntity<ResponseDo> saveWebsiteData(@RequestBody List<WebsiteData> websiteDataList) {
        try {
            List<WebsiteData> savedWebsiteDataList = propertyService.saveWebsiteData(websiteDataList);
            if(!savedWebsiteDataList.isEmpty()){
                ResponseDo response = new ResponseDo();
                response.setSuccessResponse(true);
                response.setData(savedWebsiteDataList);
                return ResponseEntity.ok(response);
            } else {
                ResponseDo response = new ResponseDo();
                response.setSuccessResponse(false);
                response.setFailureResponse("Failed to save WebsiteData.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseDo response = new ResponseDo();
            response.setSuccessResponse(false);
            response.setFailureResponse("An error occurred while saving WebsiteData.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

            }

    }

    @PostMapping("/getParticularData")
    public ResponseEntity<ResponseDo> getWebsiteData() {
        try {
            List<WebsiteData> websiteDataList = propertyService.getWebsiteData();

            if (!websiteDataList.isEmpty()) {
                ResponseDo response = new ResponseDo();
                response.setSuccessResponse(true);
                response.setData(websiteDataList);
                return ResponseEntity.ok(response);
            } else {
                ResponseDo response = new ResponseDo();
                response.setSuccessResponse(false);
                response.setFailureResponse("No WebsiteData found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResponseDo response = new ResponseDo();
            response.setSuccessResponse(false);
            response.setFailureResponse("An error occurred while retrieving WebsiteData.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    //get all Website Data
    @PostMapping("/getAllWebsiteData")
    public ResponseDo getAllWebsiteData(@RequestBody JSONObject json,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        Long id = json.getAsNumber("id").longValue();
        try {
            List<WebsiteModel> allwebsiteData = propertyService.getAllWebsiteData();

            if (allwebsiteData != null) {
                return responseDo.setSuccessResponse(allwebsiteData);
            } else {
                return responseDo.setSuccessResponse("No Data Found", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("An error occurred"); // Return a failure response in case of exception
        }
    }

// get single websiteData

    @PostMapping("/getwebsiteData")
    public ResponseEntity<Map<String, Object>> getSchemeDataBySchemeId(@RequestBody Map<String, Long> request) {
        Long schemeId = request.get("schemeId");

        Map<String, Object> schemeData = propertyService.getSchemeDataBySchemeId(schemeId);
        if (schemeData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(schemeData);
    }

    @DeleteMapping("/deleteWebsite/{id}")
    public ResponseDo deleteWebsiteData(@PathVariable("id") Long id){
        try{
            propertyService.deleteWebsiteData(id);
            return responseDo.setSuccessResponse("Successfully deleted Website Data :"+id);
        }catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("Failed to delete Website Data");
        }
    }
    // enquiry
    @PostMapping("/send-email")
    public String sendEmail(@RequestBody Enquiry enquiry,
                            @RequestParam String vPocEmail,
                            @RequestParam String vEmail) {
        String result = propertyService.sendMailToCustomer(enquiry, vPocEmail, vEmail);
        return result;
    }



    @PostMapping("/getAllResidential")
    public ResponseDo getAllResidential(@RequestBody JSONObject json,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        Long id = json.getAsNumber("id").longValue();
        try {
            List<Residential> allScheme = propertyService.getResidential();

            if (allScheme != null) {
                return responseDo.setSuccessResponse(allScheme);
            } else {
                return responseDo.setSuccessResponse("No Data Found", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return responseDo.setFailureResponse("An error occurred"); // Return a failure response in case of exception
        }
    }

}





