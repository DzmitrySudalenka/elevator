import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import by.epamlab.beans.Building;
import by.epamlab.beans.Passenger;
import by.epamlab.controllers.ElevatorController;

/**
 * @author Dzmitry Sudalenka
 *
 */
public final class Runner {
  private Runner() { }
  /**
   * @param args args
   */
  public static void main(final String[] args) {
    Building building = new Building();
    ElevatorController elevatorController = new ElevatorController(building);
    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.execute(elevatorController);
    for (Passenger passenger : building.getPassengers()) {
      executorService.execute(passenger.getTransportationTask());
    }
    executorService.shutdown();
  }
}
