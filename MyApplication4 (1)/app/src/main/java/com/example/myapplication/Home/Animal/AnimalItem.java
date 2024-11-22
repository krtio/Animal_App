package com.example.myapplication.Home.Animal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "row")
public class AnimalItem {
    @Element(name = "SIGUN_CD", required = false)
    private String sigunCd;

    @Element(name = "SIGUN_NM", required = false)
    private String sigunNm;

    @Element(name = "ABDM_IDNTFY_NO", required = false)
    private String abdmIdntfyNo;

    @Element(name = "RECEPT_DE", required = false)
    private String receptDe;

    @Element(name = "DISCVRY_PLC_INFO", required = false)
    private String discvryPlcInfo;

    @Element(name = "STATE_NM", required = false)
    private String stateNm;

    @Element(name = "PBLANC_IDNTFY_NO", required = false)
    private String pblancIdntfyNo;

    @Element(name = "PBLANC_BEGIN_DE", required = false)
    private String pblancBeginDe;

    @Element(name = "PBLANC_END_DE", required = false)
    private String pblancEndDe;

    @Element(name = "SPECIES_NM", required = false)
    private String speciesNm;

    @Element(name = "COLOR_NM", required = false)
    private String colorNm;

    @Element(name = "AGE_INFO", required = false)
    private String ageInfo;

    @Element(name = "BDWGH_INFO", required = false)
    private String bdwghInfo;

    @Element(name = "SEX_NM", required = false)
    private String sexNm;

    @Element(name = "NEUT_YN", required = false)
    private String neutYn;

    @Element(name = "SFETR_INFO", required = false)
    private String sfetrInfo;

    @Element(name = "SHTER_NM", required = false)
    private String shterNm;

    @Element(name = "SHTER_TELNO", required = false)
    private String shterTelno;

    @Element(name = "PROTECT_PLC", required = false)
    private String protectPlc;

    @Element(name = "REFINE_ROADNM_ADDR", required = false)
    private String refineRoadnmAddr;

    @Element(name = "REFINE_LOTNO_ADDR", required = false)
    private String refineLotnoAddr;

    @Element(name = "REFINE_ZIP_CD", required = false)
    private String refineZipCd;

    @Element(name = "JURISD_INST_NM", required = false)
    private String jurisdInstNm;

    @Element(name = "CHRGPSN_NM", required = false)
    private String chrgpsnNm;

    @Element(name = "CHRGPSN_CONTCT_NO", required = false)
    private String chrgpsnContctNo;

    @Element(name = "PARTCLR_MATR", required = false)
    private String partclrMatr;

    @Element(name = "IMAGE_COURS", required = false)
    private String imageCours;

    @Element(name = "THUMB_IMAGE_COURS", required = false)
    private String thumbImageCours;

    @Element(name = "REFINE_WGS84_LAT", required = false)
    private String refineWgs84Lat;

    @Element(name = "REFINE_WGS84_LOGT", required = false)
    private String refineWgs84Logt;

    public String getSigunCd() { return sigunCd; }
    public String getSigunNm() { return sigunNm; }
    public String getAbdmIdntfyNo() { return abdmIdntfyNo; }
    public String getReceptDe() { return receptDe; }
    public String getDiscvryPlcInfo() { return discvryPlcInfo; }
    public String getStateNm() { return stateNm; }
    public String getPblancIdntfyNo() { return pblancIdntfyNo; }
    public String getPblancBeginDe() { return pblancBeginDe; }
    public String getPblancEndDe() { return pblancEndDe; }
    public String getSpeciesNm() { return speciesNm; }
    public String getColorNm() { return colorNm; }
    public String getAgeInfo() { return ageInfo; }
    public String getBdwghInfo() { return bdwghInfo; }
    public String getSexNm() { return sexNm; }
    public String getNeutYn() { return neutYn; }
    public String getSfetrInfo() { return sfetrInfo; }
    public String getShterNm() { return shterNm; }
    public String getShterTelno() { return shterTelno; }
    public String getProtectPlc() { return protectPlc; }
    public String getRefineRoadnmAddr() { return refineRoadnmAddr; }
    public String getRefineLotnoAddr() { return refineLotnoAddr; }
    public String getRefineZipCd() { return refineZipCd; }
    public String getJurisdInstNm() { return jurisdInstNm; }
    public String getChrgpsnNm() { return chrgpsnNm; }
    public String getChrgpsnContctNo() { return chrgpsnContctNo; }
    public String getPartclrMatr() { return partclrMatr; }
    public String getImageCours() { return imageCours; }
    public String getThumbImageCours() { return thumbImageCours; }
    public String getRefineWgs84Lat() { return refineWgs84Lat; }
    public String getRefineWgs84Logt() { return refineWgs84Logt; }
}

