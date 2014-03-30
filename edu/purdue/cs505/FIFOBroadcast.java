package edu.purdue.cs505;

import java.util.*;

public class FIFOBroadcast extends Broadcast {
  public static boolean srbOn=false;
  public static long deliveryDelay=0;
  public FIFOBroadcast(){
    super();
  }
  public void init(Process currentProcess){
//    if (!(FIFOBroadcast.srbOn)){
      super.init(currentProcess);
      return;
//    }
  }
  public void addProcess(Process p){
//    if (!(FIFOBroadcast.srbOn)){
       super.addProcess(p);
       return;
//    }
  }
  public void rbroadcast(Message m){
    if (!(FIFOBroadcast.srbOn)){
       super.rbroadcast(m);
       return;
    }
    this.messageNumber++;
    m.setMessageNumber(this.messageNumber);
    m.setProcessID(this.myID);
    if (this.rbr!=null){
//Original broadcaster deliver the message himself
//It will never deliver the message get form channels.
      rbr.breceive(m);
    }
    else{
      System.out.println("Should register the deliver callback first");
    }
  }
  public void rblisten(BroadcastReceiver br){
    if (!(FIFOBroadcast.srbOn)){
       super.rblisten(br);
       return;
    }
    else{
      SRBReliableBroadcastReceiver srbr=new SRBReliableBroadcastReceiver(myID, sender, br, channelMap,FIFOBroadcast.deliveryDelay);
      this.rbr=srbr;
      recver.rlisten(srbr);
    }
  }
}
