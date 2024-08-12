import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.time.Instant;

public class TrackingNumberGenerator {

    private static final AtomicLong SEQUENCE = new AtomicLong(0);
    private static final ReentrantLock LOCK = new ReentrantLock();

    private static final String PREFIX = "TN";
    private static final long SEQUENCE_LIMIT = 1000000L;

    /**
     * Generates a unique tracking number.
     * @return A unique tracking number.
     */
    public static String generateTrackingNumber() {
        long timestamp = Instant.now().toEpochMilli();
        long mySeq;
        LOCK.lock();
        try {
        	mySeq = SEQUENCE.getAndIncrement();
            if (mySeq >= SEQUENCE_LIMIT) {
                SEQUENCE.set(0); // Reset sequence if limit is reached
                mySeq = SEQUENCE.getAndIncrement();
            }
        } finally {
            LOCK.unlock();
        }

        return PREFIX + "-" + timestamp + "-" + String.format("%06d", mySeq);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(generateTrackingNumber());
        }
    }
}
