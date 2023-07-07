import javafx.geometry.Insets;
import javafx.scene.control.*;
import java.sql.SQLException;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Scene;



public class TerminalsForm extends GridPane {
    private TextField terminalIdField;
    private TextField terminalNameField;
    private TextField portIdField;
	private Stage ownerStage;

    private Button addButton;
	private boolean formSubmitted = false;


    private Terminal terminalToUpdate; // Variable to hold the terminal being updated

    public TerminalsForm(Terminal terminal) {
        this(); // Call the default constructor to initialize the form

        // Set the terminal to update and fill the fields with its data
        terminalToUpdate = terminal;
        fillFieldsWithData(terminalToUpdate);

        // Change the button text to "Update" and update the event handler
        addButton.setText("Update");
        addButton.setOnAction(e -> updateTerminal());
    }

    public TerminalsForm() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label terminalIdLabel = new Label("Terminal ID:");
        Label terminalNameLabel = new Label("Terminal Name:");
        Label portIdLabel = new Label("Port ID:");
        terminalIdField = new TextField();
        terminalNameField = new TextField();
        portIdField = new TextField();
        addButton = new Button("Add");

        add(terminalIdLabel, 0, 0);
        add(terminalIdField, 1, 0);
        add(terminalNameLabel, 0, 1);
        add(terminalNameField, 1, 1);
        add(portIdLabel, 0, 2);
        add(portIdField, 1, 2);
        add(addButton, 0, 3);

        addButton.setOnAction(e -> addTerminal());
    }

    private void addTerminal() {
        int terminalId = Integer.parseInt(terminalIdField.getText());
        String terminalName = terminalNameField.getText();
        int portId = Integer.parseInt(portIdField.getText());
		

        // Perform the add operation
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.connect();
            databaseManager.insertTerminal(terminalId, terminalName, portId);

            databaseManager.disconnect();
            clearFields();
            showSuccessMessage("Terminal added successfully.");
        } catch (SQLException ex) {
            showErrorMessage("Error adding terminal: " + ex.getMessage());
        }
    }

    private void updateTerminal() {
        // Check if the terminal to update is set
        if (terminalToUpdate == null) {
            return;
        }

        // Get the updated values from the form fields
        int terminalId = Integer.parseInt(terminalIdField.getText());
        String terminalName = terminalNameField.getText();
        int portId = Integer.parseInt(portIdField.getText());
		
        // Update the terminal in the database
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.updateTerminal(terminalToUpdate.getTerminalId(), terminalName, portId);


            databaseManager.disconnect();
            showSuccessMessage("Terminal updated successfully.");
        } catch (SQLException ex) {
            showErrorMessage("Error updating terminal: " + ex.getMessage());
        }

        getScene().getWindow().hide(); // Close the form's window
    }

    private void fillFieldsWithData(Terminal terminal) {
        terminalIdField.setText(String.valueOf(terminal.getTerminalId()));
        terminalNameField.setText(terminal.getTerminalName());
        portIdField.setText(String.valueOf(terminal.getPortId()));
    }

    private void clearFields() {
        terminalIdField.clear();
terminalNameField.clear();
        portIdField.clear();
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
	public void setOwnerStage(Stage ownerStage) {
		this.ownerStage = ownerStage;
	}

	public void setTitle(String title) {
		// Set the title of the form
		if (ownerStage != null) {
			ownerStage.setTitle(title);
		}
	}



    public void show() {
		if (ownerStage != null) {
			Scene scene = new Scene(this);
			ownerStage.setScene(scene);
			ownerStage.show();
		}
	}


    public boolean isFormSubmitted() {
        // Return whether the form was submitted or not
        return formSubmitted;
    }
}
