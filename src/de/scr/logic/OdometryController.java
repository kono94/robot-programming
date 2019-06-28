package de.scr.logic;

import de.scr.ev3.components.Drivable;
import de.scr.ev3.components.MyGyroSensor;
import de.scr.ui.MainFrame;
import de.scr.utils.Instruction;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class OdometryController {
    private static Logger logger = LoggerFactory.getLogger(OdometryController.class);
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
        logger.debug("Driving back");
        driveable.rotateOnPlace(15,180, myGyroSensor);
        logger.debug("Rotation complete");
        while (!history.isEmpty()) {
            Instruction instr = history.pop();
            driveable.drive(instr.getSpeed(), -instr.getTurn());
            logger.debug("Instruction: {}", instr.toString());
            Delay.msDelay(instr.getDelay());
        }
        driveable.drive(0,0);
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
