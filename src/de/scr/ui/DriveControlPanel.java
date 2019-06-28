package de.scr.ui;

import de.scr.logic.OdometryController;
import de.scr.utils.Instruction;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class DriveControlPanel extends JPanel {
    private JSlider speedSlider;
    private JSlider directionSlider;
    private JTextArea historyArea;

    public DriveControlPanel(OdometryController odometryController) {
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
            updateHistoryArea(odometryController.getHistory());
        });
        speedSlider.addChangeListener(e -> {
            odometryController.getDriveable().drive(speedSlider.getValue(), directionSlider.getValue());
            odometryController.addInstruction(new Instruction(speedSlider.getValue(),
                    directionSlider.getValue(), 0, System.currentTimeMillis()));
            updateHistoryArea(odometryController.getHistory());
        });
        add(directionSlider);
        add(speedSlider);
        add(historyArea);
        setFocusable(true);
        requestFocusInWindow();
        setVisible(true);
    }

    private void updateHistoryArea(Stack<Instruction> history) {
        StringBuilder sb = new StringBuilder();
        for (Instruction i : history) {
            sb.append(i.toString()).append("\n");
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
