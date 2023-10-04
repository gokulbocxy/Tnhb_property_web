package com.bocxy.Property.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="WebsiteData")
public class WebsiteData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "N_ID")
    private Long N_ID;

    @Column(name="nSchemeId")
    private Long nSchemeId;

//    @Column(name="fPhoto")
//    private List<String> fPhoto;

    @Column(name = "fPhoto")
    private String[] fPhoto;

    @Column(name="fVideo")
    private String fVideo;

    @Column(name="fFloorPlanPicture")
    private String fFloorPlanPicture;

    @Column(name="fFloorPlanPdf")
    private String fFloorPlanPdf;

    @Column(name="vGeoTagLink")
    private String vGeoTagLink;

    @Column(name="vProjectDescription")
    private String vProjectDescription;

    @Column(name="vAmenities")
    private String vAmenities;

    @Column(name="fPocPicture")
    private String fPocPicture;

    @Column(name="vPocName")
    private String vPocName;

    @Column(name="vPocMobile")
    private String vPocMobile;

    @Column(name="vPocEmail")
    private String vPocEmail;
    public void setFPhotoPaths(String[] fPhotoPath) {
        this.fPhoto = fPhotoPath;
    }

    public String[] getFPhoto() {
        return fPhoto;
    }

    public void setFPhoto(String[] fPhoto) {
        this.fPhoto = fPhoto;
    }

}
