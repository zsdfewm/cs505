package edu.purdue.cs505;


public class SenderBufferOverflowException extends Exception{
  int sended_index;
  int confirmed_index;
  int latest_index;
  public SenderBufferOverflowException(int sended_index, int confirmed_index, int latest_index){
    super();
    this.sended_index=sended_index;
    this.confirmed_index=confirmed_index;
    this.latest_index=latest_index;
  }
  public void printIndexInfo(){
    System.out.printf("sended[%d], confirmed[%d], buffered[%d]\n",sended_index,confirmed_index, latest_index);
  }
}
