package Robot;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.Transform3D;
import javax.vecmath.*;


public class ArticulatedArm extends JFrame implements KeyListener , ActionListener {
        SimpleUniverse Universe;

    /** Przyciski w programie */
        Button instrukcja_button = new Button("Instrukcja obsługi programu");
        static Button nagrywanie_on_button = new Button("Rozpocznij nagrywanie");
        Button nagrywanie_off_button = new Button("Koniec nagrywania");
        static Button odtwarzanie_button = new Button("Odtworz ruch");
        Button resetKamery = new Button("Reset kamery");
    /** TransformGroupy dla poszczegolnych elementów*/

        static TransformGroup trans_podstawa = new TransformGroup();
        static TransformGroup rotScena = new TransformGroup();
        static TransformGroup trans_przegub1 = new TransformGroup();
        static TransformGroup trans_przegub2 = new TransformGroup();
        static TransformGroup trans_przegub3 = new TransformGroup();
        static TransformGroup trans_prymityw = new TransformGroup();
        static BranchGroup bg_prymityw;

    /** Transformacje dla obserwatora*/
        Transform3D set_obserwator = new Transform3D();



    /** Transformacje potrzebne do poczatkowego ustawienia robota*/

        static Transform3D  set_podstawa = new Transform3D();
        static Transform3D  set_ramie1 = new Transform3D();
        static Transform3D  set_ramie2 = new Transform3D();
        static Transform3D  set_przegub3 = new Transform3D();
        static Transform3D  set_chwytak = new Transform3D();
        static Transform3D  set_przegub1 = new Transform3D();
        static Transform3D  set_przegub2 = new Transform3D();
        static Transform3D  set_prymityw = new Transform3D();

        static Transform3D  obrot_ramie1 = new Transform3D();
        static Transform3D  obrot_chwytak = new Transform3D();
        static Transform3D  obrot_chwytak2 = new Transform3D();
        static Transform3D  obrot_przegub1 = new Transform3D();


    /** Transformacje potrzebne do wlasciwego ruchu robota*/
    static Transform3D obrotkatpodstawy = new Transform3D();
        static Transform3D obrotkatobr1 = new Transform3D();
        static Transform3D obrotkatobr2 = new Transform3D();

    /** Wektory zawierajace poczatkowe polozenia elementow */
        static Vector3f przes_podstawa = new Vector3f(0.0f,0.28f,0.0f);
        static Vector3f przes_przegub1 = new Vector3f(0.0f,0.35f,0.0f);
        static Vector3f przes_ramie1 = new Vector3f(0.75f,0.0f,0.0f);
        static Vector3f przes_przegub2 = new Vector3f(1.55f,0.0f,0.0f);
        static Vector3f przes_ramie2 = new Vector3f(0.0f,0.0f,0.7f);
        static Vector3f przes_przegub3 = new Vector3f(0.0f,0.0f,1.4f);
        static Vector3f przes_chwytak = new Vector3f(0.0f,0.0f,0.12f);
        static Vector3f przes_prymityw = new Vector3f(2.5f,0.12f,0.0f);
        static Vector3f przes_obserwator = new Vector3f(0.0f,0.4f,10.0f);
        /** elementy robota */
        Cylinder podstawa;
        Cylinder przegub1;
        Box       ramie1;
        Cylinder przegub2;
        Box       ramie2;
        Sphere    przegub3;
        static Box       chwytak;
        static Sphere    prymityw;


    /** Ograniczenie obszaru renderowania */
        protected BoundingSphere ograniczenie_pola = new BoundingSphere(new Point3d(0.0,0.0,0.0),100);

        public static float predkosc = (float) 0.9;
        /** Zmienne ustawiające kąty odgięć ramion robota */
        public static float katpodst = 0;
        public static float katobr1 =30;
        public static float katobr2 = 30;
        public static float kat1chwytak = 0;
        public static float kat2chwytak = 0;

        /** Zmienna mowiaca czy obiekt jest zlapany*/
        public static boolean zlapany = false;

        /** Deklaracja obiektow klasy wykrywajacej kolizje */

        static CollisionDetectorPrymityw prtmitywdetect;
        static CollisionDetectorPodloze podlozedetect;

