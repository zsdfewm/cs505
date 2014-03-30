package edu.purdue.cs505;

import java.nio.*;

/*
The data here is the data there.
[Index][Length][String]
//Version 1: Only One Message
*/


public class DataWrapper{
  public static final int UDP_DATA_SIZE=60000;
  public byte[] data;
  public int length;
  public int message_begin;
  public int ACKindex;
  public String procID;
  private byte[] int_to_bb(int myInteger){
    return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(myInteger).array();
  }
  private int bb_to_int(byte[] byteArray){
    return ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).getInt();
  }
  private void copyTmpToData(int begin, byte[] tmp){
    for(int i=0;i<tmp.length;i++){
      data[i+begin]=tmp[i];
    }
  } 
  private void copyDataToTmp(int begin, byte[] tmp){
    for(int i=0;i<tmp.length;i++){
      tmp[i]=data[begin+i];
    }
  }
  public void printData(){
    for(int i=0;i<this.length;i++){
      System.out.printf("[%d]",data[i]);
    }
    System.out.println();
  }

  public byte[] getData(){
    return this.data;
  }
//For The length of the total Message, 
  public void setLength(int length){
    this.length=length;
    this.data=new byte[length];
    byte[] tmp;
    tmp=this.int_to_bb(length);
    this.copyTmpToData(0,tmp);
  }
  public int getLength(){
    return this.length;
  }
//For the ACK message;
  public void setACK(int ACKindex){
    this.ACKindex=ACKindex;
    byte[] tmp;
    tmp=this.int_to_bb(ACKindex);
    this.copyTmpToData(4,tmp);
  }
  public int getACK(){
    return this.ACKindex;
  }


//For procID;
  public void setProcID(String procID){
    byte[] tmp;
    tmp=this.int_to_bb(procID.length());
    this.copyTmpToData(8,tmp);
    tmp=procID.getBytes();
    this.copyTmpToData(12,tmp);
    this.message_begin=8+4+procID.length();
  }
//For message
  public void setMessage(MessageWrapper m){
    byte[] tmp;
    tmp=this.int_to_bb(m.getIndex());
    this.copyTmpToData(this.message_begin,tmp);
    tmp=this.int_to_bb(m.getMsgLength());
    this.copyTmpToData(this.message_begin+4,tmp);
    tmp=m.getMessageContents().getBytes();
    this.copyTmpToData(this.message_begin+8,tmp);
  }
  public boolean hasMessage(){
    if (this.length>this.message_begin){
      return true;
    }
    return false;
  }
  public MessageWrapper getMessage(){
    int msgIndex;
    int msgLength;
    byte[] tmp=new byte[4];
    this.copyDataToTmp(this.message_begin,tmp);
    msgIndex=this.bb_to_int(tmp);
    this.copyDataToTmp(this.message_begin+4,tmp);
    msgLength=this.bb_to_int(tmp);
    tmp=new byte[msgLength];
    this.copyDataToTmp(this.message_begin+8,tmp);
    MessageWrapper m=new MessageWrapper(new String(tmp));
    m.setIndex(msgIndex);
    return m;
  }

  public DataWrapper(String myID){
//A empty data for ack
    this.setLength(8+4+myID.length());
    this.setACK(-1);
    this.setProcID(myID);
  }
  public DataWrapper(String myID, MessageWrapper m){
//A message packet
    this.setLength(8+4+myID.length()+m.getLength());
    this.setACK(-1);
    this.setProcID(myID);
    this.setMessage(m);
  }
  public DataWrapper(byte[] data){
//recver call it. set data, get procID and get message_begin
    int tmpInt;
    byte[] tmp=new byte[4];
//data length
    tmp[0]=data[0];tmp[1]=data[1];tmp[2]=data[2];tmp[3]=data[3];
    tmpInt=this.bb_to_int(tmp);
    this.setLength(tmpInt);
//copy data
    for(int i=0;i<tmpInt;i++){
      this.data[i]=data[i];
    }
//ACKindex
    this.copyDataToTmp(4,tmp);
    tmpInt=this.bb_to_int(tmp);
    this.ACKindex=tmpInt;
//Get ProcID;
    this.copyDataToTmp(8,tmp);
    tmpInt=this.bb_to_int(tmp);
    tmp=new byte[tmpInt];
    this.copyDataToTmp(12,tmp);
    this.procID=new String(tmp);
    this.message_begin=12+tmpInt;
  }
  public String getProcID(){
    return this.procID;
  }

  public static void main(String args[]){
  }  
}
