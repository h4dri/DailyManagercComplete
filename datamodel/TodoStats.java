package com.malikadrian.todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TodoStats {


    private static TodoStats instance = new TodoStats();
    private static String filename= "TodoStats.txt";

    int todayTaskCounter = 0;
    int monthTaskCounter = 0;
    int allTaskCounter=0;
    private ObservableList<TodoItem> todoItems;
    private DateTimeFormatter formatter;

    public static TodoStats getInstance(){
        return instance;
    }
    private TodoStats(){
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

   /* public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }*/


    public void addTodoItem(TodoItem item){
        todoItems.add(item);
    }

    public void loadTodoItems() throws IOException {
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;
        try{
            while ((input = br.readLine()) != null ) {
                String[] itemPieces = input.split("\t");

                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];
                String priorityString = itemPieces[3];

                Integer priority = Integer.parseInt(priorityString);
                LocalDate date = LocalDate.parse(dateString, formatter);
                TodoItem todoItem = new TodoItem(shortDescription,details,date, priority);
                todoItems.add(todoItem);
            }
        } finally {
            if(br!=null){
                br.close();
            }
        }
    }
    public int initStatsDay() throws IOException {
        int todayTaskCounter = 0;
        int today = LocalDate.now().getDayOfYear();
        for (TodoItem item : todoItems) {
            if (item.getDeadline().getDayOfYear() == today) {
                todayTaskCounter +=1;
            }
        }
            return todayTaskCounter;
    }



    public int initStatsMonth() throws IOException{
        int monthTaskCounter = 0;
        int month = LocalDate.now().getMonthValue();
        for (TodoItem item : todoItems){
            if (item.getDeadline().getMonthValue()==month){
                monthTaskCounter+=1;
            }
        }
        return monthTaskCounter;
    }

    public int initStatsAll() throws IOException{
        int allTaskCounter=0;
        for (TodoItem item : todoItems){
            allTaskCounter+=1;
        }

        return allTaskCounter;
    }

    public void storeTodoItems() throws IOException{
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try{
            Iterator<TodoItem> iter = todoItems.iterator();
            while(iter.hasNext()){
                TodoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadline().format(formatter),
                        item.getPriority()));
                bw.newLine();
            }
        }finally {
            if (bw!=null){
                bw.close();
            }
        }
    }

    public void deleteTodoItem(TodoItem item){
        todoItems.remove(item);
    }



}
