package edu.purdue.cs505;

import java.util.*;

public class Message{
  long ts;
  Vector<Integer> killList;
  int messageNumber;
  String contents;
  String processID;   //IP+"."+port
  public void setTimeStamp(long ts){
    this.ts=ts;
  }
  public long getTimeStamp(){
    return this.ts;
  }
  public int getMessageNumber(){
    return messageNumber;
  }
  public void setMessageNumber(int messageNumber){
    this.messageNumber=messageNumber;
  }
  public String getContents(){
    return contents;
  }
  public void setContents(String contents){
    this.contents=contents;
  }
  public String getProcessID(){
    return processID;
  }
  public void setProcessID(String processID){
    this.processID=processID;
  }
  public String toString(){
    String retval;
    retval=Integer.toString(messageNumber)+"$$$"+processID+"$$$"+contents+"$$$"+killList.size();
    if (killList.size()!=0){
      for(int i=0;i<killList.size();i++){
        retval=retval+" "+killList.elementAt(i);
      }
//System.out.println(retval);
    }
    return retval;
  }
  public Message(){
    killList=new Vector<Integer>();
  }
  public Message(String s){
    String[] tmp;
    String vtmp;
    tmp=s.split("$$$",4);
    this.messageNumber=Integer.parseInt(tmp[0]);
    this.processID=tmp[1];
    this.contents=tmp[2];
    vtmp=tmp[3];
    tmp=vtmp.split(" ");
    killList=new Vector<Integer>();
    int length=Integer.parseInt(tmp[0]);
    if (length!=0){
      for(int i=1;i<=length;i++){
        killList.add(Integer.parseInt(tmp[i]));
      }
    }
  }
  public void makesObsolete(int messageNumber){
    killList.add(messageNumber);
  }
  public int[] getObsoletedMessages(){
    int[] retval=new int[killList.size()];
    for(int i=0;i<killList.size();i++){
      retval[i]=killList.elementAt(i);
    }
    return retval;
  }
}

