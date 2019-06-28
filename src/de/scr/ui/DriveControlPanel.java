package de.scr.ui;

import de.scr.logic.OdometryController;
import de.scr.utils.Instruction;

import javax.swing.*;
import java.awt.*;

public class DriveControlPanel extends JPanel {
    private JSlider speedSlider;
    private JSlider directionSlider;
    private JTextArea historyArea;
    private OdometryController odometryController;

    public DriveControlPanel(OdometryController odometryController) {
        this.odometryController = odometryController;
        historyArea = new JTextArea();
        setLayout(new GridLayout(1, 3, 10, 10));
        setPreferredSize(new Dimension(300, 200));
        speedSlider = new JSlider(JSlider.VERTICAL, -100, 100, 0);
        directionSlider = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
        speedSlider.setEnabled(false);
        directionSlider.setEnabled(false);
        directionSlider.addChangeListener(e -> {
            odometryController.getDriveable().drive(directionSlider.getValue());
            odometryController.addInstruction(new Instruction(odometryController.getDriveable().getSpeed(),
                    directionSlider.getValue(), 0, System.currentTimeMillis()));
            updateHistoryArea();
        });
        speedSlider.addChangeListener(e -> {
            odometryController.getDriveable().drive(speedSlider.getValue(), directionSlider.getValue());
            odometryController.addInstruction(new Instruction(speedSlider.getValue(),
                    directionSlider.getValue(), 0, System.currentTimeMillis()));
            updateHistoryArea();
        });
        add(directionSlider);
        add(speedSlider);
        add(historyArea);
        setFocusable(true);
        requestFocusInWindow();
        setVisible(true);
    }

    public void updateHistoryArea() {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Instruction i : odometryController.getHistory()) {
            sb.insert(0, ++index + ". - " + i.toString() + "\n");
        }
        historyArea.setText(sb.toString());
    }
    public JSlider getSpeedSlider() {
        return speedSlider;
    }

    public JSlider getDirectionSlider() {
        return directionSlider;
    }
}
