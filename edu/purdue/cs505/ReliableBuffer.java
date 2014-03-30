package edu.purdue.cs505;

public class ReliableBuffer{
  public String procIP;
  public int port;
  public int next_index;
  public int pending_ack;
  public ReliableBuffer(String ip, int port){
    this.procIP=ip;
    this.port=port;
    this.next_index=0;
    this.pending_ack=-1;
  }
}
