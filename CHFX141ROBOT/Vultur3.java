package CHFX141ROBOT;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.awt.*;

/**
 * Designed by Carlos Hernandez and Feng Xie 
 * Targets a robot and circles around it until it is destroyed
 * then seeks out other target
 */
public class Vultur3 extends AdvancedRobot {
	boolean movingForward;
	public void run() {
		boolean movingForward;
		setColors(Color.black,Color.black,Color.black);
		setBulletColor(Color.red);
		// sets independent robot movement
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		// keep moving forward
		setAhead(10000);
		setTurnRadarLeft(360);
		movingForward = true;
		while(true) {
			// run set commands
			execute();
		}
	}
	public void onScannedRobot(ScannedRobotEvent e) {
		// get scanned robot position
		double targetDistance = e.getDistance();
		double targetDirection = getHeading() + e.getBearing();
		double directionFromGun = normalRelativeAngleDegrees(targetDirection - getGunHeading());
		double directionFromRadar = normalRelativeAngleDegrees(targetDirection - getRadarHeading());
		// move in on the scanned robot
		if(movingForward)
			setTurnRight(normalRelativeAngleDegrees(e.getBearing() + 80));
		else
			setTurnRight(normalRelativeAngleDegrees(e.getBearing() + 100));
		// check if target is within range 
		if (Math.abs(directionFromGun) <= 5) {
			// keep radar and gun focused on target
			setTurnGunRight(directionFromGun); 
			setTurnRadarRight(directionFromRadar);
			// determines the size of the bullet to use once in range	
    		if(targetDistance > 200 && targetDistance <= 500)
        		fire(3);
    		else if(targetDistance < 200)
        		fire(1);
		} else {
			// if not within range, reposition gun and radar
			setTurnGunRight(directionFromGun);
			setTurnRadarRight(directionFromRadar);
		}
	}
	public void onHitByBullet(HitByBulletEvent e) {

	}
	public void onHitWall(HitWallEvent e) {
		// once a wall is hit reverse direction
		if (movingForward) {
			setBack(10000);
			movingForward = false;
		} else {
			setAhead(10000);
			movingForward = true;
		}
	}
	public void onHitRobot(HitRobotEvent e) {
		// if a robot is hit change direction
		if(e.isMyFault()) {
			if (movingForward) {
				setBack(10000);
				movingForward = false;
			} else {
				setAhead(10000);
				movingForward = true;
			}
		}
	}	
}