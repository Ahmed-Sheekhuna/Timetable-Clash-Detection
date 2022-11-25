package com.timetable.controller;

import com.timetable.controller.ui.TimeSlot;
import com.timetable.models.Activity;
import com.timetable.models.Module;
import com.timetable.models.Program;
import com.timetable.models.Term;
import com.timetable.service.ClashDetectionService;
import com.timetable.service.ProgramService;
import com.timetable.service.TermService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

@Component
@FxmlView("programStudy.fxml")
public class ProgramController extends AbstractController {
    @FXML
    private Button cancelButton;
    @FXML
    private ListView<Term> termView;
    @FXML
    private ChoiceBox<String> typeBox;
    @FXML
    private TextField nameField;
    @FXML
    private Spinner<Integer> yearSpinner;
    private final ObservableList<Term> termList = FXCollections.observableArrayList();
    @Autowired
    private TermService termService;
    @Autowired
    private ProgramService programService;
    @Autowired
    private ClashDetectionService clashDetectionService;
    private Program program;

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void checkClashes(ActionEvent event) {
        //use clash detection service to check collisions in the timetable
        List<String> errors = clashDetectionService.checkCollisions(termView.getSelectionModel().getSelectedItem());
        if (errors.isEmpty()) {
            //this code creates inform window
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Clashes result");
            alert.setHeaderText(null);
            alert.setContentText("Clashes not detected");
            alert.showAndWait();
        } else {
            //this code creates error window
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Clashes result");
            errorAlert.setContentText(String.join(System.lineSeparator() + "==============================" + System.lineSeparator(), errors));
            errorAlert.showAndWait();
        }
    }

    @FXML
    private void save(ActionEvent event) {
        //check if all terms are configured (id != 0 and id != null)
        if (termList.stream().anyMatch(term -> term.getId() == null || term.getId() == 0)) {
            //this code creates error window
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("You must configure all terms before saving");
            errorAlert.showAndWait();
            return;
        }
        //retrieve data from UI to save it in DB
        program.setTerms(new ArrayList<>(termList));
        program.setName(nameField.getText());
        program.setStartYear(yearSpinner.getValue());
        program.setType(typeBox.getValue());
        programService.saveProgram(program);
        cancel(event);
    }

