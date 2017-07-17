/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TSP_GA;

import java.util.Arrays;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import static TSP_GA.FileTSP.permutar;
import java.util.List;


    /***************************
     *Lectura del archivo TSP
     * 
     ***************************/
public class FileTSP {

    static int dimension;

    
    private String name;
    private String type;
    private String comment;
    
    private String edgeWeightType;
    private String edgeWeightFormat;
    private String displayDataType;
    static int[][] edgWeightSection;

    /**
     * Constructor para guardar la informacion
     * @param args
     */
    //public FileTSP(File archivo){
    public static void main(String args) {
    String[] archivo=new String[1000];
    String[] file2=new String[1000];
    int i=0;
    try {
        File tsp = new File(args);  //"C:\\Users\\Matías\\Documents\\NetBeansProjects\\Tarea2\\bays29.tsp"
        BufferedReader br = new BufferedReader(new FileReader(tsp));
        String line = "";
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line,"");

        while (st.hasMoreTokens()) {
            String str=st.nextToken();
            archivo[i]=String.valueOf(str);
            i=i+1;
        }
      }
      
      br.close();

    } catch (FileNotFoundException e) {
    } catch (IOException e) {
    }
    String[] parts;
        parts = archivo[3].split(" ");
        dimension = Integer.parseInt(parts[1]);
        String[][] aux = new String[dimension][];
        edgWeightSection = new int[dimension][dimension];
    
    for(int j=8; j<8+dimension;j++){
        aux[j-8]=archivo[j].split(" ");
    }
    int x=0;
    int y=0;
        
    for(int m=0; m<dimension;m++){
        for(int n=0; n<aux[m].length; n++){
            if (!"".equals(aux[m][n]) && aux[m][n]!=null) {
                edgWeightSection[x][y]=Integer.parseInt(aux[m][n]);
                y=y+1;
            }
        }
        x=x+1;
        y=0;
    }
    String[][] aux2 = new String[dimension][];
    for(int j=9+dimension; j<9+2*dimension;j++){
        aux2[j-(9+dimension)]=archivo[j].split(" ");
    }
    x=0;
    y=0;
    double[][] displayDataSelection;
    displayDataSelection = new double[dimension][3];
    
    for(int m=0; m<dimension;m++){
        for(int n=0; n<aux2[m].length; n++){
            if (!"".equals(aux2[m][n]) && aux2[m][n]!=null) {
                displayDataSelection[x][y]=Double.parseDouble(aux2[m][n]);
                y=y+1;
            }
        }
        x=x+1;
        y=0;
    }
  } //Final de main
    /***************************
     *Algorítmo de Fisher-Yates (Población Inicial)
     * @return 
     ***************************/
    public static int[] permutar(){
    int[] a= new int[dimension];  
    for (int s=1; s<=dimension;s++){
        a[s-1]=s;
    }
    int az;
    int tmp;

    int tamaño=a.length;
    for (int k=tamaño-1;k>=1;k--){      
        az= (int) (Math.random()*k);
        tmp=a[az];
        a[az]=a[k];
        a[k]=tmp;
    }
    int [] per= new int[dimension];
    per=a;
    return per;
    }

    
    /***************************
     * Calcula el Fitness
     * Equivalente al costo del trayecto
     ***************************/
