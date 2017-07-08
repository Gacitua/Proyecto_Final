/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TSP_GA;
import static TSP_GA.FileTSP.OPT;
import static TSP_GA.FileTSP.dimension;
import java.util.Arrays;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Matías
 */


public class GA {
    static int[][] Convergencia;
    static int tmax;
    static int Mejor_Cromo;
    static int Indice;
    static int[][] Poblacion;
    static int[] SO;
    static int VO;
 
    @SuppressWarnings("empty-statement")
    public static void main(String args) {  
    FileTSP.main(args);


int psize=Integer.parseInt(SetupGA.psize.getText());    //Tamaño de la población
double pcross=Double.parseDouble(SetupGA.pcross.getText());   //Probabilidad de cruzamiento
double pmut=Double.parseDouble(SetupGA.pmut.getText());     //Probabilidad de mutación
int tmax=Integer.parseInt(SetupGA.tmax.getText());
int k=Integer.parseInt(SetupGA.k.getText());        // Tamaño del torneo
Convergencia =new int[tmax][2];
    /***************************
     *Inicializar Población
     * ALGORÍTMO DE FISHER-YATES
     ***************************/
int[] FitnessP;
Poblacion = new int[psize][dimension];
FitnessP=new int[psize];
for (int i=0;i<psize;i++){
    Poblacion[i]=FileTSP.permutar(0);           //Genera la población inicial
    FitnessP[i]=FileTSP.fitness(Poblacion[i]);  //Cálcula el fitness del cromosoma
}
    /***************************
     *Selección
     *SELECCIÓN POR DE PADRES POR TORNEO
     ***************************/
int cromosoma;
int[][] Participantes_Torneo;
int[] Mejor_F_Torneo;
int[][] Padres;


Participantes_Torneo= new int[k][2];
Mejor_F_Torneo= new int[2];
Padres= new int[2][dimension];

for(int t=0;t<tmax;t++){
for (int j=0;j<2;j++){                                    //Doble torneo, para elegir a los dos padres
    cromosoma=(int) (Math.random()*psize-1);
    Participantes_Torneo[0][0]=FitnessP[cromosoma];
    Participantes_Torneo[0][1]=cromosoma;
    Mejor_F_Torneo[0]=FitnessP[cromosoma];
    Mejor_F_Torneo[1]=cromosoma;
    for(int i=1;i<k;i++){                                 //El ciclo se repite hasta el número máximo de participantes
        cromosoma=(int) (Math.random()*psize-1);          //Elige un cromosoma al azar del total de la población
        //System.out.println(cromosoma);
        Participantes_Torneo[i][0]=FitnessP[cromosoma];   //Se guarda el Fitness del participante
        //System.out.println(FitnessP[cromosoma]);
        Participantes_Torneo[i][1]=cromosoma;             //Se guarda la posición del cromosoma en la población
        if(Mejor_F_Torneo[0]>Participantes_Torneo[i][0]){ //Selecciona al mejor de los 2 contrincantes
            Mejor_F_Torneo[0]=Participantes_Torneo[i][0]; //Guarda el Fitness del mejor
            Mejor_F_Torneo[1]=Participantes_Torneo[i][1]; //Guarda la posición que tiene en la población el mejor cromosoma del torneo
        }  
    }

    Padres[j]=Poblacion[Mejor_F_Torneo[1]];    //Almacena el mejor cromosoma del torneo
}  
    /***************************
     *PMX
     *CROSSOVER
     ***************************/
    int[] intervalo= new int[2];
    intervalo=FileTSP.interval(dimension);
    int[] Papa=Padres[0];
    int[] Mama=Padres[1];
    int[] Hijo =new int[Papa.length];
    int[] Hija =new int[Mama.length];
    if(Math.random()<pcross){
        Hijo=FileTSP.PMX2(Papa, Mama, intervalo[0], intervalo[1])[0];
        Hija=FileTSP.PMX2(Papa, Mama, intervalo[0], intervalo[1])[1];
    }
    else{
        Hijo=Papa;
        Hija=Mama;
    }
    /***************************
     *2-OPT
     * MUTACIÓN
     ***************************/
    intervalo=FileTSP.interval(dimension);
    if (intervalo[0]==0){
        intervalo[0]=1;
    }
    if(Math.random()<pmut){
        OPT(Hijo,intervalo[0],intervalo[1]);
        OPT(Hija,intervalo[0],intervalo[1]);
    }
    /***************************
     *Selección del sobreviviente
     * 
     ***************************/
    int Peor_Cromo = FitnessP[0];
    Indice = 0;
    for(int i=1; i<psize;i++){
        if (FitnessP[i]>Peor_Cromo){
            Peor_Cromo = FitnessP[i];
            Indice = i;
        } 
    }
    Poblacion[Indice]=Hijo;
    FitnessP[Indice]=FileTSP.fitness(Hijo);
    Peor_Cromo = FitnessP[0];
    Indice = 0;
    for(int i=1; i<psize;i++){
        if (FitnessP[i]>Peor_Cromo){
            Peor_Cromo = FitnessP[i];
            Indice = i;
        } 
    }
    Poblacion[Indice]=Hija;
    FitnessP[Indice]=FileTSP.fitness(Hija);
    /***************************
     *Encontrar el mejor de la poblah'
     * 
     ***************************/
    Mejor_Cromo = FitnessP[0];
    Indice = 0;
    for(int i=1; i<psize;i++){
        if (FitnessP[i]<Mejor_Cromo){
            Mejor_Cromo = FitnessP[i];
            Indice = i;
        } 
    }
    Convergencia[t][0]=t;
    Convergencia[t][1]=Mejor_Cromo;
    
    Interfaz.jtaCosto.setText("Iteración número: " + t);

    }//Final tmax

    SO=Poblacion[Indice];        //Solución de la mejor ruta encontrada por la heurística
    VO=Mejor_Cromo;              //Valor (costo) de la mejor ruta encontrada por la heurística
    }//Final para main
    public static void main2(String[] args){
                
                XYSeries Datos = new XYSeries("Fitness");
                Datos.add(Convergencia[0][0],Convergencia[0][1]);
                for(int i=1;i<Integer.parseInt(SetupGA.tmax.getText());i++){
                    Datos.add(Convergencia[i][0],Convergencia[i][1]);
                }
                XYSeriesCollection dataset = new XYSeriesCollection();
                dataset.addSeries(Datos);
                JFreeChart xylineChart = ChartFactory.createXYLineChart(
                                "Evolución del mejor cromosoma",
                                "Iteración",
                                "Fitness",
                                dataset,
                                PlotOrientation.VERTICAL, true, true, false);
                XYPlot plot = xylineChart.getXYPlot();
                ChartPanel panel = new ChartPanel(xylineChart);
 
                //Crea la ventana
                JFrame ventana = new JFrame("Grafica");
                ventana.setVisible(true);
                ventana.setSize(800, 600);
                ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventana.add(panel);
    }
}
