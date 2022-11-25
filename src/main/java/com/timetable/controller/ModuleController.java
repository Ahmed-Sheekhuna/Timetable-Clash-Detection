package com.timetable.controller;

import com.timetable.models.Activity;
import com.timetable.models.Module;
import com.timetable.service.ActivityService;
import com.timetable.service.ModuleService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@FxmlView("modules.fxml")
public class ModuleController extends AbstractController {
    @FXML
    private Button cancelButton;
    @FXML
    private TextField nameField;
    @FXML
    private ChoiceBox<String> typeBox;
    @FXML
    private ListView<Activity> activityView;
    private final ObservableList<Activity> activityList = FXCollections.observableArrayList();
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ModuleService moduleService;

    private Module module;

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void createActivity(ActionEvent event) {
        Stage stage = createStage(ActivityController.class, "Create activity", event);
        stage.setUserData(new Activity());
        stage.showAndWait();
        if (stage.getUserData() != null) {
            //update activities list
            activityList.add(activityService.getActivity((Long) stage.getUserData()));
        }
    }

    @FXML
    private void save(ActionEvent event) {
        if (activityList.isEmpty()) {
            //this code creates error window
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("You must configure at least one activity");
            errorAlert.showAndWait();
            return;
        }
        module.setName(nameField.getText());
        module.setRequired(typeBox.getValue().equals("compulsory"));
        module.setActivities(new ArrayList<>(activityList));
        cancelButton.getScene().getWindow().setUserData(moduleService.saveModule(module));
        cancel(event);
    }

    @FXML
    public void initialize() {
        activityList.clear();
        module = new Module();
        activityView.setItems(activityList);
    }

    public void setModule(Module module) {
        this.module = module;
        activityList.addAll(module.getActivities());
        nameField.setText(module.getName());
        typeBox.setValue(module.getRequired() ? "compulsory" : "optional");
    }

}
