import java.util.ArrayList;
import java.util.HashMap;

public class SimulationState {
    private HashMap<Event, Bus> BusMap;
    private ArrayList<Route> RouteList;
    private ArrayList<Stop> StopList;
    private ArrayList<Event> EventList;
    private ArrayList<Bus> BusList;

    public SimulationState(HashMap<Event, Bus> BMap, ArrayList<Route> RList, ArrayList<Stop> SList, ArrayList<Event> EList, ArrayList<Bus> BList) {
        this.BusMap = BMap;
        this.RouteList = RList;
        this.StopList = SList;
        this.EventList = EList;
        this.BusList = BList;
    }

    public SimulationState() {
        this(new HashMap<Event, Bus>(), new ArrayList<Route>(), new ArrayList<Stop>(), new ArrayList<Event>(), new ArrayList<Bus>());
    }

    public HashMap<Event, Bus> getBusMap() { return BusMap; }

    public ArrayList<Route> getRouteList() {
        return RouteList;
    }

    public ArrayList<Bus> getBusList() {
        return BusList;
    }

    public ArrayList<Stop> getStopList() {
        return StopList;
    }

    public ArrayList<Event> getEventList() {
        return EventList;
    }
}
