package org.example;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.*;

// Importing required classes
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static TaskList list;
    static long currentId = 0;
    static ObjectMapper mapper;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        //instantiate objects that I need
        mapper = new ObjectMapper();
        if(!new File("task.json").exists()){
            list = new TaskList();
        }else{
            list = readAllData();
            currentId = list.getList().size();
        }

        System.out.println(printAllTask(Filter.ALL));
        System.out.println("Welcome to the task to do app\nIn this app, we are going to be able to add, update, and delete tasks.\nWe can also check if they are done or not");
        System.out.println("================");
        System.out.println("Commands:\nadd {insert name of task}\nlist {enter all for every task, complete for only completed tasks, or done for finished tasks.}\ndelete {id} delete specific task\nupdate {id} {-n if you want to change the name} {-c if you want to change the done status} {new name if -n, done or not done if -c}");
        System.out.println("type exit to exit");
        String ans;
        while(true){
            ans = in.nextLine();
            boolean valid = false;
            if(ans.equals("exit")){
                System.out.println("exiting....");
                break;
            }
            if(ans.startsWith("add")){

                String[] keys = ans.split(" ");
                System.out.println(Arrays.toString(keys));
                for(int i = 1;i<keys.length;i++){
                    if(!keys[i].isBlank()){
                        addTask(keys[i]);
                        break;
                    }
                }

            }
            if(ans.startsWith("update")){
                valid = true;
                if(ans.contains("-n") && ans.contains("-i")){
                    System.err.println("only could change name or done status once at a time");
                }else if(ans.contains("-n")){
                    ans = ans.replaceAll("-n","");
                    String[] keys = ans.split(" ");
                    AtomicInteger index = new AtomicInteger();
                    Arrays.stream(keys).filter((e)->!e.isBlank()).forEach((e) -> {
                        keys[index.getAndIncrement()] = new String(e).replaceAll(" ","");

                    });
                    System.out.println((Arrays.toString(keys)));
                    updateTaskName(Long.parseLong(keys[1]),keys[2].replaceAll(" ",""));
                }else if(ans.contains("-i")){
                    ans = ans.replaceAll("-i","");
                    String[] keys = ans.split(" ");
                    AtomicInteger index = new AtomicInteger();
                    Arrays.stream(keys).filter((e)->!e.isBlank()).forEach((e) -> {
                        keys[index.getAndIncrement()] = new String(e).replaceAll(" ","");

                    });
                    System.out.println((Arrays.toString(keys)));
                    updateInProgress(Long.parseLong(keys[1]),Boolean.valueOf(keys[2]));
                }
            }
            if(ans.startsWith("show")){
                valid = true;
                System.out.println(printAllTask(Filter.ALL));
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

    /**
     * Called at the start of the progrom to load task from json file.
     * @return Returns the container object that stores the task.
     * @throws IOException
     */
    public static TaskList readAllData() throws IOException {
       return mapper.readerFor(TaskList.class).readValue(new File("task.json"));
    }


    public static void addTask(String nameOfTask){
        try{
            Task newTask = new Task(nameOfTask);
            list.getList().add(newTask);
            mapper.writeValue(new File("task.json"),list);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonGenerationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void updateTaskName(long id,String name) throws IOException {
        Optional<Task> updatedTask = list.getList().stream().filter((e)->{return e.getId() == id;}).findFirst();
        if(updatedTask.isPresent()){
            Task curr = updatedTask.get();
            if(curr.getId() != id){
                throw new RuntimeException("Error retrieving correct task");
            }
            curr.setName(name);
            mapper.writeValue(new File("task.json"),list);
        }else{
            System.err.println("can not find task");
        }
    }

    public static void updateInProgress(long id,boolean inProgress) throws IOException {
        Optional<Task> updatedTask = list.getList().stream().filter((e)->{return e.getId() == id;}).findFirst();
        if(updatedTask.isPresent()){
            Task curr = updatedTask.get();
            if(curr.getId() != id){
                throw new RuntimeException("Error retrieving correct task");
            }
            if(curr.getCompleted()){
                System.err.println("can not change inProgress status if already done");
                return;
            }
            curr.setInProgress(inProgress);

            mapper.writeValue(new File("task.json"),list);
        }else{
            System.err.println("can not find task");
        }
    }

    public static void finish(long id) throws IOException {
        Optional<Task> updatedTask = list.getList().stream().filter((e)->{return e.getId() == id;}).findFirst();
        if(updatedTask.isPresent()){
            Task curr = updatedTask.get();
            if(curr.getId() != id){
                throw new RuntimeException("Error retrieving correct task");
            }
            if(curr.getCompleted()){
                System.err.println("This task is already done");
                return;
            }
            curr.setCompleted(true);

            mapper.writeValue(new File("task.json"),list);
        }else{
            System.err.println("can not find task");
        }
    }

    public static void deleteTask(long id) throws IOException {
        list.setList(list.getList().stream().filter((e)->e.getId() != id).toList());
        mapper.writeValue(new File("task.json"),list);
    }

    public static String printAllTask(Filter filterParam){
        StringBuilder str = new StringBuilder();
       for(int i = 0;i<list.getList().size();i++){
           Task curr = list.getList().get(i);
           if(filterParam.equals(Filter.ALL)){
                str.append("Id: ").append(curr.getId()).append(" Name: ").append(curr.getName()).append(" Completion status: ").append(curr.getCompleted());
           }
           if(filterParam.equals(Filter.DONE)){
                if(curr.getCompleted()){
                    str.append("Id: ").append(curr.getId()).append(" Name: ").append(curr.getId()).append(" Completion status: ").append(curr.getCompleted());
                }
           }
           if(filterParam.equals(Filter.NOT_DONE)){
                if(!curr.getCompleted()){
                    str.append("Id: ").append(curr.getId()).append(" Name: ").append(curr.getId()).append(" Completion status: ").append(curr.getCompleted());
                }
           }
           str.append(" || ");
       }
       return str.toString();
    }

    public enum Filter{
        ALL,DONE,NOT_DONE;
    }


}