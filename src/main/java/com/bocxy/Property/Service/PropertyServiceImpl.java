package com.bocxy.Property.Service;
import com.bocxy.Property.Entity.*;
import com.bocxy.Property.Model.CombinedDataModel;
import com.bocxy.Property.Model.WebsiteModel;
import com.bocxy.Property.Repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


@Service
public class PropertyServiceImpl implements PropertyService {
    @Value("${upload.dir}")
    private String uploadDir;
    private final AllotteeRepo allotteeRepo;
    private final SchemeDataRepo schemeDataRepo;
    private final UnitDataRepo unitDataRepo;
    private final SalesDeedDataRepo saleddeeddataRepo;

     private final ResidentialRepo residentialRepo;
    private final WebsiteDataRepo websiteDataRepo;

    private final EnquiryRepo enquiryRepo;
    @Autowired
    public PropertyServiceImpl(AllotteeRepo allotteeRepo, SchemeDataRepo schemeDataRepo, UnitDataRepo unitDataRepo, SalesDeedDataRepo salesdeeddataRepo,WebsiteDataRepo websiteDataRepo,EnquiryRepo enquiryRepo,ResidentialRepo residentialRepo) {
        this.allotteeRepo = allotteeRepo;
        this.schemeDataRepo = schemeDataRepo;
        this.unitDataRepo = unitDataRepo;
        this.saleddeeddataRepo = salesdeeddataRepo;
        this.websiteDataRepo = websiteDataRepo;
        this.enquiryRepo = enquiryRepo;
        this.residentialRepo=residentialRepo;

    }

    @Autowired
    private JavaMailSender javaMailSender;


    @Override
    public List<Allottee> getAllotteesBySchemeId(Long schemeId) {
        return allotteeRepo.findAllByNSchemeId(schemeId);
    }

    @Transactional
    @Override
    public List<Allottee> saveAllottees(List<Allottee> allottees) {
        List<Allottee> savedAllottees = new ArrayList<>();

        for (Allottee allottee : allottees) {
            if (allottee.getV_ALLOTTE_FILE()!= null) {
                String base64FileData = allottee.getV_ALLOTTE_FILE();
                byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
                String generatedFileName = allottee.getV_ALLOTTE_FILE_NAME();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + generatedFileName;
                File dest = new File(uploadDir, uniqueFileName);

                try {
                    Files.write(dest.toPath(), decodedFileData);
                    allottee.setV_ALLOTTE_FILE_NAME(generatedFileName);
                    allottee.setV_ALLOTTE_FILE_PATH(dest.getAbsolutePath());
                } catch (IOException e) {

                }
            }
            if (allottee.getV_AADHAAR_FILE()!= null) {
                String base64FileData = allottee.getV_AADHAAR_FILE();
                byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
                String generatedFileName = allottee.getV_AADHAAR_FILE_NAME();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + generatedFileName;
                File dest = new File(uploadDir, uniqueFileName);

                try {
                    Files.write(dest.toPath(), decodedFileData);
                    allottee.setV_AADHAAR_FILE_NAME(generatedFileName);
                    allottee.setV_AADHAAR_FILE_PATH(dest.getAbsolutePath());
                } catch (IOException e) {

                }
            }
            if (allottee.getV_OTHER_FILE()!= null) {
                String base64FileData = allottee.getV_OTHER_FILE();
                byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
                String generatedFileName = allottee.getV_OTHER_FILE_NAME();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + generatedFileName;
                File dest = new File(uploadDir, uniqueFileName);

                try {
                    Files.write(dest.toPath(), decodedFileData);
                    allottee.setV_OTHER_FILE_NAME(generatedFileName);
                    allottee.setV_OTHER_FILE_PATH(dest.getAbsolutePath());
                } catch (IOException e) {

                }
            }
            if (allottee.getNID() != null) {
                System.out.println(allottee.getNID());
                List<Allottee> existingAllottees = allotteeRepo.findByNID(allottee.getNID());

                for (Allottee existingAllottee : existingAllottees) {
                    // Check if EMAILID is being updated
                    System.out.println(allottee.getEMAILID());
                    System.out.println(existingAllottee.getEMAILID());
                    if (!allottee.getEMAILID().equals(existingAllottee.getEMAILID())) {
                        System.out.println(allottee.getEMAILID());
                        // Send email for email update
                        sendEmailForEmailUpdate(allottee);
                    }
                }
            } else {
                // Send email for new allottee creation
                sendEmailForNewAllottee(allottee);
            }

            Allottee savedAllottee = allotteeRepo.save(allottee);
            savedAllottees.add(savedAllottee);


        }

        return savedAllottees;
    }
    
