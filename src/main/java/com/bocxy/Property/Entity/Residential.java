package com.bocxy.Property.Entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "residential")
public class Residential {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="N_ID")
    private Long id;

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
