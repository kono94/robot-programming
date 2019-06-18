package de.scr.ui;

import de.scr.ev3.components.Drivable;

import javax.swing.*;
import java.awt.*;
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
            synchronized (this) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    System.out.println("add " + e.getKeyCode());
                    pressed.add(e.getKeyCode());
                } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    System.out.println("remove " + e.getKeyCode());
                    pressed.remove(e.getKeyCode());
                }
            }
            return false;
        });
        new Thread(() -> {
            while (true) {
                JSlider dirSlider = driveControlPanel.getDirectionSlider();
                JSlider speedSlider = driveControlPanel.getSpeedSlider();
                boolean resetDir = true;
                boolean resetSpeed = true;
                synchronized (this) {
                    System.out.println(pressed);
                    for (int a : pressed) {
                        switch (a) {
                            case KeyEvent.VK_W:
                            case KeyEvent.VK_UP:
                                adjustSlider(speedSlider, 5);
                                resetSpeed = false;
                                break;
                            case KeyEvent.VK_S:
                            case KeyEvent.VK_DOWN:
                                adjustSlider(speedSlider, -5);
                                resetSpeed = false;
                                break;
                            case KeyEvent.VK_D:
                            case KeyEvent.VK_RIGHT:
                                adjustSlider(dirSlider, 8);
                                resetDir = false;
                                break;
                            case KeyEvent.VK_A:
                            case KeyEvent.VK_LEFT:
                                resetDir = false;
                                adjustSlider(dirSlider, -8);
                                break;
                        }
                    }
                    /*
                    if(resetDir)
                        resetSlider(dirSlider, 8);
                    if(resetSpeed)
                        resetSlider(speedSlider, 3);
                      */
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
