package Robot;

import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.*;
import javax.vecmath.Point3d;
import java.awt.*;
import java.util.Enumeration;

import static Robot.ArticulatedArm.przes_prymityw;
import static Robot.RobotMovement.StopObr;
import static Robot.RobotMovement.ostatniRuch;

public class CollisionDetectorPrymityw extends Behavior {
    public static boolean inCollision = false;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;
    Sphere element;

    public CollisionDetectorPrymityw(Sphere obiekt) {
        inCollision = false;
        element = obiekt;
        element.setCollisionBounds(new BoundingSphere(new Point3d(),0.07f));
    }

    /** Metoda inicjalizująca. */
    public void initialize() {
        wEnter = new WakeupOnCollisionEntry(element);
        wExit = new WakeupOnCollisionExit(element);
        wakeupOn(wEnter);
    }
    /** Reaguje na pojawienie się lub zniknięcie kolizji.*/
    public void processStimulus(Enumeration criteria) {

        System.out.println(StopObr[0]);
        System.out.println(StopObr[1]);
        inCollision = !inCollision;
        System.out.println(ostatniRuch);
        if (inCollision) {
            switch(ostatniRuch){
                case 1: StopObr[0] = true; break;
                case 2: StopObr[1] = true; break;
                case 3: StopObr[2] = true; break;
                case 4: StopObr[3] = true; break;
                case 5: StopObr[4] = true; break;
                case 6: StopObr[5] = true; break;
                case 7: StopObr[6] = true; break;
                case 8: StopObr[7] = true; break;
                case 9: StopObr[8] = true; break;
                case 10: StopObr[9] = true;break;
            }
            System.out.println("Kolizja");
            System.out.println(StopObr[0]);
            System.out.println(StopObr[1]);
            wakeupOn(wExit);
        }
        else {
            System.out.println("Koniec Kolizji");
            RobotMovement.resetStopObr();
            System.out.println(StopObr[0]);
            System.out.println(StopObr[1]);
            wakeupOn(wEnter);
        }

    }
}

