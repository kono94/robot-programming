package de.scr.ui;

import de.scr.logic.OdometryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
    private static Logger logger = LoggerFactory.getLogger(MainFrame.class);
    private DriveControlPanel driveControlPanel;

    public MainFrame(OdometryController odometryController) {
        super();
        setLayout(new BorderLayout());
        driveControlPanel = new DriveControlPanel(odometryController);
        add(driveControlPanel, BorderLayout.CENTER);
        JSlider dirSlider = driveControlPanel.getDirectionSlider();
        JSlider speedSlider = driveControlPanel.getSpeedSlider();


        JButton startRecordingButton = new JButton(("Start Recording"));
        JButton stopRecordingButton = new JButton("Stop Recording");
        stopRecordingButton.setEnabled(false);
        JButton driveBackButton = new JButton(("Drive Back"));
        driveBackButton.setEnabled(false);

        startRecordingButton.addActionListener((e) -> {
            startRecordingButton.setText("Recording...");
            startRecordingButton.setEnabled(false);
            stopRecordingButton.setEnabled(true);
            odometryController.enableRecording();
        });

        stopRecordingButton.addActionListener((e -> {
            odometryController.endLastInstruction();
            startRecordingButton.setText("Start Recording");
            startRecordingButton.setEnabled(true);
            driveBackButton.setEnabled(true);
            speedSlider.setValue(0);
        }));

        driveBackButton.addActionListener((e -> {
            startRecordingButton.setEnabled(false);
            stopRecordingButton.setEnabled(false);
            driveBackButton.setEnabled(false);
            driveBackButton.setText("driving back...");
            new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() {
                    odometryController.driveBack();
                    return null;
                }

                @Override
                protected void done() {
                    // this method is called when the background
                    // thread finishes execution
                    logger.info("SwingWorker done");
                    driveBackButton.setEnabled(false);
                    driveBackButton.setText("Drive back");
                    startRecordingButton.setEnabled(true);
                    stopRecordingButton.setEnabled(false);
                    driveControlPanel.updateHistoryArea();
                }
            }.execute();
        }));
        setFocusable(true);
        setFocusableWindowState(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(startRecordingButton);
        buttonPanel.add(stopRecordingButton);
        buttonPanel.add(driveBackButton);


        add(buttonPanel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_D:
                        adjustSlider(dirSlider, 5);
                        break;
                    case KeyEvent.VK_A:
                        adjustSlider(dirSlider, -5);
                        break;
                    case KeyEvent.VK_W:
                        adjustSlider(speedSlider, 3);
                        break;
                    case KeyEvent.VK_S:
                        adjustSlider(speedSlider, -3);
                        break;
                    case KeyEvent.VK_Q:
                        dirSlider.setValue(0);
                        speedSlider.setValue(0);
                }
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void adjustSlider(JSlider s, int value) {
        s.setValue(s.getValue() + value);
    }

    public DriveControlPanel getDriveControlPanel() {
        return driveControlPanel;
    }
}
