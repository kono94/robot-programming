package de.scr.ui;

import de.scr.ev3.components.Drivable;
import de.scr.logic.OdometryController;
import de.scr.utils.Instruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {
    private static Logger logger = LoggerFactory.getLogger(MainFrame.class);
    private DriveControlPanel driveControlPanel;
    private OdometryController odometryController;

    public MainFrame(OdometryController odometryController) {
        super();
        this.odometryController = odometryController;
        setLayout(new BorderLayout());
        driveControlPanel = new DriveControlPanel(odometryController);
        add(driveControlPanel, BorderLayout.CENTER);

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
            odometryController.disableRecording();
            startRecordingButton.setText("Start Recording");
            startRecordingButton.setEnabled(true);
            driveBackButton.setEnabled(true);
        }));

        driveBackButton.addActionListener((e -> {
            setEnabled(false);
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
                    setEnabled(true);
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
                        changeTurn(5);
                        break;
                    case KeyEvent.VK_A:
                        changeTurn(-5);
                        break;
                    case KeyEvent.VK_W:
                        changeSpeed(3);
                        break;
                    case KeyEvent.VK_S:
                        changeSpeed(-3);
                        break;
                    case KeyEvent.VK_Q:
                        stop();
                }
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void stop() {
        Drivable drivable = odometryController.getDriveable();
        drivable.setSpeed(0);
        drivable.setTurn(0);
        processNewInstruction();
    }

    private void changeSpeed(int value) {
        Drivable drivable = odometryController.getDriveable();
        drivable.setSpeed(drivable.getSpeed() + value);
        processNewInstruction();
    }

    private void changeTurn(int value) {
        Drivable drivable = odometryController.getDriveable();
        drivable.setTurn(drivable.getTurn() + value);
        processNewInstruction();
    }

    private void processNewInstruction() {
        Drivable drivable = odometryController.getDriveable();
        drivable.drive();

        if (odometryController.isRecording()) {
            odometryController.addInstruction(new Instruction(drivable.getSpeed(),
                    drivable.getTurn(), 0, System.currentTimeMillis()));
            driveControlPanel.updateHistoryArea();
        }

        driveControlPanel.getSpeedSlider().setValue(drivable.getSpeed());
        driveControlPanel.getDirectionSlider().setValue(drivable.getTurn());

    }

    public DriveControlPanel getDriveControlPanel() {
        return driveControlPanel;
    }
}
