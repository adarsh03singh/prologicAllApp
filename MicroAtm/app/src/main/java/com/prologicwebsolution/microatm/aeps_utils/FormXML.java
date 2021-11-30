package com.prologicwebsolution.microatm.aeps_utils;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class FormXML {
    public static String PIDOptionversion = "1.0";
    public static String env = "PP";
    public static String fCount = "1";
    public static String fType = "0";
    public static String iCount = "";
    public static String iType = "";
    public static String pType = "";
    public static String pCount = "";
    public static String format = "";
    public static String pidVer = "2.0";
    public static String timeout = "";
    public static String otp = "";
    public static String wadh = "";
    public static String posh = "";

    public FormXML() {
    }

    public String FormAuthenticationXML(String strAadhaarNumber, CaptureResponse captureResponse, DeviceInfo deviceInfo) {
        String authXML = null;
        try {
            try {
                StringWriter stringWriter = new StringWriter();
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.newDocument();
                document.setXmlStandalone(true);
                Element rootElement = document.createElement("PBAuthInfo");
                document.appendChild(rootElement);
                rootElement.setAttribute("uid", strAadhaarNumber);
                Element usesElement = document.createElement("Uses");
                rootElement.appendChild(usesElement);
                usesElement.setAttribute("pi", "n");
                usesElement.setAttribute("pa", "n");
                usesElement.setAttribute("pfa", "n");
                usesElement.setAttribute("bio", "y");
                usesElement.setAttribute("bt", "FMR");
                usesElement.setAttribute("pin", "n");
                usesElement.setAttribute("otp", "n");
                Element metaElement = document.createElement("Meta");
                rootElement.appendChild(metaElement);
                String udc = deviceInfo.dc.substring(0, 19);
                metaElement.setAttribute("udc", udc);
                metaElement.setAttribute("rdsId", deviceInfo.rdsId);
                metaElement.setAttribute("rdsVer", deviceInfo.rdsVer);
                metaElement.setAttribute("dpId", deviceInfo.dpId);
                metaElement.setAttribute("dc", deviceInfo.dc);
                metaElement.setAttribute("mi", deviceInfo.mi);
                metaElement.setAttribute("mc", deviceInfo.mc);
                Element skeyElement = document.createElement("Skey");
                rootElement.appendChild(skeyElement);
                skeyElement.setAttribute("ci", captureResponse.ci);
                skeyElement.setTextContent(captureResponse.sessionKey);
                Element dataElement = document.createElement("Data");
                rootElement.appendChild(dataElement);
                dataElement.setAttribute("type", "X");
                dataElement.setTextContent(captureResponse.Piddata);
                Element hmacElement = document.createElement("Hmac");
                rootElement.appendChild(hmacElement);
                hmacElement.setTextContent(captureResponse.hmac);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.setOutputProperty("indent", "yes");
                transformer.setOutputProperty("standalone", "yes");
                transformer.setOutputProperty("encoding", "utf-8");
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(stringWriter);
                transformer.transform(source, result);
                authXML = stringWriter.toString();
            } catch (Exception var23) {
                authXML = null;
            }

            return authXML;
        } finally {
            ;
        }
    }

    public String formCaptureRequestXML() {
        String captureRequestXML = null;

        try {
            StringWriter writer = new StringWriter();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            Element rootElement = doc.createElement("PidOptions");
            doc.appendChild(rootElement);
            Attr ver = doc.createAttribute("ver");
            ver.setValue(PIDOptionversion);
            rootElement.setAttributeNode(ver);
            Element optsElement = doc.createElement("Opts");
            rootElement.appendChild(optsElement);
            optsElement.setAttribute("env", env);
            optsElement.setAttribute("fCount", fCount);
            optsElement.setAttribute("fType", fType);
            optsElement.setAttribute("iCount", iCount);
            optsElement.setAttribute("iType", iType);
            optsElement.setAttribute("pCount", pCount);
            optsElement.setAttribute("pType", pType);
            optsElement.setAttribute("format", format);
            optsElement.setAttribute("pidVer", pidVer);
            optsElement.setAttribute("timeout", timeout);
            optsElement.setAttribute("otp", otp);
            optsElement.setAttribute("wadh", wadh);
            optsElement.setAttribute("posh", posh);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            captureRequestXML = writer.toString();
            System.out.println("Capture XML:" + captureRequestXML);
        } catch (Exception var16) {
            captureRequestXML = null;
        }


        return captureRequestXML;

    }
}