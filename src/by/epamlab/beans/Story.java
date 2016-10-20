package by.epamlab.beans;

import java.util.Collection;

import by.epamlab.containers.Container;

/**
 * @author Dzmitry Sudalenka
 *
 */
public class Story {
  private int number;
  private Container dispatchStoryContainer;
  private Container arrivalStoryContainer;
  /**
   * @param number number
   */
  public Story(final int number) {
    this.number = number;
    this.dispatchStoryContainer = new Container();
    this.arrivalStoryContainer = new Container();
  }
  /**
   * @return return
   */
  public int getNumber() {
    return number;
  }
  /**
   * @param number number
   */
  public void setNumber(final int number) {
    this.number = number;
  }
  /**
   * @return return
   */
  public Container getDispatchStoryContainer() {
    return dispatchStoryContainer;
  }
  /**
   * @param dispatchStoryContainer dispatchStoryContainer
   */
  public void setDispatchStoryContainer(final Container dispatchStoryContainer) {
    this.dispatchStoryContainer = dispatchStoryContainer;
  }
  /**
   * @return return
   */
  public Container getArrivalStoryContainer() {
    return arrivalStoryContainer;
  }
  /**
   * @param arrivalStoryContainer arrivalStoryContainer
   */
  public void setArrivalStoryContainer(final Container arrivalStoryContainer) {
    this.arrivalStoryContainer = arrivalStoryContainer;
  }
  /**
   * @return return
   */
  public int getArrivalPassengersNumber() {
    return arrivalStoryContainer.getPassengersNumber();
  }
  /**
   * @param passenger passenger
   */
  public void addDispatchPassenger(final Passenger passenger) {
    this.dispatchStoryContainer.addPassenger(passenger);
  }
  /**
   * @param passenger passenger
   */
  public void addArrivalPassenger(final Passenger passenger) {
    this.arrivalStoryContainer.addPassenger(passenger);
  }
  /**
   * @return return
   */
  public int getDispatchPassengersNumber() {
    return dispatchStoryContainer.getPassengersNumber();
  }
  /**
   * @param passengerId passengerId
   */
  public void removeDispatchPassenger(final int passengerId) {
    this.dispatchStoryContainer.removePassenger(passengerId);
  }
  /**
   * @return return
   */
  public Collection<Passenger> getDispathPassengers() {
    return this.dispatchStoryContainer.getPassengers();
  }
  /**
   * @return return
   */
  public Collection<Passenger> getArrivalPassengers() {
    return this.arrivalStoryContainer.getPassengers();
  }
}
