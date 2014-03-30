package edu.purdue.cs505;


public class MessageWrapper implements ChannelMessage{
  public String s;
  public int index;
  public MessageWrapper(String s){
    this.s=s;
  }
  public MessageWrapper(String s, int index){
    this.s=s;
    this.index=index;
  }
  public void setIndex(int index){
    this.index=index;
  }
  public int getIndex(){
    return this.index;
  }
  public void setMessageContents(String s){
    this.s=s;
  }
  public String getMessageContents(){
    return this.s;
  }
  public int getLength(){
//the length of wrapped message;
    return 8+s.length();
  }
  public int getMsgLength(){
    return s.length();
  }
}
