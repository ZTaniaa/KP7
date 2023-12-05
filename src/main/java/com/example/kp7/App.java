package com.example.kp7;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class App extends Application {
    private TextArea textArea = new TextArea();
    private TextArea textArea2 = new TextArea();
    private Thread[] readers;
    private List<Reader> readerList;
    private TextField readerCountField = new TextField();
    private TextField readTimeField = new TextField();

    private ExecutorService executorService;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainStage) {
        readerCountField.setText("5");
        readTimeField.setText("1000");

        Button startAllButton = new Button("Start");
        startAllButton.setOnAction(e -> startAllReaders());

        Button stopAllButton = new Button("Stop");
        stopAllButton.setOnAction(e -> stopAllReaders());

        VBox mainVBox = new VBox();

        HBox topHBox = new HBox(textArea, textArea2);
        VBox bottomVBox = new VBox(new HBox (new Label("Reader count:"), readerCountField, new Label("Read time:"), readTimeField));
        VBox bottomVBox2 = new VBox(new HBox(new HBox(startAllButton, stopAllButton)));

        textArea.setPrefWidth(300);
        textArea.setPrefHeight(400);
        textArea2.setPrefWidth(500);
        textArea2.setPrefHeight(400);

        mainVBox.getChildren().addAll(topHBox, bottomVBox, bottomVBox2);

        Scene scene = new Scene(mainVBox, 800, 600);
        mainStage.setScene(scene);
        mainStage.show();
    }

    private void updateThreadInfo(String message) {
        Platform.runLater(() -> {
            textArea2.appendText(message + "\n");
        });
    }


    private void startAllReaders() {
        updateThreadInfo("Start!");
        int readerCount = Integer.parseInt(readerCountField.getText());
        int readTime = Integer.parseInt(readTimeField.getText());


        executorService = Executors.newFixedThreadPool(readerCount);

        Library library = new Library(50, textArea);

        for(int i = 0; i < readerCount; i++) {
            int priority = ThreadLocalRandom.current().nextInt(Thread.MIN_PRIORITY, Thread.MAX_PRIORITY + 1);
            Reader reader = new Reader(library, textArea2, readTime);

            executorService.submit(() -> {
                Thread.currentThread().setPriority(priority);
                reader.run();
            });
        }

//        readers = new Thread[readerCount];
//        Random random = new Random();
//        for (int i = 0; i < readers.length; i++) {
//            readers[i] = new Thread(new Reader(library, textArea2, readTime), "Reader " + (i + 1));
//            readers[i].setPriority(random.nextInt(10) + 1);
//            readers[i].start();
//        }
    }

    private void stopAllReaders() {
        executorService.shutdownNow();
        updateThreadInfo("Shutdown!");
    }
}

