package com.timetable.controller;

import com.timetable.models.Module;
import com.timetable.models.Term;
import com.timetable.service.ModuleService;
import com.timetable.service.TermService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
@FxmlView("terms.fxml")
public class TermController extends AbstractController {
    @FXML
    private Button cancelButton;
    @FXML
    private ListView<Module> modulesView;
    @FXML
    private TextField startMonthField;
    @FXML
    private TextField endMonthField;
    @FXML
    private Spinner<Integer> yearSpinner;
    @FXML
    private Spinner<Integer> numberSpinner;
    private ObservableList<Module> modulesList = FXCollections.observableArrayList();
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private TermService termService;
    private Term term;

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void configureModule(ActionEvent event) {
        Stage stage = createStage(ModuleController.class, "Configure module", event);
        Module selectedModule = modulesView.getSelectionModel().getSelectedItem();
        applicationContext.getBean(ModuleController.class).setModule(selectedModule);
        stage.showAndWait();
        if (stage.getUserData() != null) {
            modulesList.set(modulesList.indexOf(selectedModule), moduleService.getModule((Long) stage.getUserData()));
        }
    }

    @FXML
    private void createModule(ActionEvent event) {
        Stage stage = createStage(ModuleController.class, "Create module", event);
        stage.showAndWait();
        if (stage.getUserData() != null) {
            modulesList.add(moduleService.getModule((Long) stage.getUserData()));
        }
    }

    @FXML
    private void save(ActionEvent event) {
        if (modulesList.size() < 4) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("You must configure four modules before saving");
            errorAlert.showAndWait();
            return;
        }
        term.setModules(new ArrayList<>(modulesList));
        cancelButton.getScene().getWindow().setUserData(termService.saveTerm(term));
        cancel(event);
    }

    @FXML
    public void initialize() {
        modulesList.clear();
        modulesView.setItems(modulesList);
    }

    public void setTerm(Term term) {
        this.term = term;
        yearSpinner.getValueFactory().setValue(term.getYear());
        numberSpinner.getValueFactory().setValue(term.getTermNumber());
        startMonthField.setText(convertToMonth(term.getStartMonth()));
        endMonthField.setText(convertToMonth(term.getEndMonth()));
        modulesList.addAll(term.getModules());
    }

    private String convertToMonth(Integer number) {
        return switch (number) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> throw new RuntimeException("Month not found");
        };
    }
}
