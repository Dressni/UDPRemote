package dressler.nick.udp_remote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Nick on 02.07.2018.
 */

public class Steuerung implements Runnable {

    private MainActivity mainActivity;
    private DatagramSocket datagramSocket;
    private Thread übertragungsThread;
    private int gas = 0;
    private byte direction = 0;
    private long lastMS = 0;

    public Steuerung(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        try {
            this.datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void starteÜbertragung(){
        this.übertragungsThread = new Thread(this);
        this.übertragungsThread.start();
    }
    public void stoppeÜbertagung(){
        this.übertragungsThread.interrupt();
        this.übertragungsThread.stop();
    }

    private void sendeDatagram(String context){
        try {
            byte[] buf = context.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, InetAddress.getByName("192.168.4.1"), 1234);
            this.datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!übertragungsThread.isInterrupted()){
            if(System.currentTimeMillis() - lastMS > 20) {
                lastMS = System.currentTimeMillis();
                String send = mainActivity.gibRichtung() + ";" + mainActivity.gibGas();
                sendeDatagram(send);
                mainActivity.anzeigen(send);
            }
        }
    }
}
