package edu.purdue.cs505;

import java.util.*;

public class Broadcast implements FIFOReliableBroadcast{
  public int messageNumber;
  public Process myProc;
  public String myID;
  public UDPSender udpSender;
  public ChannelReceiver recver;
  public ChannelSender sender;
  public HashMap<String, ReliableBuffer> channelMap;
  public ReliableBroadcastReceiver rbr; 
  public Broadcast(){
  }
  public void init(Process currentProcess){
    this.rbr=null;
    this.messageNumber=-1;
    this.myProc=currentProcess;
    this.myID=myProc.getIP()+":"+Integer.toString(myProc.getPort());
    this.channelMap=new HashMap<String, ReliableBuffer>();
    this.recver=new ChannelReceiver(this.myID,myProc.getPort(),this.channelMap);
    this.sender=new ChannelSender(this.myID,channelMap);
    this.udpSender= new UDPSender(this.myID,channelMap);
    new Thread(recver).start();
    new Thread(udpSender).start();
    
  }
  public void addProcess(Process p){
    String destID=p.getIP()+":"+Integer.toString(p.getPort());
    ReliableBuffer reliableBuffer = new ReliableBuffer(myID,destID);
    channelMap.put(destID,reliableBuffer);
  }
  public void rbroadcast(Message m){
    this.messageNumber++;
    m.setMessageNumber(this.messageNumber);
    m.setProcessID(this.myID);
//Send to all;
    Set<String> procIDs=channelMap.keySet();
    String destID;
    Iterator iter= procIDs.iterator();
    while(iter.hasNext()){
      destID=(String)iter.next();
      sender.sendMessage(destID,m);
    }
    if (this.rbr!=null){
//Original broadcaster deliver the message himself
//It will never deliver the message get form channels.
      rbr.rdeliver(m);
    }
  }
  public void rblisten(BroadcastReceiver br){
    ReliableBroadcastReceiver rbr=new ReliableBroadcastReceiver(myID, sender,br, channelMap);
    this.rbr=rbr;
    recver.rlisten(rbr); 
  }
}
