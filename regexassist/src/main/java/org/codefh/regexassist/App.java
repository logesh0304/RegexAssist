/*
 * The MIT License
 *
 * Copyright 2020 Logesh0304.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.codefh.regexassist;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
* Main class for the application.
* main method of this class is called by launcher class when execution 
*/
public class App extends Application {

    public static final String NAME="RegexAssist";
    public static final Image ICON=new Image(App.class.getResourceAsStream("icons/app-32.png"));
    public static Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws IOException
    {
        primaryStage=stage;
        FXMLLoader loader=new FXMLLoader(getClass().getResource("fxml/regexassist.fxml"));
        BorderPane root=loader.load();
        Scene scene=new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles/regexassist.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle(NAME);
        primaryStage.getIcons().add(ICON);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }   
}
