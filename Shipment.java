public class Shipment {
    private int shipmentId;
    private String shipmentName;
    private int terminalId;
    private String shipmentStatus;

    public Shipment(int shipmentId, String shipmentName, int terminalId, String shipmentStatus) {
        this.shipmentId = shipmentId;
        this.shipmentName = shipmentName;
        this.terminalId = terminalId;
        this.shipmentStatus = shipmentStatus;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(String shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }
}
