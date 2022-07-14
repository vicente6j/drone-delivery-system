import java.util.HashSet;

public class WorkerEmployee extends User {
    private boolean isManager;

    /**
     * Copy constructor that copys data from a person to a worker
     * 
     * @param person Person
     */
    public WorkerEmployee(Person person) {
        super(person.getUsername(), person.getFname(), person.getLname(), person.getBirthDate(), person.getAddress());
    }

    /**
     * Copy contructor that copys data from a pilot back into a worker
     * 
     * @param person     Pilot representing the pilot to turn back into a worker
     * @param employers  HashSet<String> representing the employers of the pilot
     * @param license    String representing the pilot license
     * @param experience Integer representing the pilot experience
     */
    public WorkerEmployee(Person person, HashSet<String> employers, String license, Integer experience) {
        super(person.getUsername(), person.getFname(), person.getLname(), person.getBirthDate(), person.getAddress(),
                employers, license, experience);
    }

    /**
     * toString method for the worker employee
     */
    public String toString() {
        // String containing the service where the pilot works
        String employers = "";
        if (this.getEmployers().size() > 0) {
            for (String deliveryService : this.getEmployers()) {
                employers += String.format("\n&> %s", deliveryService);
            }
        }

        // String containing the license
        String license = "";
        if (this.getLicense() != null) {
            license = String.format("\nuser has a pilot's license (%s) with %d successful flight(s)",
                    this.getLicense(), this.getExperience());
        }
        return (this.isManager ? super.toString() + "\n" + "employee is managing: " + this.getEmployers().toArray()[0]
                : super.toString() + "\n" + "employee is working at:" + employers + license);
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public boolean getIsManager() {
        return this.isManager;
    }
}
