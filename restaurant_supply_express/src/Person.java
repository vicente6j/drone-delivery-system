public class Person {
    private String username;
    private String fname;
    private String lname;
    private String birthdate;
    private String address;

    public Person(String username, String fname, String lname, String birthdate, String address) {
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.birthdate = birthdate;
        this.address = address;
    }

    /**
     * toString method for a person
     */
    public String toString() {
        return String.format("userId: %s, name: %s, birth date: %s. address: %s", this.username,
                this.fname + " " + this.lname, this.birthdate, this.address);
    }

    public String getUsername() {
        return this.username;
    }

    public String getFname() {
        return this.fname;
    }

    public String getLname() {
        return this.lname;
    }

    public String getBirthDate() {
        return this.birthdate;
    }

    public String getAddress() {
        return this.address;
    }
}