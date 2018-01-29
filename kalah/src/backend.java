import java.util.Scanner;


public class backend {
	private int pits=6;
	private int seeds=4;
	private int[][] gamestate;
	private long time=0;
	
	public enum mode {two_player, one_player, two_computer}
	private mode m=mode.two_player;
	private enum player {people, AI}
	public enum level {easy,medium,hard}
	private level lv=level.easy;
	private player player1 = player.people;
	private player player2 = player.people;
	private boolean gameover = false;
	private boolean tie = false;
	private boolean winner = true;//true for player1
	private boolean turn = true;//true for player1
	public backend(int hole, int seed, long t, boolean rand,mode md,level l) {//game configuration
		time=t;
		m=md;
		pits=hole;
		seeds=seed;
		lv=l;
		int board[][]=new int[2][hole+2];
		board[0][0]=0;
		board[1][hole+1]=0;
		if(md==mode.one_player)player2=player.AI;
		else if(md==mode.two_computer) {
			player1=player.AI;
			player2=player.AI;
		}
		
		for(int i=0;i<2;i++)
			for(int j=1;j<hole+1;j++) {
				board[i][j]=seed;
			}
		if(rand) {
			for(int j=1;j<30;j++) {
				int n=(int) (Math.random() * hole)+1;
				int m=(int) (Math.random() * hole)+1;
				if(board[0][n]>0) {
					board[0][n]--;
					board[0][m]++;
					board[1][hole+1-n]--;
					board[1][hole+1-m]++;
				}			
			}
		}
		gamestate=board;
		
		
	}
	
	public void printboard() {
		for(int i=0;i<pits+1;i++) {
			System.out.print(gamestate[0][i]);
			System.out.print(" ");
		}
		System.out.println("  ");
		System.out.print("  ");
		for(int i=1;i<pits+2;i++) {
			System.out.print(gamestate[1][i]);
			System.out.print(" ");
			
		}
		System.out.println(" ");
		
	}
	
	public void pie_rule() {//player 2's choice to swap
		int i=0;
		if(player2==player.people) {
			swap();
			turn=true;				
		}
		else {
			int[][] clonestate=new int[2][pits+2];
			for(int j=0;j<2;j++) {
				System.arraycopy(gamestate[j],0,clonestate[j],0,pits+2);
			}
			int numpit=pits;
			state st=new state(clonestate,numpit);
			i=AI.pie_rule(st);
		}
		if(i==1) {
			swap();
			System.out.println("swap");
			turn=true;
		}
		
	}
	
	public void swap() {
		int temp=gamestate[0][0];
		gamestate[0][0]=gamestate[1][pits+1];
		gamestate[1][pits+1]=temp;
		for(int i=1;i<pits+1;i++) {
			int t=gamestate[0][i];
			gamestate[0][i]=gamestate[1][pits+1-i];
			gamestate[1][pits+1-i]=t;
		}
	}
	
	
	public boolean mouse_move(int i) {
		if(turn) {
			basic_move(i);//player 1 move
		}
		else {
			swap();
			basic_move(pits+1-i);
			swap();
		}
		if(check_end()) {
			check_result();
			return false;
		}
		else return true;
	}
	
	public boolean move() {//the index of pit to move, from 1 to pits, left to right
		if(turn) {
			int i=0;
			if(player1==player.people) {
				System.out.println("Enter the index of pit");
				Scanner sc = new Scanner(System.in);
			    i = sc.nextInt();
	//		    sc.close();
			}
			else {
				System.out.println("AI move");
				int[][] clonestate=new int[2][pits+2];
				for(int j=0;j<2;j++) {
					System.arraycopy(gamestate[j],0,clonestate[j],0,pits+2);
				}
				int numpit=pits;
				state st=new state(clonestate,numpit);
				if(lv==level.easy)i=AI.easy(st);
				else if(lv==level.medium)i=AI.easy(st);
				else i=AI.hard(st,time);
			}
			basic_move(i);//player 1 move
			
		}
		else {
			swap();
			int i=0;
			if(player2==player.people) {
				System.out.println("Enter the index of pit");
				Scanner sc = new Scanner(System.in);
			    i = sc.nextInt();
	//		    sc.close();
			    basic_move(pits+1-i);//player 2 move
			}
			else {
				System.out.println("AI move");
				int[][] clonestate=new int[2][pits+2];
				for(int j=0;j<2;j++) {
					System.arraycopy(gamestate[j],0,clonestate[j],0,pits+2);
				}
				int numpit=pits;
				state st=new state(clonestate,numpit);
				if(lv==level.easy)i=AI.easy(st);
				else if(lv==level.medium)i=AI.easy(st);
				else i=AI.hard(st,time);
				basic_move(i);//player 2 move
			}
			
			swap();
		}
		if(check_end()) {
			check_result();
			return false;
		}
		else return true;
	}
	
