package com.prologicwebsolution.microatm.aeps_utils;

public class CaptureResponse {
    public String errCode;
    public String errInfo;
    public String fCount;
    public String fType;
    public String iCount;
    public String iType;
    public String pCount;
    public String pType;
    public String nmPoints;
    public String qScore;
    public String dpID;
    public String rdsID;
    public String rdsVer;
    public String dc;
    public String mi;
    public String mc;
    public String ci;
    public String sessionKey;
    public String hmac;
    public String PidDatatype;
    public String Piddata;

    public CaptureResponse() {
    }


    public String toString() {
        return "CaptureResponse{errCode='" + this.errCode + '\'' + ", errInfo='" + this.errInfo + '\'' + ", fCount='" + this.fCount + '\'' + ", fType='" + this.fType + '\'' + ", iCount='" + this.iCount + '\'' + ", iType='" + this.iType + '\'' + ", pCount='" + this.pCount + '\'' + ", pType='" + this.pType + '\'' + ", nmPoints='" + this.nmPoints + '\'' + ", qScore='" + this.qScore + '\'' + ", dpID='" + this.dpID + '\'' + ", rdsID='" + this.rdsID + '\'' + ", rdsVer='" + this.rdsVer + '\'' + ", dc='" + this.dc + '\'' + ", mi='" + this.mi + '\'' + ", mc='" + this.mc + '\'' + ", ci='" + this.ci + '\'' + ", sessionKey='" + this.sessionKey + '\'' + ", hmac='" + this.hmac + '\'' + ", pidDatatype='" + this.PidDatatype + '\'' + ", piddata='" + this.Piddata + '\'' + '}';
    }


}