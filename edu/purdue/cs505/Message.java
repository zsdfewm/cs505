package edu.purdue.cs505;

public class Message{
  int messageNumber;
  String contents;
  String processID;
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
}

