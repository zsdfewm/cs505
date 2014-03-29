package edu.purdue.cs505;

public class Process{
  String IP;
  int port;
  public Process(String IP, int port){
    this.IP=IP;
    this.port=port;
  }
  public String getIP(){
    return this.IP;
  }
  public int getPort(){
    return this.port;
  }
}
