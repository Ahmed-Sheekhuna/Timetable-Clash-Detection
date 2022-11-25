package com.timetable.controller;

import com.timetable.models.Activity;
import com.timetable.service.ActivityService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.LocalTimeStringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.FormatStyle;

@Component
@FxmlView("activities.fxml")
public class ActivityController extends AbstractController {
    @FXML
    private Button cancelButton;
    @FXML
    private Spinner<LocalTime> fromSpinner;
    @FXML
    private Spinner<LocalTime> toSpinner;
    @FXML
    private TextField nameField;
    @FXML
    private ChoiceBox<String> typeBox;
    @FXML
    private DatePicker datePicker;

    private Activity data;

    @Autowired
    private ActivityService activityService;

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void save(ActionEvent event) {
        Activity activity = new Activity();
        activity.setName(nameField.getText());
        activity.setType(typeBox.getValue());
        activity.setStart(fromSpinner.getValue());
        activity.setEnd(toSpinner.getValue());
        activity.setDay(datePicker.getValue().getDayOfWeek());
        //save activity data to pass ID to previous window to update data
        cancelButton.getScene().getWindow().setUserData(activityService.saveActivity(activity));
        cancel(event);
    }

    @FXML
    public void initialize() {
        //set empty default values to UI inputs
        fromSpinner.setValueFactory(new TimeSpinnerValueFactory());
        toSpinner.setValueFactory(new TimeSpinnerValueFactory());
        fromSpinner.getValueFactory().setValue(LocalTime.of(9, 0));
        toSpinner.getValueFactory().setValue(LocalTime.of(10, 0));
        datePicker.setDayCellFactory(getDayCellFactory());
        //disable edit date as text, so user could edit only using datepicker
        datePicker.getEditor().setDisable(true);
        datePicker.getEditor().setOpacity(1);
    }

    //time chooser value editors
    private static class TimeSpinnerValueFactory extends SpinnerValueFactory<LocalTime> {
        {
            setConverter(new LocalTimeStringConverter(FormatStyle.MEDIUM));
        }

        @Override
        public void decrement(int steps) {
            if (getValue() == null)
                setValue(LocalTime.of(9, 0));
            else {
                LocalTime time = getValue();
                setValue(time.minusHours(steps));
            }
        }

        @Override
        public void increment(int steps) {
            if (getValue() == null)
                setValue(LocalTime.of(9, 0));
            else {
                LocalTime time = getValue();
                setValue(time.plusHours(steps));
            }
        }
    }

    //this created to disable sundays and saturdays in datepicker
    private Callback<DatePicker, DateCell> getDayCellFactory() {
        return new Callback<>() {

            @Override
            public DateCell call(final DatePicker datePicker1) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.getDayOfWeek() == DayOfWeek.SUNDAY //
                                || item.getDayOfWeek() == DayOfWeek.SATURDAY) {
                            setDisable(true);
                            setStyle("-fx-background-color: #808080;");
                        }
                    }
                };
            }
        };
    }
}
