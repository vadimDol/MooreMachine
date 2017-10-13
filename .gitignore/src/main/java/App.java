package main.java;

import com.mxgraph.util.mxCellRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
public class App{

    public static void main(String[] args) {
        try {
            MooreMachines moore = new MooreMachines();
            moore.readTableFromFile("input/input.txt");
            Minimization minimization = new Minimization(moore);
            minimization.run();
            MooreMachines resultMinimization = minimization.getResultMinimize();

            Window window1 = new Window();
            window1.drawMachine(moore);
            window1.saveAsImage("graph1.png");

            Window window2 = new Window();
            window2.drawMachine(resultMinimization);
            window2.saveAsImage("graph2.png");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}