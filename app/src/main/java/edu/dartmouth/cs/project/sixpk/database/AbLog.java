package edu.dartmouth.cs.project.sixpk.database;


public class AbLog {

    private String name; // name of the exercise
    private int id; // database row
    private int ablogNumber; // identifier to be referenced in workouts
    private int muscleGroup; // 1 - 3
    private int[] difficultyArray; // shows difficulty (from feedback) for last 3 workouts
    private String filePath; // pointing to the instructional gif

    // constructor for main activity
    public AbLog(String name, int ablogNumber, int muscleGroup, String filePath) {
        this.name = name;
        this.ablogNumber = ablogNumber;
        this.muscleGroup = muscleGroup;
        this.filePath = filePath;

        // feedback comes in as int 0-10, 5 is neutral
        // initialize 3 most recent feedbacks as neutral
        difficultyArray = new int[3];
        difficultyArray[0] = 5;
        difficultyArray[1] = 5;
        difficultyArray[2] = 5;
    }

    // constructor for datasource
    public AbLog() {
        name = "";
        ablogNumber = 0;
        muscleGroup = 0;
        difficultyArray = new int[1];
        difficultyArray[0] = 5;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAblogNumber() {
    return ablogNumber;
    }

    public void setAblogNumber(int ablogId) {
        this.ablogNumber = ablogId;
    }

    public int getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(int muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public int[] getDifficultyArray() {
        return difficultyArray;
    }

    public void setDifficultyArray(int[] difficultyArray) {
        this.difficultyArray = difficultyArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

