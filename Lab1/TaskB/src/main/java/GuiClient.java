import javax.swing.*;

public class GuiClient {
    private final Integer monitor = Integer.valueOf("1");

    private int semaphore = SemaphoreValues.FREE;

    private final JFrame frame = new JFrame();
    private final JPanel panel = new JPanel();
    private final JProgressBar progressBar = new JProgressBar();

    private final JButton firstStartButton = new JButton("Start (1st)");
    private final JButton secondStartButton = new JButton("Start (2nd)");

    private final JButton firstStopButton = new JButton("Stop (1st)");
    private final JButton secondStopButton = new JButton("Stop (2nd)");

    private Thread firstThread;
    private Thread secondThread;

    private Runnable createValueChanger(int goalValue) {
        return () -> {
            boolean stopped = false;

            while (!stopped && !Thread.currentThread().isInterrupted()) {
                int currentValue = progressBar.getValue();

                while (currentValue != goalValue) {
                    currentValue = currentValue + (int) Math.signum(goalValue - currentValue);
                    progressBar.setValue(currentValue);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        stopped = true;
                        break;
                    }
                }
            }
            semaphore = SemaphoreValues.FREE;
        };
    }

    public GuiClient() {
        progressBar.setValue(ProgressBarValues.INITIAL_VALUE);

        performJFrameSetup();
        performJPanelSetup();
    }

    private void performJFrameSetup() {
        frame.setVisible(true);
        frame.setBounds(500, 250, 750, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
    }

    private void performJPanelSetup() {
        panel.add(progressBar);

        panel.add(firstStartButton);
        createStartFirstThreadButton();

        panel.add(secondStartButton);
        createStartSecondThreadButton();

        panel.add(firstStopButton);
        setupStopFirstThreadButton();

        panel.add(secondStopButton);
        setupStopSecondThreadButton();
    }

    private void createStartFirstThreadButton() {
        firstStartButton.addActionListener(e -> {
            if (semaphore == SemaphoreValues.TAKEN) {
                JOptionPane.showMessageDialog(null, "Blocked by 2nd thread");
                return;
            }
            semaphore = SemaphoreValues.TAKEN;

            firstStartButton.setEnabled(false);
            firstStopButton.setEnabled(true);
            firstThread = new Thread(createValueChanger(ProgressBarValues.FIRST_VALUE));
            firstThread.setPriority(Thread.MIN_PRIORITY);
            firstThread.start();
        });
    }

    private void createStartSecondThreadButton() {
        secondStartButton.addActionListener(e -> {
            if (semaphore == SemaphoreValues.TAKEN) {
                JOptionPane.showMessageDialog(null, "Blocked by 1st thread");
                return;
            }
            semaphore = SemaphoreValues.TAKEN;

            secondStartButton.setEnabled(false);
            secondStopButton.setEnabled(true);
            secondThread = new Thread(createValueChanger(ProgressBarValues.SECOND_VALUE));
            secondThread.setPriority(Thread.MAX_PRIORITY);
            secondThread.start();
        });
    }

    private void setupStopFirstThreadButton() {
        firstStopButton.setEnabled(false);

        firstStopButton.addActionListener(e -> {
            firstThread.interrupt();
            firstStartButton.setEnabled(true);
            firstStopButton.setEnabled(false);
        });
    }

    private void setupStopSecondThreadButton() {
        secondStopButton.setEnabled(false);

        secondStopButton.addActionListener(e -> {
            secondThread.interrupt();
            secondStartButton.setEnabled(true);
            secondStopButton.setEnabled(false);
        });
    }
}