package by.epamlab.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epamlab.Constants;
import by.epamlab.beans.Elevator;
import by.epamlab.beans.Passenger;
import by.epamlab.beans.Story;
import by.epamlab.controllers.ElevatorController;
import by.epamlab.enums.ElevatorControllerActions;
import by.epamlab.enums.MovementDirection;
import by.epamlab.enums.TransportationState;

/**
 * @author Dzmitry Sudalenka
 *
 */
public class TransportationTask implements Runnable {
  private static final Logger LOGGER =
      LogManager.getLogger(TransportationTask.class);
  private final Passenger passenger;
  /**
   * @param passenger passenger
   */
  public TransportationTask(final Passenger passenger) {
    this.passenger = passenger;
  }
  /**
   * @return return
   */
  public Passenger getPassenger() {
    return passenger;
  }
  @Override
  public void run() {
    ElevatorController.reduceCountDownLatch();
    passenger.setTransportationState(TransportationState.IN_PROGRESS);
    while (passenger.getTransportationState() != TransportationState.COMPLETED) {
      waitForNotification();
      ElevatorControllerActions elevatorAction = ElevatorController.getAction();
      switch (elevatorAction) {
        case DEBOADING_OF_PASSENGER :
          deboading();
          break;
        case BOADING_OF_PASSENGER :
          boading();
          break;
        default:
          break;
      }
    }
  }
  private void waitForNotification() {
    int passengerId = passenger.getId();
    Elevator elevator = ElevatorController.getElevator();
    if (elevator.hasPassenger(passengerId)) {
      synchronized (elevator) {
        try {
          elevator.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    } else {
      int passengerStartingStoryNumber = passenger.getStartingStory();
      Story startingStory = ElevatorController.getStory(passengerStartingStoryNumber);
      synchronized (startingStory) {
        try {
          startingStory.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
  private void deboading() {
    int currentStoryNumber = ElevatorController.getCurrentStoryNumber();
    int passengerId = passenger.getId();
    if (passenger.getDestinationStory() == currentStoryNumber) {
      LOGGER.info(ElevatorController.getAction() + Constants.STR_BRACE_OPEN
          + passengerId + Constants.STR_ON + currentStoryNumber
          + Constants.STR_BRACE_CLOSE);
      ElevatorController.removePassengerFromElevator(passengerId);
      ElevatorController.addArrivalPassenger(passenger);
      passenger.setTransportationState(TransportationState.COMPLETED);
      ElevatorController.reduceCountDownLatch();
    }
  }
  private void boading() {
    MovementDirection passengerMovementDirection = passenger.getMovementDirection();
    MovementDirection elevatorMovementDirection =
        ElevatorController.getMovementDirection();
    if (passengerMovementDirection == elevatorMovementDirection) {
      int currentStoryNumber = ElevatorController.getCurrentStoryNumber();
      int passengerId = passenger.getId();
      LOGGER.info(ElevatorController.getAction() + Constants.STR_BRACE_OPEN
          + passengerId + Constants.STR_ON + currentStoryNumber
          + Constants.STR_BRACE_CLOSE);
      ElevatorController.addPassengerToElevator(passenger);
      Elevator elevator = ElevatorController.getElevator();
      if (elevator.hasPassenger(passengerId)) {
        ElevatorController.removePassengerFromStory(passengerId);
      }
      ElevatorController.reduceCountDownLatch();
    }
  }
}
