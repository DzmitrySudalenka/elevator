package by.epamlab.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epamlab.Constants;
import by.epamlab.beans.Building;
import by.epamlab.beans.Elevator;
import by.epamlab.beans.Passenger;
import by.epamlab.beans.Story;
import by.epamlab.enums.TransportationState;

/**
 * @author Dzmitry Sudalenka
 *
 */
public final class Validator {
  private Validator() { }
  private static final Logger LOGGER = LogManager.getLogger(Validator.class);
  private static Building building;
  /**
   * @param building building
   */
  public static void setBuilding(final Building building) {
    Validator.building = building;
  }
  /**
   * @param building building
   */
  public static void validate(final Building building) {
    Validator.building = building;
    String result;
    if (isAllDispatchContainersEmpty()
        && isElevatorEmpty()
        && isTransportationsStateCompleted()
        && isPassengersArrivedToRightStory()
        && isAllPassengersArrived()) {
      result = Constants.STR_SUCCESSFULLY;
    } else {
      result = Constants.STR_UNSUCCESSFULLY;
    }
    LOGGER.info(Constants.STR_VALIDATION_COMPLETED + result);
  }
  /**
   * @return return
   */
  public static boolean isAllDispatchContainersEmpty() {
    for (Story story : building.getStories()) {
      int storyDispatchPassengersNumber = story.getDispatchPassengersNumber();
      if (storyDispatchPassengersNumber > 0) {
        return false;
      }
    }
    return true;
  }
  /**
   * @return return
   */
  public static boolean isElevatorEmpty() {
    Elevator elevator = building.getElevator();
    if (!elevator.isEmpty()) {
      return false;
    }
    return true;
  }
  /**
   * @return return
   */
  public static boolean isTransportationsStateCompleted() {
    for (Story story : building.getStories()) {
      for (Passenger passenger : story.getArrivalPassengers()) {
        TransportationState passengerTransportationState =
            passenger.getTransportationState();
        if (passengerTransportationState != TransportationState.COMPLETED) {
          return false;
        }
      }
    }
    return true;
  }
  /**
   * @return return
   */
  public static boolean isPassengersArrivedToRightStory() {
    for (Story story : building.getStories()) {
      for (Passenger passenger : story.getArrivalPassengers()) {
        int passengerDestinationStory = passenger.getDestinationStory();
        int storyNumber = story.getNumber();
        if (passengerDestinationStory != storyNumber) {
          return false;
        }
      }
    }
    return true;
  }
  /**
   * @return return
   */
  public static boolean isAllPassengersArrived() {
    int arrivedPasengers = 0;
    for (Story story : building.getStories()) {
      arrivedPasengers += story.getArrivalPassengersNumber();
    }
    if (arrivedPasengers != Building.PASSENGERS_NUMBER) {
      return false;
    }
    return true;
  }
}