	public void basic_move(int i) {
		int j=1;
		if(i<1||i>pits||gamestate[j][i]==0) {
			System.out.println("illegal move");
		}
		else {
			int total=gamestate[1][i];
			gamestate[1][i]=0;
			
			while(total>0) {
				if(j==1&&i<pits+1) {
					gamestate[j][i+1]++;
					total--;
					i++;
				}
				else if(j==1&&i==pits+1) {
					j=0;
					i=pits;
					gamestate[j][i]++;
					total--;
					
				}
				else if(j==0&&i==1) {
					j=1;
					i=1;
					gamestate[1][1]++;
					total--;
					
				}
				else if(j==0&&i>1) {
					gamestate[j][i-1]++;
					total--;
					i--;
				}
			}
		}
//		System.out.println(j);
//		System.out.println(i);
		if(j!=1||i!=pits+1) {//capture
			turn=!turn;
			if(j==1&&gamestate[1][i]==1&&gamestate[0][i]!=0) {
				gamestate[1][pits+1]=gamestate[1][pits+1]+1+gamestate[0][i];
				gamestate[1][i]=0;
				gamestate[0][i]=0;
			}
		}
//		gameover=check_end();
		
	}
	
	public boolean check_end() {//check if player 1 or 2 cannot move
		int p1=0;
		int p2=0;
		for(int i=1;i<pits+1;i++) {
			p1+=gamestate[1][i];
			p2+=gamestate[0][i];
		}
		return p1==0||p2==0;
	}
	
	public void check_result() {//count the number of seeds on both side, decide winner
		int p1=gamestate[1][pits+1];
		int p2=gamestate[0][0];
		for(int i=1;i<pits+1;i++) {
			p1+=gamestate[1][i];
			p2+=gamestate[0][i];
		}
		if(p1==p2)tie=true;
		else if(p1>p2) {
			winner=true;
		}
		else {
			winner=false;
		}
	}	
	public int[][] get_board()
	{
		return gamestate;
	}
	public Boolean is_player_one_turn()
	{
		return turn;
	}
	public void set_player_turn(Boolean in_turn)
	{
		turn=in_turn;
	}
	public Boolean is_player_one_winner()
	{
		return winner;
	}
	public void set_board(int[][] in_board)
	{
		gamestate=in_board;
	}
	public int AI_move()
	{
		int i=0;
		if(turn) {
			int[][] clonestate=new int[2][pits+2];
			for(int j=0;j<2;j++) {
				System.arraycopy(gamestate[j],0,clonestate[j],0,pits+2);
			}
			int numpit=pits;
			state st=new state(clonestate,numpit);
			if(lv==level.easy)i=AI.easy(st);
			else if(lv==level.medium)i=AI.easy(st);
			else i=AI.hard(st,time);
			return i;
		}
		else {
			swap();
			int[][] clonestate=new int[2][pits+2];
			for(int j=0;j<2;j++) {
				System.arraycopy(gamestate[j],0,clonestate[j],0,pits+2);
			}
			int numpit=pits;
			state st=new state(clonestate,numpit);
			if(lv==level.easy)i=AI.easy(st);
			else if(lv==level.medium)i=AI.easy(st);
			else i=AI.hard(st,time);
			swap();
			return pits+1-i;
		}
		
	}
	public int AI_want_Pie_Move()
	{
		int[][] clonestate=new int[2][pits+2];
		for(int j=0;j<2;j++) {
			System.arraycopy(gamestate[j],0,clonestate[j],0,pits+2);
		}
		int numpit=pits;
		state st=new state(clonestate,numpit);
		int i=AI.pie_rule(st);
		
		return i;
	}
}
