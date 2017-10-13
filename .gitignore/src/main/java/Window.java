package main.java;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window extends JGraphX {
    private JFrame mFrame;
    public Window() {
        create(getGraphComponent());
    }

    public void saveAsImage(String exportFileName) throws IOException {
        BufferedImage image = mxCellRenderer.createBufferedImage(getGraph(), null, 1, Color.WHITE, true, null);
        ImageIO.write(image, "PNG", new File(exportFileName));
    }

    public void create(mxGraphComponent mGraphComponent) {
        mFrame = new JFrame("Graph image");
        mFrame.getContentPane().add(mGraphComponent, BorderLayout.CENTER);
        mFrame.pack();
        mFrame.setSize(500,500);
        mFrame.setVisible(true);
    }

    public void drawMachine(MooreMachines machine) {
        runVisualization(machine);
    }

    public void close() {
        mFrame.dispose();
    }
}
