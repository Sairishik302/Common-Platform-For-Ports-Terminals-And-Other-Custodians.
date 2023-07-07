
//javac -cp ojdbc6.jar MainUI.java PortsForm.java CustodiansForm.java TerminalsForm.java ShipmentsForm.java DatabaseManager.java
//java -cp .;ojdbc6.jar MainUI


import javafx.animation.FadeTransition;
import java.util.Optional;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.stage.Stage;
import javafx.scene.Scene;


import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.sql.SQLException;
import javafx.scene.layout.HBox;
import java.util.List;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainUI extends Application {
    private Stage primaryStage;
	//private VBox container; 

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("Port Management System");

        BorderPane root = new BorderPane();

        // Create and configure the animated text
        Text animatedText = new Text("Welcome to Port Management System");
        animatedText.setFont(Font.font("Arial",FontWeight.BOLD, 40));
        animatedText.setFill(Color.BLACK);

        // Create fade transition for text
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3.5), animatedText);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(1);

        fadeTransition.play();

        root.setCenter(animatedText);

        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.DARKBLUE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
		Menu dataMenu = new Menu("View Data");

		MenuItem displayMenuItem = new MenuItem("Display");
		displayMenuItem.setOnAction(e -> displayData());
		dataMenu.getItems().add(displayMenuItem);

		// Add other menu items for ports, terminals, custodians, shipments

		menuBar.getMenus().add(dataMenu);

        Menu portMenu = new Menu("Ports");
        MenuItem portMenuItem = new MenuItem("Add Port");
        portMenuItem.setOnAction(e -> showPortsForm());
        portMenu.getItems().add(portMenuItem);

        Menu shipmentMenu = new Menu("Shipments");
        MenuItem shipmentMenuItem = new MenuItem("Add Shipments");
        shipmentMenuItem.setOnAction(e -> showShipmentsForm());
        shipmentMenu.getItems().add(shipmentMenuItem);

        Menu custodianMenu = new Menu("Custodians");
        MenuItem custodianMenuItem = new MenuItem("Add Custodians");
        custodianMenuItem.setOnAction(e -> showCustodiansForm());
        custodianMenu.getItems().add(custodianMenuItem);

        Menu terminalMenu = new Menu("Terminals");
        MenuItem terminalMenuItem = new MenuItem("Add Terminals");
        terminalMenuItem.setOnAction(e -> showTerminalsForm());
        terminalMenu.getItems().add(terminalMenuItem);

        menuBar.getMenus().addAll(portMenu, shipmentMenu, custodianMenu, terminalMenu);

        return menuBar;
    }

    /*private void showPortsForm() {
        PortsForm portsForm = new PortsForm();
        Scene scene = new Scene(portsForm, 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Manage Ports");
        stage.show();
    }*/

	private void showShipmentsForm() {
		ShipmentsForm shipmentsForm = new ShipmentsForm();
		Scene scene = new Scene(shipmentsForm, 600, 400);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Manage Shipments");
		
		stage.setOnHidden(e -> {
			primaryStage.show(); 
			stage.close(); // Close the shipments form
		});
		
		stage.showAndWait(); // Show the shipments form and wait for it to close
	}
	private VBox container; // Declare container as a class member

	private void displayData() {
		try {
			// Fetch the data from the database using the DatabaseManager class
			DatabaseManager databaseManager = new DatabaseManager();
			databaseManager.connect();
			List<Port> ports = databaseManager.getAllPorts();
			List<Terminal> terminals = databaseManager.getAllTerminals();
			List<Custodian> custodians = databaseManager.getAllCustodians();
			List<Shipment> shipments = databaseManager.getAllShipments();
			databaseManager.disconnect();

			// Create UI components to display the data
			TableView<Port> portTable = createPortTable(ports);
			TableView<Terminal> terminalTable = createTerminalTable(terminals);
			TableView<Custodian> custodianTable = createCustodianTable(custodians);
			TableView<Shipment> shipmentTable = createShipmentTable(shipments);

			// Create a VBox to hold the UI components
			VBox container = new VBox();
			container.setSpacing(10);
			container.setPadding(new Insets(10));

			// Create the back button
			Button backButton = new Button("Back");
			backButton.setOnAction(e -> showHome());
			container.getChildren().add(backButton);

			// Add the UI components to the container pane
			container.getChildren().addAll(portTable, terminalTable, custodianTable, shipmentTable);

			// Create a scene and set it in the primaryStage
			Scene scene = new Scene(container, 800, 600);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (SQLException ex) {
			showErrorMessage("Error fetching data: " + ex.getMessage());
		}
	}


	private TableView<Port> createPortTable(List<Port> ports) {
		TableView<Port> table = new TableView<>();

		TableColumn<Port, Integer> portIdColumn = new TableColumn<>("Port ID");
		portIdColumn.setCellValueFactory(new PropertyValueFactory<>("portId"));

		TableColumn<Port, String> portNameColumn = new TableColumn<>("Port Name");
		portNameColumn.setCellValueFactory(new PropertyValueFactory<>("portName"));

		TableColumn<Port, String> countryColumn = new TableColumn<>("Country");
		countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

			TableColumn<Port, Void> actionsColumn = new TableColumn<>("Actions");
		actionsColumn.setCellFactory(param -> {
			return new TableCell<Port, Void>() {
				private final Button updateButton = new Button("Update");
				private final Button deleteButton = new Button("Delete");

				{
					updateButton.setOnAction(event -> {
						Port port = (Port) getTableRow().getItem();
						openPortForm(port);
					});

					deleteButton.setOnAction(event -> {
						Port port = (Port) getTableRow().getItem();
						deletePort(port);
					});
				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(new HBox(updateButton, deleteButton));
					}
				}
			};
		});

		table.getColumns().addAll(portIdColumn, portNameColumn, countryColumn, actionsColumn);
		table.getItems().addAll(ports);

		return table;
	}
	// Inside the MainUI class

	private void openPortForm(Port port) {
		PortsForm portsForm = new PortsForm(port);
		portsForm.setOwnerStage(primaryStage); // Set the owner stage
		portsForm.setTitle("Update Port");
		portsForm.show();
	}

	private void deletePort(Port port) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Delete Port");
		alert.setContentText("Are you sure you want to delete the port?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				DatabaseManager databaseManager = new DatabaseManager();
				databaseManager.connect();
				databaseManager.deletePort(port.getPortId());
				databaseManager.disconnect();
				showInformationMessage("Port deleted successfully.");
				displayData();
			} catch (SQLException ex) {
				showErrorMessage("Error deleting port: " + ex.getMessage());
			}
		}
	}
	 private void showInformationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



	

	private void showErrorMessage(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}





    private void showCustodiansForm() {
        CustodiansForm custodiansForm = new CustodiansForm();
        Scene scene = new Scene(custodiansForm, 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Manage Custodians");
        stage.show();
    }

    private void showTerminalsForm() {
        TerminalsForm terminalsForm =new TerminalsForm();
        Scene scene = new Scene(terminalsForm, 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Manage Terminals");
        stage.show();
    }
	// Inside the MainUI class

	private void openTerminalForm(Terminal terminal) {
		// Create a new instance of the TerminalsForm
		TerminalsForm terminalsForm = new TerminalsForm(terminal);
		
		// Set the title for the form
		terminalsForm.setTitle("Update Terminal");
		
		// Show the form
		terminalsForm.show();
		
		// Refresh the terminal table after the form is closed
		if (terminalsForm.isFormSubmitted()) {
			displayData();
		}
	}

	private void deleteTerminal(Terminal terminal) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Delete Terminal");
		alert.setContentText("Are you sure you want to delete the terminal?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				DatabaseManager databaseManager = new DatabaseManager();
				databaseManager.connect();
				databaseManager.deleteTerminal(terminal.getTerminalId());
				databaseManager.disconnect();
				showInformationMessage("Terminal deleted successfully.");
				displayData();
			} catch (SQLException ex) {
				showErrorMessage("Error deleting terminal: " + ex.getMessage());
			}
		}
	}


	private void openCustodianForm(Custodian custodian) {
		// Create a new instance of the CustodiansForm
		CustodiansForm custodiansForm = new CustodiansForm(custodian);
		
		// Set the title for the form
		custodiansForm.setTitle("Update Custodian");
		
		// Show the form
		custodiansForm.show();
		
		// Refresh the custodian table after the form is closed
		if (custodiansForm.isFormSubmitted()) {
			displayData();
		}
	}

	private void deleteCustodian(Custodian custodian) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Delete Custodian");
		alert.setContentText("Are you sure you want to delete the custodian?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				DatabaseManager databaseManager = new DatabaseManager();
				databaseManager.connect();
				databaseManager.deleteCustodian(custodian.getCustodianId());
				databaseManager.disconnect();
				showInformationMessage("Custodian deleted successfully.");
				displayData();
			} catch (SQLException ex) {
				showErrorMessage("Error deleting custodian: " + ex.getMessage());
			}
		}
	}

	private void openShipmentForm(Shipment shipment) {
		// Create a new instance of the ShipmentsForm
		ShipmentsForm shipmentsForm = new ShipmentsForm(shipment);
		
		// Set the title for the form
		shipmentsForm.setTitle("Update Shipment");
		
		// Show the form
	shipmentsForm.show();
		
		// Refresh the shipment table after the form is closed
		if (shipmentsForm.isFormSubmitted()) {
			displayData();
		}
	}

	private void deleteShipment(Shipment shipment) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Delete Shipment");
    alert.setContentText("Are you sure you want to delete the shipment?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.connect();
            databaseManager.deleteShipment(shipment.getShipmentId());
            databaseManager.disconnect();
            showInformationMessage("Shipment deleted successfully.");
            displayData();
        } catch (SQLException ex) {
            showErrorMessage("Error deleting shipment: " + ex.getMessage());
        }
    }
	}
	
	private TableView<Terminal> createTerminalTable(List<Terminal> terminals) {
		TableView<Terminal> table = new TableView<>();

		TableColumn<Terminal, Integer> terminalIdColumn = new TableColumn<>("Terminal ID");
		terminalIdColumn.setCellValueFactory(new PropertyValueFactory<>("terminalId"));

		TableColumn<Terminal, String> terminalNameColumn = new TableColumn<>("Terminal Name");
		terminalNameColumn.setCellValueFactory(new PropertyValueFactory<>("terminalName"));

		TableColumn<Terminal, Void> actionsColumn = new TableColumn<>("Actions");
		actionsColumn.setCellFactory(new Callback<TableColumn<Terminal, Void>, TableCell<Terminal, Void>>() {
			@Override
			public TableCell<Terminal, Void> call(TableColumn<Terminal, Void> param) {
				return new TableCell<Terminal, Void>() {
					private final Button updateButton = new Button("Update");
					private final Button deleteButton = new Button("Delete");

					{
						updateButton.setOnAction(event -> {
							Terminal terminal = (Terminal) getTableRow().getItem();
							openTerminalForm(terminal);
						});

						deleteButton.setOnAction(event -> {
							Terminal terminal = (Terminal) getTableRow().getItem();
							deleteTerminal(terminal);
						});
					}

					@Override
					protected void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(new HBox(updateButton, deleteButton));
						}
					}
				};
			}
		});

		table.getColumns().addAll(terminalIdColumn, terminalNameColumn, actionsColumn);
		table.getItems().addAll(terminals);

		return table;
	}

	private TableView<Custodian> createCustodianTable(List<Custodian> custodians) {
		TableView<Custodian> table = new TableView<>();

		TableColumn<Custodian, Integer> custodianIdColumn = new TableColumn<>("Custodian ID");
		custodianIdColumn.setCellValueFactory(new PropertyValueFactory<>("custodianId"));

		TableColumn<Custodian, String> custodianNameColumn = new TableColumn<>("Custodian Name");
		custodianNameColumn.setCellValueFactory(new PropertyValueFactory<>("custodianName"));

		TableColumn<Custodian, Integer> terminalIdColumn = new TableColumn<>("Terminal ID");
		terminalIdColumn.setCellValueFactory(new PropertyValueFactory<>("terminalId"));

		TableColumn<Custodian, Void> actionsColumn = new TableColumn<>("Actions");
		actionsColumn.setCellFactory(param -> {
			return new TableCell<Custodian, Void>() {
				private final Button updateButton = new Button("Update");
				private final Button deleteButton = new Button("Delete");

				{
					updateButton.setOnAction(event -> {
						Custodian custodian = (Custodian) getTableRow().getItem();
						openCustodianForm(custodian);
					});

					deleteButton.setOnAction(event -> {
						Custodian custodian = (Custodian) getTableRow().getItem();
						deleteCustodian(custodian);
					});
				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(new HBox(updateButton, deleteButton));
					}
				}
			};
		});


		table.getColumns().addAll(custodianIdColumn, custodianNameColumn, terminalIdColumn, actionsColumn);
		table.getItems().addAll(custodians);

		return table;
	}

	private TableView<Shipment> createShipmentTable(List<Shipment> shipments) {
		TableView<Shipment> table = new TableView<>();

		TableColumn<Shipment, Integer> shipmentIdColumn = new TableColumn<>("Shipment ID");
		shipmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("shipmentId"));

		TableColumn<Shipment, String> shipmentNameColumn = new TableColumn<>("Shipment Name");
		shipmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipmentName"));

		TableColumn<Shipment, Integer> terminalIdColumn = new TableColumn<>("Terminal ID");
		terminalIdColumn.setCellValueFactory(new PropertyValueFactory<>("terminalId"));

		TableColumn<Shipment, String> shipmentStatusColumn = new TableColumn<>("Shipment Status");
		shipmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("shipmentStatus"));

		TableColumn<Shipment, Void> actionsColumn = new TableColumn<>("Actions");
			actionsColumn.setCellFactory(param -> {
				return new TableCell<Shipment, Void>() {
					private final Button updateButton = new Button("Update");
					private final Button deleteButton = new Button("Delete");

					{
						updateButton.setOnAction(event -> {
							Shipment shipment = (Shipment) getTableRow().getItem();
							openShipmentForm(shipment);
						});

						deleteButton.setOnAction(event -> {
							Shipment shipment = (Shipment) getTableRow().getItem();
							deleteShipment(shipment);
						});
					}

					@Override
					protected void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(new HBox(updateButton, deleteButton));
						}
					}
				};
			});


		table.getColumns().addAll(shipmentIdColumn, shipmentNameColumn, terminalIdColumn, shipmentStatusColumn, actionsColumn);
		table.getItems().addAll(shipments);

		return table;
	}
	private void showPortsForm() {
		PortsForm portsForm = new PortsForm();
		Scene scene = new Scene(portsForm, 600, 400);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Manage Ports");

		// Set the owner stage for portsForm
		portsForm.setOwnerStage(primaryStage);

		stage.show();
	}
	private void showHome() {
    // Recreate the initial scene and set it in the primaryStage
    primaryStage.setScene(createHomePage());
}

	private Scene createHomePage() {
		BorderPane root = new BorderPane();

		// Create and configure the animated text
		Text animatedText = new Text("Welcome to Port Management System");
		animatedText.setFont(Font.font("Arial", FontWeight.BOLD, 40));
		animatedText.setFill(Color.BLACK);

		// Create fade transition for text
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3.5), animatedText);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.setCycleCount(1);

		fadeTransition.play();

		root.setCenter(animatedText);

		// Create the back button
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> showHome());
		root.setBottom(backButton);

		MenuBar menuBar = createMenuBar();
		root.setTop(menuBar);

		Scene scene = new Scene(root, 800, 600);
		scene.setFill(Color.DARKBLUE);

		return scene;
	}


	





}
