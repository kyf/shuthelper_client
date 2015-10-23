package com.kyf.shuthelper_client.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kyf.shuthelper_client.MyApplication;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keyf on 2015/10/23.
 */
public class Network {

    private static final String SendTag = "shuthelper_client";

    private Handler myHandler;

    private String uri = "";

    private DatagramSocket datagramSocket;

    public Network(Activity context){
        MyApplication app = (MyApplication) context.getApplication();
        myHandler = app.getHandler();
    }

    public void listenUDP(){
        final List<String> ds = new ArrayList<String>();
        new Thread(new Runnable(){
            @Override
            public void run(){
                Message msg = Message.obtain();
                Integer port = 6060;
                byte[] message = new byte[1024];
                try {
                    datagramSocket = new DatagramSocket(port);
                    datagramSocket.setBroadcast(true);
                    DatagramPacket datagramPacket = new DatagramPacket(message, message.length);
                    try {
                        while (true) {
                            datagramSocket.receive(datagramPacket);
                            String strMsg=new String(datagramPacket.getData()).trim();
                            if(!strMsg.equals(SendTag)) {
                                ds.add(strMsg);
                            }
                        }
                    } catch (IOException e) {
                        msg.what = 1002;
                        msg.obj = e.getMessage();
                        myHandler.sendMessage(msg);
                    }
                } catch (SocketException e) {
                    msg.what = 1002;
                    msg.obj = e.getMessage();
                    myHandler.sendMessage(msg);
                }
            }
        }).start();

        Message msg = Message.obtain();
        try {
            Thread.sleep(3000);
            this.stop();
            msg.what = 1003;
            msg.obj = ds;
            myHandler.sendMessage(msg);
        }catch(InterruptedException e){
            msg.what = 1002;
            msg.obj = e.getMessage();
            myHandler.sendMessage(msg);
        }
    }

    public void boardcast(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                Message msg = Message.obtain();
                String message = SendTag;
                int server_port = 6060;
                DatagramSocket s = null;
                try {
                    s = new DatagramSocket();
                } catch (SocketException e) {
                    msg.what = 1002;
                    msg.obj = e.getMessage();
                    myHandler.sendMessage(msg);
                }
                InetAddress local = null;
                try {
                    local = InetAddress.getByName("255.255.255.255");
                } catch (UnknownHostException e) {
                    msg.what = 1002;
                    msg.obj = e.getMessage();
                    myHandler.sendMessage(msg);
                }
                int msg_length = message.length();
                byte[] messageByte = message.getBytes();
                DatagramPacket p = new DatagramPacket(messageByte, msg_length, local, server_port);
                try {
                    s.send(p);
                    s.close();
                } catch (IOException e) {
                    msg.what = 1002;
                    msg.obj = e.getMessage();
                    myHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void sendCmd(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                HttpGet get = new HttpGet(Network.this.uri);
                HttpClient client = new DefaultHttpClient();
                Message msg = Message.obtain();
                try {
                    HttpResponse res = client.execute(get);
                    String body = EntityUtils.toString(res.getEntity());
                    msg.what = 1001;
                    msg.obj = body;
                }catch(ClientProtocolException exception){
                    msg.what = 1002;
                    msg.obj = exception.getMessage();
                }catch(IOException e){
                    msg.what = 1002;
                    msg.obj = e.getMessage();
                }finally{
                    myHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void stop(){
        datagramSocket.close();
    }

    public void setURI(String uri){
        this.uri = uri;
    }
}
