package edu.dartmouth.cs.project.sixpk;

/* Exercise Class
 *
 * Holds name, section, picture, and feedback
 *
 * the other class, WorkoutSet will hold the duration of each workout
 *
 */


public class Exercise {
    private String name; // name of ab workout
    private int exId;
    private int abGroup; // ab, obliques, back, whatever other shit you can do with your core
    private double difficulty; // defaults to 0, changes based on feedback (higher is more difficult)
    private int imageId; // holds the id of the drawable (if you can even hold a gif in that)

    public Exercise(String exerciseName, int exerciseID, int muscleGroup) {
        this.name = exerciseName;
        this.exId = exerciseID;
        this.abGroup = muscleGroup;
        this.difficulty = 0;
        this.imageId = 0;
    }

    public void setFeedback(double response) {
        difficulty = response;
        // I'm thinking maybe a more complex feedback integration later
        // could depend on how many times this exercise has been done, stuff like that
    }

    public void setImageId(int id) {
        this.imageId = id;
    }

    public int getImageId() {
        return imageId;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getAbGroup() {
        return abGroup;
    }

}
