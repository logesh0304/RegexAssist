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

import org.codefh.regexassist.util.IndexRange;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.codefh.regexassist.util.Utility;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

/**
 * Controller class for RegexAssist
 */
public class RegexAssistController implements Initializable {

    private ArrayList<IndexRange> matches = new ArrayList<>();
    private ObservableList<CheckMenuItem> flags = FXCollections.observableArrayList();

    private Text regexFldHolder = new Text(), // using 'Text' as a reference to make grow 'Codearea'(regexFld)
                replaceTextHolder = new Text(); // using 'Text' as a reference to make grow 'TextArea'(replaceText_area)   

    private VirtualizedScrollPane regex_VScroll, in_VScroll, replacement_VScroll;
    private CodeArea regex_area = new CodeArea(),
                input_area = new CodeArea(),
                replacement_area = new CodeArea();

    @FXML    private VBox input_box, replacement_box;
    @FXML    private TextArea replaceText_area;
    @FXML    private TextField limit_fld;
    @FXML    private MenuButton flags_mBtn;
    @FXML    private TableView match_table;
    @FXML    private TableColumn<MatchInformation, Integer> no_col;
    @FXML    private TableColumn<MatchInformation, String> index_col, match_col;
    @FXML    private ListView<String> split_LV;
    @FXML    private CheckMenuItem auMatch_CMI, auSplit_CMI, auReplacement_CMI, renderIC_CMI;
    @FXML    private Label status_lbl, msg_lbl;
    @FXML    private Text sLength_txt;

