package by.epamlab.containers;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import by.epamlab.beans.Passenger;

/**
 * @author Dzmitry Sudalenka
 *
 */
public class Container {
  private ConcurrentHashMap<Integer, Passenger> passengers;
  /**
   *
   */
  public Container() {
    passengers = new ConcurrentHashMap<Integer, Passenger>();
  }
  /**
   * @param passenger passenger
   */
  public void addPassenger(final Passenger passenger) {
    int passengerId = passenger.getId();
    passengers.put(passengerId, passenger);
  }
  /**
   * @param passengerId passengerId
   */
  public void removePassenger(final int passengerId) {
    passengers.remove(passengerId);
  }
  /**
   * @return return
   */
  public int getPassengersNumber() {
    return passengers.size();
  }
  /**
   * @return return
   */
  public Collection<Passenger> getPassengers() {
    return passengers.values();
  }
  /**
   * @param passengerId passengerId
   * @return return
   */
  public boolean hasPassenger(final int passengerId) {
    return passengers.containsKey(passengerId);
  }
}
