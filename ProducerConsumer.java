
import java.util.LinkedList;

class Buffer {
    private final LinkedList<Integer> list = new LinkedList<>();
    private final int capacity = 5;

    public synchronized void produce(int value)
            throws InterruptedException {
        while (list.size() == capacity) {
            wait();
        }

        list.add(value);
        System.out.println("Produced: " + value);
        notifyAll();
    }

    public synchronized void consume()
            throws InterruptedException {
        while (list.isEmpty()) {
            wait();
        }

        int value = list.removeFirst();
        System.out.println("Consumed: " + value);
        notifyAll();
    }
}

class Producer extends Thread {
    private final Buffer buffer;

    Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                buffer.produce(i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer extends Thread {
    private final Buffer buffer;

    Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                buffer.consume();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();
    }
}
