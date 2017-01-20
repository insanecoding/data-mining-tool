package com.me.core.service.reporting;

import com.me.core.domain.dto.ConfusionTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


class PerformanceFileParser {
    private static List<List<String>> getSubNodes(Document doc, String outerTag, String innerTags) {
        NodeList nList = doc.getElementsByTagName(outerTag);
        List<List<String>> result = new LinkedList<>();

        for (int temp = 0; temp < nList.getLength(); temp++) {
            List<String> column = new LinkedList<>();
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                NodeList nodes = eElement.getElementsByTagName(innerTags);
                for (int count = 0; count < nodes.getLength(); count++) {
                    Node node1 = nodes.item(count);
                    if (node1.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node1;
                        column.add(element.getTextContent());
                    }
                }
            }
            result.add(column);
        }
        return result;
    }

    static ConfusionTable parsePerformanceXML(String performanceFile)
            throws ParserConfigurationException, IOException, SAXException {
        List<List<Double>> doubleMatrix = new LinkedList<>();
        List<String> categories;

        File inputFile = new File(performanceFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        List<List<String>> temp = getSubNodes(doc, "classNames", "string");
        categories = temp.get(0);

        List<List<String>> columns = getSubNodes(doc, "double-array", "double");

        for (List<String> column : columns) {
            List<Double> doubleColumn =
                    column.stream().map(Double::parseDouble)
                            .collect(Collectors.toCollection(LinkedList::new));
            doubleMatrix.add(doubleColumn);
        }

        return new ConfusionTable(categories, doubleMatrix);
    }
}
