package ttcsubway;

import java.util.Scanner;

/**
 *
 * @author Braden
 */
public class TTCSubway {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Init.initLines();

        Scanner scanner = new Scanner(System.in);

        Station start = null, end = null;
        while (start == null) {
            System.out.println("Enter the name of your starting station:");
            start = searchForStation(scanner.nextLine());
            if (start == null) {
                System.out.println("Couldn't find a station by that name.");
            }
        }

        while (end == null) {
            System.out.println("Enter the name of your destination station:");
            end = searchForStation(scanner.nextLine());
            if (end == null) {
                System.out.println("Couldn't find a station by that name.");
            }
        }
        
        System.out.println("Do you need and accessible route? (y/n)");
        boolean needAccessible = scanner.nextLine().toLowerCase().equals("y");

        Trip newTrip = new Trip(start, end, needAccessible);
        
        System.out.println(newTrip);
    }

    public static Station searchForStation(String stationName) {
        for (Line line : Init.lines) {
            for (Station station : line.stations) {
                if (station.name.toLowerCase().equals(stationName.toLowerCase())) {
                    return station;
                }
            }
        }

        return null;
    }
}
