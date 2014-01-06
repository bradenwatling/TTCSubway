package ttcsubway;

import java.util.ArrayList;

/**
 *
 * @author Braden
 */
public class Line {

    ArrayList<Station> stations = new ArrayList<>();
    String name;

    public Line(String name) {
        this.name = name;
    }

    public void addStation(Station station) {
        stations.add(station);
    }

    @Override
    public String toString() {
        String ret = "Line name : " + name + "\n";
        ret += "----------------------------\n";

        for (Station station : stations) {
            ret += station.toString();
            ret += "****************************\n";
        }

        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Line) {
            return this.name.equals(((Line) o).name);
        } else {
            return false;
        }
    }
}
