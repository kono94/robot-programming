package main;


import programs.TestProgram;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class Main {
    public static void main(String[] args) {
        TestProgram t = new TestProgram();
        try {
            t.start();
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
