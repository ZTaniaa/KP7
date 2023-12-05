package com.example.kp7;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Semaphore;

class Reader implements Runnable {
    private Library library;
    private TextArea textArea;
    private int readTime;
    private Semaphore semaphore = new Semaphore(1);

    public Reader(Library library, TextArea textArea, int readTime) {
        this.library = library;
        this.textArea = textArea;
        this.readTime = readTime;
    }
    @Override
    public void run() {
        try {
            while (true) {
                semaphore.acquire();
                library.borrowBook();
                updateThreadInfo("Thread info: " + Thread.currentThread().getName() + ",\t Time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                Thread.sleep(readTime);
                library.returnBook();
                semaphore.release();
            }
        } catch (InterruptedException e) {
            logThreadInfo();
            Thread.currentThread().interrupt();
        }
    }

    private void logThreadInfo() {
        updateThreadInfo("Thread info: " + Thread.currentThread().getName() +  ",\t Priority: " + Thread.currentThread().getPriority() + ",\t Time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void updateThreadInfo(String message) {
        Platform.runLater(() -> {
            textArea.appendText(message + "\n");
        });
    }

}