    //EMAIL CONTENTS

    private void sendEmailForNewAllottee(Allottee allottee) {
        String emailSubject = "TNHB - Property Management Dashboard Registration";
        String emailContent = "Hi Sir/Madam,\n\n" +
                "Dear " + allottee.getV_ALLOTTEE_NAME() + ", your details have been registered in TamilNadu Housing Board for buying the property. Please visit your Dashboard through the below link:\n\n" +
                "URL - \"https://www.bocxy.com/\".\n\n" +
                "Note: Your registered email is your Username.\n\n" +
                "Thanks & Regards,\n" +
                "TamilNadu Housing Board.";

        sendEmailNotification(allottee.getEMAILID(), emailSubject, emailContent);
    }

    private void sendEmailForEmailUpdate(Allottee allottee) {
        String emailSubject = "TNHB - Property Management Dashboard Login Details Update";
        String emailContent = "Hi Sir/Madam,\n\n" +
                "Dear " + allottee.getV_ALLOTTEE_NAME() + ", your login details have been updated in TamilNadu Housing Board for buying the property. Please visit your Dashboard through the below link:\n\n" +
                "URL - \"https://www.bocxy.com/\".\n\n" +
                "Note: Your registered email is your Username.\n\n" +
                "Thanks & Regards,\n" +
                "TamilNadu Housing Board.";

        sendEmailNotification(allottee.getEMAILID(), emailSubject, emailContent);
    }

    public void sendEmailNotification(String email, String subject , String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);

