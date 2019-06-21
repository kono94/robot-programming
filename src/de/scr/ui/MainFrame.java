package de.scr.ui;

import de.scr.ev3.components.Drivable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class MainFrame extends JFrame {
    private final Set<Integer> pressed = new HashSet<>();
    private DriveControlPanel driveControlPanel;

    public MainFrame(Drivable drivable) {
        super();
        setLayout(new BorderLayout());
        driveControlPanel = new DriveControlPanel(drivable);
        add(driveControlPanel, BorderLayout.CENTER);
        setFocusable(true);
        setFocusableWindowState(true);

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                pressed.add(e.getKeyCode());
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                pressed.remove(e.getKeyCode());
            }
            return false;
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                JSlider dirSlider = driveControlPanel.getDirectionSlider();
                JSlider speedSlider = driveControlPanel.getSpeedSlider();

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_D:
                        adjustSlider(dirSlider, 10);
                        break;
                    case KeyEvent.VK_A:
                        adjustSlider(dirSlider, -10);
                        break;
                    case KeyEvent.VK_W:
                        adjustSlider(speedSlider, 5);
                        break;
                    case KeyEvent.VK_S:
                        adjustSlider(speedSlider, -5);
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

    private void resetSlider(JSlider s, int velocity) {
        if (Math.abs(s.getValue()) <= velocity) {
            s.setValue(0);
        } else if (s.getValue() > 0) {
            adjustSlider(s, -velocity);
        } else if (s.getValue() < 0) {
            adjustSlider(s, velocity);
        }
    }
}
