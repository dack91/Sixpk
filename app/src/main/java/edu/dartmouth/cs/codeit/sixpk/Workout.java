package edu.dartmouth.cs.codeit.sixpk;

/**
 * Created by dackerman on 2/16/15.
 */
public class Workout {
    private String workout;
    private int abGroup;
    private double duration;
    private int difficulty;

    public Workout() {

    }

    @Override
    public String toString() {
        return workout + ", " + abGroup;
    }

    // Text to show in the listview for the itinerary preview
    public String getDescription() {
        return duration + " min," + difficulty;
    }

    public String getWorkout() {
        return workout;
    }

    public int getAbGroup() {
        return abGroup;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public void setAbGroup(int abGroup) {
        this.abGroup = abGroup;
    }
}
