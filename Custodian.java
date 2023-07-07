public class Custodian {
    private int custodianId;
    private String custodianName;
    private int terminalId;

    public Custodian(int custodianId, String custodianName, int terminalId) {
        this.custodianId = custodianId;
        this.custodianName = custodianName;
        this.terminalId = terminalId;
    }

    public int getCustodianId() {
        return custodianId;
    }

    public void setCustodianId(int custodianId) {
        this.custodianId = custodianId;
    }

    public String getCustodianName() {
        return custodianName;
    }

    public void setCustodianName(String custodianName) {
        this.custodianName = custodianName;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }
}
