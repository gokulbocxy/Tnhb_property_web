package com.bocxy.Property.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;
import java.util.List;

@Entity
@Data
@Table(name="scheme_data")
public class SchemeData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "N_ID")
    private Long N_ID;

    @Column(name="V_DIVISION")
    private String V_DIVISION;


    @Column(name="V_SCHEME_CODE")
    private String V_SCHEME_CODE;

    @Column(name="V_SCHEME_NAME")
    private String V_SCHEME_NAME;

    @Column(name="V_SCHEME_TYPE")
    private String V_SCHEME_TYPE;

    @Column(name="N_NO_OF_HIG_UNITS")
    private Long N_NO_OF_HIG_UNITS;

    @Column(name="N_NO_OF_MIG_UNITS")
    private Long N_NO_OF_MIG_UNITS;
    @Column(name="N_NO_OF_LIG_UNITS")
    private Long N_NO_OF_LIG_UNITS;
    @Column(name="N_NO_OF_EWS_UNITS")
    private Long N_NO_OF_EWS_UNITS;

    @Column(name="N_TOTAL_NO_OF_COMMERCIAL_UNITS")
    private Long N_TOTAL_NO_OF_COMMERCIAL_UNITS;
    @Column(name="N_TOTAL_NO_OF_RESIDENTIAL_UNITS")
    private Long N_TOTAL_NO_OF_RESIDENTIAL_UNITS;
    @Column(name="N_NO_OF_OUTRIGHT_UNITS")
    private Long N_NO_OF_OUTRIGHT_UNITS;
    @Column(name="N_NO_OF_HIREPURCHASE_UNITS")
    private Long N_NO_OF_HIREPURCHASE_UNITS;
    @Column(name="N_NO_OF_SFS_UNITS")
    private Long N_NO_OF_SFS_UNITS;

    @Column(name="V_CUT_OFF_DATE")
    private String V_CUT_OFF_DATE;

    @Column(name="N_RATE_OF_SCHEME_INTEREST")
    private Long N_RATE_OF_SCHEME_INTEREST;

    @Column(name="N_SELLING_PRICE")
    private Long N_SELLING_PRICE;

    @Column(name="N_TENTATIVE_LAND_COST")
    private Long N_TENTATIVE_LAND_COST;

    @Column(name="N_PROFIT_ON_LAND")
    private Long N_PROFIT_ON_LAND;

    @Column(name="V_FINAL_CUTOFF_DATE")
    private String V_FINAL_CUTOFF_DATE;

    @Column(name="V_REPAYMENT_PERIOD")
    private String V_REPAYMENT_PERIOD;

    @Column(name="V_SELLING_EXTENT")
    private String V_SELLING_EXTENT;

    @Column(name="N_FINAL_LAND_COST")
    private Long N_FINAL_LAND_COST;

    @Column(name="N_RATE_ADOPTED")
    private Long N_RATE_ADOPTED;

    @Formula("(N_NO_OF_HIG_UNITS+N_NO_OF_MIG_UNITS+N_NO_OF_LIG_UNITS+N_NO_OF_EWS_UNITS)")
    @Column(name="N_TOTAL_DEVELOPED_UNITS")
    private Long N_TOTAL_DEVELOPED_UNITS;

    @Column(name="N_TOTAL_ALLOTTED_UNITS_FOR_OUTRIGHT")
    private Long N_TOTAL_ALLOTTED_UNITS_FOR_OUTRIGHT;

    @Column(name="N_TOTAL_ALLOTTED_UNITS_FOR_COMMERCIAL")
    private Long N_TOTAL_ALLOTTED_UNITS_FOR_COMMERCIAL;

    @Column(name="N_TOTAL_ALLOTTED_UNITS_FOR_RESIDENTIAL")
    private Long N_TOTAL_ALLOTTED_UNITS_FOR_RESIDENTIAL;

    @Column(name="N_TOTAL_ALLOTTED_UNITS_FOR_HIG")
    private Long N_TOTAL_ALLOTTED_UNITS_FOR_HIG;

    @Column(name="N_TOTAL_ALLOTTED_UNITS_FOR_MIG")
    private Long N_TOTAL_ALLOTTED_UNITS_FOR_MIG;

    @Column(name="N_TOTAL_ALLOTTED_UNITS_FOR_LIG")
    private Long N_TOTAL_ALLOTTED_UNITS_FOR_LIG;

    @Column(name="N_TOTAL_ALLOTTED_UNITS_FOR_EWS")
    private Long N_TOTAL_ALLOTTED_UNITS_FOR_EWS;

    @Column(name="N_TOTAL_ALLOTTED_UNITS_FOR_SFS")
    private Long N_TOTAL_ALLOTTED_UNITS_FOR_SFS;

    @Column(name="N_TOTAL_ARREARS_EMI")
    private Long N_TOTAL_ARREARS_EMI;

    @Column(name="N_TOTAL_CURRENT_EMI")
    private Long N_TOTAL_CURRENT_EMI;

    @Column(name="N_TOTAL_LIVE_CASES_FOR_HIRE")
    private Long N_TOTAL_LIVE_CASES_FOR_HIRE;

    @Column(name="N_TOTAL_RIPPED_UNIT")
    private Long N_TOTAL_RIPPED_UNIT;

    @Column(name="N_TOTAL_ALLOTTED_UNITS")
    private Long N_TOTAL_ALLOTTED_UNITS;

    @Column(name="N_TOTAL_ALLOTTED_UNITS_FOR_HIRE_PURCHASE")
    private Long N_TOTAL_ALLOTTED_UNITS_FOR_HIRE_PURCHASE;

    @Column(name="N_TOTAL_BALANCE_EMI")
    private Long N_TOTAL_BALANCE_EMI;

    @Column(name="N_TOTAL_NO_OF_SALE_DEED_ISSUED")
    private Long N_TOTAL_NO_OF_SALE_DEED_ISSUED;

    @Column(name="N_TOTAL_NO_OF_PAID_CASES")
    private Long N_TOTAL_NO_OF_PAID_CASES;

    @Column(name="N_TOTAL_UNSOLD_UNITS")
    private Long N_TOTAL_UNSOLD_UNITS;

    public void updateTotalDevelopedUnits(){
        this.N_TOTAL_DEVELOPED_UNITS = (N_NO_OF_HIG_UNITS + N_NO_OF_MIG_UNITS + N_NO_OF_LIG_UNITS + N_NO_OF_EWS_UNITS);
    }

    @Transient
    private String mode;

    @Transient
    private List<Allottee> AllotteeList;

    @Transient
    private List<UnitData> unitData;



    @Column(name="V_SNO")
    private String sNo;

    @Column(name="D_FROM_DATE")
    private String fromDate;

    @Column(name="D_TO_DATE")
    private String toDate;

    @Column(name="V_RESERVATION_STATUS")
    private String reservationStatus;

    @Column(name="V_PROJECT_STATUS")
    private String projectStatus;

    @Column(name="V_CR_CODE")
    private String crCode;

    @Column(name="V_CC_CODE")
    private String ccCode;

    @Column(name="V_D_CODE")
    private String dCode;

    @Column(name="V_SCHTYPE_CODE")
    private String sTypeCode;

    @Column(name="V_SNAME")
    private String sName;

    @Column(name="V_SPLACE")
    private String sSpace;

    @Column(name="V_DISTRICT")
    private String district;

    @Column(name="V_UNIT_TYPE")
    private String unitType;

    @Column(name="V_TYPE")
    private String type;

    @Column(name="V_TOTAL_UNIT")
    private String totalUnit;

    @Column(name="V_ALLOTTED_UNIT")
    private String allottedUnit;

    @Column(name="V_UNSOLD_UNITS")
    private String unsoldUnits;
}
