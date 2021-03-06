package logic;

/**
 * This class is the base of every weapon in the game
 * giving basic movability to it.
 * 
 * @author João Vieira
 * @author Susana Lima
 */
public class Club extends Weapon {
	/**
	 * 
	 */
	private static final long serialVersionUID = -483781192116984874L;
	/**
	 * aboveKey is the flag indicating overlaping of this Weapon and a {@link logic.Unlocker} on {@link Map}.
	 */
	private boolean aboveKey ;
	
	/*******************CONSTRUCTORS*******************/
	
	/**
	 * Creates a Club in preferred x and y values. Is possible to choose
	 * if the Club is static relatively to the owner.
	 * 
	 * @param newX the new value of x
	 * @param newY the new value of y
	 * @param newMove the value to be passed to Club constructor indicating possibility of movement
	 */
	public Club(int newX, int newY, boolean newMove) {
		super(newX, newY, '*',newMove);
		setAboveKey(false);
	}
	
	/**
	 * Creates a movable Club in preferred position.
	 * 
	 * @param newX the new value of x
	 * @param newY the new value of y
	 */
	public Club(int newX, int newY) {
		this(newX,newY,true);
	}

	/*******************GET FUNCTIONS*******************/
	
	/**
	 * Retrieve the value of this Club aboveKey.
	 * 
	 * @return this Club aboveKey
	 */
	public boolean isAboveKey() {
		return aboveKey;
	}

	/*******************SET FUNCTIONS*******************/
	
	/**
	 * Set the value of this Club aboveKey.
	 * 
	 * @param aboveKey the new value of aboveKey
	 */
	public void setAboveKey(boolean aboveKey) {
		this.aboveKey = aboveKey;
	}
		
}
