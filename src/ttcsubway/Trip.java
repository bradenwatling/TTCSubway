package ttcsubway;

import java.util.ArrayList;

/**
 *
 * @author Braden
 */
public class Trip {

    Station start, destination;
    ArrayList<ArrayList<Station>> allPaths = new ArrayList<>();
    ArrayList<Station> path;
    boolean requiresTransfer, isAccessible, needAccessible;

    public Trip(Station start, Station destination, boolean needAccessible) {
        this.start = start;
        this.destination = destination;
        this.needAccessible = needAccessible;

        if (start != null && destination != null) {
            this.requiresTransfer = destination.requiresTransfer;
            this.isAccessible = start.isAccessible && destination.isAccessible;
            
            path = findBestPath(start, destination, needAccessible);
        }
    }

    private ArrayList<Station> findBestPath(Station start, Station target, boolean needAccessible) {
        recursiveSearch(null, start, target, true, true);

        ArrayList<Station> ret = null;
        int retSize = Integer.MAX_VALUE;

        for (ArrayList<Station> potentialPath : allPaths) {
            //If you dont care about accessibility, find the shortest path
            //If you do, then only consider paths that are accessible
            if (!needAccessible || checkAccessible(potentialPath)) {
                int pathSize = potentialPath.size();
                boolean spadinaInterchange = usesSpadinaInterchange(potentialPath);
                if (spadinaInterchange) {
                    //We are going through the Spadina interchange, so penalize this path
                    pathSize++;
                }

                if (ret == null || pathSize < retSize) {
                    //If there's a shorter path, then choose it
                    ret = potentialPath;
                    retSize = pathSize;
                }
            }
        }

        return ret;
    }

    private boolean checkAccessible(ArrayList<Station> path) {
        if (path.isEmpty()) {
            //They sent us an empty path
            return false;
        } else {
            Station lastStation = null;
            //If we find a transfer station that is not accessible then we can't suggest this route
            for (Station station : path) {
                //If we're going through an interchange and one of the stations in that interchange is not accessible
                if (station.isInterchange && station.equals(lastStation) && (!station.isAccessible || !lastStation.isAccessible)) {
                    //Then this path is not accessible
                    return false;
                }
                
                //Record the last station
                lastStation = station;
            }

            //If it passed all of these tests, then the path is accessible!
            return true;
        }
    }

    private void recursiveSearch(ArrayList<Station> currentPath, Station current, Station target, boolean searchLeft, boolean searchRight) {
        //Initialize a new path if it's not yet initialized
        if (currentPath == null) {
            currentPath = new ArrayList<>();
        }

        Line currentLine = current.line;
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
            allPaths.add(currentPath);
        } else {
            //If we're at an interchange, and we haven't been to a station with the same name yet
            if (current.isInterchange && !currentPath.contains(current)) {
                //If we are allowed to go through the interchange (we aren't if we are coming from a recursive interchange call)
                Station interchangeStation = current.interchangeStation;

                //Only pursue the interchange if we haven't already been there
                if (interchangeStation != null) {
                    ArrayList<Station> interchangePath = (ArrayList<Station>) currentPath.clone();
                    interchangePath.add(current);
                    //Recursively search the other line, but don't let the search come back onto this line
                    recursiveSearch(interchangePath, interchangeStation, target, true, true);
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
                    recursiveSearch(currentPath, stations.get(currentIndex + 1), target, false, true);
                }
                if (searchLeft) {
                    //If we're allowed to, search to the left
                    recursiveSearch(branchPath, stations.get(currentIndex - 1), target, true, false);
                }
            }
        }
    }

    private boolean usesSpadinaInterchange(ArrayList<Station> path) {
        Station spadinaStation = Init.lines.get(1).stations.get(8);

        int spadinaCount = 0;
        for (Station station : path) {
            if (station.equals(spadinaStation)) {
                spadinaCount++;
            }
        }

        return spadinaCount >= 2;
    }

    @Override
    public String toString() {
        String ret = "";

        if (path != null) {
            if (needAccessible && !isAccessible) {
                ret += "Either the start or destination station is not accessible:\n";
            }
            for (Station station : path) {
                ret += station.line.name + ": " + station.name;
                
                if (needAccessible && station.isAccessible) {
                    ret += " - accessible";
                }
                
                ret += "\n";
            }
            if (requiresTransfer) {
                ret += "You will need a transfer.\n";
            }
        } else if (start != null && destination != null) {
            ret += "Count not find a";
            if (needAccessible) {
                ret += "n accessible";
            }
            ret += " route between " + start.name + " and " + destination.name + ".\n";
        } else {
            ret += "An error occurred.\n";
        }

        return ret;
    }
}
