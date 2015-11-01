package boardGUI;

import java.awt.Color;

/**
 * An unalterable (after creation) Color scheme for BoardPanel and BoardSquare
 * <p>
 * Contains final fields for the default color, hit color, miss color, ship color, and border color
 * 
 * @author Jason Carlson
 * @version 1.1
 * @since 2015-09-21
 */
public final class ColorScheme {
	
	public final Color DEFAULT;
	public final Color HIT;
	public final Color MISS;
	public final Color BORDER;
	public final Color SHIP;

	/**
	 * Constructs a ColorScheme for a BoardPanel with the following default colors:
	 * <pre>
	 * DEFAULT = Color.BLUE;
	 * HIT = Color.RED;
	 * MISS = Color.WHITE;
	 * SHIP = Color.GRAY;
	 * BORDER = Color.BLACK;</pre>
	 */
	public ColorScheme() {
		DEFAULT = Color.BLUE;
		HIT = Color.RED;
		MISS = Color.WHITE;
		SHIP = Color.GRAY;
		BORDER = Color.BLACK;
	}
	
	/**
	 * Constructs a ColorScheme for a BoardPanel with the following user-defined colors:
	 * @param dfault The default background color of the square
	 * @param hit The color after the square is hit
	 * @param miss The color after the square is missed
	 * @param ship The color of the ship (if visible)
	 * @param border The color of the borders of the grid
	 */
	public ColorScheme (Color dfault, Color hit, Color miss, Color ship, Color border) {
		DEFAULT = dfault;
		HIT = hit;
		MISS = miss;
		SHIP = ship;
		BORDER = border;
	}
}
