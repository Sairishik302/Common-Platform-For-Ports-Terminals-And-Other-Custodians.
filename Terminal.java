public class Terminal {
    private int terminalId;
    private String terminalName;
    private int portId;
	private String location;


    public Terminal(int terminalId, String terminalName, int portId) {
        this.terminalId = terminalId;
        this.terminalName = terminalName;
        this.portId = portId;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public int getPortId() {
        return portId;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }
	public String getLocation() {
		return location;
	}

public void setLocation(String location) {
    this.location = location;
}
}