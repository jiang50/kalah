import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class state {
	public int pits=6;
	public int[][] gamestate;
	public List<state> neighbors;
	public int utility=0;
	public int move_pit=0;
	public state prev=null;
	public boolean turn=true;
	public state(int[][] gs, int pit) {
		gamestate=gs;
		pits=pit;
		move_pit=0;
		prev=null;
		neighbors = new ArrayList<state>();
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
	
	public state move(int i) {//player 1 move
		int j=1;
		int[][] clonestate=new int[2][pits+2];
		for(int m=0;m<2;m++) {
			System.arraycopy(gamestate[m],0,clonestate[m],0,pits+2);
		}
		state res=new state(clonestate,pits);
		res.move_pit=i;
		res.prev=this;
		res.turn=this.turn;
		if(i<1||i>res.pits||res.gamestate[j][i]==0||res.check_end()) {
	//		System.out.println("illegal move");
			return null;
		}
		else {
			int total=res.gamestate[1][i];
			res.gamestate[1][i]=0;
			
			while(total>0) {
				if(j==1&&i<pits+1) {
					res.gamestate[j][i+1]++;
					total--;
					i++;
				}
				else if(j==1&&i==pits+1) {
					j=0;
					i=pits;
					res.gamestate[j][i]++;
					total--;
					
				}
				else if(j==0&&i==1) {
					j=1;
					i=1;
					res.gamestate[1][1]++;
					total--;
					
				}
				else if(j==0&&i>1) {
					res.gamestate[j][i-1]++;
					total--;
					i--;
				}
			}
		}
//		System.out.println(i);
		if(j!=1||i!=pits+1) {
			res.turn=!res.turn;
			if(j==1&&res.gamestate[j][i]==1) {
				res.gamestate[1][pits+1]=res.gamestate[1][pits+1]+1+res.gamestate[0][i];
				res.gamestate[1][i]=0;
				res.gamestate[0][i]=0;
			}
		}
		return res;
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
	
	public state subtree(int i) {
		if(turn) {
			state res=maxnode(i);
			if(res!=null)res.move_pit=i;
			return res;
		}
		else {
			swap();
//			printboard();
			state res=maxnode(pits+1-i);
			swap();
			if(res!=null) {
				res.swap();
				res.move_pit=i;
			}
			
			return res;
		}
	}
	
	public state maxnode(int i) {
		state res=move(i);			
		if(res==null)return null;
		else if(res.turn!=this.turn) {
			res.utility=res.util();
//			System.out.println("turn change");
			return res;			
		}
		else {
//			res.printboard();
			LinkedList<state> queue = new LinkedList<state>();
			queue.add(res);
			List<state> r=new ArrayList<state>();
			while(!queue.isEmpty()) {
				int sz=queue.size();
	//			System.out.println(sz);
				for(int m=0;m<sz;m++) {
					state temp=queue.pop();
					
					for(int j=1;j<pits+1;j++) {
//						temp.printboard();
						state newstate=temp.move(j);
	//					newstate.printboard();
						if(newstate!=null&&newstate.turn==temp.turn) {
							queue.add(newstate);
	//						System.out.println("add");
						}
						else if(newstate!=null&&newstate.turn!=temp.turn) {
							r.add(newstate);
//							System.out.println("terminate");
						}
					}
				}	
			}
			if(r.size()==0)return res;
			int maxut=0;
			int index=0;
			for(int n=0;n<r.size();n++) {
				if(r.get(n).util()>=maxut) {
					maxut=r.get(n).util();
					index=n;
				}
			}
//			System.out.println(r.size());
			r.get(index).move_pit=i;
			return r.get(index);
		}	
	}
	
	
	
	public boolean check_end() {//check if player 1 or 2 cannot move
		int p1=0;
		int p2=0;
		for(int i=1;i<pits+1;i++) {
			p1+=gamestate[1][i];
			p2+=gamestate[0][i];
		}
		if(turn)return p1==0;
		else return p2==0;
	}
	
	public int util() {
		int res=0;
		for (int i=1;i<pits+1;i++) {
			res+=gamestate[1][i];
		}
		res+=2*gamestate[1][pits+1];
		for (int i=1;i<pits+1;i++) {
			res-=gamestate[1][i];
		}
		res-=2*gamestate[0][0];
		return res;
	}
	

}
