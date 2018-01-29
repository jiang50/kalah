

public class backend {
	private int pits=6;
	private int seeds=4;
	private int[][] gamestate;
	private long time=0;
	
	public enum mode {two_player, one_player, two_computer}
	private mode m=mode.two_player;
	private enum player {people, AI}
	private player player1 = player.people;
	private player player2 = player.people;
	private boolean gameover = false;
	private boolean tie = false;
	private boolean winner = true;//true for player1
	private boolean turn = true;//true for player1
	public backend(int hole, int seed, long t, boolean rand,mode md) {//game configuration
		time=t;
		m=md;
		pits=hole;
		seeds=seed;
		int board[][]=new int[2][hole+2];
		board[0][0]=0;
		board[1][hole+1]=0;
		
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
	
	public void pie_rule(boolean choice) {//player 2's choice to swap
		if(choice) {
			int temp=gamestate[0][0];
			gamestate[0][0]=gamestate[1][pits+1];
			gamestate[1][pits+1]=temp;
			for(int i=1;i<pits+1;i++) {
				int t=gamestate[0][i];
				gamestate[0][i]=gamestate[1][pits+1-i];
				gamestate[1][pits+1-i]=t;
			}
			turn=!turn;
		}
		
	}
	
	public boolean move(int i) {//the index of pit to move, from 1 to pits, left to right
		if(turn) {
			basic_move(i);//player 1 move
		}
		else {
			pie_rule(true);
			basic_move(pits+1-i);//player 2 move
			pie_rule(true);
		}
		if(check_end()) {
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
				if(j==1&&i<pits) {
					gamestate[j][i+1]++;
					total--;
					i++;
				}
				else if(j==1&&i==pits) {
					gamestate[j][i+1]++;
					total--;
					j=0;
					i=pits+1;
				}
				else if(j==0&&i==1) {
					gamestate[1][1]++;
					total--;
					j=1;
					i=1;
				}
				else if(j==0&&i>1) {
					gamestate[j][i-1]++;
					total--;
					i--;
				}
			}
		}
		if(j!=1||i!=pits+1) {//capture
			turn=!turn;
			if(j==1&&gamestate[j][i]==1) {
				gamestate[1][pits+1]=gamestate[1][pits+1]+1+gamestate[0][i];
				gamestate[1][i]=0;
				gamestate[0][i]=0;
			}
		}
		gameover=check_end();
		
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello world");
		backend ba= new backend(4,4,5000,false,mode.two_player);
		ba.printboard();
		ba.move(1);
		ba.printboard();
		ba.pie_rule(true);
		ba.printboard();
		ba.move(2);
		ba.printboard();
		ba.move(3);
		ba.printboard();
		ba.move(AI.easy(new state(ba.gamestate,ba.pits)));
		ba.printboard();
	}

}
