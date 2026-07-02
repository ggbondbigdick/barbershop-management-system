package com.barber.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * ClassName: ParseXMLTool
 * Description: This class is used to parse XML files.
 */
public class ParseXMLTool {

    /**
     * MethodName: parseXMLData
     * Description: This method is used to parse XML data and extract the specified content.
     * @param xmlData: The XML data to be parsed.
     * @param resultMethod: The method to extract the content.
     * @return: A JSONObject containing the extracted content.
     */
    public static JSONObject parseXMLData(String xmlData, String resultMethod) {

        JSONObject resultObject = new JSONObject();
        try {
            // Parse the XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new java.io.ByteArrayInputStream(xmlData.getBytes("UTF-8")));

            // Extract the content
            NodeList nodeList = document.getElementsByTagName(resultMethod);
            if (nodeList.getLength() > 0) {
                String jsonContent = nodeList.item(0).getTextContent();
                // Step 2: Parse the JSON content using FastJSON
                resultObject = JSON.parseObject(jsonContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultObject;
    }
}
