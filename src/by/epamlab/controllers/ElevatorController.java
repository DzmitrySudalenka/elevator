package by.epamlab.controllers;

import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epamlab.Constants;
import by.epamlab.beans.Building;
import by.epamlab.beans.Elevator;
import by.epamlab.beans.Passenger;
import by.epamlab.beans.Story;
import by.epamlab.enums.ElevatorControllerActions;
import by.epamlab.enums.MovementDirection;
import by.epamlab.utils.Validator;

/**
 * @author Dzmitry Sudalenka
 *
 */
public class ElevatorController implements Runnable {
  private static final Logger LOGGER =
      LogManager.getLogger(ElevatorController.class);
  private static Building building;
  private static Elevator elevator;
  private static ElevatorControllerActions action;
  private static MovementDirection movementDirection;
  private static CountDownLatch countDownLatch;
  /**
   * @param building building
   */
  public ElevatorController(final Building building) {
    ElevatorController.building = building;
    elevator = building.getElevator();
    movementDirection = MovementDirection.UP;
    countDownLatch = new CountDownLatch(Building.PASSENGERS_NUMBER);
  }
  /**
   * @return return
   */
  public static Building getBuilding() {
    return building;
  }
  /**
   * @param building building
   */
  public static void setBuilding(final Building building) {
    ElevatorController.building = building;
  }
  /**
   * @return return
   */
  public static Elevator getElevator() {
    return elevator;
  }
  /**
   * @param elevator elevator
   */
  public static void setElevator(final Elevator elevator) {
    building.setElevator(elevator);
    ElevatorController.elevator = elevator;
  }
  /**
   * @return return
   */
  public static int getCurrentStoryNumber() {
    return elevator.getCurrentStoryNumber();
  }
  /**
   * @return return
   */
  public static ElevatorControllerActions getAction() {
    return action;
  }
  /**
   * @param action action
   */
  public static void setAction(final ElevatorControllerActions action) {
    ElevatorController.action = action;
  }
  /**
   * @return return
   */
  public static MovementDirection getMovementDirection() {
    return movementDirection;
  }
  /**
   * @param movementDirection movementDirection
   */
  public static void setMovementDirection(
      final MovementDirection movementDirection) {
    ElevatorController.movementDirection = movementDirection;
  }
  /**
   * @return return
   */
  public static CountDownLatch getCountDownLatch() {
    return countDownLatch;
  }
  /**
   * @param countDownLatch countDownLatch
   */
  public static void setCountDownLatch(
      final CountDownLatch countDownLatch) {
    ElevatorController.countDownLatch = countDownLatch;
  }
  /**
   *
   */
  public static void reduceCountDownLatch() {
    ElevatorController.countDownLatch.countDown();
  }
  /**
   * @param number number
   * @return return
   */
  public static Story getStory(final int number) {
    return building.getStory(number);
  }
  @Override
  public void run() {
    waitPassengers();
    action = ElevatorControllerActions.STARTING_TRANSPORTATION;
    LOGGER.info(action);
    while (!isTransportationFinished()) {
      informPassengersCanDeboading();
      informPassengersCanBoading();
      if (isTransportationFinished()) {
        break;
      }
      moveElevator();
    }
  }
  /**
   *
   */
  public void moveElevator() {
    action = ElevatorControllerActions.MOVING_ELEVATOR;
    LOGGER.info(action + Constants.STR_FROM + getCurrentStoryNumber()
      + Constants.STR_TO + getNextStoryNumber() + Constants.STR_BRACE_CLOSE);
    switch (movementDirection) {
      case UP :
        elevator.moveUp();
        break;
      case DOWN :
        elevator.moveDown();
        break;
      default :
        throw new EnumConstantNotPresentException(MovementDirection.class,
            movementDirection.name());
    }
    if (getCurrentStoryNumber() >= Building.STORIES_NUMBER) {
      movementDirection = MovementDirection.DOWN;
    }
    if (getCurrentStoryNumber() <= Constants.NUM_STORY_FIRST) {
      movementDirection = MovementDirection.UP;
    }
  }
  /**
   *
   */
  public void waitPassengers() {
    try {
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  /**
   *
   */
  public void informPassengersCanDeboading() {
    int deboadingPassengersNumber = getDeboadingPassengersNumber();
    if (deboadingPassengersNumber > 0) {
      action = ElevatorControllerActions.DEBOADING_OF_PASSENGER;
      countDownLatch = new CountDownLatch(deboadingPassengersNumber);
      synchronized (elevator) {
        elevator.notifyAll();
      }
      waitPassengers();
    }
  }
  /**
   *
   */
  public void informPassengersCanBoading() {
    int boadingPassengersNumber = getBoadingPassengersNumber();
    if (boadingPassengersNumber > 0) {
      action = ElevatorControllerActions.BOADING_OF_PASSENGER;
      countDownLatch = new CountDownLatch(boadingPassengersNumber);
      int currentStoryNumber = getCurrentStoryNumber();
      Story currentStory = building.getStory(currentStoryNumber);
      synchronized (currentStory) {
        currentStory.notifyAll();
      }
      waitPassengers();
    }
  }
  /**
   * @return return
   */
  public int getDeboadingPassengersNumber() {
    int deboadingPassengersNumber = 0;
    int currentStoryNumber = getCurrentStoryNumber();
    for (Passenger passenger : elevator.getPassengers()) {
      int destinationStory = passenger.getDestinationStory();
      if (destinationStory == currentStoryNumber) {
        deboadingPassengersNumber++;
      }
    }
    return deboadingPassengersNumber;
  }
  private int getBoadingPassengersNumber() {
    int currentStoryNumber = getCurrentStoryNumber();
    Story currentStory = building.getStory(currentStoryNumber);
    int boadingPassengersNumber = 0;
    for (Passenger passenger : currentStory.getDispathPassengers()) {
      MovementDirection passengerMovementDirection = passenger.getMovementDirection();
      if (passengerMovementDirection == movementDirection) {
        boadingPassengersNumber++;
      }
    }
    return boadingPassengersNumber;
  }
  /**
   * @param passengerId passengerId
   */
  public static void removePassengerFromElevator(final int passengerId) {
    elevator.removePassenger(passengerId);
  }
  /**
   * @param passengerId passengerId
   */
  public static void removePassengerFromStory(final int passengerId) {
    int currentStoryNumber = getCurrentStoryNumber();
    Story currentStory = building.getStory(currentStoryNumber);
    currentStory.removeDispatchPassenger(passengerId);
  }
  /**
   * @param passenger passenger
   */
  public static void addPassengerToElevator(final Passenger passenger) {
    if (elevator.getCurrentCapacity() > 0) {
      elevator.addPassenger(passenger);
    }
  }
  /**
   * @param passenger passenger
   */
  public static void addArrivalPassenger(final Passenger passenger) {
    int currentStoryNumber = getCurrentStoryNumber();
    Story currentStory = building.getStory(currentStoryNumber);
    currentStory.addArrivalPassenger(passenger);
  }
  /**
   * @return return
   */
  public int getNextStoryNumber() {
    int nextStoryNumber = getCurrentStoryNumber();
    switch (movementDirection) {
      case UP :
        nextStoryNumber++;
        break;
      case DOWN :
        nextStoryNumber--;
        break;
      default :
        throw new EnumConstantNotPresentException(MovementDirection.class,
            movementDirection.name());
    }
    return nextStoryNumber;
  }
  /**
   * @return return
   */
  public boolean isTransportationFinished() {
    if (!elevator.isEmpty()) {
      return false;
    }
    for (Story story : building.getStories()) {
      int storyDispatchPassengersNumber = story.getDispatchPassengersNumber();
      if (storyDispatchPassengersNumber > 0) {
        return false;
      }
    }
    action = ElevatorControllerActions.COMPLETION_TRANSPORTATION;
    LOGGER.info(action);
    Validator.validate(building);
    return true;
  }
}
