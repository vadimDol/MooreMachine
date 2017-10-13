package main.java;

import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxPerimeter;
import com.mxgraph.view.mxStylesheet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class JGraphX {
    private mxGraph mGraph;
    private mxGraphComponent mGraphComponent;
    private Object mParent;
    private Map<Integer, Object> mVertex;

    public JGraphX() {
        mGraph = new mxGraph();
        mGraphComponent = new mxGraphComponent(mGraph);

        mParent = mGraph.getDefaultParent();
        mVertex = new LinkedHashMap<>();
    }

    public mxGraph getGraph() {
        return mGraph;
    }

    public mxGraphComponent getGraphComponent() {
        return mGraphComponent;
    }

    public void runVisualization(MooreMachines moore)  {

        mGraph.getModel().beginUpdate();
        try {

            initVertices(moore);
            initArc(moore);
            setStyle();
            setLayout();

        } finally {
            mGraph.getModel().endUpdate();
        }
    }

    private void setLayout() {
        new mxCircleLayout(mGraph).execute(mGraph.getDefaultParent());
        new mxParallelEdgeLayout(mGraph).execute(mGraph.getDefaultParent());
    }

    private void initVertices(MooreMachines moore) {
        for(int i = 0; i < moore.getOutputSignals().size(); ++i) {
            String id = "q" + moore.getStates().get(i) + "/" + "y" + moore.getOutputSignals().get(i);
            Object object =  mGraph.insertVertex(mParent, id, id, 0, 0, 80, 30);
            mVertex.put(moore.getStates().get(i), object);
        }
    }

    private  void initArc(MooreMachines moore) {
        for(int index = 0; index < moore.getStates().size(); index++){
            Object start = mVertex.get(moore.getStates().get(index));
            for(int i = 0; i < moore.getTable().size(); ++i) {
                ArrayList<Integer> value = moore.getTable().get(i);
                Object end = mVertex.get(value.get(index));
                String id = "x" + (i + 1);
                mGraph.insertEdge(mParent, id, id, start, end);
            }
        }
    }

    private void setStyle() {
        mxStylesheet stylesheet = mGraph.getStylesheet();
        Map<String, Object> vertexStyle = stylesheet.getDefaultVertexStyle();
        vertexStyle.put(mxConstants.STYLE_PERIMETER, mxPerimeter.RectanglePerimeter);
        vertexStyle.put(mxConstants.STYLE_GRADIENTCOLOR, "#ffff");
        vertexStyle.put(mxConstants.STYLE_PERIMETER_SPACING, 6);
        vertexStyle.put(mxConstants.STYLE_ROUNDED, true);
        vertexStyle.put(mxConstants.STYLE_SHADOW, true);
    }
}