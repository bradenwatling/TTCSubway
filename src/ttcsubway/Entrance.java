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

    public Entrance(String name, Element entranceElement) {
        this.name = name;

        NodeList elevatorNodes = entranceElement.getElementsByTagName("elevator");
        NodeList escalatorNodes = entranceElement.getElementsByTagName("escalator");
        NodeList stairNodes = entranceElement.getElementsByTagName("stairs");

        for (int i = 0; i < elevatorNodes.getLength(); i++) {
            Element elevatorElement = (Element) elevatorNodes.item(i);
            if (elevatorElement.getNodeType() == Node.ELEMENT_NODE) {
                String position = elevatorElement.getTextContent().trim();

                if (position != null) {
                    PlatformElement elevator = new PlatformElement(Double.parseDouble(position));

                    elevators.add(elevator);
                }
            }
        }

        for (int i = 0; i < escalatorNodes.getLength(); i++) {
            Element escalatorElement = (Element) escalatorNodes.item(i);
            if (escalatorElement.getNodeType() == Node.ELEMENT_NODE) {
                boolean usable = escalatorElement.getAttribute("usable").equals("TRUE");
                String position = escalatorElement.getTextContent().trim();

                if (position != null) {
                    PlatformElement escalator = new PlatformElement(Double.parseDouble(position), usable);

                    escalators.add(escalator);
                }
            }
        }

        for (int i = 0; i < stairNodes.getLength(); i++) {
            Element stairElement = (Element) stairNodes.item(i);
            if (stairElement.getNodeType() == Node.ELEMENT_NODE) {
                String position = stairElement.getTextContent().trim();

                if (position != null) {
                    PlatformElement stair = new PlatformElement(Double.parseDouble(position));

                    stairs.add(stair);
                }
            }
        }
    }
}
