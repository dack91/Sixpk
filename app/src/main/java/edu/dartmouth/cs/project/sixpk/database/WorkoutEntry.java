package edu.dartmouth.cs.project.sixpk.database;

import java.util.Date;

public class WorkoutEntry {

  private int difficulty; //Easy, medium, hard
  private int[] exerciseIdList; //i.e. [1,5,2] where 1 = crunches, 2 = planks, etc.
  private float[] feedBackList; // corresponds to ordering of exercise id list
  private Date dateTime; //time of workout
  private int duration; //length of workout
  private Long id;

  public int getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(int difficulty) {
    this.difficulty = difficulty;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int[] getExerciseIdList() {
    return exerciseIdList;
  }

  public void setExerciseIdList(int[] exerciseIdList) {
    this.exerciseIdList = exerciseIdList;
  }

  public float[] getFeedBackList() {
    return feedBackList;
  }

  public void setFeedBackList(float[] feedBackList) {
    this.feedBackList = feedBackList;
  }

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date dateTime) {
    this.dateTime = dateTime;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }
}
