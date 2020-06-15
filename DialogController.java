package com.malikadrian.todolist;

import com.malikadrian.todolist.datamodel.TodoData;
import com.malikadrian.todolist.datamodel.TodoItem;
import com.malikadrian.todolist.datamodel.TodoStats;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class DialogController {

    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea detailsArea;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private Spinner<Integer> prioritySpinner;


    public void setSetup(){

        prioritySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0, 1));
        deadlinePicker.setValue(LocalDate.now());
    }




    public TodoItem processResult() {

        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailsArea.getText().trim();
        LocalDate deadlineValue = deadlinePicker.getValue();
        Integer priority = prioritySpinner.getValue();



        TodoItem newItem = new TodoItem(shortDescription, details, deadlineValue,priority);
        //TodoStats.getInstance().addTodoItem(newItem);
        TodoData.getInstance().addTodoItem(newItem);
        return newItem;
    }
}
