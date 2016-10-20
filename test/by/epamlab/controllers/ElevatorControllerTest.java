package by.epamlab.controllers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import by.epamlab.beans.Building;
import by.epamlab.beans.Elevator;
import by.epamlab.beans.Passenger;
import by.epamlab.beans.Story;
import by.epamlab.enums.ElevatorControllerActions;
import by.epamlab.enums.MovementDirection;
import by.epamlab.enums.TransportationState;

/**
 * @author Dzmitry Sudalenka
 *
 */
public class ElevatorControllerTest {
  private ElevatorController elevatorController;
  private Building building;
  private Elevator elevator;
  private Passenger passenger;
  /**
   *
   */
  @Before
  public void setUp() {
    final int passengerId = 0;
    final int passengerStartingStoryNumber = 1;
    final int passengerDestinationStoryNumber = 2;
    building = new Building();
    elevatorController = new ElevatorController(building);
    elevator = ElevatorController.getElevator();
    passenger = new Passenger(passengerId,
        passengerStartingStoryNumber,
        passengerDestinationStoryNumber);
  }
  /**
   *
   */
  @Test
  public void checksIfEachPassengerHasTransportationTask() {
    for (Passenger passenger : building.getPassengers()) {
      assertNotNull("Passenger has transportation task",
          passenger.getTransportationTask());
    }
  }
  /**
   *
   */
  @Test
  public void returnChangedStoryNumberAfterMoving() {
    int storyNumberBeforeMoving = elevator.getCurrentStoryNumber();
    elevatorController.moveElevator();
    int storyNumberAfterMoving = elevator.getCurrentStoryNumber();
    assertThat("Story number changed after elevator moving",
        storyNumberBeforeMoving, not(storyNumberAfterMoving));
  }
  /**
   *
   */
  @Test
  public void returnGreaterNumberOfStoryAfterMovingWhenDirectionOfElevatorIsUp() {
    int storyNumberBeforeMoving = elevator.getCurrentStoryNumber();
    elevatorController.moveElevator();
    int storyNumberAfterMoving = elevator.getCurrentStoryNumber();
    assertTrue("Story number becomes greater after elevator moving",
        storyNumberAfterMoving > storyNumberBeforeMoving);
  }
  /**
   *
   */
  @Test
  public void returnLowerNumberOfStoryAfterMovingWhenDirectionOfElevatorIsDown() {
    ElevatorController.setMovementDirection(MovementDirection.DOWN);
    int storyNumberBeforeMoving = elevator.getCurrentStoryNumber();
    elevatorController.moveElevator();
    int storyNumberAfterMoving = elevator.getCurrentStoryNumber();
    assertTrue("Story number becomes lower after elevator moving",
        storyNumberAfterMoving < storyNumberBeforeMoving);
  }
  /**
   *
   */
  @Test
  public void changeMovementDirectionWhenReachesExtremeStory() {
    elevator.setCurrentStoryNumber(Building.STORIES_NUMBER);
    MovementDirection movementDitectionBeforeMoving =
        ElevatorController.getMovementDirection();
    elevatorController.moveElevator();
    MovementDirection movementDitectionAfterMoving =
        ElevatorController.getMovementDirection();
    assertThat("Movement ditection change after elevator moving",
        movementDitectionBeforeMoving, not(movementDitectionAfterMoving));
  }
  /**
   *
   */
  @Test
  public void returnMovingElevatorAsElevatorControllerAction() {
    elevatorController.moveElevator();
    ElevatorControllerActions elevatorControllerAction = ElevatorController.getAction();
    assertThat("Elevator controller action is MOVING_ELEVATOR",
        elevatorControllerAction, is(ElevatorControllerActions.MOVING_ELEVATOR));
  }
  /**
   *
   */
  @Test(timeout = 2000)
  public void returnDeboadingOfPassengerAsElevatorControllerAction() {
    deboadingPassenger(passenger);
    ElevatorControllerActions elevatorControllerAction = ElevatorController.getAction();
    assertThat("Elevator controller action is DEBOADING_OF_PASSENGER",
        elevatorControllerAction, is(ElevatorControllerActions.DEBOADING_OF_PASSENGER));
  }
  /**
   *
   */
  @Test(timeout = 2000)
  public void checkIfDeboadingOnlyElevatorPassengers() {
    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.execute(passenger.getTransportationTask());
    CountDownLatch countDownLatch = new CountDownLatch(1);
    ElevatorController.setCountDownLatch(countDownLatch);
    elevatorController.waitPassengers();
    elevatorController.informPassengersCanDeboading();
    TransportationState passengerTransportationState = passenger.getTransportationState();
    executorService.shutdown();
    assertThat("Passenger who was not in elevator transportation state is not COMPLETED",
        passengerTransportationState, not(TransportationState.COMPLETED));
  }
  /**
   *
   */
  @Test(timeout = 2000)
  public void checkIfPassengerIsNotInElevatorAfterDeboading() {
    deboadingPassenger(passenger);
    boolean isElevatorEmpty = elevator.isEmpty();
    assertTrue("Elevator is empty", isElevatorEmpty);
  }
  /**
   *
   */
  @Test(timeout = 2000)
  public void returnCompletedAsPassengerTransportationStateAfterDeboading() {
    deboadingPassenger(passenger);
    assertThat("Passenger transportation state is COMPLETED",
        passenger.getTransportationState(), is(TransportationState.COMPLETED));
  }
  /**
   *
   */
  @Test(timeout = 2000)
  public void returnBoadingOfPassengerAsElevatorControllerAction() {
    int currentStoryNumber = ElevatorController.getCurrentStoryNumber();
    Story currentStory = building.getStory(currentStoryNumber);
    freeFloorByDispathPassengers(currentStory);
    currentStory.addDispatchPassenger(passenger);
    boadingPassenger(passenger);
    ElevatorControllerActions elevatorControllerAction = ElevatorController.getAction();
    assertThat("Elevator controller action is BOADING_OF_PASSENGER",
        elevatorControllerAction, is(ElevatorControllerActions.BOADING_OF_PASSENGER));
  }
  /**
   *
   */
  @Test(timeout = 2000)
  public void checkIfBoadingOnlyPassengersLocatedOnSameFloorAsElevator() {
    final int addToGetStoryNumber = 1;
    int currentStoryNumber = ElevatorController.getCurrentStoryNumber();
    int nextStoryNumber = currentStoryNumber + addToGetStoryNumber;
    Story currentStory = building.getStory(currentStoryNumber);
    Story nextStory = building.getStory(nextStoryNumber);
    freeFloorByDispathPassengers(currentStory);
    freeFloorByDispathPassengers(nextStory);
    nextStory.addDispatchPassenger(passenger);
    boadingPassenger(passenger);
    boolean isElevatorEmpty = elevator.isEmpty();
    assertTrue("Elevator is empty", isElevatorEmpty);
  }
  /**
   *
   */
  @Test(timeout = 2000)
  public void checkIfPassengerIsNotStayOnStoryAfterBoading() {
    int currentStoryNumber = ElevatorController.getCurrentStoryNumber();
    Story currentStory = building.getStory(currentStoryNumber);
    freeFloorByDispathPassengers(currentStory);
    currentStory.addDispatchPassenger(passenger);
    int boadingPassengerId = passenger.getId();
    boadingPassenger(passenger);
    Collection<Passenger> dispathPassengers = currentStory.getDispathPassengers();
    for (Passenger dispathPassenger : dispathPassengers) {
      int dispathPassengerId = dispathPassenger.getId();
      assertThat("Dispath passenger is not boaded passenger",
          dispathPassengerId, not(boadingPassengerId));
    }
  }
  /**
   *
   */
  @Test
  public void checkIfInElevatorCanNotBeMorePassengersThanItsCapacity() {
    final int addToOverflowElevatorCapacity = 1;
    int elevatorCapacity = Building.ELEVATOR_CAPACITY;
    int overflowElevatorCapacity = elevatorCapacity + addToOverflowElevatorCapacity;
    for (int i = 0; i < overflowElevatorCapacity; i++) {
      Passenger passenger = mock(Passenger.class);
      when(passenger.getId()).thenReturn(i);
      elevator.addPassenger(passenger);
    }
    int passengersNumber = elevator.getPassengersNumber();
    assertThat("Passengers number equal elevator capacity",
        passengersNumber, is(Building.ELEVATOR_CAPACITY));
  }
  /**
   *
   */
  @Test(timeout = 2000)
  public void checkIfBoadingOnlyPassengersWhoseMovementDirectionIsSameAsElevator() {
    ElevatorController.setMovementDirection(MovementDirection.DOWN);
    int currentStoryNumber = ElevatorController.getCurrentStoryNumber();
    Story currentStory = building.getStory(currentStoryNumber);
    freeFloorByDispathPassengers(currentStory);
    currentStory.addDispatchPassenger(passenger);
    boadingPassenger(passenger);
    boolean isElevatorEmpty = elevator.isEmpty();
    assertTrue("Elevator is empty", isElevatorEmpty);
  }
  /**
   *
   */
  @Test
  public void returnCompletionTransportationAsElevatorControllerAction() {
    for (Story story : building.getStories()) {
      freeFloorByDispathPassengers(story);
    }
    elevatorController.isTransportationFinished();
    ElevatorControllerActions elevatorControllerAction = ElevatorController.getAction();
    assertThat("Elevator controller action is COMPLETION_TRANSPORTATION",
        elevatorControllerAction, is(ElevatorControllerActions.COMPLETION_TRANSPORTATION));
  }
  /**
   *
   */
  @Test
  public void checkIfElevatorIsEmpty() {
    for (Story story : building.getStories()) {
      freeFloorByDispathPassengers(story);
    }
    elevatorController.isTransportationFinished();
    ElevatorControllerActions elevatorControllerAction = ElevatorController.getAction();
    assertThat("Elevator controller action is COMPLETION_TRANSPORTATION",
        elevatorControllerAction, is(ElevatorControllerActions.COMPLETION_TRANSPORTATION));
    boolean isElevatorEmpty = elevator.isEmpty();
    assertTrue("Elevator is empty", isElevatorEmpty);
  }
  /**
   *
   */
  @Test
  public void checkIfNoPassengersWaitingForTransportation() {
    final int zeroPassengersNumber = 0;
    for (Story story : building.getStories()) {
      freeFloorByDispathPassengers(story);
    }
    elevatorController.isTransportationFinished();
    ElevatorControllerActions elevatorControllerAction = ElevatorController.getAction();
    assertThat("Elevator controller action is COMPLETION_TRANSPORTATION",
        elevatorControllerAction, is(ElevatorControllerActions.COMPLETION_TRANSPORTATION));
    for (Story story : building.getStories()) {
      int storyDispatchPassengersNumber = story.getDispatchPassengersNumber();
      assertThat("No passengers waiting for transportation",
          storyDispatchPassengersNumber, is(zeroPassengersNumber));
    }
  }
  private void deboadingPassenger(final Passenger passenger) {
    final int elevatorCurrentStoryNumber = 2;
    final int passengersNumber = 1;
    elevator.setCurrentStoryNumber(elevatorCurrentStoryNumber);
    elevator.addPassenger(passenger);
    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.execute(passenger.getTransportationTask());
    CountDownLatch countDownLatch = new CountDownLatch(passengersNumber);
    ElevatorController.setCountDownLatch(countDownLatch);
    elevatorController.waitPassengers();
    elevatorController.informPassengersCanDeboading();
    executorService.shutdown();
    try {
      executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  private void boadingPassenger(final Passenger passenger) {
    final int passengersNumber = 1;
    ExecutorService executorService = Executors.newCachedThreadPool();
    executorService.execute(passenger.getTransportationTask());
    CountDownLatch countDownLatch = new CountDownLatch(passengersNumber);
    ElevatorController.setCountDownLatch(countDownLatch);
    elevatorController.waitPassengers();
    elevatorController.informPassengersCanBoading();
    executorService.shutdown();
  }
  private void freeFloorByDispathPassengers(final Story story) {
    Collection<Passenger> dispathPassengers = story.getDispathPassengers();
    for (Passenger dispathPassenger : dispathPassengers) {
      int dispathPassengerId = dispathPassenger.getId();
      story.removeDispatchPassenger(dispathPassengerId);
    }
  }
}
