package dev.sam.scheduler.controller;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;

import java.util.List;

public abstract class Table {
//    void initializeTable();

    <T> void initializeTable(TableView<T> tableView, List<T> tableItems) {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setItems((ObservableList<T>) tableItems);
    }
}
