public class Deadline extends TaskWithDate {
    public Deadline(String description, String dateTime) throws SnomException {
        super(description, dateTime);
    }

    @Override
    public String toString(){
        return "[D]" + super.toString() + "(by: " + getDateTimeString() + ")";
    }
}
