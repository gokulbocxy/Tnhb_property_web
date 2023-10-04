package com.bocxy.Property.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "UnitData")
public class UnitData{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "N_ID")
    private Long N_ID;

    @Column(name = "N_SCHEME_ID")
    private Long N_SCHEME_ID;

    @Column(name = "V_TYPE_NAME")
    private String V_TYPE_NAME;

    @Column(name = "V_UNIT_ID")
    private String V_UNIT_ID;

    @Column(name = "V_ASSET_CATEGORY")
    private String V_ASSET_CATEGORY;

    @Column(name = "VAssetSubCategory")
    private String VAssetSubCategory;

    @Column(name = "V_ASSET_TYPE")
    private String V_ASSET_TYPE;

    @Column(name = "V_UNIT_NO")
    private String V_UNIT_NO;

    @Column(name = "V_BLOCK_NO")
    private String V_BLOCK_NO;

    @Column(name = "V_FLOOR_NO")
    private String V_FLOOR_NO;

    @Column(name = "V_PLINTH_AREA")
    private String V_PLINTH_AREA;

    @Column(name = "V_UDS_AREA")
    private String V_UDS_AREA;

    @Column(name = "V_PLOT_AREA")
    private String V_PLOT_AREA;

    @Column(name = "V_CARPET_AREA")
    private String V_CARPET_AREA;

    @Column(name = "V_ROAD_FACING")
    private String V_ROAD_FACING;

    @Column(name = "V_CORNER_PLOT_STATUS")
    private String V_CORNER_PLOT_STATUS;

    @Column(name = "V_GOVT_DISCRETION_QUOTA")
    private String V_GOVT_DISCRETION_QUOTA;

    @Column(name = "V_UNIT_ALLOTTED_STATUS")
    private String V_UNIT_ALLOTTED_STATUS;

    @Column(name = "V_ALLOTMENT_TYPE")
    private String V_ALLOTMENT_TYPE;

    @Column(name = "V_CATEGORY")
    private String V_CATEGORY;

    @Transient
    private String mode;

}
