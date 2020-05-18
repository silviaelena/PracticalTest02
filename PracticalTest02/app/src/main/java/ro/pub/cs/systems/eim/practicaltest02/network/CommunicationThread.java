package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.Constants;

public class CommunicationThread extends Thread{
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
    }
}
