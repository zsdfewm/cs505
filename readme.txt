Two members in our team:
Mu Wang (wang970)  &&  Di Jin (jind)

The makefile is included, use "make" to compiler the .class files. use "make run" for a test run.

We've implemented a reliable bi-directional FIFO channel. 

Channel.java: implements ReliableChannel
	The primary class for our channel. 

Channel(String name, int listenPort):
	The constructor. Two arguments are the name of the this endpoint and the port it should listen to.

void init(String destinationIP, int destinationPort)
	Initiate the sender.

void rsend(Message m):
	Send the message m through the channel to another end. This message is passed to ChannelSender. 

void rlisten():
	Initiate the DatagramSocket listener at the listening port, which is initialized in the Contructor.
	The recevier/listener works in the individual thread, passing information via shring the SenderBuffer. 

void halt():
	Stop all threads.	
 
ChannelSender.java:
	The upperlevel of the Sender, it passes the message to SenderBuffer. If the buffer is full, A SenderBufferOverflowException is thrown.  

SenderBuffer.jave:
	The buffer for sending messages. It maintains the number of messages in the buffer, the indexes of confirmed messages, and the indexes of sended but pending confirm messages.
	The access of the buffer is synchronized. 

UDPSender.java:
	The lowerlevel of the Sender, it works in an individual thread. It periodically asks the SenderBuffer whether there is a message to be send. (First time, resend, or ACKonly)

ChannelReceiver.java: implements ReliableChannelReceiver
	The receiver of the channel keeps listening at the designated port. Whenever a Datagram packet is received, it analyzes the format of the data. 
	If it contains ACK index, it will notify the SenderBuffer. If it contains message, for each message the rreceive(Message m) will be called, and notify SenderBuffer to send ACK information to another end.

void rreceive(Message m);
	The callback function when a message is arrived. For now, it just prints the length of the message String.  


MessageWrapper.java: implements Message
	The wrapper for Message, contains the unique index for each message.

DataWrapper.java:
	The byteArray of the UDP packet. The first 4bytes is the length of the total byteArray, the next 4bytes is the ACKed index of the message. Then contains several Messages as [int index][int length][String content].


Now how the data is transfered from one end to another will be discussed:
Channel.rsend(m): will pass the message m to ChannelSender.sendMsg(m);
ChannelSender.sendMsg(m): will pass the message to SenderBuffer.addSendJob(m);
SenderBuffer.addSendJob(m): if the buffered message is larger than SENDER_BUFFER_SIZE=20000, a SenderBufferOverflowException will be thrown. Otherwise, an unique index is given to the message increasingly. buffered_index is the largest index we have assigned. 

UDPSender.run(): Periodically asks the SenderBuffer whether there is a job to do. If sended_index<buffered_index, we have to send a message. If pending_ACK_index!=-1, it means we have received a message and have to send ACK back. We also try to pack messages if they can fit in to the UDP datagram size, which is set as 60000. 

ChannelReceiver.recvfrom(): When a message is arrived, we check whether it contains ACK information. If it does, we notify SenderBuffer that the messages that upto ACKindex have been received. 
	Then we check if the index of the received message is the one we are waiting for. If it is, accept the message, call rreceive(m) and notify SenderBuffer to send ACKindex back. 
	If the index is smaller than expected, it means the message is resend. If the index is larger than expected, it means the expected packet is skipped (missing/delay). In either cases, we send the index of the expect message to another end. 

UDPSender.confirmed(int ACKindex): If the ACKindex is larger than already confirmed_index, it means messages upto ACKindex have been received, so they will be removed from the buffer.
 Otherwise, it means we have received a same ACKindex at least twice. We will set pending_send_index=ACKindex, because the other end is asking for it. 

When randomly drop packets at receive end, the channel still works in the FIFO manner. 