    public RegexAssistController() {
        
        // setting flag options to observableList(flags) 
        for (Map.Entry<String, Integer> eflag : Configs.flagsNvalue.entrySet()) {
            CheckMenuItem flag = new CheckMenuItem(eflag.getKey());
            flag.selectedProperty().addListener(o -> computeFlags());
            flag.setMnemonicParsing(false);
            flags.add(flag);
        }
        
        // attaching Codearea to VirtualiedSCrollPane
        regex_VScroll = new VirtualizedScrollPane(regex_area);
        in_VScroll = new VirtualizedScrollPane(input_area);
        replacement_VScroll = new VirtualizedScrollPane(replacement_area);
        replacement_area.setEditable(false);
        input_area.setOnKeyTyped(e -> onTyped());
        regex_area.setOnKeyTyped(e -> onTyped());

        VBox.setVgrow(in_VScroll, Priority.ALWAYS);
        VBox.setVgrow(replacement_VScroll, Priority.ALWAYS);

        // height is set to 25 (i.e) 1 row
        regex_area.setPrefHeight(25);
        regex_area.setMinHeight(Region.USE_PREF_SIZE);
        regex_area.setMaxHeight(Region.USE_PREF_SIZE);

    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {

        makeAutoGrowable();

        input_box.getChildren().add(1, regex_VScroll);
        input_box.getChildren().add(in_VScroll);
        replacement_box.getChildren().add(replacement_VScroll);

        // make split-limit's Textfield (limitFld) to acccept only integers
        limit_fld.setTextFormatter(new TextFormatter<>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change c) {
                if (c.getControlNewText().matches("[\\d]*"))
                    return c;
                else
                    return null;
            }
        }));

        // adding observableList(flags) to menuButton(flagsMenuBtn)
        flags_mBtn.getItems().addAll(flags);

        auMatch_CMI.selectedProperty().set(Configs.autoMatch);
        auSplit_CMI.selectedProperty().set(Configs.autoSplit);
        auReplacement_CMI.selectedProperty().set(Configs.autoReplace);
        renderIC_CMI.selectedProperty().set(Configs.renderInvisibleChars);

        // autoupdateMatch checkeMenuItem should be selected to select autoupdateSplit & replacement
        auMatch_CMI.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Configs.autoMatch = newValue;
            if (newValue == false) {
                auSplit_CMI.setSelected(false);
                auReplacement_CMI.setSelected(false);
                auSplit_CMI.setDisable(true);
                auReplacement_CMI.setDisable(true);
            } else {
                auSplit_CMI.setDisable(false);
                auReplacement_CMI.setDisable(false);
            }
        });
        auSplit_CMI.selectedProperty().addListener((ov, oldval, newval) -> Configs.autoSplit = newval);
        auReplacement_CMI.selectedProperty().addListener((ov, oldval, newval) -> Configs.autoReplace = newval);
        renderIC_CMI.selectedProperty().addListener((ov, oldval, newval) -> Configs.renderInvisibleChars = newval);

        // for showing matches in table
        no_col.setCellValueFactory(new PropertyValueFactory<MatchInformation, Integer>("matchNumber"));
        index_col.setCellValueFactory(new PropertyValueFactory<MatchInformation, String>("indexRange"));
        match_col.setCellValueFactory(new PropertyValueFactory<MatchInformation, String>("match"));
        
        // setting font to codearea via css is not working
        // so, font is setted directly
        regex_area.setStyle("-fx-font-family:\"Roboto Mono\";");
        input_area.setStyle("-fx-font-family:\"Roboto Mono\";");
        replacement_area.setStyle("-fx-font-family:\"Roboto Mono\";");
        
        // set focus to regex_area
        regex_area.requestFocus();
    }
    
    // to make textarea grow as we number of lines increase
    // with maximum limit 4
    public void makeAutoGrowable() {

        // binding TextAreas textProperty with TextHolders 
        regexFldHolder.textProperty().bind(regex_area.textProperty());
        replaceTextHolder.textProperty().bind(replaceText_area.textProperty());

        // making regex_area grow as we type
        regexFldHolder.layoutBoundsProperty().addListener((ov, o, n) -> {
            if (n.getHeight() != o.getHeight()) {
                int holderRowCount = computeRowCount(n.getHeight());
                if (holderRowCount != -1 && holderRowCount < Configs.rgxAreaMaxRow + 1) {
                    regex_area.setPrefHeight((17*holderRowCount)+8); // 8 was added as padding height
                }
            }
        });

        // making replaceText_area grow as we type
        replaceTextHolder.layoutBoundsProperty().addListener((ov, o, n) -> {
            if (n.getHeight() != o.getHeight()) {
                int holderRowCount = computeRowCount(n.getHeight());
                if (holderRowCount != -1 && holderRowCount < Configs.replaceTextMaxRow + 1) {
                    replaceText_area.setPrefHeight((17*holderRowCount)+8); // 8 was added as paddiing height
                }
            }
        });
    }

    // returns the number of by computing the height by considering font size as 12
    // however this value is not always true
    // This would change in next release
    private static int computeRowCount(double height) {
        if (height < 160) {
            if (height < 18) {
                return 1;
            } else if (height < 32) {
                return 2;
            } else if (height < 48) {
                return 3;
            } else if (height < 64) {
                return 4;
            } else if (height < 80) {
                return 5;
            } else if (height < 96) {
                return 6;
            } else if (height < 112) {
                return 7;
            } else if (height < 128) {
                return 8;
            } else if (height < 144) {
                return 9;
            } else if (height < 160) {
                return 10;
            }
        }
        return -1;
    }

    // displays the match in table (match_table)
    public void showMatch() {
        match_table.getItems().clear();
        String inString = input_area.getText();
        int n = 0;
        for (IndexRange ir : matches) {
            // render invisible chracters if option enabled
            String matchString = (Configs.renderInvisibleChars)
                    ? Utility.renderInvisibleChars(inString.substring(ir.getStart(), ir.getEnd()))
                    : inString.substring(ir.getStart(), ir.getEnd());
            match_table.getItems().add(new MatchInformation(n + 1, ir.toString(), matchString));
            n++;
        }
    }

    // split the input string and display in the listview (split_LV)
    public void showSplit() {
        split_LV.getItems().clear();
        String inString = input_area.getText();
        int limit = (limit_fld.getText().isEmpty()) ? 0 : Integer.parseInt(limit_fld.getText());
        int idx = 0, splitLenth = 0;
        ArrayList<String> ref = new ArrayList<>();
        // spliting the string
        for (IndexRange ir : matches) {
            if (splitLenth == limit - 1) {
                break;
            }
            ref.add(inString.substring(idx, ir.getStart()));
            idx = ir.getEnd();
            splitLenth++;
        }
        if (idx < inString.length()) {
            ref.add(inString.substring(idx));
            splitLenth++;
        }
        // add splited to splitlistview by rendering invisibel character if option enabled
        if (Configs.renderInvisibleChars)
            split_LV.getItems().addAll(ref.stream().map((s) -> Utility.renderInvisibleChars(s)).collect(Collectors.toList()));
        else
            split_LV.getItems().addAll(ref);
  
        sLength_txt.setText(Integer.toString(splitLenth));
    }

    public void showReplacement() {
        replacement_area.clear();
        String toReplace = replaceText_area.getText();
        String text = input_area.getText();
        int idx = 0;
        for (IndexRange ir : matches) {
            replacement_area.append(text.substring(idx, ir.getStart()), "none");    //append un-matched value
            replacement_area.append(toReplace, "replacement");   //append replacement with highlithing
            idx = ir.getEnd();  //trim buffer to next unmatched value 
        }
        //append unmatched value that remains in buffer 
        if (idx < text.length())
            replacement_area.append(text.substring(idx), "none");
    }

    // performs match on the input string by regex and stores the match index in variable 'matches'
    public void doMatch() {
        this.matches.clear();
        String regex = regex_area.getText(),
                toMatch = input_area.getText();
        // if regex is empty it does not perform match and update 'no match' in status
        if (regex.isEmpty()) {
            updateStatus("no match", null);
            return;
        }
        Matcher matcher = null;
        // if error occurs it was handeld by 'handlePatternError' method 
        try {
            matcher = Pattern.compile(regex, Configs.flagInt).matcher(toMatch);
        } catch (PatternSyntaxException pse) {
            handlPatternError(pse.getDescription(), pse.getIndex());
            return;
        }
        while (matcher.find())
            matches.add(new IndexRange(matcher.start(), matcher.end()));
     
        int n = matches.size();
        // update match status
        updateStatus(((n == 0) ? "no match" : (n == 1) ? "1 match" : n + " matches"), null);
    }

    // highlights the matched string
    public void highlightMatch() {
        matches.forEach((ir) -> input_area.setStyleClass(ir.getStart(), ir.getEnd(), "match"));
    }

    // show message about error and highlight the error
    public void handlPatternError(String msg, int idx) {
        idx -= 1;
        if (msg.startsWith("Unclosed group")) {
            String regex = regex_area.getText();
            if (regex.charAt(idx) != '(') {
                idx = regex.lastIndexOf("(", idx);
            }
        } else if (msg.startsWith("Unclosed character class")) {
            idx += 1;
            String regex = regex_area.getText();
            if (regex.charAt(idx) != '[') {
                idx = regex.lastIndexOf("[", idx);
            }
        } else if (msg.startsWith("Dangling meta character")) {
            idx += 1;
        } else if (msg.startsWith("Unmatched closing") || msg.startsWith("Illegal repetition")) {
            idx += 2;
        }

        updateStatus("Pattern Error", msg + " at index " + (idx + 1));
        regex_area.setStyleClass(idx, idx + 1, "error");
    }

    /*
    * update status with message
    * if status is null, retain old status (used to update message only)
    * if msg is null, set empty message
    */
    public void updateStatus(String status, String msg) {
        if (status != null) {
            status_lbl.setText(status);
        }
        msg_lbl.setText((msg == null) ? "" :msg);
    }

    // update flagInt with flag code by performing bitwise operation in selected flags
    // this method is called every time when the flag selection is changed
    private void computeFlags() {
        Configs.flagInt = 0;
        flags.forEach((t) -> {
            if (t.isSelected()) {
                Configs.flagInt = Configs.flagInt | Configs.flagsNvalue.get(t.getText());
            }
        });
    }
    
    @FXML
    public void onTyped() {
        regex_area.clearStyle(0, regex_area.getLength());
        input_area.clearStyle(0, input_area.getLength());
        if (Configs.autoMatch) {
            doMatch();
            if (matches != null) {
                highlightMatch();
                showMatch();
                if (Configs.autoSplit)
                    showSplit();
                if (Configs.autoReplace)
                    showReplacement();
            }
        }
    }

    @FXML
    public void onMatchClick() {
        doMatch();
        if (matches != null) {
            highlightMatch();
            showMatch();
        }
    }

    @FXML
    public void onSplitClick() {
        doMatch();
        if (matches != null)
            showSplit();
    }

    @FXML
    public void onReplaceClick() {
        doMatch();
        if (matches != null)
            showReplacement();
    }

    @FXML
    public void copyRgx() {
        String toCopy = regex_area.getText();
        if (toCopy.isEmpty())
            updateStatus(null, "Nothing to copy");
        else {
            Utility.copyToClipboard(toCopy);
            updateStatus(null, "Regex copied successfully");
        }
    }

    @FXML
    public void copyRgxAsString() {
        String regex = regex_area.getText();
        if (regex.isEmpty())
            updateStatus(null, "Nothing to copy");
        else {
            Utility.copyToClipboard(regex.replace("\\", "\\\\").replace("\"", "\\\"").replace("\'", "\\\'"));
            updateStatus(null, "Regex copied successfully");
        }
    }
    
    @FXML
    public void showAboutWindow() {
        Stage aStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/about.fxml"));
        VBox root = null;
        try {
            root = loader.load();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Scene aboutScene = new Scene(root);
        aStage.setTitle("About RegexAssist");
        aStage.setResizable(false);
        aStage.initModality(Modality.WINDOW_MODAL);
        aStage.initOwner(App.primaryStage);
        aStage.getIcons().add(App.ICON);
        aStage.setScene(aboutScene);

        aStage.show();

    }
}