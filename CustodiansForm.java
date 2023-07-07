import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.sql.SQLException;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

public class CustodiansForm extends GridPane {
    private TextField custodianIdField;
    private TextField custodianNameField;
    private Button addButton;
	private TextField terminalIdField;

    private Custodian custodianToUpdate; // Variable to hold the custodian being updated
    private boolean formSubmitted = false;

    private Stage ownerStage;

    public CustodiansForm(Custodian custodian) {
        this(); // Call the default constructor to initialize the form

        // Set the custodian to update and fill the fields with its data
        custodianToUpdate = custodian;
        fillFieldsWithData(custodianToUpdate);

        // Change the button text to "Update" and update the event handler
        addButton.setText("Update");
        addButton.setOnAction(e -> updateCustodian());
    }

    public CustodiansForm() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label custodianIdLabel = new Label("Custodian ID:");
        Label custodianNameLabel = new Label("Custodian Name:");
        custodianIdField = new TextField();
        custodianNameField = new TextField();
		terminalIdField = new TextField();
		add(new Label("Terminal ID:"), 0, 2);
		add(terminalIdField, 1, 2);
        addButton = new Button("Add");

        add(custodianIdLabel, 0, 0);
        add(custodianIdField, 1, 0);
        add(custodianNameLabel, 0, 1);
        add(custodianNameField, 1, 1);
        add(addButton, 0, 3);

        addButton.setOnAction(e -> addCustodian());
    }

    private void addCustodian() {
		int custodianId = Integer.parseInt(custodianIdField.getText());
		String custodianName = custodianNameField.getText();
		int terminalId = Integer.parseInt(terminalIdField.getText()); // Retrieve the terminal ID

		// Perform the add operation
		try {
			DatabaseManager databaseManager = new DatabaseManager();
			databaseManager.connect();
			databaseManager.insertCustodian(custodianId, custodianName, terminalId); // Pass the terminal ID to the insertCustodian() method
			databaseManager.disconnect();
			clearFields();
			showSuccessMessage("Custodian added successfully.");
		} catch (SQLException ex) {
			showErrorMessage("Error adding custodian: " + ex.getMessage());
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

    private void updateCustodian() {
		// Check if the custodian to update is set
		if (custodianToUpdate == null) {
			return;
		}

		// Get the updated values from the form fields
		int custodianId = Integer.parseInt(custodianIdField.getText());
		String custodianName = custodianNameField.getText();

		// Update the custodian in the database
		try {
			DatabaseManager databaseManager = new DatabaseManager();
			databaseManager.connect();
			databaseManager.updateCustodian(custodianToUpdate.getCustodianId(), custodianName);
			databaseManager.disconnect();
			showSuccessMessage("Custodian updated successfully.");
		} catch (SQLException ex) {
			showErrorMessage("Error updating custodian: " + ex.getMessage());
		}

		// Close the form's window
		Stage stage = (Stage) getScene().getWindow();
		stage.close();
	}
    private void fillFieldsWithData(Custodian custodian) {
        custodianIdField.setText(String.valueOf(custodian.getCustodianId()));
        custodianNameField.setText(custodian.getCustodianName());
    }

    private void clearFields() {
        custodianIdField.clear();
        custodianNameField.clear();
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
