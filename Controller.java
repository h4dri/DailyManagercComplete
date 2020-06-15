package com.malikadrian.todolist;

import com.malikadrian.todolist.datamodel.TodoData;
import com.malikadrian.todolist.datamodel.TodoItem;
import com.malikadrian.todolist.datamodel.TodoStats;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import org.controlsfx.control.Notifications;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;

public class Controller {

    private List<TodoItem> todoItems;
    private List<TodoItem> todoStats;

    @FXML
    private ListView<TodoItem> todoListView;

    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private Label deadlineLabel;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    RadioButton notificationsButton;

    @FXML
    private ToggleButton filterToggleButton, sortPriorityButton;

    @FXML
    private Label time,days,month,all;

    SortedList<TodoItem> sortedList, sortedListPriority;
    boolean empty;
    int iloscEl;





    private FilteredList<TodoItem> filteredList;

    private Predicate<TodoItem> wantAllItems;
    private Predicate<TodoItem> wantTodaysItems;
    private String file;

    public void initialize() throws IOException {

        days.setText(Integer.toString(TodoStats.getInstance().initStatsDay()));
        month.setText(Integer.toString(TodoStats.getInstance().initStatsMonth()));
        all.setText(Integer.toString(TodoStats.getInstance().initStatsAll()));



        /*
        TodoItem item1 = new TodoItem("Mail birthday card", "Buy a 30th birthday card for John",
                LocalDate.of(2020, Month.JANUARY, 13));
        TodoItem item2 = new TodoItem("Wizyta u doktora", "Isc do dentystki na lisinskiego, dodatkowa plomba",
                LocalDate.of(2020, Month.JANUARY, 17));
        TodoItem item3 = new TodoItem("zrobic projekt zdarzeniowki", "nauczyc sie javaFX i zrobic projekt",
                LocalDate.of(2020, Month.JANUARY, 29));
        TodoItem item4 = new TodoItem("AWD", "Wyslac 7 labke z awd i zrobic projekt z AWD",
                LocalDate.of(2020, Month.JANUARY, 1));
        TodoItem item5 = new TodoItem("PSY", "symulatory",
                LocalDate.of(2020, Month.FEBRUARY, 11));

        todoItems  = new ArrayList<TodoItem>();
        todoItems.add(item1);
        todoItems.add(item2);
        todoItems.add(item3);
        todoItems.add(item4);
        todoItems.add(item5);

        TodoData.getInstance().setTodoItems(todoItems);*/
        //TodoStat stat1 = new TodoStat(LocalDate.of(2020, Month.JANUARY, 13));
        //todoStats.add(stat1);


        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");

        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                editItemDialog(item);

            }
        });


        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);

            }
        });

        listContextMenu.getItems().addAll(deleteMenuItem);
        listContextMenu.getItems().addAll(editMenuItem);

        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observableValue, TodoItem oldValue, TodoItem newValue) {
                if (newValue !=null){
                    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                    itemDetailsTextArea.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy"); //"d M yy"); //class DateTimeFormatter documentation
                    deadlineLabel.setText(df.format(item.getDeadline()));



                }
            }
        });
//        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
//                (EventHandler) event -> {
//
//                    time.setText(LocalTime.now().toString());
//                }));
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();


        wantAllItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem item) {
                return true;
            }
        };
        wantTodaysItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem item) {
                return item.getDeadline().equals(LocalDate.now());
            }
        };

        filteredList = new FilteredList<>(TodoData.getInstance().getTodoItems(),wantAllItems);
//                new Predicate<TodoItem>() {
//                    @Override
//                    public boolean test(TodoItem item) {
//                        return true;
//                    }
//                });

        sortedList = new SortedList<TodoItem>(filteredList,
                new Comparator<TodoItem>() {
                    @Override
                    public int compare(TodoItem o1, TodoItem o2) {
                        return o1.getDeadline().compareTo(o2.getDeadline());
                    }
                });
        sortedListPriority = new SortedList<TodoItem>(filteredList,
                new Comparator<TodoItem>() {
                    @Override
                    public int compare(TodoItem o1, TodoItem o2) {
                        return o2.getPriority().compareTo(o1.getPriority());
                    }
                });


