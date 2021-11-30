package com.prologicwebsolution.microatm.aeps_utils;

import android.util.Log;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SplitXML {
    public SplitXML() {
    }

    public DeviceInfo SplitDeviceInfo(String strDeviceInfo) {
        DeviceInfo deviceInfo = null;

        try {
            try {
                if (strDeviceInfo != null && !strDeviceInfo.isEmpty() && !strDeviceInfo.startsWith("<?xml")) {
                    strDeviceInfo = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + strDeviceInfo;
                }

                DocumentBuilderFactory dbBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = dbBuilderFactory.newDocumentBuilder();
                InputSource inputSource = new InputSource(new StringReader(strDeviceInfo));
                Document document = documentBuilder.parse(inputSource);
                deviceInfo = new DeviceInfo();
                document.getDocumentElement().normalize();
                Node nodedeviceInfo = document.getElementsByTagName("DeviceInfo").item(0);
                Element deviceInfoElement = (Element) nodedeviceInfo;
                deviceInfo.dpId = deviceInfoElement.getAttribute("dpId");
                deviceInfo.rdsId = deviceInfoElement.getAttribute("rdsId");
                deviceInfo.rdsVer = deviceInfoElement.getAttribute("rdsVer");
                deviceInfo.dc = deviceInfoElement.getAttribute("dc");
                deviceInfo.mi = deviceInfoElement.getAttribute("mi");
                deviceInfo.mc = deviceInfoElement.getAttribute("mc");
            } catch (Exception var12) {
                deviceInfo = null;
            }

            return deviceInfo;
        } finally {
            ;
        }
    }

    public RDServiceInfo SplitRDServiceInfo(String strRDServiceInfo) {
        RDServiceInfo rdServiceInfo = null;

        try {
            try {
                if (strRDServiceInfo != null && !strRDServiceInfo.isEmpty() && !strRDServiceInfo.startsWith("<?xml")) {
                    strRDServiceInfo = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + strRDServiceInfo;
                }

                DocumentBuilderFactory dbBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = dbBuilderFactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(strRDServiceInfo));
                Document document = documentBuilder.parse(is);
                rdServiceInfo = new RDServiceInfo();
                document.getDocumentElement().normalize();
                Node noderdServiceInfo = document.getElementsByTagName("RDService").item(0);
                Element rdServiceInfoElement = (Element) noderdServiceInfo;
                rdServiceInfo.status = rdServiceInfoElement.getAttribute("status");
                rdServiceInfo.info = rdServiceInfoElement.getAttribute("info");
                NodeList noderdServiceInterfaceList = document.getElementsByTagName("Interface");

                for (int i = 0; i < noderdServiceInterfaceList.getLength(); ++i) {
                    Element interfaceElement = (Element) noderdServiceInterfaceList.item(i);
                    if (interfaceElement.getAttribute("id").equalsIgnoreCase("CAPTURE")) {
                        rdServiceInfo.CapturePath = interfaceElement.getAttribute("path");
                        Log.d("Test", rdServiceInfo.CapturePath);
                    } else if (interfaceElement.getAttribute("id").equalsIgnoreCase("DEVICEINFO")) {
                        rdServiceInfo.DeviceInfopath = interfaceElement.getAttribute("path");
                        Log.d("Test", rdServiceInfo.DeviceInfopath);
                    }
                }
            } catch (Exception var15) {
                rdServiceInfo = null;
                var15.printStackTrace();
            }

            return rdServiceInfo;
        } finally {
            ;
        }
    }

    public CaptureResponse SplitCaptureResponse(String strcaptureRespone) {
        CaptureResponse captureResponse = null;
        try {
            captureResponse = new CaptureResponse();
            if (strcaptureRespone != null && !strcaptureRespone.isEmpty() && !strcaptureRespone.startsWith("<?xml")) {
                strcaptureRespone = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + strcaptureRespone;
            }

            DocumentBuilderFactory dbBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbBuilderFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(strcaptureRespone));
            Document document = documentBuilder.parse(is);
            document.getDocumentElement().normalize();
            Element elementResp = (Element) document.getElementsByTagName("Resp").item(0);
            captureResponse.errCode = elementResp.getAttribute("errCode");
            captureResponse.errInfo = elementResp.getAttribute("errInfo");
            captureResponse.fCount = elementResp.getAttribute("fCount");
            captureResponse.fType = elementResp.getAttribute("fType");
            captureResponse.iCount = elementResp.getAttribute("iCount");
            captureResponse.pCount = elementResp.getAttribute("pCount");
            captureResponse.pType = elementResp.getAttribute("pType");
            if (captureResponse.errCode.equalsIgnoreCase("0")) {
                captureResponse.nmPoints = elementResp.getAttribute("nmPoints");
                captureResponse.qScore = elementResp.getAttribute("qScore");
                Element elementsKey = (Element) document.getElementsByTagName("Skey").item(0);
                captureResponse.ci = elementsKey.getAttribute("ci");
                captureResponse.sessionKey = elementsKey.getTextContent();
                Element elementHmac = (Element) document.getElementsByTagName("Hmac").item(0);
                captureResponse.hmac = elementHmac.getTextContent();
                Element elementData = (Element) document.getElementsByTagName("Data").item(0);
                captureResponse.PidDatatype = elementData.getAttribute("type");
                captureResponse.Piddata = elementData.getTextContent();
            }
        } catch (Exception var14) {
            captureResponse = null;
        }
        return captureResponse;
    }


    public AepsCaptureResponseModel SplitAepsCaptureResponseModel(String strcaptureRespone) {
        AepsCaptureResponseModel captureResponse = null;
        try {
            captureResponse = new AepsCaptureResponseModel();
            if (strcaptureRespone != null && !strcaptureRespone.isEmpty() && !strcaptureRespone.startsWith("<?xml")) {
                strcaptureRespone = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + strcaptureRespone;
            }
            DocumentBuilderFactory dbBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbBuilderFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(strcaptureRespone));
            Document document = documentBuilder.parse(is);
            document.getDocumentElement().normalize();
            Element elementResp = (Element) document.getElementsByTagName("Resp").item(0);
            captureResponse.errCode = elementResp.getAttribute("errCode");
            captureResponse.errInfo = elementResp.getAttribute("errInfo");
            captureResponse.fCount = elementResp.getAttribute("fCount");
            captureResponse.fType = elementResp.getAttribute("fType");
            captureResponse.iCount = elementResp.getAttribute("iCount");
            captureResponse.iType = elementResp.getAttribute("iType");
            captureResponse.pCount = elementResp.getAttribute("pCount");
            captureResponse.pType = elementResp.getAttribute("pType");
            if (captureResponse.errCode.equalsIgnoreCase("0")) {
                captureResponse.nmPoints = elementResp.getAttribute("nmPoints");
                captureResponse.qScore = elementResp.getAttribute("qScore");
                //deviceInfo
                Element elementsDeviceInfo = (Element) document.getElementsByTagName("DeviceInfo").item(0);
                captureResponse.dpID = elementsDeviceInfo.getAttribute("dpId");
                captureResponse.rdsID = elementsDeviceInfo.getAttribute("rdsId");
                captureResponse.rdsVer = elementsDeviceInfo.getAttribute("rdsVer");
                captureResponse.dc = elementsDeviceInfo.getAttribute("dc");
                captureResponse.mi = elementsDeviceInfo.getAttribute("mi");
                captureResponse.mc = elementsDeviceInfo.getAttribute("mc");
                Element elementsAdditionInfo = (Element) elementsDeviceInfo.getElementsByTagName("additional_info").item(0);
                NodeList nList = elementsAdditionInfo.getElementsByTagName("Param");
                captureResponse.additional_info = new JSONObject();
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node node = nList.item(temp);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        captureResponse.additional_info.put(eElement.getAttribute("name"), eElement.getAttribute("value"));
                    }
                }
                Element elementsKey = (Element) document.getElementsByTagName("Skey").item(0);
                captureResponse.ci = elementsKey.getAttribute("ci");
                captureResponse.sessionKey = elementsKey.getTextContent();
                Element elementHmac = (Element) document.getElementsByTagName("Hmac").item(0);
                captureResponse.hmac = elementHmac.getTextContent();
                Element elementData = (Element) document.getElementsByTagName("Data").item(0);
                captureResponse.pidDatatype = elementData.getAttribute("type");
                captureResponse.piddata = elementData.getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
            captureResponse = null;
        }
        return captureResponse;

    }
}