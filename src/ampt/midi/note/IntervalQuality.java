package ampt.midi.note;


/**
 *
 * @author Rob
 */
public enum IntervalQuality {

    MAJOR("Major", -1, 2, 4, -1, -1, 9, 11, -1),
    MINOR("Minor", -1, 1, 3, -1, -1, 8, 10, -1),
    PERFECT("Perfect", 0, -1, -1, 5, 7, -1, -1, 12),
    AUGMENTED("Augmented", 1, 3, 5, 6, 8, 10, 12, -1),
    DIMINISHED("Diminished", -1, 0, 2, 4, 6, 7, 9, 11);

    private final int unison;
    private final int second;
    private final int third;
    private final int fourth;
    private final int fifth;
    private final int sixth;
    private final int seventh;
    private final int octave;
    private String type;

    IntervalQuality(String type, int unison, int second, int third, int fourth, int fifth, int sixth, int seventh, int octave) {

        this.type = type;
        this.unison = unison;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
        this.sixth = sixth;
        this.seventh = seventh;
        this.octave = octave;
    }

    public int unison() throws InvalidIntervalException {
        if (unison < 0)
            throw new InvalidIntervalException("Invalid Interval: " + type + " Unison");
        return unison;
    }

    public int second() throws InvalidIntervalException {
        if (second < 0)
            throw new InvalidIntervalException("Invalid Interval: " + type + " Second");
        return second;
    }

    public int third() throws InvalidIntervalException {
        if (third < 0)
            throw new InvalidIntervalException("Invalid Interval: " + type + " Third");
        return third;
    }

    public int fourth() throws InvalidIntervalException {
        if (fourth < 0)
            throw new InvalidIntervalException("Invalid Interval: " + type + " Fourth");
        return fourth;
    }

    public int fifth() throws InvalidIntervalException {
        if (fifth < 0)
            throw new InvalidIntervalException("Invalid Interval: " + type + " Fifth");
        return fifth;
    }

    public int sixth() throws InvalidIntervalException {
        if (sixth < 0)
            throw new InvalidIntervalException("Invalid Interval: " + type + " Sixth");
        return sixth;
    }

    public int seventh() throws InvalidIntervalException {
        if (seventh < 0)
            throw new InvalidIntervalException("Invalid Interval: " + type + " Seventh");
        return seventh;
    }

    public int octave() throws InvalidIntervalException {
        if (octave < 0)
            throw new InvalidIntervalException("Invalid Interval: " + type + " Octave");
        return octave;
    }

    public static int tritone() {
        return 6;
    }

    @Override
    public String toString() {
        return type;
    }
}