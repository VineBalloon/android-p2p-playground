package com.example.myapplication.model;

import io.underdark.Underdark;
import io.underdark.transport.Link;
import io.underdark.transport.Transport;
import io.underdark.transport.TransportKind;
import io.underdark.transport.TransportListener;
import io.underdark.util.nslogger.NSLogger;
import io.underdark.util.nslogger.NSLoggerAdapter;

import com.example.myapplication.MainActivity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

public class Node implements TransportListener {

    private boolean running;
    private MainActivity activity;
    private Transport transport;

    private ArrayList<Link> links = new ArrayList<>();
    private int framesCount = 0;

    public Node(MainActivity activity) {
        this.activity = activity;

        long nodeID;
        do {
            nodeID = new Random().nextLong();
        } while (nodeID == 0);

        if (nodeID < 0) {
            nodeID = -nodeID;
        }

        // logging lmao
        //configureLogging();

        // create set of enum types representing the kinds of transport implemented
        EnumSet<TransportKind> kinds = EnumSet.of(TransportKind.BLUETOOTH, TransportKind.WIFI);
        //kinds = EnumSet.of(TransportKind.WIFI);
        //kinds = EnumSet.of(TransportKind.BLUETOOTH);

        // this configures the transport
        this.transport = Underdark.configureTransport(
                // hardcoded value lmao
                234235,
                nodeID,
                this,
                null,
                activity.getApplicationContext(),
                kinds
        );
    }

    /*
    private void configureLogging()
    {
        NSLoggerAdapter adapter = (NSLoggerAdapter)
                StaticLoggerBinder.getSingleton().getLoggerFactory().getLogger(Node.class.getName());
        adapter.logger = new NSLogger(activity.getApplicationContext());
        adapter.logger.connect("192.168.5.203", 50000);

        Underdark.configureLogging(true);
    }
    */

    public void start() {
        if (running)
            return;

        running = true;
        transport.start();
    }

    public void stop() {
        if (!running)
            return;

        running = false;
        transport.stop();
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public int getFramesCount() {
        return framesCount;
    }

    /*
     * broadcastFrame broadcasts a frame to all connected remote devices
     */
    public void broadcastFrame(byte[] frameData) {
        if (links.isEmpty()) {
            return;
        }

        // increment frames sent
        ++framesCount;
        activity.refreshFrames();

        for (Link link : links) {
            link.sendFrame(frameData);
        }
    }

    @Override
    public void transportNeedsActivity(Transport transport, ActivityCallback activityCallback) {
        activityCallback.accept(activity);
    }

    @Override
    public void transportLinkConnected(Transport transport, Link link) {
        links.add(link);
        activity.refreshPeers();
    }

    @Override
    public void transportLinkDisconnected(Transport transport, Link link) {
        links.remove(link);
        if (links.isEmpty()) {
            framesCount = 0;
        }

        activity.refreshPeers();
    }

    @Override
    public void transportLinkDidReceiveFrame(Transport transport, Link link, byte[] bytes) {
        ++framesCount;
        activity.refreshFrames();
    }
}