        javaMailSender.send(message);
    }

    @Override
    public List<SchemeData>getAllSchemeData() {
        return schemeDataRepo.findAll();
    }

    @Override
    public SchemeData getSchemeData(Long id) {
        Optional<SchemeData> optionalSchemeData = schemeDataRepo.findById(id);
        return optionalSchemeData.orElse(null);
    }


    @Override
    public List<SchemeData> saveSchemeData(List<SchemeData> schemeData) {
        for(SchemeData scheme : schemeData){
            scheme.updateTotalDevelopedUnits();
            schemeDataRepo.save(scheme);
        }
        return schemeData;
    }

    @Override
    public String getDivisionCode(String divisionName) {
        return schemeDataRepo.getDivisionCode(divisionName);
    }

    @Override
    public int getDivSchemeCount(String divName) {
        return schemeDataRepo.getDivSchemeCount(divName);
    }

    @Override
    public List<String> getAllDivision() {
        return schemeDataRepo.getAllDivision();
    }

    @Override
    public List<UnitData> saveUnitData(List<UnitData> unitData) {
        unitDataRepo.saveAll(unitData);
        Long id = unitData.get(0).getN_SCHEME_ID();
        Optional<SchemeData> schemeDataOptional = schemeDataRepo.findById(id);
        SchemeData schemeData = schemeDataOptional.orElseThrow();
        schemeData.setN_TOTAL_ALLOTTED_UNITS(unitDataRepo.updateTotalAllottedUnits(id));
        schemeData.setN_TOTAL_ALLOTTED_UNITS_FOR_RESIDENTIAL(unitDataRepo.allottedResidential(id));
        schemeData.setN_TOTAL_ALLOTTED_UNITS_FOR_COMMERCIAL(unitDataRepo.allottedCommercial(id));
        schemeData.setN_TOTAL_ALLOTTED_UNITS_FOR_HIRE_PURCHASE(unitDataRepo.allotedHirePurchase(id));
        schemeData.setN_TOTAL_ALLOTTED_UNITS_FOR_SFS(unitDataRepo.allottedSelfFinance(id));
        schemeData.setN_TOTAL_ALLOTTED_UNITS_FOR_OUTRIGHT(unitDataRepo.allottedOutright(id));
        schemeData.setN_TOTAL_ALLOTTED_UNITS_FOR_HIG(unitDataRepo.allottedHIG(id));
        schemeData.setN_TOTAL_ALLOTTED_UNITS_FOR_MIG(unitDataRepo.allottedMIG(id));
        schemeData.setN_TOTAL_ALLOTTED_UNITS_FOR_LIG(unitDataRepo.allottedLIG(id));
        schemeData.setN_TOTAL_ALLOTTED_UNITS_FOR_EWS(unitDataRepo.allottedEWS(id));
        schemeDataRepo.save(schemeData);

        return unitData;
    }


    public List<UnitData> getUnits(Long nSchemeId){
        return unitDataRepo.findByNSchemeId(nSchemeId);
    }

    @Override
    public void deleteSchemeData(Long id) {
        schemeDataRepo.deleteById(id);
    }

    @Override
    public void deleteAllotteeData(Long id) {
        allotteeRepo.deleteById(id);
    }

    @Override
    public void deleteUnitData(Long id) {
        unitDataRepo.deleteById(id);
    }

    //Sales Deed Document Upload
    @Transactional
    @Override
    public List<SalesDeedData> saveSalesDeed(List<SalesDeedData> salesdeed) {
        List<SalesDeedData> savedSalesDeeds = new ArrayList<>();

        for (SalesDeedData salesdeedData : salesdeed) {
            if (salesdeedData.getAllotmentOrderFile()!= null) {
                String base64FileData = salesdeedData.getAllotmentOrderFile();
                byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
                String generatedFileName = salesdeedData.getAllotmentOrderFileName();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + generatedFileName;
                File dest = new File(uploadDir, uniqueFileName);

                try {
                    Files.write(dest.toPath(), decodedFileData);
                    salesdeedData.setAllotmentOrderFileName(generatedFileName);
                    salesdeedData.setAllotmentOrderFilePath(dest.getAbsolutePath());
                } catch (IOException e) {

                }
            }
            if (salesdeedData.getLCSFile()!= null) {
                String base64FileData = salesdeedData.getLCSFile();
                byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
                String generatedFileName = salesdeedData.getLCSFileName();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + generatedFileName;
                File dest = new File(uploadDir, uniqueFileName);

                try {
                    Files.write(dest.toPath(), decodedFileData);
                    salesdeedData.setLCSFileName(generatedFileName);
                    salesdeedData.setLCSFilePath(dest.getAbsolutePath());
                } catch (IOException e) {

                }
            }
            if (salesdeedData.getHandingReportFile()!= null) {
                String base64FileData = salesdeedData.getHandingReportFile();
                byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
                String generatedFileName = salesdeedData.getHandingReportFileName();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + generatedFileName;
                File dest = new File(uploadDir, uniqueFileName);

                try {
                    Files.write(dest.toPath(), decodedFileData);
                    salesdeedData.setHandingReportFileName(generatedFileName);
                    salesdeedData.setHandingReportFilePath(dest.getAbsolutePath());
                } catch (IOException e) {

                }
            }
            if (salesdeedData.getFieldBookFile()!= null) {
                String base64FileData = salesdeedData.getFieldBookFile();
                byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
                String generatedFileName = salesdeedData.getFieldBookFileName();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + generatedFileName;
                File dest = new File(uploadDir, uniqueFileName);

                try {
                    Files.write(dest.toPath(), decodedFileData);
                    salesdeedData.setFieldBookFileName(generatedFileName);
                    salesdeedData.setFieldBookFilePath(dest.getAbsolutePath());
                } catch (IOException e) {

                }
            }
            if (salesdeedData.getLoanFile()!= null) {
                String base64FileData = salesdeedData.getLoanFile();
                byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
                String generatedFileName = salesdeedData.getLoanFileName();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + generatedFileName;
                File dest = new File(uploadDir, uniqueFileName);

                try {
                    Files.write(dest.toPath(), decodedFileData);
                    salesdeedData.setLoanFileName(generatedFileName);
                    salesdeedData.setLoanFilePath(dest.getAbsolutePath());
                } catch (IOException e) {

                }
            }
            if (salesdeedData.getSaleDeedFile()!= null) {
                String base64FileData = salesdeedData.getSaleDeedFile();
                byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
                String generatedFileName = salesdeedData.getSaleDeedFileName();
                String uniqueFileName = UUID.randomUUID().toString() + "_" + generatedFileName;
                File dest = new File(uploadDir, uniqueFileName);

                try {
                    Files.write(dest.toPath(), decodedFileData);
                    salesdeedData.setSaleDeedFileName(generatedFileName);
                    salesdeedData.setSaleDeedFilePath(dest.getAbsolutePath());
                } catch (IOException e) {

                }
            }

            SalesDeedData savedsalesdeed = saleddeeddataRepo.save(salesdeedData);
            savedSalesDeeds.add(savedsalesdeed);
        }

        return savedSalesDeeds;
    }

    //Sales Deed get by Scheme ID

    public List<SalesDeedData> getAllSalesDeeds(Long nSchemeId){
        return saleddeeddataRepo.findByNSchemeId(nSchemeId);
    }


    //ALLOTEE DASHBOARD

    //Allotte OTP for Login

    public boolean emailExists(String email) {
        List<Allottee> allottees = allotteeRepo.findAllByEMAILID(email);
        return !allottees.isEmpty();
    }

    public class OtpGenerator {
        public static String generateOtp(int length) {
            return RandomStringUtils.randomNumeric(length);
        }
    }
    public void sendOtpToAllottee(String email) {
        List<Allottee> allottees = allotteeRepo.findAllByEMAILID(email);

        String otp = OtpGenerator.generateOtp(6);

        for (Allottee allottee : allottees) {
            allottee.setV_OTP(otp);
            allotteeRepo.save(allottee);

            sendOtpEmail(allottee.getEMAILID(), otp);
        }
    }

    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code for TNHB-Property Dashboard");
        message.setText("Hi! Your OTP code is: " + otp);

        javaMailSender.send(message);
    }

    //Verify OTP
    @Override
    public List<Allottee> verifyOtpAndGetAllottee(String email, String otp) {
        List<Allottee> allottees = allotteeRepo.findAllByEMAILID(email);
        List<Allottee> responseList = new ArrayList<>();
        for (Allottee allottee : allottees) {
            if (allottee.getV_OTP().equals(otp)) {
                responseList.add(allottee);
            }
        }
        return responseList;
    }



    // Allotte by Id
    public List<Allottee> getAllotteebyId(Long id){
        return allotteeRepo.findByNID(id);
    }

    //Combined Allotte Details
    @Transactional
    @Override
    public CombinedDataModel getDetailsById(Long id) {
        List<Object[]> queryResult = allotteeRepo.findDetailsById(id);

        if (!queryResult.isEmpty()) {
            Object[] resultRow = queryResult.get(0);

            CombinedDataModel combinedDataModel = new CombinedDataModel();
            combinedDataModel.setAllotteeDate((String) resultRow[0]);
            combinedDataModel.setAllotteeNo((String) resultRow[1]);
            combinedDataModel.setBlockNo((String) resultRow[2]);
            combinedDataModel.setCarpetArea((String) resultRow[3]);
            combinedDataModel.setFloorNo((String) resultRow[4]);
            combinedDataModel.setPlotArea((String) resultRow[5]);
            combinedDataModel.setTypeName((String) resultRow[6]);
            combinedDataModel.setUdsArea((String) resultRow[7]);
            combinedDataModel.setUnitNo((String) resultRow[8]);
            combinedDataModel.setAssetCategory((String) resultRow[9]);
            combinedDataModel.setAssetSubCategory((String) resultRow[10]);
            combinedDataModel.setAssetType((String) resultRow[11]);
            combinedDataModel.setPlinthArea((String) resultRow[12]);
            combinedDataModel.setFinalLandCost(resultRow[13] != null ? resultRow[13].toString() : null);
            combinedDataModel.setSchemeName((String) resultRow[14]);

            return combinedDataModel;
        } else {
            return null; // Handle the case where no result is found
        }
    }

    //Get Allottee Documents
    public List<SalesDeedData> getSalesDeedDatabyId(Long id){
        return saleddeeddataRepo.findByNAllotteeId(id);
    }

