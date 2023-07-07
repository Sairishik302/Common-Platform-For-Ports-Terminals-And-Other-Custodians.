public class Port {
    private int portId;
    private String portName;
    private String country;

    public Port(int portId, String portName, String country) {
        this.portId = portId;
        this.portName = portName;
        this.country = country;
    }

    public int getPortId() {
        return portId;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}