    ArticulatedArm(){
        /** Utworzenie okna i przygotowanie sceny*/
        super("ArticulatedArmAlpha");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);


        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(1400,1400));
        add(canvas3D);
        canvas3D.addKeyListener(this);
        instrukcja_button.setBounds(0,0,150,20);
        nagrywanie_on_button.setBounds(0,0,150,20);
        nagrywanie_off_button.setBounds(0,0,150,20);
        odtwarzanie_button.setBounds(0,0,150,20);

        instrukcja_button.setFont(new Font("Courier New",(Font.BOLD|Font.ITALIC),20));
        nagrywanie_on_button.setFont(new Font("Courier New",(Font.BOLD|Font.ITALIC),20));
        nagrywanie_off_button.setFont(new Font("Courier New",(Font.BOLD|Font.ITALIC),20));
        resetKamery.setFont(new Font("Courier New",(Font.BOLD|Font.ITALIC),20));
        odtwarzanie_button.setFont(new Font("Courier New",(Font.BOLD|Font.ITALIC),20));

        nagrywanie_off_button.setEnabled(false);
        odtwarzanie_button.setEnabled(false);

        Panel panelPrzyciski = new Panel(new GridLayout());
        add("North",panelPrzyciski);
        panelPrzyciski.add(instrukcja_button);
        instrukcja_button.addActionListener(this);
        panelPrzyciski.add(nagrywanie_on_button);
        nagrywanie_on_button.addActionListener(this);
        panelPrzyciski.add(nagrywanie_off_button);
        nagrywanie_off_button.addActionListener(this);
        panelPrzyciski.add(resetKamery);
        resetKamery.addActionListener(this);
        panelPrzyciski.add(odtwarzanie_button);
        odtwarzanie_button.addActionListener(this);
        pack();
        setVisible(true);
        Sounds.grajDzwiek("welcomeSound.WAV");



        Universe = new SimpleUniverse(canvas3D);
        BranchGroup scene = set_up_scene(Universe);

        OrbitBehavior obr_kam = new OrbitBehavior();
        obr_kam.setSchedulingBounds(ograniczenie_pola);
        /** Początkowe ustawienie obserwatora*/
        set_obserwator.set(przes_obserwator);
        Universe.getViewingPlatform().getViewPlatformTransform().setTransform(set_obserwator);
        Universe.getViewer().getView().setBackClipDistance(1000);
        /** Dodanie obrotu kamery myszka */
        Universe.getViewingPlatform().setViewPlatformBehavior(obr_kam);


        Universe.addBranchGraph(scene);
    }

    private BranchGroup set_up_scene(SimpleUniverse Universe){
            BranchGroup scena = new BranchGroup();
            UstawSwiatlo(scena);

            rotScena.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            scena.addChild(rotScena);


        /**Podłoże na ktorym stoi robot*/
            Appearance wyg_podloza = new Appearance();
            wyg_podloza.setTexture(createTexture("img/podloze.jpg"));
            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.BLEND);
            texAttr.setPerspectiveCorrectionMode(TextureAttributes.NICEST);
            wyg_podloza.setTextureAttributes(texAttr);

        Point3f[]  coords = new Point3f[8];
        for(int i = 0; i< 8; i++)
            coords[i] = new Point3f();

        Point2f[]  tex_coords = new Point2f[8];
        for(int i = 0; i< 8; i++)
            tex_coords[i] = new Point2f();

        coords[0].y = 0.0f;
        coords[1].y = 0.0f;
        coords[2].y = 0.0f;
        coords[3].y = 0.0f;

        coords[0].x = 5f;
        coords[1].x = 5f;
        coords[2].x = -5f;
        coords[3].x = -5f;

        coords[0].z = 5f;
        coords[1].z = -5f;
        coords[2].z = -5f;
        coords[3].z = 5f;

        coords[4].y = 0f;
        coords[5].y = 0f;
        coords[6].y = 0f;
        coords[7].y = 0f;

        coords[4].x = 5f;
        coords[5].x = 5f;
        coords[6].x = -5f;
        coords[7].x = -5f;

        coords[4].z = -5f;
        coords[5].z = 5f;
        coords[6].z = 5f;
        coords[7].z = -5f;

        tex_coords[0].x = 0.0f;
        tex_coords[0].y = 0.0f;

        tex_coords[1].x = 10.0f;
        tex_coords[1].y = 0.0f;

        tex_coords[2].x = 0.0f;
        tex_coords[2].y = 10.0f;

        tex_coords[3].x = 10.0f;
        tex_coords[3].y = 10.0f;

        tex_coords[4].x = 0.0f;
        tex_coords[4].y = 0.0f;

        tex_coords[5].x = 10.0f;
        tex_coords[5].y = 0.0f;

        tex_coords[6].x = 0.0f;
        tex_coords[6].y = -10.0f;

        tex_coords[7].x = 10.0f;
        tex_coords[7].y = -10.0f;

        QuadArray qa_ground = new QuadArray(8, GeometryArray.COORDINATES|
                GeometryArray.TEXTURE_COORDINATE_2);
        qa_ground.setCoordinates(0,coords);

        qa_ground.setTextureCoordinates(0, tex_coords);

        Shape3D podloze = new Shape3D(qa_ground);

        podloze.setAppearance(wyg_podloza);
        scena.addChild(podloze);


        /** Wyglad robota */
            Material podklad_podtexture = new Material();
            podklad_podtexture.setDiffuseColor(new Color3f(Color.white));
            podklad_podtexture.setSpecularColor(new Color3f(Color.white));
            podklad_podtexture.setAmbientColor(new Color3f(Color.pink));

            Appearance wyg_robota = new Appearance();
            wyg_robota.setMaterial(podklad_podtexture);
        /** Wyglad przegubow */
            Material podklad_przegubu = new Material();
            podklad_przegubu.setDiffuseColor(new Color3f(Color.white));
            podklad_przegubu.setSpecularColor(new Color3f(Color.white));
            podklad_przegubu.setAmbientColor(new Color3f(Color.gray));

            Appearance wyg_przegubu = new Appearance();
            wyg_przegubu.setMaterial(podklad_przegubu);
        /** wyglad chwytaka */
            Material podklad_chwytaka = new Material();
            podklad_chwytaka.setDiffuseColor(new Color3f(Color.gray));
            podklad_chwytaka.setSpecularColor(new Color3f(Color.white));
            podklad_chwytaka.setAmbientColor(new Color3f(Color.red));

            Appearance wyg_chwytaka = new Appearance();
            wyg_chwytaka.setMaterial(podklad_chwytaka);

        /** elementy robota */
        podstawa = new Cylinder(0.35f,0.5f,Cylinder.GENERATE_NORMALS | Cylinder.GENERATE_TEXTURE_COORDS,wyg_robota);
        przegub1 = new Cylinder (0.15f,0.4f,wyg_przegubu);
        ramie1  = new Box(0.1f,0.1f,0.7f,wyg_robota);
        przegub2 = new Cylinder (0.15f,0.4f,wyg_przegubu);
        ramie2  = new Box(0.08f,0.08f,0.7f,wyg_robota);
        przegub3= new Sphere(0.12f,wyg_przegubu);
        chwytak = new Box(0.05f,0.05f,0.05f,wyg_chwytaka);
        prymityw= new Sphere(0.1f);


        /** Funkcja ustawiajaca polozenia poczatkowe elementow robota */
        SetUpRobot(rotScena,trans_podstawa,trans_przegub1,trans_przegub2,trans_przegub3,podstawa,przegub1,ramie1,przegub2,ramie2,przegub3,chwytak,prymityw,katpodst,katobr1,katobr2);


        /** Dodanie wykrywania kolizji */
        prtmitywdetect = new CollisionDetectorPrymityw(prymityw);
        prtmitywdetect.setSchedulingBounds(ograniczenie_pola);
        scena.addChild(prtmitywdetect);

        podlozedetect = new CollisionDetectorPodloze(podloze);
        podlozedetect.setSchedulingBounds(ograniczenie_pola);
        scena.addChild(podlozedetect);

        /** Tło dookoła sceny */
        Appearance wyglad_tla = new Appearance();
        wyglad_tla.setTexture(createTexture("img/clouds.jpg"));

        Appearance wyg_sufit = new Appearance();
        Material material_sufit = new Material();
        material_sufit.setEmissiveColor(new Color3f(Color.white));
        wyg_sufit.setMaterial(material_sufit);


        Appearance wyg_podloga = new Appearance();
        wyg_podloga.setTexture(createTexture("img/podloga.jpg"));



        Box sciana1=new Box(0.1f,20f,60f,Box.GENERATE_NORMALS|Box.GENERATE_TEXTURE_COORDS,wyglad_tla);
        Box sciana2=new Box(0.1f,20f,60f,Box.GENERATE_NORMALS|Box.GENERATE_TEXTURE_COORDS,wyglad_tla);
        Box sciana3=new Box(60f,20f,0.1f,Box.GENERATE_NORMALS|Box.GENERATE_TEXTURE_COORDS,wyglad_tla);
        Box sciana4=new Box(60f,20f,0.1f,Box.GENERATE_NORMALS|Box.GENERATE_TEXTURE_COORDS,wyglad_tla);
        Box sufit  = new Box(60f,0.1f,60f,Box.GENERATE_NORMALS|Box.GENERATE_TEXTURE_COORDS,wyg_sufit);
        Box podloga  = new Box(60f,0.1f,60f,Box.GENERATE_NORMALS|Box.GENERATE_TEXTURE_COORDS,wyg_podloga);


        Transform3D move_sciana1 = new Transform3D();
        Transform3D move_sciana2 = new Transform3D();
        Transform3D move_sciana3 = new Transform3D();
        Transform3D move_sciana4 = new Transform3D();
        Transform3D move_sufit   = new Transform3D();
        Transform3D move_podloga = new Transform3D();


        move_sciana1.set(new Vector3f(30f,0f,0f));
        move_sciana2.set(new Vector3f(-30f,0f,0f));
        move_sciana3.set(new Vector3f(0f,0f,30f));
        move_sciana4.set(new Vector3f(0f,0f,-30f));
        move_sufit.set(new Vector3f(0f,30f,0f));
        move_podloga.set(new Vector3f(0f,-30f,0));



        TransformGroup transSciana1 = new TransformGroup(move_sciana1);
        TransformGroup transSciana2 = new TransformGroup(move_sciana2);
        TransformGroup transSciana3 = new TransformGroup(move_sciana3);
        TransformGroup transSciana4 = new TransformGroup(move_sciana4);
        TransformGroup transSufit   = new TransformGroup(move_sufit);
        TransformGroup transPodloga = new TransformGroup(move_podloga);

        transSciana1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transSciana2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transSciana3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transSciana4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transSufit.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transPodloga.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);


        transSciana1.addChild(sciana1);
        transSciana2.addChild(sciana2);
        transSciana3.addChild(sciana3);
        transSciana4.addChild(sciana4);
        transSufit.addChild(sufit);
        transPodloga.addChild(podloga);



        scena.addChild(transSciana1);
        scena.addChild(transSciana2);
        scena.addChild(transSciana3);
        scena.addChild(transSciana4);
        scena.addChild(transSufit);
        scena.addChild(transPodloga);



        scena.compile();
        return scena;
    }
    private Texture createTexture(String sciezka) {
        // Załadowanie tekstury
        TextureLoader loader = new TextureLoader(sciezka, null);
        ImageComponent2D image = loader.getImage();

        if (image == null) {
            System.out.println("Nie udało się załadować tekstury: " + sciezka);
        }


        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB, image.getWidth(), image.getHeight());
        texture.setMagFilter(Texture.NICEST);
        texture.setMinFilter(Texture.NICEST);
        texture.setImage(0, image);

        return texture;
    }
     private void UstawSwiatlo ( BranchGroup bg){
        /** Oswietlenie całości sceny */
        Color3f ambientColor = new Color3f(0.3f,0.3f,0.3f);
        AmbientLight ambientLight = new AmbientLight(ambientColor);
        ambientLight.setInfluencingBounds(ograniczenie_pola);
        bg.addChild(ambientLight);
        /** Swiatlo kierunkowe */
        Color3f kolorswiatla = new Color3f(1.0f,1.0f,1.0f);
        Vector3f kierunekswiatla = new Vector3f(3.0f,-8.0f,-4.0f);
        DirectionalLight swiatlokierunkowe = new DirectionalLight(kolorswiatla,kierunekswiatla);
        swiatlokierunkowe.setInfluencingBounds(ograniczenie_pola);
        bg.addChild(swiatlokierunkowe);

     }
     private void SetUpRobot (TransformGroup rotScena,TransformGroup Transformacja_podstawy,TransformGroup Transformacja_przegub1,TransformGroup Transfromacja_przegub2,TransformGroup Transformacja_przegub3,Cylinder podstawa,Cylinder przegub1,Box ramie1,Cylinder przegub2,Box ramie2,Sphere przegub3,Box chwytak,Sphere prymityw,float katpodst,float katobr1,float katobr2){
         /**Podstawa robota obracajaca sie w osi Z*/
         set_podstawa.set(przes_podstawa);
         obrotkatpodstawy.rotY((katpodst*Math.PI*2)/360);
         set_podstawa.mul(obrotkatpodstawy);
         Transformacja_podstawy.setTransform(set_podstawa);
         Transformacja_podstawy.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         Transformacja_podstawy.addChild(podstawa);
         rotScena.addChild(Transformacja_podstawy);
         rotScena.setCapability(rotScena.ALLOW_CHILDREN_EXTEND);
         rotScena.setCapability(rotScena.ALLOW_CHILDREN_READ);
         rotScena.setCapability(rotScena.ALLOW_CHILDREN_WRITE);


         /** Przegub 1 miedzy podstawa a pierwszym ramieniem */
         set_przegub1.set(przes_przegub1);
         obrot_przegub1.rotX(Math.PI/2);
         obrotkatobr1.rotY((katobr1*Math.PI*2)/360);
         set_przegub1.mul(obrot_przegub1);
         set_przegub1.mul(obrotkatobr1);
         Transformacja_przegub1.setTransform(set_przegub1);
         Transformacja_przegub1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         Transformacja_przegub1.addChild(przegub1);
         Transformacja_podstawy.addChild(Transformacja_przegub1);
         /** Ramie 1 */
         set_ramie1.set(przes_ramie1);
         obrot_ramie1.rotY(Math.PI/2);
         set_ramie1.mul(obrot_ramie1);
         TransformGroup Transformacja_ramie1= new TransformGroup(set_ramie1);
         Transformacja_ramie1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         Transformacja_ramie1.addChild(ramie1);
         Transformacja_przegub1.addChild(Transformacja_ramie1);
         /** Przegub 2 */
         set_przegub2.set(przes_przegub2);
         obrotkatobr2.rotY((katobr2*Math.PI*2)/360);
         set_przegub2.mul(obrotkatobr2);
         Transfromacja_przegub2.setTransform(set_przegub2);
         Transfromacja_przegub2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         Transfromacja_przegub2.addChild(przegub2);
         Transformacja_przegub1.addChild(Transfromacja_przegub2);
         /** Ramie 2*/
         set_ramie2.set(przes_ramie2);
         TransformGroup Transformacja_ramie2 = new TransformGroup(set_ramie2);
         Transformacja_ramie2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         Transformacja_ramie2.addChild(ramie2);
         Transfromacja_przegub2.addChild(Transformacja_ramie2);
         /** Przegub 3 */
         set_przegub3.set(przes_przegub3);
         obrot_chwytak.rotY((kat1chwytak*Math.PI*2)/360);
         set_przegub3.mul(obrot_chwytak);
         obrot_chwytak2.rotX((kat2chwytak*Math.PI*2)/360);
         set_przegub3.mul(obrot_chwytak2);
         set_chwytak.set(przes_chwytak);
         TransformGroup Transformacja_chwytak = new TransformGroup(set_chwytak);
         Transformacja_chwytak.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         Transformacja_chwytak.addChild(chwytak);
         Transformacja_przegub3.setTransform(set_przegub3);
         Transformacja_przegub3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         Transformacja_przegub3.addChild(przegub3);
         Transformacja_przegub3.addChild(Transformacja_chwytak);
         Transfromacja_przegub2.addChild(Transformacja_przegub3);
         trans_przegub3.setCapability(trans_przegub3.ALLOW_CHILDREN_EXTEND);
         trans_przegub3.setCapability(trans_przegub3.ALLOW_CHILDREN_WRITE);
         trans_przegub3.setCapability(trans_przegub3.ALLOW_CHILDREN_READ);
         /** Prymityw */
         bg_prymityw = new BranchGroup();
         bg_prymityw.setCapability(bg_prymityw.ALLOW_DETACH);
         bg_prymityw.setCapability(bg_prymityw.ALLOW_CHILDREN_WRITE);
         bg_prymityw.setCapability(bg_prymityw.ALLOW_CHILDREN_READ);
         bg_prymityw.setCapability(bg_prymityw.ALLOW_CHILDREN_EXTEND);
         set_prymityw.set(przes_prymityw);
         trans_prymityw.setTransform(set_prymityw);
         trans_prymityw.addChild(prymityw);
         trans_prymityw.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         trans_prymityw.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
         trans_prymityw.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
         trans_prymityw.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
         bg_prymityw.addChild(trans_prymityw);
         rotScena.addChild(bg_prymityw);


     }



    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                RobotMovement.ObrotPodstawaLewo(); break;
            case KeyEvent.VK_RIGHT:
                RobotMovement.ObrotPodstawaPrawo(); break;
            case KeyEvent.VK_UP:
                RobotMovement.ObrotRamie1Gora(); break;
            case KeyEvent.VK_DOWN:
                RobotMovement.ObrotRamie1Dol(); break;
            case KeyEvent.VK_W:
                RobotMovement.ObrotRamie2Gora(); break;
            case KeyEvent.VK_S:
                RobotMovement.ObrotRamie2Dol(); break;
            case KeyEvent.VK_NUMPAD8:
                RobotMovement.ObrotChwytakGora(); break;
            case KeyEvent.VK_NUMPAD5:
                RobotMovement.ObrotChwytakDol(); break;
            case KeyEvent.VK_NUMPAD6:
                RobotMovement.ObrotChwytakPrawo(); break;
            case KeyEvent.VK_NUMPAD4:
                RobotMovement.ObrotChwytakLewo(); break;
            case KeyEvent.VK_SPACE: {
                if (!zlapany)
                    Złap();
                else
                    Pusc();
                break;
            }
        }
    }
    public void keyReleased(KeyEvent e){

    }
    public void keyTyped(KeyEvent e){

    }
        @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==instrukcja_button){
            new InstrukcjaObslugi();
        }
        if(e.getSource()==resetKamery){
            Universe.getViewingPlatform().getViewPlatformTransform().setTransform(set_obserwator);
        }
        if(e.getSource()==nagrywanie_on_button){
            RepeatingMovement.nagrywanie_trwa=true;
            nagrywanie_on_button.setEnabled(false);
            nagrywanie_off_button.setEnabled(true);
            Sounds.grajDzwiek("startedRecording.WAV");
            RepeatingMovement.nagrywanie();
        }
        if(e.getSource()==nagrywanie_off_button){
            RepeatingMovement.nagrywanie_trwa=false;
            nagrywanie_off_button.setEnabled(false);
            odtwarzanie_button.setEnabled(true);
            Sounds.grajDzwiek("stopedRecording.WAV");
        }
        if(e.getSource()==odtwarzanie_button){
            RepeatingMovement.odtwarzanie();
            nagrywanie_on_button.setEnabled(true);
            nagrywanie_off_button.setEnabled(false);
            Sounds.grajDzwiek("replayingMoves.WAV");
        }

    }


    public static void Złap(){
        if(prtmitywdetect.inCollision && !zlapany && CzyWPoblizu())
        {
            set_prymityw.set(new Vector3f(0.0f,0.0f,0.2f));
            trans_prymityw.setTransform(set_prymityw);
            rotScena.removeChild(bg_prymityw);
            trans_przegub3.addChild(bg_prymityw);
            RobotMovement.resetStopObr();
            zlapany=true;
        }
    }
    public static void Pusc(){

            prymityw.getLocalToVworld(set_prymityw);
            set_prymityw.get(przes_prymityw);
            trans_przegub3.removeChild(bg_prymityw);
            rotScena.addChild(bg_prymityw);
            zlapany=false;
            Spadanie();

    }
    public static boolean CzyWPoblizu(){
        chwytak.getLocalToVworld(set_chwytak);
        set_chwytak.get(przes_chwytak);
        float odleglosc = (float) Math.sqrt((Math.pow((przes_chwytak.x-przes_prymityw.x),2))+(Math.pow((przes_chwytak.y-przes_prymityw.y),2))+(Math.pow((przes_chwytak.x-przes_prymityw.x),2)));
        if (odleglosc<0.2f){
            return true;
        }
        else
            return false;
    }
    public static void Spadanie(){
        System.out.println(przes_prymityw);
        set_prymityw.set(przes_prymityw);
        trans_prymityw.setTransform(set_prymityw);
        Thread thread = new Thread(()-> {
            while (przes_prymityw.y > 0.12f) {
                przes_prymityw.y -= 0.01f;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                set_prymityw.set(przes_prymityw);
                trans_prymityw.setTransform(set_prymityw);
            }
        });
        thread.start();

    }



}
class Robot_Run {
    public static void main(String args[]){
        new ArticulatedArm();
    }
}
