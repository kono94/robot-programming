package de.scr.ui;

import de.scr.logic.OdometryController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
    private DriveControlPanel driveControlPanel;

    public MainFrame(OdometryController odometryController) {
        super();
        setLayout(new BorderLayout());
        driveControlPanel = new DriveControlPanel(odometryController);
        add(driveControlPanel, BorderLayout.CENTER);
        JButton startRecordingButton = new JButton(("Start Recording"));
        startRecordingButton.addActionListener((e) -> {
            startRecordingButton.setText("Recording...");
            startRecordingButton.setEnabled(false);
            odometryController.setRecording(true);
        });

        JButton stopRecordingButton = new JButton("Stop");
        stopRecordingButton.addActionListener((e -> {
            startRecordingButton.setText("Start Recording");
            startRecordingButton.setEnabled(true);
            odometryController.endLastInstruction();
            odometryController.setRecording(false);
        }));

        JButton driveBackButton = new JButton(("Drive Back"));
        driveBackButton.addActionListener((e -> {
            odometryController.endLastInstruction();
            new Thread(odometryController::driveBack).start();
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
                JSlider dirSlider = driveControlPanel.getDirectionSlider();
                JSlider speedSlider = driveControlPanel.getSpeedSlider();

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
}
