module RegexAssist {
    
    requires javafx.controlsEmpty;
    requires javafx.controls;
    requires javafx.graphicsEmpty;
    requires javafx.graphics;
    requires javafx.baseEmpty;
    requires javafx.base;
    requires javafx.fxmlEmpty;
    requires javafx.fxml;
    requires org.fxmisc.richtext;
    requires reactfx;
    requires undofx;
    requires flowless;
    requires wellbehavedfx;
    
    opens org.codefh.regexassist to javafx.fxml;
    exports org.codefh.regexassist;
}