    @FXML
    public void initialize() {
        termList.clear();
        //as year 1 presented both in updergraduate and postgraduate - populate it
        termList.add(new Term(0L, 1, 9, 12, 1, emptyList()));
        termList.add(new Term(0L, 2, 1, 4, 1, emptyList()));
        populateUndergraduateSpecificTerms();
        termView.setItems(termList);
        typeBox.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if ("undergraduate".equals(newValue)) {
                        if (termList.size() < 6) {
                            populateUndergraduateSpecificTerms();
                        }
                    } else {
                        if (termList.size() > 2) {
                            termList.remove(2, 6);
                        }
                    }
                });
        program = new Program();
    }

    //Populate year 2 and year 3 terms
    private void populateUndergraduateSpecificTerms() {
        termList.add(new Term(0L, 1, 9, 12, 2, emptyList()));
        termList.add(new Term(0L, 2, 1, 4, 2, emptyList()));
        termList.add(new Term(0L, 1, 9, 12, 3, emptyList()));
        termList.add(new Term(0L, 2, 1, 4, 3, emptyList()));
    }

    @FXML
    private void configureTerm(ActionEvent event) {
        Stage stage = createStage(TermController.class, "Configure term", event);
        Term term = termView.getSelectionModel().getSelectedItem();
        applicationContext.getBean(TermController.class).setTerm(term);
        stage.showAndWait();
        if (stage.getUserData() != null && !termList.isEmpty()) {
            termList.set(termList.indexOf(term), termService.getTerm((Long) stage.getUserData()));
            System.out.println(termService.getTerm((Long) stage.getUserData()).getModules());
        }
    }

    public void setProgram(Program program) {
        this.program = program;
        termList.clear();
        termList.addAll(program.getTerms());
        nameField.setText(program.getName());
        typeBox.setValue(program.getType());
    }


    private final LocalTime firstSlotStart = LocalTime.of(9, 0);
    private final Duration slotLength = Duration.ofHours(1);
    private final LocalTime lastSlotStart = LocalTime.of(21, 0);

    public static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private final List<TimeSlot> timeSlots = new ArrayList<>();

    @FXML
    public void visualizeTerm(ActionEvent event) {
        List<String> errors = clashDetectionService.checkCollisions(termView.getSelectionModel().getSelectedItem());
        if (!errors.isEmpty()) {
            //if clashes detected - show error
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Clashes result");
            alert.setHeaderText(null);
            alert.setContentText("Clashes detected, cannot visualize");
            alert.showAndWait();
            return;
        }
        //create calendar view
        GridPane calendarView = new GridPane();
        //collect all activities from term
        List<Activity> activities = termView.getSelectionModel().getSelectedItem().getModules().stream().map(Module::getActivities).filter(Objects::nonNull).flatMap(List::stream).toList();

        ObjectProperty<TimeSlot> mouseAnchor = new SimpleObjectProperty<>();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(4);

        //create timeslots
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            int slotIndex = 1;
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            List<Activity> currentDayActivities = activities.stream().filter(it -> Objects.equals(it.getDay(), dayOfWeek)).toList();
            for (LocalDateTime startTime = date.atTime(firstSlotStart);
                 !startTime.isAfter(date.atTime(lastSlotStart));
                 startTime = startTime.plus(slotLength)) {
                LocalTime time = startTime.toLocalTime();
                //if activity for a timeslot found - add it to timeslot
                Activity slotActivity = currentDayActivities.stream().filter(it ->
                        (it.getStart().equals(time) ||
                                (it.getStart().isBefore(time)) && (
                                        it.getEnd().isAfter(time.plusHours(1)) ||
                                                it.getEnd().equals(time.plusHours(1))))).findAny().orElse(null);
                TimeSlot timeSlot = slotActivity == null ? new TimeSlot(startTime, slotLength) :
                        new TimeSlot(startTime, slotLength, slotActivity);
                timeSlots.add(timeSlot);

                registerDragHandlers(timeSlot, mouseAnchor);

                calendarView.add(timeSlot.getView(), timeSlot.getDayOfWeek().getValue(), slotIndex);
                slotIndex++;
            }
        }

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E");

        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            Label label = new Label(date.format(dayFormatter));
            label.setPadding(new Insets(1));
            label.setTextAlignment(TextAlignment.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            calendarView.add(label, date.getDayOfWeek().getValue(), 0);
        }

        int slotIndex = 1;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        for (LocalDateTime startTime = today.atTime(firstSlotStart);
             !startTime.isAfter(today.atTime(lastSlotStart));
             startTime = startTime.plus(slotLength)) {
            Label label = new Label(startTime.format(timeFormatter));
            label.setPadding(new Insets(2));
            GridPane.setHalignment(label, HPos.RIGHT);
            calendarView.add(label, 0, slotIndex);
            slotIndex++;
        }

        ScrollPane scroller = new ScrollPane(calendarView);

        Scene scene = new Scene(scroller);
        scene.getStylesheets().add(getClass().getResource("calendar-view.css").toExternalForm());
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    // Registers handlers on the time slot to manage selecting a range of slots in the grid.

    private void registerDragHandlers(TimeSlot timeSlot, ObjectProperty<TimeSlot> mouseAnchor) {
        timeSlot.getView().setOnDragDetected(event -> {
            mouseAnchor.set(timeSlot);
            timeSlot.getView().startFullDrag();
            timeSlots.forEach(slot ->
                    slot.setSelected(slot == timeSlot));
        });

        timeSlot.getView().setOnMouseDragEntered(event -> {
            TimeSlot startSlot = mouseAnchor.get();
            timeSlots.forEach(slot ->
                    slot.setSelected(isBetween(slot, startSlot, timeSlot)));
        });

        timeSlot.getView().setOnMouseReleased(event -> mouseAnchor.set(null));
    }

    // Utility method that checks if testSlot is "between" startSlot and endSlot
    // Here "between" means in the visual sense in the grid: i.e. does the time slot
    // lie in the smallest rectangle in the grid containing startSlot and endSlot
    //
    // Note that start slot may be "after" end slot (either in terms of day, or time, or both).

    // The strategy is to test if the day for testSlot is between that for startSlot and endSlot,
    // and to test if the time for testSlot is between that for startSlot and endSlot,
    // and return true if and only if both of those hold.

    // Finally we note that x <= y <= z or z <= y <= x if and only if (y-x)*(z-y) >= 0.

    private boolean isBetween(TimeSlot testSlot, TimeSlot startSlot, TimeSlot endSlot) {

        boolean daysBetween = testSlot.getDayOfWeek().compareTo(startSlot.getDayOfWeek())
                * endSlot.getDayOfWeek().compareTo(testSlot.getDayOfWeek()) >= 0;

        boolean timesBetween = testSlot.getTime().compareTo(startSlot.getTime())
                * endSlot.getTime().compareTo(testSlot.getTime()) >= 0;

        return daysBetween && timesBetween;
    }

}
