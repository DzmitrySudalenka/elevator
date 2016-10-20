package by.epamlab.beans;

import java.util.Collection;

import by.epamlab.Constants;
import by.epamlab.containers.Container;

/**
 * @author Dzmitry Sudalenka
 *
 */
public class Elevator {
  private int currentStoryNumber;
  private Container evelvatorContainer;
  /**
   * @param elevatorCapacity elevatorCapacity
   */
  public Elevator(final int elevatorCapacity) {
    this.currentStoryNumber = Constants.NUM_STORY_FIRST;
    evelvatorContainer = new Container();
  }
  /**
   * @return return
   */
  public int getCurrentStoryNumber() {
    return currentStoryNumber;
  }
  /**
   * @param currentStoryNumber currentStoryNumber
   */
  public void setCurrentStoryNumber(final int currentStoryNumber) {
    this.currentStoryNumber = currentStoryNumber;
  }
  /**
   * @return return
   */
  public Container getEvelvatorContainer() {
    return evelvatorContainer;
  }
  /**
   * @param evelvatorContainer evelvatorContainer
   */
  public void setEvelvatorContainer(final Container evelvatorContainer) {
    this.evelvatorContainer = evelvatorContainer;
  }
  /**
   *
   */
  public void moveUp() {
    currentStoryNumber++;
  }
  /**
   *
   */
  public void moveDown() {
    currentStoryNumber--;
  }
  /**
   * @return return
   */
  public int getPassengersNumber() {
    return evelvatorContainer.getPassengersNumber();
  }
  /**
   * @param passenger passenger
   */
  public void addPassenger(final Passenger passenger) {
    if (getCurrentCapacity() > 0) {
      evelvatorContainer.addPassenger(passenger);
    }
  }
  /**
   * @param passengerId passengerId
   */
  public void removePassenger(final int passengerId) {
    evelvatorContainer.removePassenger(passengerId);
  }
  /**
   * @return return
   */
  public int getCapacity() {
    return Building.ELEVATOR_CAPACITY;
  }
  /**
   * @return return
   */
  public int getCurrentCapacity() {
    return Building.ELEVATOR_CAPACITY - evelvatorContainer.getPassengersNumber();
  }
  /**
   * @return return
   */
  public Collection<Passenger> getPassengers() {
    return evelvatorContainer.getPassengers();
  }
  /**
   * @return return
   */
  public boolean isEmpty() {
    if (evelvatorContainer.getPassengersNumber() > 0) {
      return false;
    }
    return true;
  }
  /**
   * @param passengerId passengerId
   * @return return
   */
  public boolean hasPassenger(final int passengerId) {
    return evelvatorContainer.hasPassenger(passengerId);
  }
}
