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
class Cluster { 
        int id; 
        int pixelCount; 
        int red; 
        int green; 
        int blue; 
        int reds; 
        int greens; 
        int blues; 
         
        public Cluster(int id, int rgb) { 
            int R = rgb>>16&0x000000FF;  
            int G = rgb>> 8&0x000000FF;  
            int B = rgb>> 0&0x000000FF;  
            red = R; 
            green = G; 
            blue = B; 
            this.id = id; 
            addPixel(rgb); 
        } 
         
        public void clear() { 
            red = 0; 
            green = 0; 
            blue = 0; 
            reds = 0; 
            greens = 0; 
            blues = 0; 
            pixelCount = 0; 
        } 
         
        int getId() { 
            return id; 
        } 
         
        int getRGB() { 
            int R = reds / pixelCount; 
            int G = greens / pixelCount; 
            int B = blues / pixelCount; 
            return 0xff000000|R<<16|G<<8|B; 
        } 
        void addPixel(int color) { 
            int R = color>>16&0x000000FF;  
            int G = color>> 8&0x000000FF;  
            int B = color>> 0&0x000000FF;  
            reds+=R; 
            greens+=G; 
            blues+=B; 
            pixelCount++; 
            red   = reds/pixelCount; 
            green = greens/pixelCount; 
            blue  = blues/pixelCount; 
        } 
         
        void removePixel(int color) { 
            int R = color>>16&0x000000FF;  
            int G = color>> 8&0x000000FF;  
            int B = color>> 0&0x000000FF;  
            reds-=R; 
            greens-=G; 
            blues-=B; 
            pixelCount--; 
            red   = reds/pixelCount; 
            green = greens/pixelCount; 
            blue  = blues/pixelCount; 
        } 
         
        int distance(int color) { 
            int R = color>>16&0x000000FF;  
            int G = color>> 8&0x000000FF;  
            int B = color>> 0&0x000000FF;  
            int rx = Math.abs(red-R); 
            int gx = Math.abs(green-G); 
            int bx = Math.abs(blue-B); 
            int dist = (rx+gx+bx) / 3; 
            return dist; 
        } 
    } 
