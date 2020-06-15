package com.malikadrian.todolist;

import com.malikadrian.todolist.datamodel.TodoData;
import com.malikadrian.todolist.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class EditDialogController {

    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea detailsArea;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private Spinner<Integer> prioritySpinner;


    public void setSetup(TodoItem todoItem){

        prioritySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, todoItem.getPriority(), 1));
        shortDescriptionField.setText(todoItem.getShortDescription());
        detailsArea.setText(todoItem.getDetails());
        deadlinePicker.setValue(todoItem.getDeadline());
    }

    public TodoItem processResult() {



        String shortDescription = shortDescriptionField.getText().trim();
        String details = detailsArea.getText().trim();
        LocalDate deadlineValue = deadlinePicker.getValue();
        Integer priority = prioritySpinner.getValue();



        TodoItem newItem = new TodoItem(shortDescription, details, deadlineValue,priority);
        TodoData.getInstance().addTodoItem(newItem);
        return newItem;
    }

}
