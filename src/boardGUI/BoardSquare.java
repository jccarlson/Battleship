package boardGUI;

import java.awt.Color;
import java.awt.Graphics;

import boardAPI.BattleshipBoard;

/**
 * Describes a square on a {@link BoardPanel}. 
 * 
 * @author Jason Carlson
 * @version 1.0
 * @since 2015-09-19
 */
public class BoardSquare {
	
	/** Colors used in painting the BoardSquare */
	private ColorScheme sqColors;
	private Color currentColor;
	
	/** The size (height and width) of the BoardSquare, in pixels */
	private int size;
	
	/** Passed from parent Component, this is the actual x,y pixel location on that component of the top left pixel, used for painting */
	private int paintX, paintY;
	
	/** The BattleshipBoard that controls the status of this BoardSquare */
	BattleshipBoard logicalBoard;
	
	/** The logical row and column coordinates of this BoardSquare on the logicalBoard */
	public final int ROW, COL;

	
	/**
	 * Constructor with explicit paint location
	 * 
	 * @param l A BattleshipBoard which has the logic for this square
	 * @param r the row of this square on l
	 * @param c the column of this square on l
	 * @param s the size, in pixels, of the width and height to paint
	 * @param pX the x pixel location of the top left square of this BoardSquare
	 * @param pY the y pixel location of the top left square of this BoardSquare
	 * @param cs a ColorScheme describing the colors of this square. 
	 */
	public BoardSquare(BattleshipBoard l, int r, int c, int s, int pX, int pY, ColorScheme cs) {
		logicalBoard = l;
		ROW = r;
		COL = c;
		size = s;
		sqColors = cs;
		setPaintCoord(pX, pY);
		currentColor = cs.DEFAULT;
	}
	
	
	/** Sets the X and Y coordinates to paint this BoardSquare at (top left) on the parent component */
	void setPaintCoord(int pX, int pY) {
		paintX = pX;
		paintY = pY;
	}
	
	public char update() {
		char status = logicalBoard.checkStatus(ROW, COL);
		currentColor = sqColors.DEFAULT;
		if(status == 'M')
			currentColor = sqColors.MISS;
		if(status == 'H')
			currentColor = sqColors.HIT;
		return status;
	}
	
	/** 
	 *  ONLY CALL FROM PARENT paint(g) METHOD! 
	 * 	<p>
	 * 	Paints the square at paintX and paintY of Graphics g.
	 */
	void paint(Graphics g) {
		g.setColor(currentColor);
		g.fillRect(paintX, paintY, size + 1, size + 1);
		g.setColor(sqColors.BORDER);
		g.drawRect(paintX, paintY, size, size);
	}
	
	/**
	 * Call when the square is clicked on
	 * 
	 * @return status of the square after a click
	 */
	public char click() {
		if(logicalBoard.fireShot(ROW, COL)) {
			char status = update();
			return status;
		}
		return ' ';
	}	
}
