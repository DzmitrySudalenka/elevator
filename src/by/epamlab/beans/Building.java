package by.epamlab.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import by.epamlab.Constants;
import by.epamlab.utils.ConfigReader;

/**
 * @author Dzmitry Sudalenka
 *
 */
public class Building {
  /**
   *
   */
  public static final int STORIES_NUMBER =
      ConfigReader.getParamVal(Constants.KEY_STORIES_NUMBER);
  /**
   *
   */
  public static final int ELEVATOR_CAPACITY =
      ConfigReader.getParamVal(Constants.KEY_ELEVATOR_CAPACITY);
  /**
   *
   */
  public static final int PASSENGERS_NUMBER =
      ConfigReader.getParamVal(Constants.KEY_PASSENGERS_NUMBER);
  private Elevator elevator;
  private List<Passenger> passengers;
  private Map<Integer, Story> stories;
  /**
   *
   */
  public Building() {
    elevator = new Elevator(ELEVATOR_CAPACITY);
    passengers = new ArrayList<Passenger>();
    stories = new HashMap<Integer, Story>();
    createPassengers();
    createStories();
    putPassengersOnStories();
  }
  /**
   * @return return
   */
  public List<Passenger> getPassengers() {
    return passengers;
  }
  /**
   * @param passengers passengers
   */
  public void setPassengers(final List<Passenger> passengers) {
    this.passengers = passengers;
  }
  /**
   * @return return
   */
  public Collection<Story> getStories() {
    return stories.values();
  }
  /**
   * @param stories stories
   */
  public void setStories(final Map<Integer, Story> stories) {
    this.stories = stories;
  }
  /**
   * @return return
   */
  public Elevator getElevator() {
    return elevator;
  }
  /**
   * @param elevator elevator
   */
  public void setElevator(final Elevator elevator) {
    this.elevator = elevator;
  }
  /**
   *
   */
  public void createPassengers() {
    for (int i = 0; i < PASSENGERS_NUMBER; i++) {
      int startingStoryNumber = getRandomStoryNumber();
      int destinationNumber = getRandomStoryNumber();
      while (startingStoryNumber == destinationNumber) {
        destinationNumber = getRandomStoryNumber();
      }
      Passenger passenger = new Passenger(i, startingStoryNumber,
          destinationNumber);
      passengers.add(passenger);
    }
  }
  /**
   * @return return
   */
  public int getRandomStoryNumber() {
    Random random = new Random();
    return random.nextInt(STORIES_NUMBER) + Constants.NUM_STORY_FIRST;
  }
  private void createStories() {
    for (int i = 0; i < STORIES_NUMBER; i++) {
      int storyNumber = i + Constants.NUM_STORY_FIRST;
      Story story = new Story(storyNumber);
      stories.put(storyNumber, story);
    }
  }
  private void putPassengersOnStories() {
    for (Passenger passenger : passengers) {
      int passengerStartingStory = passenger.getStartingStory();
      stories.get(passengerStartingStory).addDispatchPassenger(passenger);
    }
  }
  /**
   * @param number number
   * @return return
   */
  public Story getStory(final int number) {
    return stories.get(number);
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
  public int getStoriesNumber() {
    return stories.size();
  }
}
