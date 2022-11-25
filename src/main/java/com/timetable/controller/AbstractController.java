package com.timetable.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

//abstract class providing method for creating a new window based on passed parameters
public abstract class AbstractController {
    //autowired to inject object in runtime
    @Autowired
    protected ApplicationContext applicationContext;

    //create new window, set title and pass control to another controller
    public Stage createStage(Class<?> controllerType, String title, ActionEvent event) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(controllerType);
        Scene scene = new Scene(root);
        //stage = window
        Stage stage = new Stage();
        stage.setScene(scene);
        //set title
        stage.setTitle(title);
        //set window mode as modal
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        return stage;
    }
}
