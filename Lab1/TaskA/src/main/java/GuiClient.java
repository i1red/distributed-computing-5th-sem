import javax.swing.*;
import java.awt.event.ActionListener;


public class GuiClient {
    private final JFrame frame = new JFrame();
    private final JPanel panel = new JPanel();
    private final JProgressBar progressBar = new JProgressBar();

    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");

    @SuppressWarnings("rawtypes")
    private final JComboBox firstThreadPriority = new JComboBox(Priority.values());
    @SuppressWarnings("rawtypes")
    private final JComboBox secondThreadPriority = new JComboBox(Priority.values());

    private Thread firstThread;
    private Thread secondThread;

    private Runnable createValueChanger(int goalValue) {
        return () -> {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (progressBar) {
                    int currentValue = progressBar.getValue();
                    while (currentValue != goalValue) {
                        currentValue = currentValue + (int) Math.signum(goalValue - currentValue);
                        progressBar.setValue(currentValue);
                    }
                }
            }
        };
    }

    public GuiClient() {
        progressBar.setValue(Values.INITIAL_VALUE);

        setupFrame();
        setupPanel();
    }

    private void setupFrame() {
        frame.setVisible(true);
        frame.setBounds(500, 250, 750, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.add(firstThreadPriority);
        panel.add(secondThreadPriority);
    }

    private void setupPanel() {
        panel.add(progressBar);

        panel.add(startButton);
        setupStartButton();
        startButton.setEnabled(true);

        panel.add(stopButton);
        setupStopButton();
        stopButton.setEnabled(false);
    }

    private void setupStartButton() {
        startButton.addActionListener(e -> {
            firstThread = new Thread(createValueChanger(Values.FIRST_VALUE));
            secondThread = new Thread(createValueChanger(Values.SECOND_VALUE));

            firstThreadPriority.addActionListener(createPriorityChanger(firstThreadPriority, firstThread));
            secondThreadPriority.addActionListener(createPriorityChanger(secondThreadPriority, secondThread));

            firstThread.start();
            secondThread.start();

            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });
    }

    private void setupStopButton() {
        stopButton.addActionListener(e -> {
            firstThread.interrupt();
            secondThread.interrupt();

            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });
    }

    private ActionListener createPriorityChanger(JComboBox comboBox, Thread thread) {
        return e -> {
            Object selectedItem = comboBox.getSelectedItem();

            System.out.printf(
                    "%s priority for %s thread\n",
                    selectedItem,
                    thread.getId() == firstThread.getId() ? "first" : "second"
            );

            if (selectedItem == Priority.LOW) {
                thread.setPriority(Thread.MIN_PRIORITY);
            }
            else if (selectedItem == Priority.MEDIUM) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            else if (selectedItem == Priority.HIGH) {
                thread.setPriority(Thread.MAX_PRIORITY);
            }
        };
    }
}