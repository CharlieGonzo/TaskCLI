package org.example;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

    List<Task> list;

    public TaskList(){
        list = new ArrayList<>();
    }

    public List<Task> getList() {
        return list;
    }

    public void setList(List<Task> list) {
        this.list = list;
    }
}
