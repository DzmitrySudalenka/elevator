package by.epamlab.utils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import by.epamlab.beans.Building;
import by.epamlab.beans.Elevator;
import by.epamlab.beans.Passenger;
import by.epamlab.beans.Story;
import by.epamlab.enums.TransportationState;

/**
 * @author Dzmitry Sudalenka
 *
 */
public class ValidatorTest {
  private Building building;
  /**
   *
   */
  @Before
  public void setUp() {
    building = new Building();
    Validator.setBuilding(building);
  }
  /**
   *
   */
  @Test
  public void checkIfAllDispatchContainersAreEmpty() {
    final int storyNumber = 1;
    final int dispathPassengersNumber = 0;
    Story story = mock(Story.class);
    when(story.getDispatchPassengersNumber())
      .thenReturn(dispathPassengersNumber);
    Map<Integer, Story> stories = new HashMap<Integer, Story>();
    stories.put(storyNumber, story);
    building.setStories(stories);
    boolean isAllDispatchContainersEmpty =
        Validator.isAllDispatchContainersEmpty();
    assertThat("All dispatch containers are empty",
        isAllDispatchContainersEmpty, is(true));
  }
  /**
   *
   */
  @Test
  public void checkIfElevatorIsEmpty() {
    Elevator elevator = mock(Elevator.class);
    when(elevator.isEmpty()).thenReturn(true);
    building.setElevator(elevator);
    boolean isElevatorEmpty = Validator.isElevatorEmpty();
    assertTrue("Elevator is empty", isElevatorEmpty);
  }
  /**
   *
   */
  @Test
  public void returnCompletedAsTransportationStateOfEachPassengers() {
    final int storyNumber = 1;
    Passenger passenger = mock(Passenger.class);
    when(passenger.getTransportationState()).thenReturn(TransportationState.COMPLETED);
    Story story = mock(Story.class);
    story.addArrivalPassenger(passenger);
    Map<Integer, Story> stories = new HashMap<Integer, Story>();
    stories.put(storyNumber, story);
    building.setStories(stories);
    boolean isTransportationsStateCompleted = Validator.isTransportationsStateCompleted();
    assertTrue("Transportations completed", isTransportationsStateCompleted);
  }
  /**
   *
   */
  @Test
  public void checkIfPassengersDestinationStoryNumberSameAsArrivalContainer() {
    final int storyNumber = 1;
    Passenger passenger = mock(Passenger.class);
    when(passenger.getDestinationStory()).thenReturn(storyNumber);
    Story story = mock(Story.class);
    story.addArrivalPassenger(passenger);
    Map<Integer, Story> stories = new HashMap<Integer, Story>();
    stories.put(storyNumber, story);
    building.setStories(stories);
    boolean isPassengersArrivedToRightStory = Validator.isPassengersArrivedToRightStory();
    assertTrue("Passengers arrived to right story", isPassengersArrivedToRightStory);
  }
  /**
   *
   */
  @Test
  public void checkIfTotalNumberOfPeopleInAllArrivalStoryContainersSameAsPassengersNumber() {
    final int storyNumber = 1;
    Story story = mock(Story.class);
    when(story.getArrivalPassengersNumber()).thenReturn(Building.PASSENGERS_NUMBER);
    Map<Integer, Story> stories = new HashMap<Integer, Story>();
    stories.put(storyNumber, story);
    building.setStories(stories);
    boolean isAllPassengersArrived = Validator.isAllPassengersArrived();
    assertTrue("All passengers arrived", isAllPassengersArrived);
  }
}
