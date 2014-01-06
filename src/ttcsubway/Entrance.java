package ttcsubway;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Braden
 */
public class Entrance {

    String name;
    boolean primaryDirection;

    class PlatformElement {

        double location;//(car number).(door number)
        boolean usable;

        public PlatformElement(double location, boolean usable) {
            this.location = location;
            this.usable = usable;
        }

        public PlatformElement(double location) {
            this(location, true);
        }
    }
    
    ArrayList<PlatformElement> elevators = new ArrayList<>();
    ArrayList<PlatformElement> escalators = new ArrayList<>();
    ArrayList<PlatformElement> stairs = new ArrayList<>();

    public Entrance(String name, boolean primaryDirection, Element entranceElement) {
        this.name = name;
        this.primaryDirection = primaryDirection;

        NodeList elevatorNodes = entranceElement.getElementsByTagName("elevator");
        NodeList escalatorNodes = entranceElement.getElementsByTagName("escalator");
        NodeList stairNodes = entranceElement.getElementsByTagName("stairs");

        for (int i = 0; i < elevatorNodes.getLength(); i++) {
            Element elevatorElement = (Element) elevatorNodes.item(i);
            if (elevatorElement.getNodeType() == Node.ELEMENT_NODE) {
                String positionString = elevatorElement.getTextContent().trim();

                if (positionString != null) {
                    double position = Double.parseDouble(positionString);
                    if (!primaryDirection) {
                        //Find out the opposite car.door by subtracting from 7.5
                        position = 7.5 - position;
                    }
                    PlatformElement elevator = new PlatformElement(position);

                    elevators.add(elevator);
                }
            }
        }

        for (int i = 0; i < escalatorNodes.getLength(); i++) {
            Element escalatorElement = (Element) escalatorNodes.item(i);
            if (escalatorElement.getNodeType() == Node.ELEMENT_NODE) {
                boolean usable = escalatorElement.getAttribute("usable").equals("TRUE");
                String positionString = escalatorElement.getTextContent().trim();

                if (positionString != null) {
                    double position = Double.parseDouble(positionString);
                    if (!primaryDirection) {
                        //Find out the opposite car.door by subtracting from 7.5
                        position = 7.5 - position;
                    }
                    PlatformElement escalator = new PlatformElement(position, usable);

                    escalators.add(escalator);
                }
            }
        }

        for (int i = 0; i < stairNodes.getLength(); i++) {
            Element stairElement = (Element) stairNodes.item(i);
            if (stairElement.getNodeType() == Node.ELEMENT_NODE) {
                String positionString = stairElement.getTextContent().trim();

                if (positionString != null) {
                    double position = Double.parseDouble(positionString);
                    if (!primaryDirection) {
                        //Find out the opposite car.door by subtracting from 7.5
                        position = 7.5 - position;
                    }
                    PlatformElement stair = new PlatformElement(position);

                    stairs.add(stair);
                }
            }
        }
    }
}
