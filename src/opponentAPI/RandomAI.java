package opponentAPI;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

import boardAPI.battleshipInterface.BattleshipEvent;

public class RandomAI implements Opponent {
	private static final String NAME = "RANDOM SEARCH AI";
	private Random rng;
	private static final int RANDOM = 0, NORTH = 1, EAST = 2, SOUTH = 3, WEST = 4;
	private int nextShot = RANDOM;
	private Point prevShot;
	private Point foundAt;
	private Dimension dim;
	private boolean[][] isShot;

	public RandomAI(Dimension d) {
		dim = d;
		prevShot = null;
		foundAt = null;
		rng = new Random();
		isShot = new boolean[dim.width][dim.height];
		for (int x = 0; x < dim.width; x++) {
			for (int y = 0; y < dim.height; y++) {
				isShot[x][y] = false;
			}
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Point getNextShot() {

		if (prevShot == null) {
			prevShot = new Point(0, 0);
			nextShot = RANDOM;
		}
		switch (nextShot) {
		case RANDOM:
			do {
				prevShot.move(rng.nextInt(dim.width), rng.nextInt(dim.height));
			} while (isShot[prevShot.x][prevShot.y]);
			break;
		case NORTH:
			prevShot.move(prevShot.x, prevShot.y - 1);
			break;
		case EAST:
			prevShot.move(prevShot.x + 1, prevShot.y);
			break;
		case SOUTH:
			prevShot.move(prevShot.x, prevShot.y + 1);
			break;
		case WEST:
			prevShot.move(prevShot.x - 1, prevShot.y);
			break;
		}
		isShot[prevShot.x][prevShot.y] = true;
		return prevShot;
	}

	@Override
	public void sendShotResult(BattleshipEvent e) {

		if ((e.getEvent() & BattleshipEvent.HIT) == BattleshipEvent.HIT) {
			if (((e.getEvent() & BattleshipEvent.SHIP_SUNK) == BattleshipEvent.SHIP_SUNK)) {
				nextShot = RANDOM;
				foundAt = null;
			} else {
				decideHit: switch (nextShot) {
				case RANDOM:
					foundAt = (Point) prevShot.clone();
					if (prevShot.y > 0 && !isShot[prevShot.x][prevShot.y - 1]) {
						nextShot = NORTH;
						break decideHit;
					}
					if (prevShot.x < (dim.width - 1) && !isShot[prevShot.x + 1][prevShot.y]) {
						nextShot = EAST;
						break decideHit;
					}
					if (prevShot.y < (dim.height - 1) && !isShot[prevShot.x][prevShot.y + 1]) {
						nextShot = SOUTH;
						break decideHit;
					}
					if (prevShot.x > 0 && !isShot[prevShot.x - 1][prevShot.y]) {
						nextShot = WEST;
						break decideHit;
					}
					if (prevShot.x < (dim.width - 1) && !isShot[prevShot.x + 1][prevShot.y]) {
						nextShot = EAST;
						break decideHit;
					}
					nextShot = RANDOM;
					break;

				case NORTH:
					if (prevShot.y > 0 && !isShot[prevShot.x][prevShot.y - 1]) {
						nextShot = NORTH;
						break decideHit;
					}
					if (foundAt.y < (dim.height - 1) && !isShot[foundAt.x][foundAt.y + 1]) {
						prevShot = (Point) foundAt.clone();
						nextShot = SOUTH;
						break decideHit;
					}
					nextShot = RANDOM;
					break;

				case EAST:
					if (prevShot.x < (dim.width - 1) && !isShot[prevShot.x + 1][prevShot.y]) {
						nextShot = EAST;
						break decideHit;
					}
					if (foundAt.x > 0 && !isShot[foundAt.x - 1][foundAt.y]) {
						prevShot = (Point) foundAt.clone();
						nextShot = WEST;
						break decideHit;
					}
					nextShot = RANDOM;
					break;

				case SOUTH:
					if (prevShot.y < (dim.height - 1) && !isShot[prevShot.x][prevShot.y + 1]) {
						nextShot = SOUTH;
						break decideHit;
					}
					if (foundAt.y > 0 && !isShot[foundAt.x][foundAt.y - 1]) {
						prevShot = (Point) foundAt.clone();
						nextShot = NORTH;
						break decideHit;
					}
					nextShot = RANDOM;
					break;

				case WEST:
					if (prevShot.x > 0 && !isShot[prevShot.x - 1][prevShot.y]) {
						nextShot = WEST;
						break decideHit;
					}
					if (foundAt.x < (dim.width - 1) && !isShot[foundAt.x + 1][foundAt.y]) {
						prevShot = (Point) foundAt.clone();
						nextShot = EAST;
						break decideHit;
					}
					nextShot = RANDOM;
					break;
				}
			}
		} else {
			if ((foundAt != null) && ((int) prevShot.distanceSq(foundAt)) <= 1) {
				prevShot = (Point) foundAt.clone();
			}
			decideMiss: switch (nextShot) {
			case RANDOM:
				nextShot = RANDOM;
				break;

			case NORTH:
				if ((foundAt != null) && ((int) prevShot.distanceSq(foundAt)) > 1) {
					prevShot = (Point) foundAt.clone();
					if (prevShot.y < (dim.height - 1) && !isShot[prevShot.x][prevShot.y + 1]) {
						nextShot = SOUTH;
						break decideMiss;
					}
					nextShot = RANDOM;
					break decideMiss;
				}
				if (prevShot.x < (dim.width - 1) && !isShot[prevShot.x + 1][prevShot.y]) {
					nextShot = EAST;
					break decideMiss;
				}
			case EAST:
				if ((foundAt != null) && ((int) prevShot.distanceSq(foundAt)) > 1) {
					prevShot = (Point) foundAt.clone();
					if (prevShot.x > 0 && !isShot[prevShot.x - 1][prevShot.y]) {
						nextShot = WEST;
						break decideMiss;
					}
					nextShot = RANDOM;
					break decideMiss;
				}
				if (prevShot.y < (dim.height - 1) && !isShot[prevShot.x][prevShot.y + 1]) {
					nextShot = SOUTH;
					break decideMiss;
				}
			case SOUTH:
				if ((foundAt != null) && ((int) prevShot.distanceSq(foundAt)) > 1) {
					prevShot = (Point) foundAt.clone();
					if (prevShot.y > 0 && !isShot[prevShot.x][prevShot.y - 1]) {
						nextShot = NORTH;
						break decideMiss;
					}
					nextShot = RANDOM;
					break decideMiss;
				}
				if (prevShot.x > 0 && !isShot[prevShot.x - 1][prevShot.y]) {
					nextShot = WEST;
					break decideMiss;
				}

			case WEST:
				if ((foundAt != null) && ((int) prevShot.distanceSq(foundAt)) > 1) {
					prevShot = (Point) foundAt.clone();
					if (prevShot.x < (dim.width - 1) && !isShot[prevShot.x + 1][prevShot.y]) {
						nextShot = EAST;
						break decideMiss;
					}
					nextShot = RANDOM;
					break decideMiss;
				}
			default:
				nextShot = RANDOM;
				break;
			}
		}
	}

	@Override
	public String getStrategy() {
		return "RANDOM SEARCH WITH INTELLIGENT DESTROY WHEN FOUND";
	}
}
