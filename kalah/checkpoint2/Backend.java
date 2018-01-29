import java.util.Scanner;
	public class Backend{
	private enum Player {P1, P2}
	private enum pieOption {Option1, Option2}

	private static int pits = 6; //base case for number of pits set to 6...just because
	private static int seeds = 10; //default seeds is 4
	private static boolean randSeeds = false; 
	private static boolean pieChoice = false;
	private static boolean playervsAi = false;
	
	public static int turnCounter = 1;
	
	private static boolean gameOver = false;
	private static boolean playerWait = true; 

	private static int[][] gameState; //creates an array of 2 rows and number of pits + 1 representing the kalah pits

	private static Player turn = Player.P1; //turn variable, changed only by turn handler. Moves are performed differently depending on this variable
	private static pieOption pieSwitch = pieOption.Option1;
	
	//class methods
	public static void pieRule(){}
	public static void printBoard(){}
	public static void setAi(boolean option){}
	public static void setPits(int numPits){}
	public static void setSeeds(int numSeeds){}
	public static void setTurn(String player){}
	public static void setRand(boolean option){} //sets randSeeds to true or false
	public static void setplayerWait(boolean option){}
	public static void setPie(boolean option){}
	public static void setPieOption(boolean option){}
	public static boolean gameInit(){
		return false;
	} //called before board drawn
	public static int[] basicMove(int[]position){
		return position;
	}
	public static void changeState(int[]position){}
	//-------Get functions-------	
	
	public int[][] getState()
	{
		return gameState;
	}
	

	public String getTurn()
	{
		if (turn == Player.P1)
			return "P1";
		else
			return "P2";
	}
	
	public boolean getplayerWait()
	{
		return playerWait;
	}
	

	public int getSeeds()
	{
		return seeds;
	}
	

	public int getPits()
	{
		return pits;
	}
	
	public boolean isGameover()
	{
		return gameOver;
	}

	public boolean isAi()
	{
		return playervsAi;
	}
	
	public boolean getPieChoice()
	{
		return pieChoice;
	}
	
	public static void main(String args[]){

		System.out.print("Hello World");
	
	
}

}
	



