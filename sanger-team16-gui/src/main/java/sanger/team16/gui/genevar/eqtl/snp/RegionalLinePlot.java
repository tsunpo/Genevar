/**
 *  This file is part of Genevar (GENe Expression VARiation)
 *  Copyright (C) 2010  Genome Research Ltd.
 *
 *  Genevar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sanger.team16.gui.genevar.eqtl.snp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import sanger.team16.common.xqtl.QTL;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
public class RegionalLinePlot// extends BaseJPane
{
    private XYSeriesCollection dataset = new XYSeriesCollection();
    
    public RegionalLinePlot() {}
   
    public void addToDataset(String population, List<QTL> qtls) {
        XYSeries series = new XYSeries(population);
        
        for (int i=0 ; i<qtls.size() ; i++) {
            QTL qtl = qtls.get(i);
            series.add(qtl.transcriptMapping.getProbeStart(), qtl.getMinusLog10P());
        }                    // CHANGED FROM getTranscriptionStartSite() 13/10/10 

        dataset.addSeries(series);
    }
    
    public ChartPanel getChartPanel (String chromosome, int position, int distance, double threshold) throws ArrayIndexOutOfBoundsException {
        JFreeChart chart = createChart(chromosome, position, distance, threshold, dataset);
        chart.setBackgroundPaint(Color.white);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(680, 210));

        return chartPanel;
    }
    
    private JFreeChart createChart(String chromosome, int position, int distance, double threshold, XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(null,
            "Position on chromosome "+ chromosome + " (bp)", "-log10(P)", dataset, PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.lightGray);
        
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setShapesVisible(true);
        //renderer.setShapesFilled(false);   //CHANGED 12/12/11
        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
 /*       
        XYItemRenderer renderer = plot.getRenderer();  
        int size = dataset.getSeriesCount();
        for (int i=0 ; i<size ; i++) {
            //renderer.setSeriesPaint(i, new Color(255, 0, 0));
            renderer.setSeriesShape(i, ShapeUtilities.createDiamond((float) 3));
            renderer.setBaseSeriesVisibleInLegend(false);
        }
       */ 
        ValueMarker upperMarker = new ValueMarker(- Math.log10(threshold));
        upperMarker.setPaint(Color.gray);
        float [] f={4,3,4,3};
        upperMarker.setStroke(new BasicStroke(1.0f,1,1,0,f,1.0f));  
        plot.addRangeMarker(upperMarker);
        
        ValueMarker marker = new ValueMarker(0.0);
        marker.setPaint(Color.lightGray);
        plot.addRangeMarker(marker);
        
        XYSeries series = new XYSeries("Range");
        series.add(position - distance, -0.05);
        series.add(position + distance, -0.05);
        ((XYSeriesCollection) dataset).addSeries(series);
        renderer.setSeriesVisible(dataset.getSeriesCount() - 1, false, false);
        
        return chart;
    }
}
