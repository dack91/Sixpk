package edu.dartmouth.cs.project.sixpk.database;


public class AbLog {

    private String name;
    private int id; // database row
    private int ablogId; // corresponds to ids in workout entry id list
    private int muscleGroup; // i.e. obliques
    private int[] difficultyArray; //shows difficulty for last several workouts (need several because of statistics)

    // for debugging (with the driver in Main
//    public AbLog(String name, int ablogId, int muscleGroup, int[] difficultyArray) {
//        this.name = name;
//        this.ablogId = ablogId;
//        this.muscleGroup = muscleGroup;
//        this.difficultyArray = difficultyArray;
//    }


    public AbLog() {
        name = "";
        id = 0;
        ablogId = 0;
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

    public int getAblogId() {
    return ablogId;
    }

    public void setAblogId(int ablogId) {
        this.ablogId = ablogId;
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
}

