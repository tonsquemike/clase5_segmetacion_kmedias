/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clase4_kmeans;

/**
 *
 * @author miguel
 */
import java.awt.image.BufferedImage; 
import java.io.File; 
import java.util.Arrays; 
import javax.imageio.ImageIO; 
public class KMeans { 
    BufferedImage original; 
    BufferedImage segmentation; 
    Cluster[] clusters; 
    public static final int CONTINUOUS = 1; 
    public static final int ITERATIVE = 2; 
     
     
    public static void main(String[] args) { 
        if (args.length!=4) { 
            System.out.println("Uso: " 
                        + " imagen de entrada" 
                        + " imagen destino" 
                        + " n_grupos 1>255" 
                        + " [modo -i (ITERATIVO)|-c (CONTINUO)]"); 
            return; 
        } 
        // parse arguments 
        String in = args[0]; 
        String out = args[1]; 
        int k = Integer.parseInt(args[2]); 
        String m = args[3]; 
        int mode = 1; 
        if (m.equals("-i")) { 
            mode = ITERATIVE; 
        } else if (m.equals("-c")) { 
            mode = CONTINUOUS; 
        } 
         
        // crear nuevo objeto KMeans
        KMeans kmeans = new KMeans(); 
        // comienza la creación de los clusters 
        BufferedImage dstImage = kmeans.calculate(loadImage(in), 
                                                    k,mode); 
         
        saveImage(out, dstImage); 
    } 
     
    public KMeans() {    } 
     
    public BufferedImage calculate(BufferedImage image,  
                                            int k, int mode) { 
        long start = System.currentTimeMillis(); 
        int w = image.getWidth(); 
        int h = image.getHeight(); 
        // crear clusters
        clusters = createClusters(image,k); 
        // creación de tabla de clusters 
        int[] lut = new int[w*h]; 
        Arrays.fill(lut, -1); 
         
         
        boolean cambioPixelDeCluster = true; 
        // repetir mientras los grupos cambien 
        int loops = 0; 
        while (cambioPixelDeCluster) { 
            cambioPixelDeCluster = false; 
            loops++; 
            for (int y=0;y<h;y++) { 
                for (int x=0;x<w;x++) { 
                    int pixel = image.getRGB(x, y); 
                    Cluster cluster = findMinimalCluster(pixel); 
                    if (lut[w*y+x]!=cluster.getId()) { 
                        // cambio de cluster 
                        if (mode==CONTINUOUS) { 
                            if (lut[w*y+x]!=-1) { 
                                // Eliminar el pixel del cluster donde se encontraba  
                                clusters[lut[w*y+x]].removePixel( 
                                                            pixel); 
                            } 
                            // agregar pixel a un cluster 
                            cluster.addPixel(pixel); 
                        } 
                        // repite el proceso  
                        cambioPixelDeCluster = true; 
                     
                        // actualiza tabla de clusters
                        lut[w*y+x] = cluster.getId(); 
                    } 
                } 
            } 
            if (mode==ITERATIVE) { 
                // actualiza clusters 
                for (int i=0;i<clusters.length;i++) { 
                    clusters[i].clear(); 
                } 
                for (int y=0;y<h;y++) { 
                    for (int x=0;x<w;x++) { 
                        int clusterId = lut[w*y+x]; 
                        // agrega el pixel actul (x,y) al cluster (clusterId) 
                        clusters[clusterId].addPixel( 
                                            image.getRGB(x, y)); 
                    } 
                } 
            } 
             
        } 
        // crear imagen segmentada 
        BufferedImage result = new BufferedImage(w, h,  
                                    BufferedImage.TYPE_INT_RGB); 
        for (int y=0;y<h;y++) { 
            for (int x=0;x<w;x++) { 
                int clusterId = lut[w*y+x]; 
                result.setRGB(x, y, clusters[clusterId].getRGB()); 
            } 
        } 
        long end = System.currentTimeMillis(); 
        System.out.println("grupos generados "+k 
                            + " en "+loops+" iteraciones " 
                            +" "+(end-start)+" ms."); 
        return result; 
    } 
     
    public Cluster[] createClusters(BufferedImage bi, int k) { 
        //revisar para asgnar clusters aleatoriamente 
        Cluster[] result = new Cluster[k]; 
        int x = 0; int y = 0; 
        int dx = bi.getWidth()/k; 
        int dy = bi.getHeight()/k; 
        for (int i=0;i<k;i++) { 
            result[i] = new Cluster(i,bi.getRGB(x, y)); 
            x+=dx; y+=dy; 
        } 
        return result; 
    } 
     
    public Cluster findMinimalCluster(int rgb) { 
        Cluster cluster = null; 
        int min = Integer.MAX_VALUE; 
        for (int i=0;i<clusters.length;i++) { 
            int distance = clusters[i].distance(rgb); 
            if (distance<min) { 
                min = distance; 
                cluster = clusters[i]; 
            } 
        } 
        return cluster; 
    } 
     
    public static void saveImage(String name,  
            BufferedImage bi) { 
        File file = new File(name); 
        try { 
            ImageIO.write(bi, "png", file); 
        } catch (Exception e) {} 
    } 
     
    public static BufferedImage loadImage(String name) { 
        BufferedImage bo = null; 
        try { 
            bo = ImageIO.read(new File(name)); 
        } catch (Exception e) { 
            System.out.println(e.toString()+" Imagen '" 
                                +name+"' no encontrada."); 
        } 
        return bo; 
    } 
     
    
     
} 
