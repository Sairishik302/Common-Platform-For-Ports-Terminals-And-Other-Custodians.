import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.sql.SQLException;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

public class PortsForm extends GridPane {
    private TextField portIdField;
    private TextField portNameField;
    private TextField countryField;
    private Button addButton;

    private Port portToUpdate; // Variable to hold the port being updated
    private boolean formSubmitted = false;

    private Stage ownerStage;

    public PortsForm(Port port) {
        this(); // Call the default constructor to initialize the form

        // Set the port to update and fill the fields with its data
        portToUpdate = port;
        fillFieldsWithData(portToUpdate);

        // Change the button text to "Update" and update the event handler
        addButton.setText("Update");
        addButton.setOnAction(e -> updatePort());
    }

    public PortsForm() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label portIdLabel = new Label("Port ID:");
        Label portNameLabel = new Label("Port Name:");
        Label countryLabel = new Label("Country:");
        portIdField = new TextField();
        portNameField = new TextField();
        countryField = new TextField();
        addButton = new Button("Add");

        add(portIdLabel, 0, 0);
        add(portIdField, 1, 0);
        add(portNameLabel, 0, 1);
        add(portNameField, 1, 1);
        add(countryLabel, 0, 2);
        add(countryField, 1, 2);
        add(addButton, 0, 3);

        addButton.setOnAction(e -> addPort());
    }

    private void addPort() {
        int portId = Integer.parseInt(portIdField.getText());
        String portName = portNameField.getText();
        String country = countryField.getText();

        // Perform the add operation
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.connect();
            databaseManager.insertPort(portId, portName, country);
            databaseManager.disconnect();
            clearFields();
            showSuccessMessage("Port added successfully.");
        } catch (SQLException ex) {
            showErrorMessage("Error adding port: " + ex.getMessage());
        }
    }

    public void setTitle(String title) {
        // Set the title of the form
        if (ownerStage != null) {
            ownerStage.setTitle(title);
        }
    }

    public void setOwnerStage(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    public void show() {
        // Show the form
        Stage stage = new Stage();
        Scene scene = new Scene(this);
        stage.setScene(scene);
        stage.show();
    }

    public boolean isFormSubmitted() {
        // Return whether the form was submitted or not
        return formSubmitted;
    }

    private void updatePort() {
        // Check if the port to update is set
        if (portToUpdate == null) {
            return;
        }

        // Get the updated values from the form fields
        int portId = Integer.parseInt(portIdField.getText());
        String portName = portNameField.getText();
        String country = countryField.getText();

        // Update the port in the database
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.connect();
            databaseManager.updatePort(portToUpdate.getPortId(), portName, country);
            databaseManager.disconnect();
            showSuccessMessage("Port updated successfully.");
        } catch (SQLException ex) {

            showErrorMessage("Error updating port: " + ex.getMessage());
        }

        // Close the form's window
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }

    private void fillFieldsWithData(Port port) {
        portIdField.setText(String.valueOf(port.getPortId()));
        portNameField.setText(port.getPortName());
        countryField.setText(port.getCountry());
    }

    private void clearFields() {
        portIdField.clear();
        portNameField.clear();
        countryField.clear();
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        // Close the form's window
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