public static int fitness(int[] a){
    int Costo=0;            //Almacena el costo total de viajar
    int City1;              //Posición de la ciudad de inicio
    int City2;              //Posición de la ciudad de destinto
    for (int s=0;s<dimension;s++){
        City1=a[s];
        if((s+1)<dimension){   //
            City2=a[s+1];}
        else{City2=a[0];}
        Costo=edgWeightSection[City1-1][City2-1]+Costo;
    }
    return Costo;
}


    /***************************
     *2-OPT (Mutación)
     * @param ruta
     * @param i
     * @param k
     * @return
     ***************************/


    public static int[]OPT (int[]Ruta, int i, int k){  //i punto de inicio y k punto de termino de la mutación
        int[] Mutado;
        int[] Reverse;
        Mutado = new int[Ruta.length];
        Reverse= new int[k+1-i];
        System.arraycopy(Ruta, 0, Mutado, 0, i-1);
        for (int c = k-1, j=0; c>=i-1;c--,j++){
            Reverse[j]=Ruta[c];
        }
        System.arraycopy(Reverse, 0, Mutado, i-1, (k+1-i));
        System.arraycopy(Ruta, k, Mutado, k, Ruta.length-k);
        return Mutado;
    }/*



    
    /***************************
     *PMX(Crossover)
     * 
     ***************************/
    /*
     *
     * @param Padre
     * @param Madre
     * @param inicio
     * @param fin
     * @return
     */
    public static int[][] PMX(int[] Padre, int[] Madre, int inicio, int fin) {
        int [] Hijo;
        int [] Hija;
        int [][] Decendientes;
        boolean Contenido; //Variable que indica si un número i está en determinado arreglo
        int pos;
        pos=0;
        
        Hijo= new int[Padre.length];
        Hija= new int[Madre.length];
        Decendientes= new int[2][Padre.length];

        System.arraycopy(Padre, inicio, Hija, inicio, fin-inicio);
        System.arraycopy(Madre, inicio, Hijo, inicio, fin-inicio);
        
        ///Genera Hijo///
        ///Cadena Inicial///
        for (int i=0;i<inicio ;i++){
            Contenido=false;
            for(int j =0;j<Hijo.length;j++){
                if (Padre[i]==Hijo[j]){
                    Contenido=true;
                    pos=j;
                    for(int r=inicio;r<fin;r++){          //Vuelve a buscar si es que se repite algún alelo en la hija
                        for (int k=inicio;k<fin;k++){
                            if (Hija[pos]==Hijo[k]){     //Condición si se repite algún alelo
                                pos=k;                   //Devuelve la posición en la que se encontró la repetición
                            }
                        }    
                    }
                }
            }
            
            if (Contenido==false) {
                Hijo[i]=Padre[i];
            }
            else{
                Hijo[i]=Hija[pos];
            }
        }
        
        ///Cadena final///
        for (int i=fin;i<Hijo.length ;i++){
            Contenido=false;
            for(int j =0;j<Hijo.length;j++){
                if (Padre[i]==Hijo[j]){
                    Contenido=true;
                    pos=j;
                    for(int r=inicio;r<fin;r++){          //Vuelve a buscar si es que se repite algún alelo en la hija
                        for (int k=inicio;k<fin;k++){
                            if (Hija[pos]==Hijo[k]){     //Condición si se repite algún alelo
                                pos=k;                   //Devuelve la posición en la que se encontró la repetición
                            }
                        }    
                    }
                }
            }
            
            if (Contenido==false) {
                Hijo[i]=Padre[i];
            }
            else{
                Hijo[i]=Hija[pos];
            }
        }
        
        ///Genera Hija//
        ///Cadena Inicial///
        for (int i=0;i<inicio;i++){
            Contenido=false;
            for(int j =0;j<Hija.length;j++){
                if (Madre[i]==Hija[j]){
                    Contenido=true;
                    pos=j;
                    for(int r=inicio;r<fin;r++){          //Vuelve a buscar si es que se repite algún alelo en la hija
                        for (int k=inicio;k<fin;k++){
                            if (Hijo[pos]==Hija[k]){     //Condición si se repite algún alelo
                                pos=k;                   //Devuelve la posición en la que se encontró la repetición
                            }
                        }    
                    }
                }
            }
            
            if (Contenido==false) {
                Hija[i]=Madre[i];
            }
            else{
                Hija[i]=Hijo[pos];
            }
        }
        ///Cadena Final///
        for (int i=fin;i<Hija.length;i++){
            Contenido=false;
            for(int j =0;j<Hija.length;j++){
                if (Madre[i]==Hija[j]){
                    Contenido=true;
                    pos=j;
                    for(int r=inicio;r<fin;r++){          //Vuelve a buscar si es que se repite algún alelo en la hija
                        for (int k=inicio;k<fin;k++){
                            if (Hijo[pos]==Hija[k]){     //Condición si se repite algún alelo
                                pos=k;                   //Devuelve la posición en la que se encontró la repetición
                            }
                        }    
                    }
                }
            }
            
            if (Contenido==false) {
                Hija[i]=Madre[i];
            }
            else{
                Hija[i]=Hijo[pos];
            }
        }
        
        
        Decendientes[0]=Hijo;
        Decendientes[1]=Hija;
        return Decendientes;
    }
    public static int[] interval(int long_array){
        int inicio;
        int fin;
        int [] rango;
        rango= new int[2];
        
        inicio=(int) Math.floor(Math.random()*long_array);
        fin=(int)Math.floor(Math.random()*(inicio-(long_array))+(long_array));
        rango[0]=inicio;
        rango[1]=fin;
        
        return rango;
    }

    }//Final de la clase



