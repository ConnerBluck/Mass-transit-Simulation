import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class SimulationManager {
    private Stack<SimulationState> previousStates;
    private SimulationState initialState;
    private SimulationState currentState;
    private SimulationState nextState;

    public SimulationManager(SimulationState currentState) {
        this.nextState = currentState;
        this.initialState = copyState(currentState);
        this.currentState = copyState(currentState);
        this.previousStates = new Stack<SimulationState>();
    }

    public SimulationState copyState(SimulationState currentState) {
        HashMap<Event, Bus> BMap = new HashMap<>();
        ArrayList<Route> RouteList = currentState.getRouteList();
        ArrayList<Stop> StopList = currentState.getStopList();
        ArrayList<Event> EList = new ArrayList<>();
        ArrayList<Bus> BList = new ArrayList<>();

        for (int i = 0; i < currentState.getBusList().size(); i++) {
            Bus bus = currentState.getBusList().get(i);
            BList.add(new Bus(bus.getId(), bus.getRoute(), bus.getCurrentStopIndex(), 0, 0, bus.getPassengers(),
                    bus.getPassengerCapacity(), bus.getFuel(), bus.getFuelCapacity(), bus.getSpeed()));
        }

        for (int i = 0; i < currentState.getEventList().size(); i++) {
            Event event = currentState.getEventList().get(i);
            EList.add(new Event(event.getId(), event.getTime(), event.getType()));
        }

        for (int i = 0; i < BList.size(); i++) {
            BMap.put(EList.get(i), BList.get(i));
        }

        return new SimulationState(BMap, RouteList, StopList, EList, BList);
    }

    public SimulationManager() {
        this(new SimulationState());
    }

    public SimulationState getInitialState() {
        return initialState;
    }

    public SimulationState getCurrentState() {
        return currentState;
    }

    public SimulationState getNextState() {
        return nextState;
    }

    public Stack<SimulationState> getPreviousStates() {
        return previousStates;
    }

    public void emptyPreviousStates() { previousStates = new Stack<SimulationState>(); }

    public void setCurrentState(SimulationState state) {
        this.currentState = state;
    }

    public void setNextState(SimulationState state) {
        this.nextState = state;
    }
}
