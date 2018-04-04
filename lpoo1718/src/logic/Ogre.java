package logic;

import java.util.Random;

import cli.OgreStatusDisplay;

/**
 * This class represents a specific type of {@link Character}.
 * This specific type is one of the Hero's enemies (currently present in the 
 * second level and player created levels).
 * The Ogre has (pseudo)random movement patern.
 * 
 * @author João Vieira
 * @author Susana Lima
 *
 */
public class Ogre extends Character {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 678238776851162692L;
	
	private boolean stunned;
	private int stunCount;
	
	static private OgreStatusDisplay display = new OgreStatusDisplay();

	/*******************CONSTRUCTOR*******************/
	
	public Ogre(int newX, int newY, boolean MoveClub) {
		super(newX, newY, 'O', true);
		//club starts at right side of Ogre
		Club club = new Club(newX-1, newY,MoveClub);
		this.setWeapon(club);
		stunned = false;
		stunCount = 0;
	}
	
	public Ogre(int newX, int newY) {
		this(newX,newY,true);
	}
	
	/*******************GET FUNCTIONS*******************/
	
	public boolean isStunned() {
		return stunned;
	}

	public int getStunCount() {
		return stunCount;
	}

	/*******************SET FUNCTIONS*******************/
	
	public void setStunned(boolean stunned) {
		if(this.stunned != stunned) {
			if(stunned)
				display.justStunned(isShowCli());
			this.stunned = stunned;
		}
	}
	
	public void setStunCount(int stunCount) {
		this.stunCount = stunCount;
	}

	/*******************UPDATES MANAGEMENT*******************/
	
	public char getNextMove() {
		char retChar = 'E';
		if (!isMove())
			return retChar;
		Random rand = new Random();

		do {
			int randomNumber = rand.nextInt(4);

			switch(randomNumber) {
			case 0: 
				return 'u';
			case 1:
				return 'd';
			case 2:
				return 'l';
			case 3:
				return 'r';
			default:
				return 'E';
			}
		} while(retChar == 'E');
	}
	
	public void updatePosition(char cOgre, char cClub) {
		if(stunned) {
			super.setSymbol('8');
			++stunCount;
			display.stunned(2-stunCount, isShowCli());
			stunCount %= 2;
			if(stunCount == 0)
				stunned = false;
		} else {
			super.updatePosition(cOgre);
			super.setSymbol('O');
		}
		
		int X = super.getX();
		int Y = super.getY();
		super.getWeapon().setPosition(X, Y);
		super.getWeapon().updatePosition(cClub);
	}

	@Override
	public boolean isMove() {
		return true;
	}

}
