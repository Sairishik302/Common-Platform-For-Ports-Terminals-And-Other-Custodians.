import javafx.geometry.Insets;
import javafx.scene.control.*;
import java.sql.SQLException;
import javafx.stage.Stage;
import javafx.scene.Scene;


import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

public class ShipmentsForm extends GridPane {
    private TextField shipmentIdField;
    private TextField shipmentNameField;
    private TextField terminalIdField;
    private TextField shipmentStatusField;
    private Button addButton;
	private Stage ownerStage;
	private boolean formSubmitted = false;


    private Shipment shipmentToUpdate; // Variable to hold the shipment being updated

    public ShipmentsForm(Shipment shipment) {
        this(); // Call the default constructor to initialize the form

        // Set the shipment to update and fill the fields with its data
        shipmentToUpdate = shipment;
        fillFieldsWithData(shipmentToUpdate);

        // Change the button text to "Update" and update the event handler
        addButton.setText("Update");
        addButton.setOnAction(e -> updateShipment());
    }

    public ShipmentsForm() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label shipmentIdLabel = new Label("Shipment ID:");
        Label shipmentNameLabel = new Label("Shipment Name:");
        Label terminalIdLabel = new Label("Terminal ID:");
        Label shipmentStatusLabel = new Label("Shipment Status:");
        shipmentIdField = new TextField();
        shipmentNameField = new TextField();
        terminalIdField = new TextField();
        shipmentStatusField = new TextField();
        addButton = new Button("Add");

        add(shipmentIdLabel, 0, 0);
        add(shipmentIdField, 1, 0);
        add(shipmentNameLabel, 0, 1);
        add(shipmentNameField, 1, 1);
        add(terminalIdLabel, 0, 2);
        add(terminalIdField, 1, 2);
        add(shipmentStatusLabel, 0, 3);
        add(shipmentStatusField, 1, 3);
        add(addButton, 0, 4);

        addButton.setOnAction(e -> addShipment());
    }

    private void addShipment() {
        int shipmentId = Integer.parseInt(shipmentIdField.getText());
        String shipmentName = shipmentNameField.getText();
        int terminalId = Integer.parseInt(terminalIdField.getText());
        String shipmentStatus = shipmentStatusField.getText();

        // Perform theadd operation
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.connect();
            databaseManager.insertShipment(shipmentId, shipmentName, terminalId, shipmentStatus);
            databaseManager.disconnect();
            clearFields();
            showSuccessMessage("Shipment added successfully.");
        } catch (SQLException ex) {
            showErrorMessage("Error adding shipment: " + ex.getMessage());
        }
    }

    private void updateShipment() {
        // Check if the shipment to update is set
        if (shipmentToUpdate == null) {
            return;
        }

        // Get the updated values from the form fields
        int shipmentId = Integer.parseInt(shipmentIdField.getText());
        String shipmentName = shipmentNameField.getText();
        int terminalId = Integer.parseInt(terminalIdField.getText());
        String shipmentStatus = shipmentStatusField.getText();

        // Update the shipment in the database
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.connect();
            databaseManager.updateShipment(shipmentToUpdate.getShipmentId(), shipmentName, terminalId, shipmentStatus);


            databaseManager.disconnect();
            showSuccessMessage("Shipment updated successfully.");
        } catch (SQLException ex) {
            showErrorMessage("Error updating shipment: " + ex.getMessage());
        }

        getScene().getWindow().hide(); // Close the form's window
    }

    private void fillFieldsWithData(Shipment shipment) {
        shipmentIdField.setText(String.valueOf(shipment.getShipmentId()));
        shipmentNameField.setText(shipment.getShipmentName());
        terminalIdField.setText(String.valueOf(shipment.getTerminalId()));
        shipmentStatusField.setText(shipment.getShipmentStatus());
    }

    private void clearFields() {
        shipmentIdField.clear();
        shipmentNameField.clear();
        terminalIdField.clear();
        shipmentStatusField.clear();
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        getScene().getWindow().hide(); // Close the form's window
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	public void setTitle(String title) {
		// Set the title of the form
		if (ownerStage != null) {
			ownerStage.setTitle(title);
		}
	}


    public void show() {
		Stage stage = new Stage();
		Scene scene = new Scene(this);
		stage.setScene(scene);
		stage.showAndWait();
	}


    public boolean isFormSubmitted() {
        // Return whether the form was submitted or not
        return formSubmitted;
    }
	public void setOwnerStage(Stage ownerStage) {
		this.ownerStage = ownerStage;
	}

	

}
