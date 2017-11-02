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
package sanger.team16.gui.genevar.eqtl.query;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.util.ShapeUtilities;

import sanger.team16.common.business.dao.Tuple;
import sanger.team16.gui.jface.BaseJPane;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class SNPGeneAssocPlot extends BaseJPane
{
    public SNPGeneAssocPlot(List<Tuple> tuples, double max, double min) throws ArrayIndexOutOfBoundsException {
        JPanel mainPanel = new JPanel(new GridLayout(0, 3));
        mainPanel.setBackground(Color.white);
        
        int size = tuples.size();
        for (int i=0 ; i<size ; i++) {
            Tuple tuple = tuples.get(i);
            String populationName = tuple.populationName;

            // if (trait.expressionRanks != null) {   // BUG FIXED 02/02/10
            if (tuple.phenotypes != null && tuple.phenotypes.length != 0) {
                CategoryDataset dataset = this.createDataset(tuple);
                JFreeChart chart = createChart(populationName, dataset, max, min);
                chart.setBackgroundPaint(Color.white);
                
                TextTitle textTitle = new TextTitle(tuple.subtitle);
                textTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
                //subtitle1.setPosition(RectangleEdge.BOTTOM);
                textTitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                chart.addSubtitle(textTitle);

                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(this.getAutoPreferredSize());   // Original Dimension(680, 420)
                mainPanel.add(chartPanel);
                
            } else {
                BaseJPane empty = new BaseJPane(2,50,0,0);
                empty.setPreferredSize(this.getAutoPreferredSize());
                BaseJPane na = new BaseJPane();
                na.setLayout(new BorderLayout());
                
                JLabel name = new JLabel("      " + populationName);
                name.setFont(new Font("Arial", Font.BOLD, 12));
                name.setForeground(Color.gray);
                na.add(name, BorderLayout.PAGE_START);
                
                JLabel message = new JLabel("Not Available");
                message.setForeground(Color.lightGray);
                na.add(message, BorderLayout.CENTER);

                empty.add(Box.createHorizontalGlue());
                empty.add(na);
                empty.add(Box.createHorizontalGlue());
                empty.setBaseSpringBox();
 
                mainPanel.add(empty);
            }
        }

        this.add(mainPanel);
    }
    
    private Dimension getAutoPreferredSize() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); 
        
        if (dim.width > 1024)
            //return new Dimension(680/7*3, 420/7*3); // 3/7 Dimension(680, 420) for 1024 widescreen
            return new Dimension(680/5*2, 420/5*2);   // 1024 wide
        
        //return new Dimension(250, 166);   // Original Dimension(680, 420)
        return new Dimension(250, 420*250/680);
    }
   
    /**
     *  categorydataset.addValue(1, "NA1", "CC");
     */
    private CategoryDataset createDataset(Tuple tuple) {
        DefaultCategoryDataset categorydataset = new DefaultCategoryDataset();

        String categoryLabel = "Category Label";
        categorydataset.addValue(0, categoryLabel, tuple.a1 + tuple.a1);
        categorydataset.addValue(0, categoryLabel, tuple.a1 + tuple.a2);
        categorydataset.addValue(0, categoryLabel, tuple.a2 + tuple.a2);
        categorydataset.removeRow(categoryLabel);
        
        for (int i=0; i<tuple.individuals.length; i++)
            categorydataset.addValue(tuple.phenotypes[i], tuple.individuals[i], tuple.genotypes[i]);

        return categorydataset;
    }
    
    private JFreeChart createChart(String populationName, CategoryDataset categoryDataset, double max, double min) {
        CategoryAxis categoryaxis = new CategoryAxis();
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        //categoryaxis.setMaximumCategoryLabelWidthRatio(5F);
        //categoryaxis.setMaximumCategoryLabelLines(141);
        //categoryaxis.setCategoryMargin(450);
        
        LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
        lineandshaperenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        lineandshaperenderer.setBaseShapesFilled(false);
        lineandshaperenderer.setBaseShape(ShapeUtilities.createDiamond((float) 3));
        lineandshaperenderer.setBaseSeriesVisibleInLegend(false);
        //lineandshaperenderer.setBaseLinesVisible(false);
        lineandshaperenderer.setAutoPopulateSeriesShape(false);
        lineandshaperenderer.setAutoPopulateSeriesPaint(false);
        //lineandshaperenderer.findRangeBounds(categoryDataset);
        
        NumberAxis numberaxis = new NumberAxis("Expression");
        numberaxis.setAutoRangeIncludesZero(false);
        //numberaxis.setRangeWithMargins(min, max);
        
        CategoryPlot categoryplot = new CategoryPlot(categoryDataset, categoryaxis, numberaxis, lineandshaperenderer);
        categoryplot.setDomainGridlinesVisible(false);
        categoryplot.setOrientation(PlotOrientation.VERTICAL);
        
        JFreeChart jfreechart = new JFreeChart(populationName, new Font("SansSerif", 1, 14), categoryplot, true);
        return jfreechart;
    }
}