//WEBSITE MODULE
@Transactional
@Override
public List<WebsiteData> saveWebsiteData(List<WebsiteData> websiteDataList) {
    List<WebsiteData> savedWebsiteDataList = new ArrayList<>();

    for (WebsiteData websiteData : websiteDataList) {
        if (websiteData.getFPhoto() != null) {
            String[] base64FileDataList = websiteData.getFPhoto();

            List<String> uploadedPhotoPaths = new ArrayList<>();

            File destFolder = new File(uploadDir);
            if (!destFolder.exists()) {
                destFolder.mkdirs();
            }

            for (String base64FileData : base64FileDataList) {
                byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
                String generatedFileName = UUID.randomUUID().toString() + ".jpg";

                File dest = new File(destFolder, generatedFileName);

                try {
                    Files.write(dest.toPath(), decodedFileData);
                    uploadedPhotoPaths.add(dest.getAbsolutePath());
                } catch (IOException e) {
                    // Handle the exception (e.g., log or throw)
                }
            }

            // Convert the list of file paths to an array of strings
            String[] uploadedPhotoPathsArray = uploadedPhotoPaths.toArray(new String[0]);

            // Set the array of file paths in the 'fPhotoPaths' field
            websiteData.setFPhotoPaths(uploadedPhotoPathsArray);

            // Clear the 'fPhoto' field (if you no longer need it)
            websiteData.setFPhoto(null);
        }


        if (websiteData.getFFloorPlanPicture() != null) {
            String base64FileData = websiteData.getFFloorPlanPicture();
            byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
            String generatedFileName = UUID.randomUUID().toString() + ".jpg";

            File destFolder = new File(uploadDir);
            if (!destFolder.exists()) {
                destFolder.mkdirs();
            }

            File dest = new File(destFolder, generatedFileName);

            try {
                Files.write(dest.toPath(), decodedFileData);
                websiteData.setFFloorPlanPicture(dest.getAbsolutePath());
            } catch (IOException e) {

            }
        }

        if (websiteData.getFFloorPlanPdf() != null) {
            String base64PdfData = websiteData.getFFloorPlanPdf();
            byte[] decodedPdfData = Base64.getDecoder().decode(base64PdfData);
            String generatedPdfFileName = UUID.randomUUID().toString() + ".pdf";

            File destFolder = new File(uploadDir);
            if (!destFolder.exists()) {
                destFolder.mkdirs();
            }

            File dest = new File(destFolder, generatedPdfFileName);

            try {
                Files.write(dest.toPath(), decodedPdfData);
                websiteData.setFFloorPlanPdf(dest.getAbsolutePath());
            } catch (IOException e) {

            }
        }

        if (websiteData.getFPocPicture() != null) {
            String base64FileData = websiteData.getFPocPicture();
            byte[] decodedFileData = Base64.getDecoder().decode(base64FileData);
            String generatedFileName = UUID.randomUUID().toString() + ".jpg";

            File destFolder = new File(uploadDir);
            if (!destFolder.exists()) {
                destFolder.mkdirs();
            }

            File dest = new File(destFolder, generatedFileName);

            try {
                Files.write(dest.toPath(), decodedFileData);
                websiteData.setFPocPicture(dest.getAbsolutePath());
            } catch (IOException e) {

            }
        }

        if (websiteData.getFVideo() != null) {
            String base64VideoData = websiteData.getFVideo();
            byte[] decodedVideoData = Base64.getDecoder().decode(base64VideoData);
            String generatedVideoFileName = UUID.randomUUID().toString() + ".mp4";

            File destFolder = new File(uploadDir);
            if (!destFolder.exists()) {
                destFolder.mkdirs();
            }

            File dest = new File(destFolder, generatedVideoFileName);

            try {
                Files.write(dest.toPath(), decodedVideoData);
                websiteData.setFVideo(dest.getAbsolutePath());
            } catch (IOException e) {

            }
        }

        WebsiteData savedWebsiteData = websiteDataRepo.save(websiteData);
        savedWebsiteDataList.add(savedWebsiteData);
    }

    return savedWebsiteDataList;
}
    @Override
    public List<WebsiteData> getWebsiteData() {
        return websiteDataRepo.findAll();
    }

    public List<WebsiteModel> getAllWebsiteData() {
        List<Map<String, Object>> queryResults = websiteDataRepo.findAllUnitData();

        List<WebsiteModel> websiteModels = new ArrayList<>();
        for (Map<String, Object> result : queryResults) {
            WebsiteModel websiteModel = new WebsiteModel();
            websiteModel.setV_SCHEME_NAME((String) result.get("v_scheme_name"));
            websiteModel.setV_DIVISION((String) result.get("v_division"));
            websiteModel.setN_TOTAL_UNSOLD_UNITS((Long) result.get("n_total_unsold_units"));
            websiteModel.setN_NO_OF_MIG_UNITS((Long) result.get("n_no_of_mig_units"));
            websiteModel.setN_NO_OF_LIG_UNITS((Long) result.get("n_no_of_lig_units"));
            websiteModel.setN_NO_OF_EWS_UNITS((Long) result.get("n_no_of_ews_units"));
            websiteModel.setN_NO_OF_HIG_UNITS((Long) result.get("n_no_of_hig_units"));
            websiteModel.setN_SCHEME_ID((Long) result.get("n_scheme_id"));
            websiteModel.setN_ID((Long) result.get("n_id"));
            websiteModel.setV_UNIT_ALLOTTED_STATUS((String) result.get("v_unit_allotted_status"));
            websiteModel.setV_UNIT_TYPE((String) result.get("v_unit_type"));
            websiteModel.setF_PHOTO((String) result.get("f_photo"));
            websiteModels.add(websiteModel);
        }

        return websiteModels;
    }

    @Override
    public Map<String, Object> getSchemeDataBySchemeId(Long schemeId) {
        return websiteDataRepo.findSchemeDataBySchemeId(schemeId);
    }
    @Override
    public void deleteWebsiteData(Long id) {
        websiteDataRepo.deleteById(id);
    }

    //Enquiry
    public String sendMailToCustomer(Enquiry enquiry, String vPocEmail, String vEmail) {
        String infoMsg = "Success";

        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom("yazhinikuzhali98@gmail.com");
            helper.addTo(vPocEmail);
            helper.addTo(vEmail);
            helper.setSubject("TNHB- Property Request");
            String emailContent = "Hi Sir/Madam,<br>" +
                    "Hello " + enquiry.getVName() + ",<br>" +
                    "Your below enquiry has been successfully submitted.<br>" +
                    "Name: " + enquiry.getVName() + "<br>" +
                    "Phone No: " + enquiry.getNContactNo() + "<br>" +
                    "Email: " + enquiry.getVEmail() + "<br>" +
                    "Query: " + enquiry.getVMessage();

            helper.setText(emailContent, true);


            javaMailSender.send(msg);
        } catch (MessagingException e) {
            infoMsg = "Error sending email: " + e.getMessage();
            e.printStackTrace();
        }

        return infoMsg;
    }

    @Override
    public List<Residential>getResidential() {
        return residentialRepo.findAll();
    }

}

