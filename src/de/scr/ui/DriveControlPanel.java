package de.scr.ui;

import de.scr.ev3.components.Drivable;

import javax.swing.*;
import java.awt.*;

public class DriveControlPanel extends JPanel {
    private JSlider speedSlider;
    private JSlider directionSlider;

    public DriveControlPanel(Drivable drivable) {
        setLayout(new GridLayout(1, 2, 10, 10));
        setPreferredSize(new Dimension(300, 200));
        speedSlider = new JSlider(JSlider.VERTICAL, -100, 100, 0);
        directionSlider = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
        speedSlider.setEnabled(false);
        directionSlider.setEnabled(false);
        directionSlider.addChangeListener(e -> drivable.drive(directionSlider.getValue()));
        speedSlider.addChangeListener(e -> drivable.drive(speedSlider.getValue(), directionSlider.getValue()));
        add(directionSlider);
        add(speedSlider);
        setFocusable(true);
        requestFocusInWindow();
        setVisible(true);
    }

    public JSlider getSpeedSlider() {
        return speedSlider;
    }

    public JSlider getDirectionSlider() {
        return directionSlider;
    }
}
