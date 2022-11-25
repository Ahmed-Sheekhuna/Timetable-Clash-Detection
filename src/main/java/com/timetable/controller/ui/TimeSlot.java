package com.timetable.controller.ui;


// Class representing a time interval, or "Time Slot", along with a view.
// View is just represented by a region with minimum size, and style class.

// Has a selected property just to represent selection.

import com.timetable.models.Activity;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.timetable.controller.ProgramController.SELECTED_PSEUDO_CLASS;

public class TimeSlot {

    private final LocalDateTime start;
    private final Duration duration;
    private final Pane view;

    private final BooleanProperty selected = new SimpleBooleanProperty();

    public final BooleanProperty selectedProperty() {
        return selected;
    }

    public final boolean isSelected() {
        return selectedProperty().get();
    }

    public final void setSelected(boolean selected) {
        selectedProperty().set(selected);
    }

    public TimeSlot(LocalDateTime start, Duration duration) {
        this.start = start;
        this.duration = duration;

        view = new Pane();
        view.setMinSize(150, 20);
        view.getStyleClass().add("time-slot");

        selectedProperty().addListener((obs, wasSelected, isSelected) ->
                view.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, isSelected));

    }

    public TimeSlot(LocalDateTime start, Duration duration, Activity activity) {
        this(start, duration);
        view.getStyleClass().remove("time-slot");
        view.getStyleClass().add("time-slot-1");
        Label label = new Label("[" + activity.getType() + "] " + activity.getName());
        view.getChildren().add(label);
        label.setTextFill(Color.web("#ffffff"));
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalTime getTime() {
        return start.toLocalTime();
    }

    public DayOfWeek getDayOfWeek() {
        return start.getDayOfWeek();
    }

    public Duration getDuration() {
        return duration;
    }

    public Node getView() {
        return view;
    }

}