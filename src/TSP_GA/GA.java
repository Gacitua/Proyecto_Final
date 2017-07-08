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
//import TSP_GA.Interfaz;

/**
 *
 * @author Matías
 */


public class GA {
    //static String path = "C:\\Users\\Matías\\Documents\\NetBeansProjects\\Tarea2\\bays29.tsp";
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

/*int psize=10000;    //Tamaño de la población
double pcross=0.6;   //Probabilidad de cruzamiento
double pmut=0.001;     //Probabilidad de mutación
int tmax=100000;
int k=3;        // Tamaño del torneo
*/

//int[][] Convergencia; //
Convergencia =new int[tmax][2];
 /* GA()
    initialize population
    find fitness of population
        while (termination criteria is reached) do
        parent selection
        crossover with probability pc
        mutation with probability pm
        decode and fitness calculation
        survivor selection
        find best
    return best 
    */
 
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
    //System.out.println(Arrays.toString(Poblacion[i]));
    //System.out.println(FitnessP[i]);
}


//for(int i=0;i<psize;i++){
//    System.out.println(Arrays.toString(Poblacion[i]));
//}



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
    



//int[][] Participantes_Torneo;
//Participantes_Torneo= new int[k][23];

//Participantes_Torneo[0]=Poblacion[cromosoma];
//Mejor_Torneo=Participantes_Torneo[0];
//System.out.println(Arrays.toString(Participantes_Torneo[0]));



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
    //System.out.println("Papá: " + Arrays.toString(Papa));
    //System.out.println("Mamá: " + Arrays.toString(Mama));
    if(Math.random()<pcross){
        Hijo=FileTSP.PMX2(Papa, Mama, intervalo[0], intervalo[1])[0];
        Hija=FileTSP.PMX2(Papa, Mama, intervalo[0], intervalo[1])[1];
        //System.out.println("Hijo: " + Arrays.toString(Hijo));
        //System.out.println("Hija: " + Arrays.toString(Hija));
    }
    else{
        Hijo=Papa;
        Hija=Mama;
    }
    //System.out.println("Hijo: " + Arrays.toString(Hijo));
    //System.out.println("Hija: " + Arrays.toString(Hija));
    /***************************
     *2-OPT
     * MUTACIÓN
     ***************************/
    intervalo=FileTSP.interval(dimension);
    if (intervalo[0]==0){
        intervalo[0]=1;
    }
    //System.out.println(Arrays.toString(intervalo));
    if(Math.random()<pmut){
        OPT(Hijo,intervalo[0],intervalo[1]);
        OPT(Hija,intervalo[0],intervalo[1]);
    }
    //System.out.println("Hijo: " + Arrays.toString(Hijo));
    //System.out.println("Fitness Hijo: " +FileTSP.fitness(Hijo));
    //System.out.println("Hija: " + Arrays.toString(Hija));
    //System.out.println("Fitness Hija: " +FileTSP.fitness(Hija));
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
    //System.out.println("Peor Cromosoma: " + Peor_Cromo);
    //System.out.println("Fitness de la hijo: " + FitnessP[Indice]);
    
    Peor_Cromo = FitnessP[0];
    Indice = 0;
    for(int i=1; i<psize;i++){
        if (FitnessP[i]>Peor_Cromo){
            Peor_Cromo = FitnessP[i];
            Indice = i;
        } 
    }
    //System.out.println("Peor Cromosoma: " + Peor_Cromo);
    Poblacion[Indice]=Hija;
    FitnessP[Indice]=FileTSP.fitness(Hija);
    //System.out.println("Fitness de la hija: " + FitnessP[Indice]);
    
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
    /*if (t==0){
        System.out.println(FitnessP[Indice]);
    }
    if (t==500){
        System.out.println(FitnessP[Indice]);
    }
    if (t==tmax-1){
        System.out.println(FitnessP[Indice]);
    }
*/
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
 
                // Creamos la ventana
                JFrame ventana = new JFrame("Grafica");
                ventana.setVisible(true);
                ventana.setSize(800, 600);
                ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventana.add(panel);
    }
}
