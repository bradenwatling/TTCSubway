package ttcsubway;

import java.util.ArrayList;
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

        if (start != null && end != null) {
            ArrayList<Station> path = findBestPath(start, end);

            if (path != null) {
                for (Station station : path) {
                    System.out.println(station.toString());
                }
            } else {
                System.out.println("Couldn't find a path between " + start.name + " and " + end.name);
            }
        }
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

    public static ArrayList<Station> findBestPath(Station start, Station target) {
        ArrayList<ArrayList<Station>> paths = new ArrayList<>();
        recursiveSearch(paths, null, start, target, true, true, true);

        ArrayList<Station> ret = null;
        int retSize = Integer.MAX_VALUE;

        for (ArrayList<Station> path : paths) {
            int pathSize = path.size();
            if (usesSpadinaInterchange(path)) {
                //We are going through the Spadina interchange, so penalize this path
                pathSize++;
            }

            if (ret == null || pathSize < retSize) {
                //If there's a shorter path, then choose it
                ret = path;
                retSize = pathSize;
            }
        }

        return ret;
    }

    public static boolean usesSpadinaInterchange(ArrayList<Station> path) {
        Station spadinaStation = Init.lines.get(1).stations.get(8);

        int spadinaCount = 0;
        for (Station station : path) {
            if (station.equals(spadinaStation)) {
                spadinaCount++;
            }
        }

        return spadinaCount >= 2;
    }

    public static void recursiveSearch(ArrayList<ArrayList<Station>> ret, ArrayList<Station> currentPath, Station current, Station target, boolean searchLeft, boolean searchRight, boolean searchInterchange) {
        //Initialize a new path if it's not yet initialized
        if (currentPath == null) {
            currentPath = new ArrayList<>();
        }

        Line currentLine = current.currentLine;
        ArrayList<Station> stations = currentLine.stations;
        int currentIndex = stations.indexOf(current);

        //If we're at the end of the line, then we can't search right
        if (currentIndex == stations.size() - 1) {
            searchRight = false;
        }
        //If we're at the beginning of the line, then we can't search left
        if (currentIndex == 0) {
            searchLeft = false;
        }

        if (current.equals(target)) {
            currentPath.add(current);
            ret.add(currentPath);
        } else {
            if (searchInterchange && current.isInterchange) {
                //If we are allowed to go through the interchange (we aren't if we are coming from a recursive interchange call)
                Station interchangeStation = current.interchangeStation;

                //Only pursue the interchange if it found the matching Line and if we haven't already been there
                if (interchangeStation != null && !currentPath.contains(interchangeStation)) {
                    ArrayList<Station> interchangePath = (ArrayList<Station>) currentPath.clone();
                    interchangePath.add(current);
                    //Recursively search the other line, but don't let the search come back onto this line
                    recursiveSearch(ret, interchangePath, interchangeStation, target, true, true, false);
                }
            }

            if ((!searchLeft && currentIndex >= stations.size() - 1) || (!searchRight && currentIndex <= 0)) {
                //You can't look left and we're at the last index
                //You can't look right and we're at the first index
                //If we reach the end of the line in the current direction
            } else {
                currentPath.add(current);
                ArrayList<Station> branchPath = (ArrayList<Station>) currentPath.clone();

                if (searchRight) {
                    //If we're allowed to, search to the right
                    recursiveSearch(ret, currentPath, stations.get(currentIndex + 1), target, false, true, true);
                }
                if (searchLeft) {
                    //If we're allowed to, search to the left
                    recursiveSearch(ret, branchPath, stations.get(currentIndex - 1), target, true, false, true);
                }
            }
        }
    }
}
