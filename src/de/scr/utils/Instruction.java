package de.scr.utils;

public class Instruction {
    private int speed;
    private int turn;
    private long delay;
    private long startedTimestamp;

    public Instruction(int speed, int turn, long delay, long startedTimestamp) {
        this.speed = speed;
        this.turn = turn;
        this.delay = delay;
        this.startedTimestamp = startedTimestamp;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getStartedTimestamp() {
        return startedTimestamp;
    }

    public void setStartedTimestamp(long startedTimestamp) {
        this.startedTimestamp = startedTimestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Speed: ").append(speed).append(" Turn: ").append(turn).append(" Delay: ").append(delay);
        return sb.toString();
    }
}
