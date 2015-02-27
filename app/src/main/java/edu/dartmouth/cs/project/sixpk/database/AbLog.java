package edu.dartmouth.cs.project.sixpk.database;

/**
 * Created by ben on 2/27/15.
 */
public class AbLog {

  private int id; //corresponds to ids in workout entry id list
  private int ablogId;
  private int muscleGroup; // i.e. obliques
  private int[] difficultyArray; //shows difficulty for last several workouts (need several because of statistics)

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
}

