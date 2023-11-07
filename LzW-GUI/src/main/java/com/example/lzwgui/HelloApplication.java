package com.example.lzwgui;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("JavaFX App");

        // Create the first scene (full screen before the delay)
        VBox firstScene = new VBox();
        firstScene.setStyle("-fx-background-image: url('logo.jpg'); -fx-background-size: cover; -fx-alignment: center;");
        Label welcomeLabel = new Label("Welcome to LzW Compressor");
        welcomeLabel.setStyle("-fx-font-size: 35px; -fx-font-weight: bold; -fx-text-fill: #dad9d9;");
        firstScene.getChildren().add(welcomeLabel);

        // Create the second scene (normal screen after the delay)
        VBox secondScene = new VBox();
        secondScene.setStyle("-fx-background-image: url('logo.jpg'); -fx-background-size: cover; -fx-alignment: center;");

        Button browseButton = new Button("Browse");
        browseButton.setStyle("-fx-padding: 40px 0 0 0;");
        browseButton.setAlignment(Pos.CENTER);

        Label fileLabel = new Label("File Name:");
        fileLabel.setStyle("-fx-text-fill: #d2d2d2; -fx-margin: 40 0 0 0;");
        Label filepath = new Label();

        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a File");
            java.io.File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                String filePath = selectedFile.getAbsolutePath();
                Path path = Paths.get(filePath);

                fileLabel.setText("File Name: " + path.getFileName());
                filepath.setText(filePath);
            }
        });

        HBox buttons = new HBox(20);

        Button compressButton = new Button("Compress");
        compressButton.setOnAction(event -> {
            if (!filepath.getText().isEmpty()) {
                Compressor compressor = new Compressor();
                File to_compress = new File(filepath.getText());
                ArrayList<String> arr = new ArrayList<>(to_compress.read_to_compress());
                StringBuilder text = new StringBuilder();
                for (String s : arr) {
                    text.append(s);
                }
                String compressedText = compressor.compress(text.toString());
                ArrayList<String> ByteStrings = new ArrayList<>(to_compress.convert_to_bytes(compressedText));
                to_compress.string_to_binary_file(ByteStrings, "/path/to/compressed.bin");
            } else {
                System.out.println("Please select a file before compressing.");
            }
        });

        Button decompressButton = new Button("Decompress");
        decompressButton.setOnAction(event -> {
            if (!filepath.getText().isEmpty()) {
                DeCompressor decompressor = new DeCompressor();
                File to_decompress = new File(filepath.getText());
                ArrayList<String> ByteStringss = new ArrayList<>(to_decompress.read_binary_file());
                StringBuilder binaryText = new StringBuilder();
                for (String s : ByteStringss) {
                    binaryText.append(s);
                }
                String decompressedText = decompressor.decompress(binaryText.toString());
                to_decompress.string_to_text_file(decompressedText, "/path/to/decompressed.txt");
            } else {
                System.out.println("Please select a file before decompressing.");
            }
        });

        buttons.setAlignment(Pos.CENTER);

        buttons.getChildren().addAll(compressButton, decompressButton);
        secondScene.getChildren().addAll(browseButton, fileLabel, filepath, buttons);

        // Create the scenes
        Scene scene1 = new Scene(firstScene, 620, 620);
        Scene scene2 = new Scene(secondScene, 1020, 620);

        // Set the stage to go full screen before the delay
        primaryStage.setScene(scene1);
        primaryStage.setFullScreen(true);

        // Transition to the second scene after a delay of 3500 ms
        PauseTransition delay = new PauseTransition(Duration.millis(3500));
        delay.setOnFinished(e -> {
            primaryStage.setFullScreen(false); // Return to normal screen
            primaryStage.setScene(scene2);
            primaryStage.setResizable(false);
        });
        delay.play();

        primaryStage.show();
    }
}
