package edu.purdue.cs505;

import java.util.*;

public class Broadcast implements ReliableBroadcast{
  public int messageID;
  public Process currProc;
  public String currProcID;
  public ChannelReceiver recver;
  public ChannelSender sender;
  public SenderBuffer senderBuffer;
  public HashMap<String, ReliableBuffer> procMap; 
  public void init(Process currentProcess){
    this.currProc=currentProcss;
    this.currProcID=currProc.getIP()+":"+Integer.toString(currProc.getPort());
    this.procMap=new HashMap<String, ReliableBuffer>();
    this.senderBuffer=new TreeMap<Integer, MessageWrapper>();
    recver= new ChannelRecevier(currentProc.getIP(),currentProc.getPort());
    recver.init(currentProc.getIP(),currentProc.getPort());
    sender=new ChannelSender();
  }
  public void addProcess(Process p){
    ReliableBuffer reliableBuffer = new ReliableBuffer(p.getIP(),p.getPort());
    procMap.put(p.getIP()+":"+Integer.toString(p.getPort),reliableBuffer);
  }
  public void rbroadcast(Message m){
    m.setProcessID(this.currProcID);
    this.senderBuffer.addSendJob(m);
  }
  public void rblisten(BroadcastReceiver br){
  }
}
