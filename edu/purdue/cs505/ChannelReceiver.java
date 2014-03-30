package edu.purdue.cs505;

import java.net.*;
import java.util.*;

public class ChannelReceiver implements Runnable{
  byte[] recvData;
  public ReliableChannelReceiver callBackReceiver;
  public String myID;
  public int listenPort;
  public DatagramSocket socket;
  public Random rand;
  public boolean workFlag;
  public HashMap<String, ReliableBuffer> channelMap; 
  public ChannelReceiver(String myID, int listenPort, HashMap<String, ReliableBuffer> channelMap){
    this.myID=myID;
    this.listenPort=listenPort;
    this.channelMap=channelMap;
    rand=new Random(12345);
    this.callBackReceiver=null;
  }

  public void init(){
    recvData=new byte[DataWrapper.UDP_DATA_SIZE];
    try{
      socket=new DatagramSocket(listenPort);
      socket.setSoTimeout(200);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  public void rlisten(ReliableChannelReceiver cb){
    this.callBackReceiver=cb;
  }
  public void breceive(byte[] data){
//randomly drop packages;*
    
    double drop=rand.nextDouble();
    if (drop>0.90){
      System.out.println("Drops");
      return;
    }


    DataWrapper dataWrapper=new DataWrapper(data);
//    dataWrapper.printData();
    String procID=dataWrapper.getProcID();
    ReliableBuffer rb=channelMap.get(procID);
    int ACKindex=dataWrapper.getACK();
//    System.out.println("getACK: "+ACKindex);
    if (ACKindex!=-1){
      rb.confirmed(ACKindex);
    }
//Else there must be a message
    if (dataWrapper.hasMessage()){
      MessageWrapper m=dataWrapper.getMessage();
      if (rb.acceptMessage(m.getIndex())){  //Accept all Messages
        if (this.callBackReceiver!=null){
          this.callBackReceiver.rreceive(m);
        }
      }
    }
  }

  public void run(){
    this.init();
    this.workFlag=true;
    while(workFlag){
      DatagramPacket recvPacket=new DatagramPacket(recvData,recvData.length);
      try{
        socket.receive(recvPacket);
        this.breceive(recvPacket.getData());
      }
      catch(SocketTimeoutException e){
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    System.out.println(myID+" listening stops!");
  }
  public void halt(){
    this.workFlag=false;
  }
}
