class CustomReadWriteLock {
    private static final int WRITE_LOCKED = -1, FREE = 0;

    private int numberOfReaders = FREE;
    private Thread currentWriteLockOwner;

    public synchronized void readLock() throws InterruptedException {
        while(this.numberOfReaders == WRITE_LOCKED) {
            this.wait();
        }
        ++this.numberOfReaders;
    }

    public synchronized void readUnlock() {
        if (this.numberOfReaders <= 0) {
            throw new IllegalMonitorStateException();
        }

        --this.numberOfReaders;
        if (this.numberOfReaders == FREE) {
            this.notifyAll();
        }
    }

    public synchronized void writeLock() throws InterruptedException {
        while (this.numberOfReaders != FREE) {
            this.wait();
        }
        this.numberOfReaders = WRITE_LOCKED;
        this.currentWriteLockOwner = Thread.currentThread();
    }

    public synchronized void writeUnlock() {
        if (this.numberOfReaders != WRITE_LOCKED || this.currentWriteLockOwner != Thread.currentThread()){
            throw new IllegalMonitorStateException();
        }

        this.numberOfReaders = FREE;
        this.currentWriteLockOwner = null;
        this.notifyAll();
    }
}