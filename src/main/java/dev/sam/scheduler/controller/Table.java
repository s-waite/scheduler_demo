package dev.sam.scheduler.controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Abstract class for controllers with a table
 */
public abstract class Table {
    <T> void initializeTable(TableView<T> tableView, List<T> tableItems) {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setItems((FilteredList<T>) tableItems);
    }
}
