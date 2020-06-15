package com.malikadrian.todolist.datamodel;

import java.time.LocalDate;

public class TodoItem {
    private String shortDescription;
    private String details;
    private LocalDate deadline;
    private int priority;

    public TodoItem(String shortDescription, String details, LocalDate deadline, Integer priority) {
        this.shortDescription = shortDescription;
        this.details = details;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }


//
//    @Override
//    public String toString() {
//        return shortDescription;
//    }
}
