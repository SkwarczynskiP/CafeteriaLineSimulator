public class Worker implements Comparable<Worker> {

    private String employeeType;
    private int employeeId;
    public  int waiting;

    public Worker(String employeeType, int employeeId, int waiting) {
        this.employeeType = employeeType;
        this.employeeId = employeeId;
        this.waiting = waiting;
    }

    public int getWaiting() {
        return waiting;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    @Override
    public int compareTo(Worker o) {
        if(this.getEmployeeId() > o.getEmployeeId()) {
            return -1;
        } else if(this.getEmployeeId() < o.getEmployeeId()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return this.employeeType;
    }
}
