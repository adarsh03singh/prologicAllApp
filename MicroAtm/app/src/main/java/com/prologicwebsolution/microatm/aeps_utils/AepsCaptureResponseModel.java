package com.prologicwebsolution.microatm.aeps_utils;


import org.json.JSONObject;

public class AepsCaptureResponseModel {

    public String errCode; //done
    public String errInfo;//done
    public String fCount;//done
    public String fType;//done
    public String iCount;//done
    public String iType;//done
    public String pCount;//done
    public String pType;//done
    public String nmPoints;//done
    public String qScore;//done
    public String dpID; //done
    public String rdsID; //done
    public String rdsVer;//
    public String dc; //
    public String mi; //done
    public String mc; //done
    public String ci; //doone
    public String sessionKey; //done
    public String hmac; //done
    public String pidDatatype;
    public String piddata;
    public JSONObject additional_info = new JSONObject();


    public AepsCaptureResponseModel() {
    }




    @Override
    public String toString() {
        return "AepsCaptureResponseModel{" +
                "errCode='" + errCode + '\'' +
                ", errInfo='" + errInfo + '\'' +
                ", fCount='" + fCount + '\'' +
                ", fType='" + fType + '\'' +
                ", iCount='" + iCount + '\'' +
                ", iType='" + iType + '\'' +
                ", pCount='" + pCount + '\'' +
                ", pType='" + pType + '\'' +
                ", nmPoints='" + nmPoints + '\'' +
                ", qScore='" + qScore + '\'' +
                ", dpID='" + dpID + '\'' +
                ", rdsID='" + rdsID + '\'' +
                ", rdsVer='" + rdsVer + '\'' +
                ", dc='" + dc + '\'' +
                ", mi='" + mi + '\'' +
                ", mc='" + mc + '\'' +
                ", ci='" + ci + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                ", hmac='" + hmac + '\'' +
                ", pidDatatype='" + pidDatatype + '\'' +
                ", piddata='" + piddata + '\'' +
                ", additional_info=" + additional_info +
                '}';
    }

    public JSONObject getJSON(String aadharno) {
        JSONObject jsonObject = new JSONObject();
        try {
            additional_info.put("aadharno;",  aadharno);
            additional_info.put("Locking;", "N");
            additional_info.put("errCode", "" + errCode);
            additional_info.put("errInfo", "" + errInfo);
            additional_info.put("fCount", "" + fCount);
            additional_info.put("fType", "" + fType);
            additional_info.put("nmPoints", "" + nmPoints);
            additional_info.put("qScore", "" + qScore);
            additional_info.put("dpID", "" + dpID);
            additional_info.put("rdsID", "" + rdsID);
            additional_info.put("rdsVer", "" + rdsVer);
            additional_info.put("dc", "" + dc);
            additional_info.put("mi", "" + mi);
            additional_info.put("mc", "" + mc);
            additional_info.put("iCount", "" + iCount);
            additional_info.put("iType", "" + iType);
            additional_info.put("pCount", "" + pCount);
            additional_info.put("pType", "" + pType);
            additional_info.put("ci", "" + ci);
            additional_info.put("sessionKey", "" + sessionKey);
            additional_info.put("hmac", "" + hmac);
            additional_info.put("pidDatatype", "" + pidDatatype);
            additional_info.put("piddata", "" + piddata);
            return additional_info;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

}