import java.io.IOException;

import javax.swing.JPanel;

public class DataServer_threaded extends Thread {

	int pits;
	int seeds;
	int time;
	boolean rand;
	String server_ip;
	Integer port;
	public DataServer_threaded(int in_pits,int in_seeds,int in_time,boolean in_rand,String in_server_ip,Integer in_server_port) 
	{
		pits=in_pits;
		seeds=in_seeds;
		time=in_time;
		rand=in_rand;
		server_ip=in_server_ip;
		port=in_server_port;
	}
	public void run()
	{
		try {
			DataServer my_dataserver=new DataServer(pits,seeds,time,rand,server_ip,port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
