import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class AI {
	
	
	public static int max_val(state s, int a, int b) {
		if(s.neighbors.size()==0) {
			s.utility=s.util();
			return s.util();
		}
		int mi=-1000;
		for(state st:s.neighbors) {
			mi=Math.max(mi,min_val(st,a,b));
			if(mi>=b) {
				s.utility=mi;
				return mi;
			}
		}
		a=Math.max(a, mi);
		s.utility=mi;
		return mi;
	}
	
	public static int min_val(state s,int a,int b) {
		if(s.neighbors.size()==0) {
			s.utility=s.util();
			return s.util();
		}
		int ma=1000;
		for(state st:s.neighbors) {
			ma=Math.min(ma, max_val(st,a,b));
			if(ma<=a) {
				s.utility=ma;
				return ma;
			}
		}
		b=Math.min(b,ma);
		s.utility=ma;
		return ma;
	}
	
	
	public static state build_minmax(state s, int level) {
		LinkedList<state> queue = new LinkedList<state>();
		queue.add(s);
		while(level>0&&queue.size()!=0) {
			int sz=queue.size();
			for(int i=0;i<sz;i++) {
				state temp=queue.pop();
				for(int j=1;j<temp.pits+1;j++) {
					state newstate=temp.subtree(j);
					if(newstate!=null) {
						
						temp.neighbors.add(newstate);
						queue.add(newstate);
					}					
					
				}
			}
			level--;
		}
		return s;
	}
	
	public static int easy(state s) {
		int res=0;
		while(s.gamestate[1][res]==0) {
			res=(int) ((Math.random() * s.pits)+1);
		}
		return res;
		
	}
	
	public static int med(state s) {
		build_minmax(s,3);
		max_val(s,-1000,1000);
		int max_ut=0;
		int res=0;
		for(state st:s.neighbors) {
			if(st.utility>max_ut) {
				res=st.move_pit;
				max_ut=st.utility;
			}
		}
		return res;
	}
/*
	private static class iterative extends TimerTask {
		public int result=1;
		public state s;
		public iterative(state st) {
			s=st;
		}
		public void run() {
			int level=3;
			while(level<10) {
				build_minmax(s,level);
				max_val(s,-1000,1000);
				int max_ut=0;
				int res=0;
				for(state st:s.neighbors) {
					if(st.utility>max_ut) {
						res=st.move_pit;
						max_ut=st.utility;
					}
				}
				level++;
				result=res;
			}
		}
	}
	
	*/
	
	
	public static int hard(state s,long t) {
		long startTime = System.currentTimeMillis();
		if(t>8000)t=5000;
		else t=t/2;
		long endTime = startTime + t;
		int depth = 3;
		int result=1;
		while(depth<10) {
			long currentTime = System.currentTimeMillis();		
			if (currentTime >= endTime) break;		
			build_minmax(s,depth);
			max_val(s,-1000,1000);
			int max_ut=-100;
			int res=1;
			for(state st:s.neighbors) {
				if(st.utility>max_ut) {
					res=st.move_pit;
					max_ut=st.utility;
				}
			}
			result=res;
		}
		return result;
/*		build_minmax(s,8);
		max_val(s,-1000,1000);
		int max_ut=0;
		int res=0;
		for(state st:s.neighbors) {
			if(st.utility>max_ut) {
				res=st.move_pit;
				max_ut=st.utility;
			}
		}
		return res;*/
	}

	public static int pie_rule(state s) {
/*		int u1=0;
		for (int i=1;i<s.pits+2;i++) {
			u1+=s.gamestate[1][i];
		}
		int u2=0;
		for (int i=0;i<s.pits+1;i++) {
			u2+=s.gamestate[0][i];
		}
		if(u1>=u2)
			return 1;
		else return 0;*/
		if(s.util()>=0)return 1;
		else return 0;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] st=new int[2][6];
		
		for(int i=0;i<5;i++)st[0][i]=1;
		st[1][1]=4;
		st[1][2]=0;
		st[1][3]=0;
		st[1][4]=0;
		st[1][5]=0;
		state s=new state(st,4);
		state res=s.maxnode(1);
		System.out.println(res.move_pit);
		res.printboard();

	}

}
