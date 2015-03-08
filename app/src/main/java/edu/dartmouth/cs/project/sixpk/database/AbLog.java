package edu.dartmouth.cs.project.sixpk.database;


public class AbLog {

    private String name;
    private int id; // database row
    private int ablogNumber; // corresponds to ids in workout entry id list
    private int muscleGroup; // i.e. obliques
    private int[] difficultyArray; //shows difficulty for last several workouts (need several because of statistics)
    private String filePath;

//    // for debugging (with the driver in Main
//    public AbLog(String name, int ablogId, int muscleGroup) {
//        this.name = name;
//        this.ablogId = ablogId;
//        this.muscleGroup = muscleGroup;
//    }

    public AbLog(String name, int ablogNumber, int muscleGroup, String filePath) {
        this.name = name;
        this.ablogNumber = ablogNumber;
        this.muscleGroup = muscleGroup;
        this.filePath = filePath;

        difficultyArray = new int[3];
        difficultyArray[0] = 5;
        difficultyArray[1] = 5;
        difficultyArray[2] = 5;
    }

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

