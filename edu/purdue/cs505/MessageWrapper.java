package edu.purdue.cs505;


public class MessageWrapper implements ChannelMessage{
  public String s;
  public int index;
  public boolean packed;
  public DataWrapper dataWrapper;
  public MessageWrapper(String s){
    this.packed=false;
    this.s=s;
  }
  public MessageWrapper(String s, int index){
    this.packed=false;
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
  public boolean isPacked(){
    return this.packed;
  }
  public void setPacked(){
    this.packed=true;
  }
  public void setDataWrapper(DataWrapper dataWrapper){
    this.packed=true;
    this.dataWrapper=dataWrapper;
  }
  public DataWrapper getDataWrapper(){
    return this.dataWrapper;
  }
//Util for test;
  public static MessageWrapper getMsgFromString(String s){
    MessageWrapper m;
    m=new MessageWrapper(s);
    return m;
  }
}
