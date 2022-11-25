package com.timetable.controller;

import com.timetable.models.Program;
import com.timetable.models.Term;
import com.timetable.service.ProgramService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("app.fxml")
public class MainController extends AbstractController {
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private ListView<Program> programListView;
    @Autowired
    private ProgramService programService;
    private final ObservableList<Program> programList = FXCollections.observableArrayList();

    @FXML
    private void addNew(ActionEvent event) {
        createStage(ProgramController.class, "Programs", event).showAndWait();
        loadData();
    }

    @FXML
    private void edit(ActionEvent event) {
        //create new window to edit program of study
        Stage stage = createStage(ProgramController.class, "Programs", event);
        //set selected program to program controller
        applicationContext.getBean(ProgramController.class).setProgram(programListView.getSelectionModel().getSelectedItem());
        //show window and wait until it closes
        stage.showAndWait();
        //after program window close - refresh data on the page
        loadData();
    }

    //refresh data on this page
    public void loadData() {
        programList.clear();
        programList.addAll(programService.getAllPrograms());
        programListView.setItems(programList);
    }
}
