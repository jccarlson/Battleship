package boardGUI;

import java.awt.Color;

/**
 * An unalterable (after creation) Color scheme for BoardPanel and BoardSquare
 * <p>
 * Contains final fields for the default color, hit color, miss color, and border color
 * 
 * @author Jason Carlson
 * @version 1.0
 * @since 2015-09-21
 */
public final class ColorScheme {
	
	public final Color DEFAULT;
	public final Color HIT;
	public final Color MISS;
	public final Color BORDER;

	public ColorScheme (Color dfault, Color hit, Color miss, Color border) {
		DEFAULT = dfault;
		HIT = hit;
		MISS = miss;
		BORDER = border;
	}
}
