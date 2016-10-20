package by.epamlab.beans;

import by.epamlab.enums.MovementDirection;
import by.epamlab.enums.TransportationState;
import by.epamlab.tasks.TransportationTask;

/**
 * @author Dzmitry Sudalenka
 *
 */
public class Passenger {
  private int id;
  private int startingStory;
  private int destinationStory;
  private TransportationState transportationState;
  private TransportationTask transportationTask;
  private MovementDirection movementDirection;
  /**
   * @param id id
   * @param startingStory startingStory
   * @param destinationStory destinationStory
   */
  public Passenger(final int id, final int startingStory, final int destinationStory) {
    this.id = id;
    this.startingStory = startingStory;
    this.destinationStory = destinationStory;
    this.transportationState = TransportationState.NOT_STARTED;
    transportationTask = new TransportationTask(this);
    if (startingStory < destinationStory) {
      movementDirection = MovementDirection.UP;
    } else {
      movementDirection = MovementDirection.DOWN;
    }
  }
  /**
   * @return return
   */
  public int getId() {
    return id;
  }
  /**
   * @param id id
   */
  public void setId(final int id) {
    this.id = id;
  }
  /**
   * @return return
   */
  public int getStartingStory() {
    return startingStory;
  }
  /**
   * @param startingStory startingStory
   */
  public void setStartingStory(final int startingStory) {
    this.startingStory = startingStory;
  }
  /**
   * @return return
   */
  public int getDestinationStory() {
    return destinationStory;
  }
  /**
   * @param destinationStory destinationStory
   */
  public void setDestinationStory(final int destinationStory) {
    this.destinationStory = destinationStory;
  }
  /**
   * @return return
   */
  public TransportationState getTransportationState() {
    return transportationState;
  }
  /**
   * @param transportationState transportationState
   */
  public void setTransportationState(final TransportationState transportationState) {
    this.transportationState = transportationState;
  }
  /**
   * @return return
   */
  public TransportationTask getTransportationTask() {
    return transportationTask;
  }
  /**
   * @param transportationTask transportationTask
   */
  public void setTransportationTask(final TransportationTask transportationTask) {
    this.transportationTask = transportationTask;
  }
  /**
   * @return return
   */
  public MovementDirection getMovementDirection() {
    return movementDirection;
  }
  /**
   * @param movementDirection movementDirection
   */
  public void setMovementDirection(final MovementDirection movementDirection) {
    this.movementDirection = movementDirection;
  }
}
