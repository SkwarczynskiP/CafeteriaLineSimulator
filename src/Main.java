import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Hospital Cafeteria Lunch Line Simulator!");
        System.out.println("Please enter \"start\" to begin");
        int i = 0;

        while (i == 0) {
            String choice = scanner.next();

            if (choice.equalsIgnoreCase("start")) {
                simulation();
                i++;
            } else {
                System.out.println("Not a valid entry. Please try again.");
            }
        }
    }

    public static void simulation() {
        Random random = new Random();

        Queue<Worker> cafeteriaLine = new PriorityQueue<>();

        ArrayList<Worker> copy = new ArrayList<>();
        ArrayList<Worker> waitingArea = new ArrayList<>();
        ArrayList<Integer> doctorAveWait = new ArrayList<>();
        ArrayList<Integer> nurseAveWait = new ArrayList<>();
        ArrayList<Integer> otherAveWait = new ArrayList<>();

        String[] workerTypes = new String[]{"\"Other Employee\"", "Doctor", "Nurse"};

        int startWait = 0;
        int timeToCreateFood = 0;
        int internalTimer = 120;

        //time loop (using 120 iterations instead of 120 minutes)
        for(int i = 1; i <= 120; i++) {
            System.out.println("Minute: " + i);

            //possibly adds a worker to the cafeteria line every "minute"
            int chanceAddWorker = random.nextInt(3);
            if (chanceAddWorker == 0) {
                int employeeId = random.nextInt(workerTypes.length);
                Worker worker = new Worker(workerTypes[employeeId], employeeId, startWait);
                System.out.println("\tA " + worker + " has entered the cafeteria line");
                cafeteriaLine.add(worker);
            }

            //take the order of the person in the front of the line, removing them from the line, and placing them into the waiting area
            if (!cafeteriaLine.isEmpty() && timeToCreateFood == 0) {
                timeToCreateFood = random.nextInt(6) + 1;
                waitingArea.add(cafeteriaLine.remove());
                addToWaiting(timeToCreateFood, waitingArea);
                //time is incremented by one because the loop will force the time to be decremented during the same iteration the order is being taken
                timeToCreateFood +=1;
                internalTimer = timeToCreateFood;
            }

            if (!waitingArea.isEmpty()) {
                timeToCreateFood--;
                internalTimer--;
                System.out.println("\t" + waitingArea.get(0).getEmployeeType() + " food countdown: " + timeToCreateFood);
            }

            if (timeToCreateFood == 0 && internalTimer == 0 && !waitingArea.isEmpty()) {
                if (waitingArea.get(0).getEmployeeType() == "\"Other Employee\"") {
                    otherAveWait.add(waitingArea.get(0).getWaiting());
                } else if (waitingArea.get(0).getEmployeeType() == "Doctor") {
                    doctorAveWait.add(waitingArea.get(0).getWaiting());
                } else if (waitingArea.get(0).getEmployeeType() == "Nurse") {
                    nurseAveWait.add(waitingArea.get(0).getWaiting());
                }
                waitingArea.remove(0);
            }

            print(cafeteriaLine, copy);
            System.out.println("\tCurrently Waiting For Food: " + Arrays.toString(waitingArea.toArray()));
        }

        System.out.println("\nUnfortunately, the lunch line is now closed. Everyone currently in the line will no longer be able to get food.\n");

        nurseAveTime(nurseAveWait);
        doctorAveTime(doctorAveWait);
        otherAveTime(otherAveWait);
    }

    private static void addToWaiting(int timeToCreateFood, ArrayList<Worker> waitingArea) {

        String tempEmpType = waitingArea.get(0).getEmployeeType();
        int tempEmpId = waitingArea.get(0).getEmployeeId();
        int tempWaiting = waitingArea.get(0).getWaiting() + timeToCreateFood;

        waitingArea.set(0, new Worker(tempEmpType, tempEmpId, tempWaiting));
    }

    private static void print(Queue<Worker> cafeteriaLine, ArrayList<Worker> copy) {
        while (!cafeteriaLine.isEmpty()) {
            copy.add(cafeteriaLine.poll());
        }

        for (int i = 0; i < copy.size(); i++) {
            String tempEmpType = copy.get(i).getEmployeeType();
            int tempEmpId = copy.get(i).getEmployeeId();
            int tempWaiting = copy.get(i).getWaiting() + 1;
            copy.set(i, new Worker(tempEmpType, tempEmpId, tempWaiting));
        }

        System.out.println("\tCurrent Cafeteria Line: " + copy);
        while (!copy.isEmpty()) {
            cafeteriaLine.add(copy.remove(0));
        }
    }

    private static void nurseAveTime(ArrayList<Integer> nurse) {
        double add = 0;
        for(int i = 0; i < nurse.size(); i++) {
            add += nurse.get(i);
        }
        double average = add / nurse.size();
        System.out.println("Average wait times for Nurses: " + String.format("%.2f", average) + " minutes.");
    }

    private static void doctorAveTime(ArrayList<Integer> doctor) {
        double add = 0;
        for(int i = 0; i < doctor.size(); i++) {
            add += doctor.get(i);
        }
        double average = add / doctor.size();
        System.out.println("Average wait times for Doctors: " + String.format("%.2f", average) + " minutes.");
    }

    private static void otherAveTime(ArrayList<Integer> other) {
        double add = 0;
        if (other.isEmpty()) {
            System.out.println("Average wait times for \"Other Employees\" is not available as none were able to get their food!");
        } else {
            for (int i = 0; i < other.size(); i++) {
                add += other.get(i);
            }
            double average = add / other.size();
            System.out.println("Average wait times for \"Other Employees\": " + String.format("%.2f", average) + " minutes.");
        }
    }
}