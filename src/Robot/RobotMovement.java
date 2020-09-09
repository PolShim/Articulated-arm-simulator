package Robot;

import Robot.ArticulatedArm;


import java.awt.event.KeyListener;

public class RobotMovement extends ArticulatedArm implements KeyListener {

    /** Status pozwolenia na ruch*/
    public static boolean[] StopObr = new boolean[10];

    /** funkcja resetujaca statusy zezwolenia na ruch */
    static public void resetStopObr() {
        for (int i = 0; i < 10; i++) {
            StopObr[i] = false;
        }
    }

    public static int ostatniRuch = 0;

    /** Funkcje wykonujace ruchy ramionami robota */

    static public void ObrotPodstawaPrawo(){
        if(!StopObr[0]) {
            katpodst -= predkosc;
            set_podstawa.set(przes_podstawa);
            obrotkatpodstawy.rotY((katpodst * Math.PI * 2) / 360);
            set_podstawa.mul(obrotkatpodstawy);
            trans_podstawa.setTransform(set_podstawa);
            ostatniRuch = 1;
        }
    }
    public static void ObrotPodstawaLewo(){
        if(!StopObr[1]) {
            katpodst += predkosc;
            set_podstawa.set(przes_podstawa);
            obrotkatpodstawy.rotY((katpodst * Math.PI * 2) / 360);
            set_podstawa.mul(obrotkatpodstawy);
            trans_podstawa.setTransform(set_podstawa);
            ostatniRuch = 2;
        }
    }
    public static void ObrotRamie1Gora(){
        if(!StopObr[2]) {
            if(katobr1 < 175) {
                katobr1 += predkosc;
                set_przegub1.set(przes_przegub1);
                obrot_przegub1.rotX(Math.PI / 2);
                obrotkatobr1.rotY((katobr1 * Math.PI * 2) / 360);
                set_przegub1.mul(obrot_przegub1);
                set_przegub1.mul(obrotkatobr1);
                trans_przegub1.setTransform(set_przegub1);
                ostatniRuch = 3;
            }
        }
    }
    public static void ObrotRamie1Dol(){
        if(!StopObr[3]) {
            if(katobr1 > 5) {
                katobr1 -= predkosc;
                set_przegub1.set(przes_przegub1);
                obrot_przegub1.rotX(Math.PI / 2);
                obrotkatobr1.rotY((katobr1 * Math.PI * 2) / 360);
                set_przegub1.mul(obrot_przegub1);
                set_przegub1.mul(obrotkatobr1);
                trans_przegub1.setTransform(set_przegub1);
                ostatniRuch = 4;
            }
        }
    }
    public static void ObrotRamie2Gora(){
        if(katobr2 < 240){
            if(!StopObr[4]) {
                katobr2 += predkosc;
                set_przegub2.set(przes_przegub2);
                obrotkatobr2.rotY((katobr2 * Math.PI * 2) / 360);
                set_przegub2.mul(obrotkatobr2);
                trans_przegub2.setTransform(set_przegub2);
                ostatniRuch = 5;
            }
        }
    }
    public static void ObrotRamie2Dol(){
        if(!StopObr[5]) {
            if(katobr2 > -60) {
                katobr2 -= predkosc;
                set_przegub2.set(przes_przegub2);
                obrotkatobr2.rotY((katobr2 * Math.PI * 2) / 360);
                set_przegub2.mul(obrotkatobr2);
                trans_przegub2.setTransform(set_przegub2);
                ostatniRuch = 6;
            }
        }
    }
    public static void ObrotChwytakGora(){
        if(!StopObr[6]) {
            if(kat1chwytak < 1.7) {
                kat1chwytak += 0.05;
                set_przegub3.set(przes_przegub3);
                obrot_chwytak.rotY(kat1chwytak);
                obrot_chwytak2.rotX(kat2chwytak);
                obrot_chwytak.mul(obrot_chwytak2);
                set_przegub3.mul(obrot_chwytak);
                trans_przegub3.setTransform(set_przegub3);
                ostatniRuch = 7;
            }
        }

    }
    public static void ObrotChwytakDol(){
        if(!StopObr[7]) {
            if(kat1chwytak > -1.7) {
                kat1chwytak -= 0.05;
                set_przegub3.set(przes_przegub3);
                obrot_chwytak.rotY(kat1chwytak);
                obrot_chwytak2.rotX(kat2chwytak);
                obrot_chwytak.mul(obrot_chwytak2);
                set_przegub3.mul(obrot_chwytak);
                trans_przegub3.setTransform(set_przegub3);
                ostatniRuch = 8;
            }
        }
    }
    public static void ObrotChwytakPrawo(){
        if(!StopObr[8]) {
            if(kat2chwytak < 1.7) {
                kat2chwytak += 0.05;
                set_przegub3.set(przes_przegub3);
                obrot_chwytak.rotY(kat1chwytak);
                obrot_chwytak2.rotX(kat2chwytak);
                obrot_chwytak.mul(obrot_chwytak2);
                set_przegub3.mul(obrot_chwytak);
                trans_przegub3.setTransform(set_przegub3);
                ostatniRuch = 9;
            }
        }
    }
    public static void ObrotChwytakLewo(){
        if(!StopObr[9]) {
            if(kat2chwytak > -1.7) {
                kat2chwytak -= 0.05;
                set_przegub3.set(przes_przegub3);
                obrot_chwytak.rotY(kat1chwytak);
                obrot_chwytak2.rotX(kat2chwytak);
                obrot_chwytak.mul(obrot_chwytak2);
                set_przegub3.mul(obrot_chwytak);
                trans_przegub3.setTransform(set_przegub3);
                ostatniRuch = 10;
            }
        }
    }

}
