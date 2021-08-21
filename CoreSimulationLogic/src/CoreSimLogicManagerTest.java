import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public class CoreSimLogicManagerTest {

    private static final int TIMEOUT = 222222222;

    private Random rand;

    private ArrayList<Bus> busList;
    private ArrayList<Route> routeList;
    private ArrayList<Event> eventList;
    private ArrayList<Stop> stopList;
    private HashMap<Event, Bus> busMap;

    private SimulationState startSimulation;
    private SimulationManager manager;
    private CoreSimulationLogicManager simulation;


    @Before
    public void setUp() {
        rand = new Random();
        busList = new ArrayList<>();
        routeList = new ArrayList<>();
        eventList = new ArrayList<>();
        stopList = new ArrayList<>();
        busMap = new HashMap<>();
        System.out.println("1");
        for (int i = 0; i < 50; i++) {
            stopList.add(new Stop(i, "Stop " + i, rand.nextInt(50), (i / 10) * 100, (i % 10) * 100));
        }
        System.out.println("2");
        HashSet<Integer> set = new HashSet<>();
        int j = 0;
        ArrayList<ArrayList<Stop>> stops = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stops.add(new ArrayList<>());
            set = new HashSet<>();
            for (int k = 0; k < 10; k++) {
                j = rand.nextInt(50);
                while (set.contains(j)) {
                    j = rand.nextInt(50);
                }
                set.add(j);
                stops.get(i).add(stopList.get(j));
            }
            routeList.add(new Route(i, i, "Route " + i, stops.get(i)));
        }
        System.out.println("3");
        for (int i = 0; i < 10; i++) {
            busList.add(new Bus(i, routeList.get(i), 0, 0, 0, 0, 50, 100, 100, rand.nextInt(25) + 50));
        }
        System.out.println("4");
        for (int i = 0; i < 10; i++) {
            eventList.add(new Event(i, rand.nextInt(5000) + 500, EventType.move_bus));
        }
        System.out.println("5");
        for (int i = 0; i < 10; i++) {
            busMap.put(eventList.get(i), busList.get(i));
        }

        System.out.println("6");
        startSimulation = new SimulationState(busMap, routeList, stopList, eventList, busList);
        manager = new SimulationManager(startSimulation);
        simulation = new CoreSimulationLogicManager(manager);
    }

    @Test(timeout = TIMEOUT)
    public void testSimulationLogic() {
        assertTrue(true);
        for (int h = 0; h < 10000; h++) {
            for (int i = 0; i < 10; i++) {
                System.out.println(busList.get(i));
            }
            System.out.println();
            for (int i = 0; i < 10; i++) {
                System.out.println(eventList.get(i));
            }
            System.out.println();
            for (int i = 0; i < 10; i++) {
                System.out.println(routeList.get(i));
            }
            System.out.println();
            System.out.println();
            System.out.println();
            simulation.moveNextBus();
            System.out.println("This is the current ITTERATION " + h);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(busList.get(i));
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.println(eventList.get(i));
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.println(routeList.get(i));
        }

        for (int j = 0; j < 120; j++) {
            System.out.println();
            System.out.println();
            System.out.println();
            simulation.rewind();
            System.out.println("This is the current ITTERATION " + (100 - j));
            SimulationState curr = simulation.getSimulationManager().getCurrentState();
            if ((100 - j) >= 0) {
                for (int i = 0; i < 10; i++) {
                    System.out.println(curr.getBusList().get(i));
                }
                System.out.println();
                for (int i = 0; i < 10; i++) {
                    System.out.println(curr.getEventList().get(i));
                }
                System.out.println();
                for (int i = 0; i < 10; i++) {
                    System.out.println(routeList.get(i));
                }
            }
        }

        assertTrue(true);
        for (int h = 0; h < 10; h++) {
            for (int i = 0; i < 10; i++) {
                System.out.println(busList.get(i));
            }
            System.out.println();
            for (int i = 0; i < 10; i++) {
                System.out.println(eventList.get(i));
            }
            System.out.println();
            for (int i = 0; i < 10; i++) {
                System.out.println(routeList.get(i));
            }
            System.out.println();
            System.out.println();
            System.out.println();
            simulation.moveNextBus();
            System.out.println("This is the current ITTERATION " + h);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(busList.get(i));
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.println(eventList.get(i));
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.println(routeList.get(i));
        }
        System.out.println("\n\n\n\n\n\n\n\n");

        simulation.replay();

        SimulationState curr = simulation.getSimulationManager().getCurrentState();
            for (int i = 0; i < 10; i++) {
                System.out.println(curr.getBusList().get(i));
            }
            System.out.println();
            for (int i = 0; i < 10; i++) {
                System.out.println(curr.getEventList().get(i));
            }
            System.out.println();
            for (int i = 0; i < 10; i++) {
                System.out.println(routeList.get(i));
            }

        simulation.moveNextBus();

        assertTrue(true);
        System.out.println("Done");
    }
}
