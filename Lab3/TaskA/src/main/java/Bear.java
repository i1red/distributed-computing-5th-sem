public class Bear {
    public void consumeHoney(HoneyPot honeyPot) throws Exception {
        honeyPot.eat();
        System.out.printf("Bear consumed all honey from the pot with capacity %d\n",  honeyPot.getCapacity());
    }
}
