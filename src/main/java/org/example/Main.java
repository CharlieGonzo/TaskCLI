package org.example;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Importing required classes
import java.io.*;

public class Main {

    static TaskList list;
    static long currentId = 0;
    static PrintWriter writer;
    static ObjectMapper mapper;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        //instantiate objects that I need
        mapper = new ObjectMapper();
        if(!new File("task.json").exists()){
            list = new TaskList();
        }else{
            list = readAllData();
        }
        writeAllData();
        System.out.println(list.getList());
        System.out.println("Welcome to the task to do app\nIn this app, we are going to be able to add, update, and delete tasks.\nWe can also check if they are done or not");
        System.out.println("================");
        System.out.println("Commands:\nadd {insert name of task}\nlist {enter all for every task, complete for only completed tasks, or done for finished tasks.}\ndelete {id} delete specific task\nupdate {id} {-n if you want to change the name} {-c if you want to change the done status} {new name if -n, done or not done if -c}");
        System.out.println("type exit to exit");
        String ans;
        while(true){
            ans = in.next();
            boolean valid = false;
            if(ans.equals("exit")){
                System.out.println("exiting....");
                break;
            }
            if(ans.startsWith("add")){
                valid = true;

            }
            if(ans.startsWith("add")){
                valid = true;
            }
            if(ans.startsWith("add")){
                valid = true;
            }


        }






    }

    /**
     * is called anytime the user creates,updates, or reads a task
     * @throws IOException
     */
    public static void writeAllData() throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("task.json"),list);
    }

    public static TaskList readAllData() throws IOException {
       return mapper.readerFor(TaskList.class).readValue(new File("task.json"));
    }


    public static void addTask(String nameOfTask){
        try{
            Task newTask = new Task(nameOfTask);
            list.getList().add(newTask);
            mapper.writeValue(new File("task.json"),list);
            String taskJson = mapper.writeValueAsString(newTask);
            System.out.println(taskJson);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonGenerationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void updateTaskName(long id,String name){

    }
}