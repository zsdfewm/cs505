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
  public int ACKindex;
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

//For message
  public void setMessage(MessageWrapper m){
    byte[] tmp;
    tmp=this.int_to_bb(m.getIndex());
    this.copyTmpToData(8,tmp);
    tmp=this.int_to_bb(m.getMsgLength());
    this.copyTmpToData(12,tmp);
    tmp=m.getMessageContents().getBytes();
    this.copyTmpToData(16,tmp);
  }
  public boolean hasMessage(){
    if (this.length>8){
      return true;
    }
    return false;
  }
  public MessageWrapper getMessage(){
    int msgIndex;
    int msgLength;
    byte[] tmp=new byte[4];
    this.copyDataToTmp(8,tmp);
    msgIndex=this.bb_to_int(tmp);
    this.copyDataToTmp(12,tmp);
    msgLength=this.bb_to_int(tmp);
    tmp=new byte[msgLength];
    this.copyDataToTmp(16,tmp);
    MessageWrapper m=new MessageWrapper(new String(tmp));
    m.setIndex(msgIndex);
    return m;
  }

  public DataWrapper(){
//A empty data for ack
    this.setLength(8);
    this.setACK(-1);
  }
  public DataWrapper(MessageWrapper m){
//A message packet
    this.setLength(8+m.getLength());
    this.setACK(-1);
    this.setMessage(m);
  }
  public DataWrapper(byte[] data){
//recver call it. set data, get message;
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
  }



  public boolean appendMessage(MessageWrapper m){
    byte[] oldData;
    if (this.length+m.getLength()<DataWrapper.UDP_DATA_SIZE){
      int oldLength=this.data.length;
      int newLength=this.data.length+m.getLength();
      oldData=this.data;
      this.data=new byte[newLength];
      byte[] tmp;
      tmp=this.int_to_bb(newLength);
      this.length=newLength;
      this.copyTmpToData(0,tmp);
      for(int i=4;i<oldLength;i++){
        this.data[i]=oldData[i];
      }
      tmp=this.int_to_bb(m.getIndex());
      this.copyTmpToData(oldLength,tmp);
      tmp=this.int_to_bb(m.getMsgLength());
      this.copyTmpToData(oldLength+4,tmp);
      tmp=m.getMessageContents().getBytes();
      this.copyTmpToData(oldLength+8,tmp);
      return true;      
    }
    else{
      return false;
    }        
  }
  public MessageWrapper[] extractMessage(){
    int numOfMessage=0;
    int lengthPointer=8;
    int messageLength=0;
    int messageIndex=0;
    byte[] tmp;
    while(lengthPointer<this.length){
      numOfMessage++;
      tmp=new byte[4];
      for(int i=0;i<4;i++){
        tmp[i]=this.data[lengthPointer+4+i];
      }
      messageLength=this.bb_to_int(tmp);
      lengthPointer+=8+messageLength;
    }
    MessageWrapper[] m=new MessageWrapper[numOfMessage];
    numOfMessage=0;
    lengthPointer=8;
    while(lengthPointer<this.length){
      tmp=new byte[4];
      for(int i=0;i<4;i++){
        tmp[i]=this.data[lengthPointer+i];
      }
      messageIndex=this.bb_to_int(tmp);
      for(int i=0;i<4;i++){
        tmp[i]=this.data[lengthPointer+4+i];
      }
      messageLength=this.bb_to_int(tmp);
      tmp=new byte[messageLength];
      for(int i=0;i<messageLength;i++){
        tmp[i]=this.data[lengthPointer+8+i];
      }
      m[numOfMessage]= new MessageWrapper(new String(tmp),messageIndex);
      lengthPointer+=8+messageLength;
      numOfMessage++;
    }
    return m;
  }
  public static void main(String args[]){
    MessageWrapper m=new MessageWrapper("aaaaaaaaaaaaaaaaaaaaaa",1);
    DataWrapper data=new DataWrapper(m);
    data.printData();
    m=new MessageWrapper("bbbbbbbbbbbbbbbbbbbbb",2);
    if (data.appendMessage(m)){
      System.out.println("Append success!");
    }
    else{
      System.out.println("Append Fails");
    }
    data.printData();
    MessageWrapper[] mArray;
    mArray=data.extractMessage();
    for(int i=0;i<mArray.length;i++){
      System.out.println(mArray[i].getMessageContents());
    }
  }  
}
