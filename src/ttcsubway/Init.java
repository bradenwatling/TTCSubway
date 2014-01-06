package ttcsubway;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

/**
 *
 * @author Braden
 */
public class Init {
    
    public static ArrayList<Line> lines = new ArrayList<>();
    
    public static void initLines() {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src\\ttcsubway\\TTC_Subway_Map.xml"));
            
            document.getDocumentElement().normalize();
            
            NodeList lineNodes = document.getElementsByTagName("line");
            
            for (int i = 0; i < lineNodes.getLength(); i++) {
                Node lineNode = lineNodes.item(i);
                if (lineNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element lineElement = (Element) lineNode;
                    Line curLine = new Line(lineElement.getAttribute("name"));
                    
                    //Get the stations
                    NodeList stationNodes = lineElement.getElementsByTagName("station");
                    
                    for (int j = 0; j < stationNodes.getLength(); j++) {
                        Element stationElement = (Element) stationNodes.item(j);
                        if (stationElement.getNodeType() == Node.ELEMENT_NODE) {
                            Station newStation = new Station(curLine, stationElement);
                            curLine.addStation(newStation);
                        }
                    }

                    Init.lines.add(curLine);
                    //System.out.println(curLine.toString());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        matchInterchangeStations();
    }
    
    public static void matchInterchangeStations() {
        for (Line currentLine : lines) {
            for (Station currentStation : currentLine.stations) { //For each station in the subway
                if (currentStation.isInterchange && currentStation.interchangeStation == null) { //If it's an interchange station and we haven't found it yet
                    for (Line line : Init.lines) { //Iterate over all other lines
                        //If we find a different line
                        if (!currentLine.equals(line)) {
                            for (Station station : line.stations) { //Check each station in this other line
                                if (station.equals(currentStation)) {
                                    //If we find a station with the same name as our requested station
                                    //Then give each of the interchange stations a reference to their counter-part
                                    station.interchangeStation = currentStation;
                                    currentStation.interchangeStation = station;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
