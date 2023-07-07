import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;





public class DatabaseManager {
    private Connection connection;
    private final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private final String DB_USER = "rishik";
    private final String DB_PASSWORD = "1819";

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        System.out.println("Connected to the database.");
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Disconnected from the database.");
        }
    }
	public List<Port> getAllPorts() throws SQLException {
        List<Port> ports = new ArrayList<>();
        String query = "SELECT * FROM Ports";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            int portId = resultSet.getInt("port_id");
            String portName = resultSet.getString("port_name");
            String country = resultSet.getString("country");
            Port port = new Port(portId, portName, country);
            ports.add(port);
        }
        return ports;
    }
	 public List<Terminal> getAllTerminals() throws SQLException {
        List<Terminal> terminals = new ArrayList<>();
        String query = "SELECT * FROM Terminals";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            int terminalId = resultSet.getInt("terminal_id");
            String terminalName = resultSet.getString("terminal_name");
            int portId = resultSet.getInt("port_id");
            Terminal terminal = new Terminal(terminalId, terminalName, portId);
            terminals.add(terminal);
        }
        return terminals;
    }
	public List<Custodian> getAllCustodians() throws SQLException {
        List<Custodian> custodians = new ArrayList<>();
        String query = "SELECT * FROM Custodians";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            int custodianId = resultSet.getInt("custodian_id");
            String custodianName = resultSet.getString("custodian_name");
            int terminalId = resultSet.getInt("terminal_id");
            Custodian custodian = new Custodian(custodianId, custodianName, terminalId);
            custodians.add(custodian);
        }
        return custodians;
    }
	public List<Shipment> getAllShipments() throws SQLException {
        List<Shipment> shipments = new ArrayList<>();
        String query = "SELECT * FROM Shipments";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            int shipmentId = resultSet.getInt("shipment_id");
            String shipmentName = resultSet.getString("shipment_name");
            int terminalId = resultSet.getInt("terminal_id");
            String shipmentStatus = resultSet.getString("shipment_status");
            Shipment shipment = new Shipment(shipmentId, shipmentName, terminalId, shipmentStatus);
            shipments.add(shipment);
        }
        return shipments;
    }

    // Ports

    public void insertPort(int portId, String portName, String country) throws SQLException {
        String query = "INSERT INTO Ports (port_id, port_name, country) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, portId);
        statement.setString(2, portName);
        statement.setString(3, country);
        statement.executeUpdate();
    }

    public void updatePort(int portId, String portName, String country) throws SQLException {
        String query = "UPDATE Ports SET port_name = ?, country = ? WHERE port_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, portName);
        statement.setString(2, country);
        statement.setInt(3, portId);
        statement.executeUpdate();
    }

    public void deletePort(int portId) throws SQLException {
        String query = "DELETE FROM Ports WHERE port_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, portId);
        statement.executeUpdate();
    }

    // Custodians

	  public void insertCustodian(int custodianId, String custodianName, int terminalId) throws SQLException {
		// Create the SQL query with placeholders for the parameters
		String query = "INSERT INTO custodians (custodian_id, custodian_name, terminal_id) VALUES (?, ?, ?)";

		// Prepare the statement and set the values for the parameters
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, custodianId);
		statement.setString(2, custodianName);
		statement.setInt(3, terminalId);

		// Execute the query
		statement.executeUpdate();
	}


     public void updateCustodian(int custodianId, String custodianName) throws SQLException {
        String query = "UPDATE Custodians SET custodian_name = ? WHERE custodian_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, custodianName);
        statement.setInt(2, custodianId);
        statement.executeUpdate();
    }

    public void deleteCustodian(int custodianId) throws SQLException {
        String query = "DELETE FROM Custodians WHERE custodian_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, custodianId);
        statement.executeUpdate();
    }

    // Terminals

    public void insertTerminal(int terminalId, String terminalName, int portId) throws SQLException {
        String query = "INSERT INTO Terminals (terminal_id, terminal_name, port_id) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, terminalId);
        statement.setString(2, terminalName);
        statement.setInt(3, portId);
        statement.executeUpdate();
    }

     public void updateTerminal(int terminalId, String terminalName, int portId) throws SQLException {
        String query = "UPDATE Terminals SET terminal_name = ?, port_id = ? WHERE terminal_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, terminalName);
        statement.setInt(2, portId);
        statement.setInt(3, terminalId);
        statement.executeUpdate();
    }

    public void deleteTerminal(int terminalId) throws SQLException {
        String query = "DELETE FROM Terminals WHERE terminal_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, terminalId);
        statement.executeUpdate();
    }

    // Shipments

    public void insertShipment(int shipmentId, String shipmentName, int terminalId, String shipmentStatus) throws SQLException {
        String query = "INSERT INTO Shipments (shipment_id, shipment_name, terminal_id, shipment_status) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, shipmentId);
        statement.setString(2, shipmentName);
        statement.setInt(3, terminalId);
        statement.setString(4, shipmentStatus);
        statement.executeUpdate();
    }

    public void updateShipment(int shipmentId, String shipmentName, int terminalId, String shipmentStatus) throws SQLException {
        String query = "UPDATE Shipments SET shipment_name = ?, terminal_id = ?, shipment_status = ? WHERE shipment_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, shipmentName);
        statement.setInt(2, terminalId);
        statement.setString(3, shipmentStatus);
        statement.setInt(4, shipmentId);
        statement.executeUpdate();
    }

    public void deleteShipment(int shipmentId) throws SQLException {
        String query = "DELETE FROM Shipments WHERE shipment_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, shipmentId);
        statement.executeUpdate();
    }
}
