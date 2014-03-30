package edu.purdue.cs505;

import java.net.*;
import java.util.*;

public class ChannelReceiver implements Runnable{
  byte[] recvData;
  public ReliableChannelReceiver callBackReceiver;
  public int latest_index;
  public String myName;
  public int listenPort;
  public SenderBuffer senderBuffer;
  public DatagramSocket socket;
  public Random rand;
  public boolean workFlag;
  public ChannelReceiver(String myName, int listenPort, SenderBuffer senderBuffer){
    this.myName=myName;
    this.listenPort=listenPort;
    this.senderBuffer=senderBuffer;
    this.latest_index=-1;
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
  public boolean acceptMessage(int ACKindex){
    if (latest_index+1==ACKindex){
      latest_index++;
      return true;
    }    
    return false;
  }

  public void breceive(byte[] data){
//randomly drop packages;*
/*    
    double drop=rand.nextDouble();
    if (drop>0.90){
      System.out.println("Drops");
      return;
    }
*/

    DataWrapper dataWrapper=new DataWrapper(data);
    int ACKindex=dataWrapper.getACK();
    if (ACKindex!=-1){
      senderBuffer.confirmed(ACKindex);
      return;
    }
//Else there must be a message
    mArray=dataWrapper.getMessage();
    if (this.acceptMessages(mArray)){  //Accept all Messages
      senderBuffer.addACKJob(mArray[mArray.length-1].getIndex());
      for(int i=0;i<mArray.length;i++){
        if (this.callBackReceiver!=null){
          this.callBackReceiver.rreceive(mArray[i]);
        }
      }
      else{//skipped message, resend ACK;
        senderBuffer.addACKJob(this.latest_index);
      }
    }
  }

  public void run(){
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
    System.out.println(myName+" listening stops!");
  }
  public void halt(){
    this.workFlag=false;
  }
}
