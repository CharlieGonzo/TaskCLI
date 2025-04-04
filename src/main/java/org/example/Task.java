package org.example;

public class Task {

    private long id;
    private String name;
    private boolean completed;
    private boolean inProgress;

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public  Task(String name){
        id = Main.currentId++;
        this.name = name;
        completed = false;
        inProgress = false;
    }

    public Task() {
        // Required for Jackson
    }

    public boolean isCompleted() {
        return completed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public boolean getCompleted(){
        return completed;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }




}
