package Robot;

import javax.vecmath.Vector3f;
import java.util.Vector;

import static Robot.ArticulatedArm.*;
/** Klasa zajmujaca sie nagrywaniem i odtrwarzaniem ruchow z wykorzystaniem wielowątkowości*/
public class RepeatingMovement {

    static public boolean nagrywanie_trwa = false;
    static Vector3f pocz_poz_prymityw = new Vector3f();


    public static void nagrywanie(){

        MovementStruct.katpodstawy.clear();
        MovementStruct.kat1.clear();
        MovementStruct.kat2.clear();
        MovementStruct.katchwytak1.clear();
        MovementStruct.katchwytak2.clear();
        MovementStruct.zlapany.clear();

        prymityw.getLocalToVworld(set_prymityw);
        set_prymityw.get(pocz_poz_prymityw);

        /** Opis wątku zapisujacego polozenia robota do vectorow */
        Thread thread_nagrywanie = new Thread(()->{
           while (nagrywanie_trwa){
                MovementStruct.katpodstawy.add(ArticulatedArm.katpodst);
                MovementStruct.kat1.add(ArticulatedArm.katobr1);
                MovementStruct.kat2.add(ArticulatedArm.katobr2);
                MovementStruct.katchwytak1.add(ArticulatedArm.kat1chwytak);
                MovementStruct.katchwytak2.add(ArticulatedArm.kat2chwytak);
                MovementStruct.zlapany.add(ArticulatedArm.zlapany);
               try {
                   Thread.sleep(10);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               if(MovementStruct.katpodstawy.size()>=6000)
                   nagrywanie_trwa=false;
           }
        });
        thread_nagrywanie.start();
    }
    /** Funkcja odtrwarzająca ruch robota na podstawie wczesniej zapisanych wartosci zgiec ramion i informacji o tym czy obiekt jest trzymany*/
    public static void odtwarzanie(){
        //Ustawienie prymitywu w poczatkowej pozycji
        set_prymityw.set(pocz_poz_prymityw);
        trans_prymityw.setTransform(set_prymityw);

        //Sprawdzamy czy na poczatku nagrania obiekt jest załapany, jesli tak to dołączamy go do robota
        if(MovementStruct.zlapany.get(0)){
            set_prymityw.set(new Vector3f(0.0f,0.0f,0.2f));
            trans_prymityw.setTransform(set_prymityw);
            rotScena.removeChild(bg_prymityw);
            trans_przegub3.addChild(bg_prymityw);
        }
        Thread thread_odtwarzanie = new Thread(()->{
           for(int i=0;i<MovementStruct.katpodstawy.size();i++)
           {
               //Jesli stan zmiennej mowiacej o zlapaniu zmienia sie z falszu na prawde to dołączamy obiekt do robota
               if(!MovementStruct.zlapany.get(i) && MovementStruct.zlapany.get(i+1)) {
                   set_prymityw.set(new Vector3f(0.0f,0.0f,0.2f));
                   trans_prymityw.setTransform(set_prymityw);
                   rotScena.removeChild(bg_prymityw);
                   trans_przegub3.addChild(bg_prymityw);
               }
               // Jesli stan zmiennej przechodzi z prawdy na fałsz to odlaczamy obiekt od robota
               if(MovementStruct.zlapany.get(i) && !MovementStruct.zlapany.get(i+1))
               {
                   prymityw.getLocalToVworld(set_prymityw);
                   set_prymityw.get(przes_prymityw);
                   trans_przegub3.removeChild(bg_prymityw);
                   rotScena.addChild(bg_prymityw);
                   zlapany=false;
                   Spadanie();
               }
                // Przypisujemy wartosci zapisane do zmiennych ktore ustawiaja robota i wykonujemy ruch kazdym z przegubow
               ArticulatedArm.katpodst= MovementStruct.katpodstawy.get(i);
               ArticulatedArm.katobr1= MovementStruct.kat1.get(i);
               ArticulatedArm.katobr2= MovementStruct.kat2.get(i);
               ArticulatedArm.kat1chwytak= MovementStruct.katchwytak1.get(i);
               ArticulatedArm.kat2chwytak= MovementStruct.katchwytak2.get(i);

               RobotMovement.ObrotPodstawaPrawo();
               RobotMovement.ObrotRamie1Gora();
               RobotMovement.ObrotRamie2Gora();
               RobotMovement.ObrotChwytakGora();
               RobotMovement.ObrotChwytakPrawo();

               try {
                   Thread.sleep(10);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        thread_odtwarzanie.start();

        ArticulatedArm.nagrywanie_on_button.setEnabled(true);
        ArticulatedArm.odtwarzanie_button.setEnabled(true);
    }

}
    // Struktura przechowujaca zapisane dane
class MovementStruct{
    static Vector<Float> katpodstawy = new Vector<>();
    static Vector<Float> kat1 =  new Vector<>();
    static Vector<Float> kat2 =  new Vector<Float>();
    static Vector<Float> katchwytak1 =  new Vector<Float>();
    static Vector<Float> katchwytak2 =  new Vector<>();
    static Vector<Boolean> zlapany =  new Vector<Boolean>();

}