//        todoListView.setItems(TodoData.getInstance().getTodoItems());

        todoListView.setItems(sortedList);






        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>(){
                    @Override
                    protected void updateItem(TodoItem todoItem, boolean b) {
                        super.updateItem(todoItem, b);
                        if (b){
                            setText(null);
                        } else {
                            setText(todoItem.getShortDescription());
                            if (todoItem.getDeadline().isBefore(LocalDate.now().plusDays(1))){
                                setTextFill(Color.RED);
                            }
                            else if(todoItem.getDeadline().equals(LocalDate.now().plusDays(1))){
                                setTextFill(Color.ORANGE);
                            }
                            else if (todoItem.getDeadline().isAfter(LocalDate.now().plusDays(1))){
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };

                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty){
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }

                        });

                return cell;
            }
        });
    }



    public void showNewItemDialog(){

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Todo Item");
        dialog.setHeaderText("Use this dialog to create a new todo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
        try {
//            Parent root = FXMLLoader.load(getClass().getResource("todoItemDialog.fxml"));

            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e){
            System.out.println("Nie mozna zaladowac okna");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        DialogController controller = fxmlLoader.getController();
        controller.setSetup();

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            controller = fxmlLoader.getController();
            TodoItem newItem = controller.processResult();
            todoListView.getSelectionModel().select(newItem);
        }
    }

    public void editItemDialog(TodoItem todoItem){

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit Todo Item");
        dialog.setHeaderText("Use this dialog to edit a todo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemEditDialog.fxml"));
        try {
//            Parent root = FXMLLoader.load(getClass().getResource("todoItemDialog.fxml"));

            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e){
            System.out.println("Nie mozna zaladowac okna");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);


        EditDialogController controller = fxmlLoader.getController();
        controller.setSetup(todoItem);
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            controller = fxmlLoader.getController();
            TodoItem newItem = controller.processResult();
            todoListView.getSelectionModel().select(newItem);
            TodoData.getInstance().deleteTodoItem(todoItem);
        }

    }


    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (selectedItem !=null){
            if(keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteItem(selectedItem);
            }
        }
    }

    @FXML
    public void handleClicListView(){
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        itemDetailsTextArea.setText(item.getDetails());
        deadlineLabel.setText(item.getDeadline().toString());
    /*    System.out.println("The selected item is " + item);*/

       /* StringBuilder sb = new StringBuilder(item.getDetails());
        sb.append("\n\n\n\n");
        sb.append("Due: ");
        sb.append(item.getDeadline().toString());
        //itemDetailsTextArea.setText(item.getDetails());
        itemDetailsTextArea.setText(sb.toString());*/
    }

    public void deleteItem(TodoItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete item: "+item.getShortDescription());
        alert.setContentText("Are tou sure?  Press OK to continue, or CANCEL to Back out");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)){
            TodoData.getInstance().deleteTodoItem(item);
        }
    }


    @FXML
    public void handleFilterToggleButton(){
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (filterToggleButton.isSelected()){
            filteredList.setPredicate(wantTodaysItems);
//                    new Predicate<TodoItem>() {
//                @Override
//                public boolean test(TodoItem item) {
//                    return item.getDeadline().equals(LocalDate.now());
//                }
//            });

            if (filteredList.isEmpty()){
                itemDetailsTextArea.clear();
                deadlineLabel.setText("");
            } else if (filteredList.contains(selectedItem)){
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                todoListView.getSelectionModel().selectFirst();
            }

        }else {
            filteredList.setPredicate(wantAllItems);
                   /* (new Predicate<TodoItem>() {
                @Override
                public boolean test(TodoItem item) {
                    return true;
                }
            });*/

                   todoListView.getSelectionModel().select(selectedItem);
        }
    }

   @FXML
    public void handleDoneTask(ActionEvent actionEvent) throws IOException {
       TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
       if (selectedItem != null) {
           TodoStats.getInstance().addTodoItem(selectedItem);
           TodoData.getInstance().deleteTodoItem(selectedItem);
           days.setText(Integer.toString(TodoStats.getInstance().initStatsDay()));
           month.setText(Integer.toString(TodoStats.getInstance().initStatsMonth()));
           all.setText(Integer.toString(TodoStats.getInstance().initStatsDay()));

       }
   }

    final Timeline timeline = new Timeline
            (new KeyFrame(Duration.seconds(3),

                    (EventHandler) event -> {

                       // Notifications.create()
                         //       .text("You have so much to do")

                           //     .showInformation();
                        iloscEl = todoListView.getItems().size();
                        empty = todoListView.getSelectionModel().isEmpty();
                        if (empty!=true) {
                            String title = "To do list";
                            String message = notificationsButton.getText();
                            TrayNotification tray = new TrayNotification();
                            AnimationType type = AnimationType.POPUP;
                            tray.setAnimationType(type);
                            tray.setTitle(title);
                            tray.setMessage("Amount of tasks: " + Integer.toString(iloscEl));
                            tray.setNotificationType(NotificationType.SUCCESS);
                            tray.showAndDismiss(Duration.millis(3000));
                        }
// time.setText(LocalTime.now().toString());


                    }));

    @FXML
    public void handleNotifications(ActionEvent actionEvent) throws IOException {
        if(notificationsButton.isSelected()){
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
        else{
            timeline.stop();
        }
    }



    @FXML
    public void handleExit(ActionEvent actionEvent) {
        Platform.exit();
    }


    public void handleSortPriorityButton() {
        if(sortPriorityButton.isSelected()){
            todoListView.setItems(sortedListPriority);
        }
        else{
            todoListView.setItems(sortedList);
        }
    }
}
