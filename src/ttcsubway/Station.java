package ttcsubway;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Braden
 */
public class Station {
    
    Line line;
    Station interchangeStation;
    String name;
    int ridership;
    boolean isInterchange, centrePlatform;
    String direction;
    boolean isAccessible, requiresTransfer;
    
    ArrayList<Entrance> entrances = new ArrayList<>();
    
    /*String primaryStairs, secondaryStairs;
    String primaryEscalator, secondaryEscalator;
    String primaryElevator, secondaryElevator;*/

    public Station(Line currentLine, Element stationElement) {//, String name, int ridership, boolean isInterchange, boolean centrePlatform, String direction, boolean isAccessible, boolean requiresTransfer) {
        this.line = currentLine;
        this.interchangeStation = null;
        
        this.name = stationElement.getAttribute("name");
        this.ridership = Integer.parseInt(stationElement.getAttribute("ridership"));
        this.isInterchange = stationElement.getAttribute("interchange").equals("TRUE");
        this.centrePlatform = stationElement.getAttribute("centre").equals("TRUE");
        this.direction = stationElement.getAttribute("direction");
        this.isAccessible = stationElement.getAttribute("accessible").equals("TRUE");
        this.requiresTransfer = stationElement.getAttribute("transfer").equals("TRUE");
        
        NodeList entranceNodes = stationElement.getElementsByTagName("entrance");
        for (int i = 0; i < entranceNodes.getLength(); i++) {
            Element entranceElement = (Element) entranceNodes.item(i);
            String entranceName = entranceElement.getAttribute("name");
            String platformDirection = entranceElement.getAttribute("platform");
            
            if (platformDirection.equals(direction) || platformDirection.equals("B")) {                
                entrances.add(new Entrance(entranceName, true, entranceElement));
            }
            if (!platformDirection.equals(direction) || platformDirection.equals("B")) {
                entrances.add(new Entrance(entranceName, false, entranceElement));
            }
        }
    }
    
    @Override
    public String toString() {
        String ret = "Name : " + name + "\n";
        ret += "Ridership : " + ridership + "\n";
        ret += "Centre Platform : " + centrePlatform + "\n";
        ret += "Direction : " + direction + "\n";
        ret += "Accessible : " + isAccessible + "\n";
        ret += "Requires Transfer : " + requiresTransfer + "\n";
        
        return ret;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Station) {
            return name.equals(((Station) o).name);
        } else {
            return false;
        }
    }
}
