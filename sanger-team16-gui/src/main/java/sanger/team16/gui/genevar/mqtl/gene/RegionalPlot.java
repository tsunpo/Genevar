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
package sanger.team16.gui.genevar.mqtl.gene;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import sanger.team16.common.xqtl.QTL;
import sanger.team16.gui.jface.BaseJPane;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class RegionalPlot extends BaseJPane
{
    public RegionalPlot(String geneChromosome, int geneStart, int distance, double threshold, List<QTL> significances, List<QTL> insignificances) throws ArrayIndexOutOfBoundsException {
        XYDataset dataset = this.createDataset(significances, insignificances);
        JFreeChart chart = createChart(geneChromosome, geneStart, distance, threshold, dataset);
        chart.setBackgroundPaint(Color.white);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(680, 175));

        this.add(chartPanel);
    }
   
    private XYDataset createDataset(List<QTL> significances, List<QTL> insignificances) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        this.addToDataset(dataset, significances);
        this.addToDataset(dataset, insignificances);

        return dataset;
    }
    
    private void addToDataset(XYSeriesCollection dataset, List<QTL> qtls) {
        int size = qtls.size();
        for (int i=0 ; i<size ; i++) {
            QTL qtl = qtls.get(i);
            
            XYSeries series = new XYSeries(qtl.snp.getName());
            series.add(qtl.snp.getPosition(), qtl.getMinusLog10P());

            dataset.addSeries(series);
        }
    }
    
    private JFreeChart createChart(String geneChromosome, int geneStart, int distanceToTSS, double threshold, XYDataset dataset) {
        JFreeChart chart = ChartFactory.createScatterPlot(null,
            "Position on chromosome "+ geneChromosome + " (bp)", "-log10(P)", dataset, PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.lightGray);
        //plot.setRangeGridlinePaint(Color.lightGray);
        //plot.setRangeCrosshairVisible(true);
        
        //NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        //domainAxis.setRange(geneStart - distance, geneStart + distance);       
        //domainAxis.setUpperMargin(1000);
        //domainAxis.setLowerMargin(1000);
        
        //ValueAxis rangeAxis = plot.getRangeAxis();
        //rangeAxis.setUpperMargin(dataset.getYValue(0, 0)/5);
        //rangeAxis.setLowerBound(0);
        
        XYItemRenderer renderer = plot.getRenderer();  
        int size = dataset.getSeriesCount();
        for (int i=0 ; i<size ; i++) {
            //int scale = (int) Math.round((255 - (255 * dataset.getYValue(i, 0)) / top) / 1.4);
            //renderer.setSeriesPaint(i, new Color(255, scale, scale));

            renderer.setSeriesPaint(i, new Color(50, 205, 50));
            renderer.setSeriesShape(i, ShapeUtilities.createDiamond((float) 3));
            renderer.setBaseSeriesVisibleInLegend(false);
        }
        
        ValueMarker upperMarker = new ValueMarker(- Math.log10(threshold));
        upperMarker.setPaint(Color.gray);
        //upperMarker.setLabelOffsetType(LengthAdjustmentType.EXPAND);        
        //upperMarker.setLabel("-log10(10E-4)");
        //upperMarker.setLabelPaint(Color.red);
        //upperMarker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        //upperMarker.setLabelTextAnchor(TextAnchor.BOTTOM_RIGHT);
        float [] f={4,3,4,3};
        upperMarker.setStroke(new BasicStroke(1.0f,1,1,0,f,1.0f));  
        plot.addRangeMarker(upperMarker);
        
        ValueMarker marker = new ValueMarker(0.0);
        marker.setPaint(Color.lightGray);
        plot.addRangeMarker(marker);
        
        XYSeries series = new XYSeries("Range");
        series.add(geneStart - distanceToTSS, -0.05);
        series.add(geneStart + distanceToTSS, -0.05);
        ((XYSeriesCollection) dataset).addSeries(series);
        renderer.setSeriesVisible(dataset.getSeriesCount() - 1, false, false);
        
        return chart;
    }
}
