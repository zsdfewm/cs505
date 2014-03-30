package edu.purdue.cs505;

public class Message{
  int messageNumber;
  String contents;
  String processID;   //IP+"."+port
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
    String mString;
    mString=Integer.toString(messageNumber);
    return mString+" "+processID+" "+contents;
  }
  public Message(){
  }
  public Message(String s){
    String[] tmp[];
    tmp=s.split(" ",3);
    this.messageNumer=Integer.parseInt(tmp[0]);
    this.processID=tmp[1];
    this.contents=tmp[2];
  }
}

