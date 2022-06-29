module it.unipd.sweng {
    requires javafx.controls;
    requires javafx.fxml;
    //requires javafx.web;

    requires org.controlsfx.controls;
    //requires com.dlsc.formsfx;
    //requires validatorfx;
    //requires org.kordamp.ikonli.javafx;
    //requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;
    //requires com.almasb.fxgl.all;
    requires org.json;
    requires java.desktop;

    opens it.unipd.sweng to javafx.fxml;
    exports it.unipd.sweng;
    exports lib.interfaces;
    opens lib.interfaces to javafx.fxml;
    exports lib.internal;
    opens lib.internal to javafx.fxml;
    exports lib;
    opens lib to javafx.fxml;
}