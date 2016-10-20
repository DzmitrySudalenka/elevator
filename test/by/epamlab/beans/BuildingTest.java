package by.epamlab.beans;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import by.epamlab.Constants;
import by.epamlab.utils.ConfigReader;

/**
 * @author Dzmitry Sudalenka
 *
 */
public class BuildingTest {
  private static final int STORIES_NUMBER =
      ConfigReader.getParamVal(Constants.KEY_STORIES_NUMBER);
  private static final int ELEVATOR_CAPACITY =
      ConfigReader.getParamVal(Constants.KEY_ELEVATOR_CAPACITY);
  private static final int PASSENGERS_NUMBER =
      ConfigReader.getParamVal(Constants.KEY_PASSENGERS_NUMBER);
  private Building building;
  /**
   *
   */
  @Before
  public void setUp() {
    building = new Building();
  }
  /**
   *
   */
  @Test
  public void createsPersonsWithUniqueId() {
    for (Passenger passenger : building.getPassengers()) {
      int passengerId = passenger.getId();
      int sameId = 0;
      for (Passenger comparePassenger : building.getPassengers()) {
        int comparePassengerId = comparePassenger.getId();
        if (passengerId == comparePassengerId) {
          sameId++;
        }
      }
      assertThat("Each id is uique", sameId, is(1));
    }
  }
  /**
   *
   */
  @Test
  public void createsPersonsWithSourceFloorIsNotEqualToTargetFloor() {
    for (Passenger passenger : building.getPassengers()) {
      int passengerSourceFloor = passenger.getStartingStory();
      int passengerTargetFloor = passenger.getDestinationStory();
      assertThat("Source and target floors is not equal",
          passengerSourceFloor, not(passengerTargetFloor));
    }
  }
  /**
   *
   */
  @Test
  public void createsSpecifiedNumberFloorsAndPersons() {
    int floorsNumber = building.getStories().size();
    assertThat("Creates specified floors number",
        floorsNumber, is(STORIES_NUMBER));
    int personsNumber = building.getPassengers().size();
    assertThat("Creates specified persons number",
        personsNumber, is(PASSENGERS_NUMBER));
  }
  /**
   *
   */
  @Test
  public void returnsPersonsCountSameAsContains() {
    int returnPersonsCoutn = building.getPassengersNumber();
    int containsPersonsCount = building.getPassengers().size();
    assertThat("Returns persons count same as contains",
        returnPersonsCoutn, is(containsPersonsCount));
  }
  /**
   *
   */
  @Test
  public void returnsFloorsCountSameAsContains() {
    int returnFloorsCoutn = building.getStoriesNumber();
    int containsFloorsCount = building.getStories().size();
    assertThat("Returns floors count same as contains",
        returnFloorsCoutn, is(containsFloorsCount));
  }
  /**
   *
   */
  @Test
  public void checksIfThereArePersonsOnSourceFloors() {
    for (Story story : building.getStories()) {
      int floorNumber = story.getNumber();
      for (Passenger passenger : story.getDispathPassengers()) {
        int personSourceNumber = passenger.getStartingStory();
        assertThat("Person is on source floor",
            personSourceNumber, is(floorNumber));
      }
    }
  }
  /**
   *
   */
  @Test
  public void returnsElevatorCapacitySameAsContains() {
    Elevator elevator = building.getElevator();
    int returnElevatorCapacity = elevator.getCurrentCapacity();
    assertThat("Elevator capacity same as contains",
        returnElevatorCapacity, is(ELEVATOR_CAPACITY));
  }
  /**
   *
   */
  @Test
  public void returnsTransportedPersonsCount() {
    Elevator elevator = building.getElevator();
    for (int i = 0; i < Building.ELEVATOR_CAPACITY; i++) {
      Passenger passenger = mock(Passenger.class);
      when(passenger.getId()).thenReturn(i);
      elevator.addPassenger(passenger);
    }
    int elevatortPassengersNumber = elevator.getPassengersNumber();
    assertThat("Return transported persons count",
        elevatortPassengersNumber, is(Building.ELEVATOR_CAPACITY));
  }
}
