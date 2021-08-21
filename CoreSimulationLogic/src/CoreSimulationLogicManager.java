import java.util.*;

public class CoreSimulationLogicManager {
    SimulationManager simulationManager;
    PriorityQueue<Event> eventQueue;
    Random rand;

    /**
     * add a new simulation manager
     * priority queue is initialized and the events are added into a priority queue
     * @param simulationManager
     */
    public CoreSimulationLogicManager(SimulationManager simulationManager) {
        rand = new Random();
        this.simulationManager = simulationManager;
        eventQueue = new PriorityQueue<Event>();
        //Fill the priority queue
        ArrayList<Event> eventListCurrent = simulationManager.getCurrentState().getEventList();

        for (int i = 0; i < eventListCurrent.size(); i++) {
            eventQueue.add(eventListCurrent.get(i));
        }
    }

    public CoreSimulationLogicManager() {
        this(new SimulationManager());
    }

    public SimulationManager getSimulationManager() { return simulationManager; }

    /**
     * Moves the Bus to the next state
     * Updates the next state and makes it the current state
     * Puts current state into stack of previous states.
     */
    public void moveNextBus() {
        //Adds the current state to the stack
        simulationManager.getPreviousStates().add(simulationManager.getCurrentState());

        //Gets the Events and Buses of the current state
        ArrayList<Event> eventList = simulationManager.getNextState().getEventList();
        HashMap<Event, Bus> busMap = simulationManager.getNextState().getBusMap();

        //Gets the bus with the least amount of time for the next event
        updateEvents(eventList, busMap);

        simulationManager.setCurrentState(simulationManager.copyState(simulationManager.getNextState()));
    }

    /**
     * Drop off and passengers and pick up passengers at stop
     * @param currentStop
     * @param currentBus
     * @return
     */
    private void passengerExchanges(Stop currentStop, Bus currentBus) {
        double prob = rand.nextDouble();
        int busPass = (int) (prob * currentBus.getPassengers());

        prob = rand.nextDouble();
        int stopPass = (int) (prob * currentStop.getRiders());

        currentBus.setPassengers(currentBus.getPassengers() - busPass);

        if ((stopPass + currentBus.getPassengers()) >= currentBus.getPassengerCapacity()) {
            stopPass = currentBus.getPassengerCapacity() - currentBus.getPassengers();
        }


        currentStop.setRiders(currentStop.getRiders() + busPass - stopPass);
        currentBus.setPassengers(currentBus.getPassengers() + stopPass);
    }

    /**
     * gets the distance calculated between two stops
     * @param currentStop
     * @param nextStop
     * @return
     */
    private double getDistance(Stop currentStop, Stop nextStop) {
        double diffY = nextStop.getLatitude() - currentStop.getLatitude();
        double diffX = nextStop.getLongitude() - currentStop.getLongitude();
        double dist = 70.0 * Math.sqrt((Math.pow(diffX, 2) + Math.pow(diffY, 2)));
        return dist;
    }

    /**
     * Update the bus stop position and the time to the next stop as well as the passengers
     * @param event
     * @param bus
     */
    private void updateBus(Event event, Bus bus, int timeSub) {
        Route route = bus.getRoute();
        Stop currentStop;
        Stop nextStop;

        if (bus.getCurrentStopIndex() == route.getStops().size() - 2) {
            bus.setCurrentStopIndex(bus.getCurrentStopIndex() + 1);
            nextStop = route.getStops().get(0);
        } else if (bus.getCurrentStopIndex() == route.getStops().size() - 1) {
            bus.setCurrentStopIndex(0);
            nextStop = route.getStops().get(1);
        } else {
            bus.setCurrentStopIndex(bus.getCurrentStopIndex() + 1);

            nextStop = route.getStops().get(bus.getCurrentStopIndex() + 1);
        }

        currentStop = route.getStops().get(bus.getCurrentStopIndex());

        double distance = getDistance(currentStop, nextStop);

        event.setTime(1 + (int)(distance * 5 / bus.getSpeed()));

        passengerExchanges(currentStop, bus);
    }

    /**
     * Updates the time of events
     * @param eventList
     * @return
     * @throws RuntimeException
     */
    private void updateEvents(ArrayList<Event> eventList, HashMap<Event, Bus> busMap) throws RuntimeException {
        reset();
        Event minTime = eventQueue.poll();
        int timeSub = minTime.getTime();

        for (int i = 0; i < eventList.size(); i++) {
            //Updates the current time of the event time
            eventList.get(i).setTime(eventList.get(i).getTime() - timeSub);
            //Checks if the bus has reached the next stop and then update event time
            if (eventList.get(i).getTime() < 0) {
                throw new RuntimeException("Negative time. Min Time is " + timeSub + " and had ID " + minTime.getId() + ". And the current ID is " + i);
            } else if (eventList.get(i).getTime() == 0) {
                updateBus(eventList.get(i), busMap.get(eventList.get(i)), timeSub);
            }
        }
    }

    public void reset() {
        eventQueue.clear();
        ArrayList<Event> eventList = simulationManager.getNextState().getEventList();
        for (int i = 0; i < eventList.size(); i++) {
            eventQueue.add(eventList.get(i));
        }
    }

    /**
     * Goes back a state in the simulation
     */
    public boolean rewind() {
        if (simulationManager.getPreviousStates().empty()) {
            System.out.println("Couldn't Rewind, Stack is Empty");
            return false;
        }

        simulationManager.setCurrentState(simulationManager.getPreviousStates().pop());
        simulationManager.setNextState(simulationManager.copyState(simulationManager.getCurrentState()));
        return true;
    }

    /**
     * Start the simulation from the beginning of the simulation
     */
    public void replay() {
        simulationManager.setCurrentState(simulationManager.getInitialState());
        simulationManager.setNextState(simulationManager.getInitialState());
        simulationManager.emptyPreviousStates();
    }

    public void extendRoute(Bus bus, Stop stop) {
        Route route = bus.getRoute();

        List<Stop> stopList = route.getStops();
        stopList.add(stop);
    }

    public void extendRoute(Route route, Stop stop) {
        List<Stop> stopList = route.getStops();
        stopList.add(stop);
    }

    public void changeSpeed(Bus bus, int speed) {
        bus.setSpeed(speed);
    }

    public void changePassengerCapacity(Bus bus, int capacity) {
        bus.setPassengerCapacity(capacity);
    }
}
