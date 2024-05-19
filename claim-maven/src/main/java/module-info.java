

module claim.maven {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;

    opens org.claimsystem.g24fp to javafx.fxml;
    opens org.claimsystem.g24fp.controllers to javafx.fxml;
    opens org.claimsystem.g24fp.controllers.customer to javafx.fxml;
    opens org.claimsystem.g24fp.controllers.provider to javafx.fxml;
    opens org.claimsystem.g24fp.controllers.admin to javafx.fxml;


    opens org.claimsystem.g24fp.model to javafx.fxml;
    opens org.claimsystem.g24fp.model.user to javafx.base;

    exports org.claimsystem.g24fp;
    exports org.claimsystem.g24fp.controllers;
    exports org.claimsystem.g24fp.controllers.admin;
    exports org.claimsystem.g24fp.controllers.customer;
    exports org.claimsystem.g24fp.controllers.provider;
    exports org.claimsystem.g24fp.model;
    exports org.claimsystem.g24fp.model.user;
}