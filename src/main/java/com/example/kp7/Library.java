package com.example.kp7;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

class Library {
    private int totalBooks;
    private TextArea textArea;
    private Semaphore semaphore;

    public Library(int totalBooks, TextArea textArea) {
        this.totalBooks = totalBooks;
        this.semaphore = new Semaphore(totalBooks);
        this.textArea = textArea;
    }

    public void borrowBook() throws InterruptedException {
        semaphore.acquire();
        synchronized(this) {
            totalBooks--;
            UIUpdating("Now book take " + Thread.currentThread().getName() + ". Only " + totalBooks + " books left!");
        }
    }

    public void returnBook() {
        synchronized(this) {
            totalBooks++;
            UIUpdating("Now book return " + Thread.currentThread().getName() + ". Only " + totalBooks + " books left!");
        }
        semaphore.release();
    }

    private void UIUpdating(String message) {
        javafx.application.Platform.runLater(() -> textArea.appendText(message + "\n"));
    }
}

