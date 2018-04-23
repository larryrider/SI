/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Rule;

/**
 *
 * @author fidel
 */
public class FuzzyController {

    //Controlador difuso 
    private FIS fis;
    
    //Salida del controlador difuso
    private float vel;
    private float rot;

    public FuzzyController() {
        fis = FIS.load(getClass().getResourceAsStream("controller.fcl"), true);

        if (fis == null) {
            System.err.println("Can't load file: 'controller.fcl'");
            return;
        }

        //Si se quieren mostrar los gráficos de conjuntos de las variables difusas
        //fis.chart();
    }

    //Calcula la intensidad de frenado para una velocidad y posicion dada
    void step(float[] sonar, double giro) {

        fis.setVariable("s0", sonar[0]);
        fis.setVariable("s1", sonar[1]);
        fis.setVariable("s2", sonar[2]);
        fis.setVariable("s3", sonar[3]);
        fis.setVariable("s4", sonar[4]);
        fis.setVariable("s5", sonar[5]);
        fis.setVariable("s6", sonar[6]);
        fis.setVariable("s7", sonar[7]);
        fis.setVariable("s8", sonar[8]);
        fis.setVariable("sig",giro);

        fis.evaluate();
        
        vel = (float) fis.getVariable("vel").defuzzify();
        rot = (float) fis.getVariable("rot").defuzzify();
        
        //System.out.println("Velocidad actual: " + vel + "\nGiro actual: " + rot);
        
        //Para mostrar las reglas evaluadas
        for( Rule r : fis.getFunctionBlock("aceleracion").getFuzzyRuleBlock("No1").getRules() )
            System.out.println(r);

    }
    
    public float getVel() { return vel; }
    public float getRot() { return rot; }
    
}
