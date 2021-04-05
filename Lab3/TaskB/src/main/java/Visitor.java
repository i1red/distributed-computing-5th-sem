public class Visitor extends Thread {
    private final int visitorId;
    private Barber barber;

    public Visitor(int visitorId, Barber personalBarber) {
        this.visitorId = visitorId;
        this.barber = personalBarber;
    }

    @Override
    public void run() {
        this.barber.doHaircut(this);
    }

    public int getVisitorId() {
        return this.visitorId;
    }
}
