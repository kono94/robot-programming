package de.scr.logic;

import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyGyroSensor;
import de.scr.ui.MainFrame;
import de.scr.utils.Instruction;
import lejos.utility.Delay;

import java.util.Stack;

public class OdometryController {
    private Stack<Instruction> history;
    private Drivable driveable;
    private boolean isRecording;
    private MyGyroSensor myGyroSensor;

    public OdometryController(Drivable drivable, MyGyroSensor myGyroSensor) {
        this.driveable = drivable;
        this.myGyroSensor = myGyroSensor;
        history = new Stack<>();
    }

    public void start() {
        new MainFrame(this);
    }

    public Stack<Instruction> getHistory() {
        return history;
    }

    public void driveBack() {
        driveable.rotateOnPlace(180, myGyroSensor);
        while (!history.isEmpty()) {
            Instruction instr = history.pop();
            driveable.drive(instr.getSpeed(), -instr.getTurn());
            Delay.msDelay(instr.getDelay());
        }
    }

    public Drivable getDriveable() {
        return driveable;
    }

    public void addInstruction(Instruction newInstr) {
        endLastInstruction();
        history.add(newInstr);
    }

    public void endLastInstruction() {
        if (history.isEmpty())
            return;
        Instruction lastInstr = history.peek();
        lastInstr.setDelay(System.currentTimeMillis() - lastInstr.getStartedTimestamp());
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }
}
