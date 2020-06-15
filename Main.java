package com.malikadrian.todolist;

import com.malikadrian.todolist.datamodel.TodoData;
import com.malikadrian.todolist.datamodel.TodoStats;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));
        primaryStage.setTitle("Todo List");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        try{
            TodoData.getInstance().storeTodoItems();
            TodoStats.getInstance().storeTodoItems();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        try{
            TodoData.getInstance().loadTodoItems();
            TodoStats.getInstance().loadTodoItems();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
