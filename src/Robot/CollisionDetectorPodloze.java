package Robot;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.*;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.util.Enumeration;

public class CollisionDetectorPodloze extends Behavior {
    public static boolean inCollision = false;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;
    Shape3D element;

    public CollisionDetectorPodloze(Shape3D obiekt) {
        inCollision = false;
        element = obiekt;
    }

    /** Metoda inicjalizująca. */
    public void initialize() {
        wEnter = new WakeupOnCollisionEntry(element);
        wExit = new WakeupOnCollisionExit(element);
        wakeupOn(wEnter);
    }
    /** Reaguje na pojawienie się lub zniknięcie kolizji.*/
    public void processStimulus(Enumeration criteria) {

        inCollision = !inCollision;

        if (inCollision) {
            switch(RobotMovement.ostatniRuch){
                case 1: RobotMovement.StopObr[0] = true; break;
                case 2: RobotMovement.StopObr[1] = true; break;
                case 3: RobotMovement.StopObr[2] = true; break;
                case 4: RobotMovement.StopObr[3] = true; break;
                case 5: RobotMovement.StopObr[4] = true; break;
                case 6: RobotMovement.StopObr[5] = true; break;
                case 7: RobotMovement.StopObr[6] = true; break;
                case 8: RobotMovement.StopObr[7] = true; break;
                case 9: RobotMovement.StopObr[8] = true; break;
                case 10: RobotMovement.StopObr[9] = true;break;
            }
            System.out.println("Kolizja z podlozem");
            wakeupOn(wExit);

        }
        else {
            System.out.println("Koniec Kolizji z podlozem");
            RobotMovement.resetStopObr();
            wakeupOn(wEnter);
        }

    }
}
