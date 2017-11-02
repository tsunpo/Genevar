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
package sanger.team16.gui.jface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import sanger.team16.gui.genevar.UI;

/**
 * @author Tsun-Po Yang <tpy@sanger.ac.uk>
 * @link   http://www.sanger.ac.uk/Teams/Team16/
 */
@SuppressWarnings("serial")
public class BaseJFrame extends JFrame
{
    protected DefaultMutableTreeNode root;
    protected DefaultTreeModel treeModel;
    protected JTree tree;   //TODO protected
    protected JSplitPane splitPanel;
    protected JScrollPane treePanel, objectPanel;
    //private JDesktopPane dp;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    public BaseJFrame(String name) {
        super(name);
        
        setPreferredSize(new Dimension(1280, 800));
        //setVisible(true);   // !!!
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setLocationRelativeTo(true);
        
        //dp = new JDesktopPane();
        //dp.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
        //setContentPane(dp);
    }
    
    public void setLocationToCentre() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        
        this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
    }
    
    public void setBasicJTree(TreeSelectionListener listener) {
        this.root = new DefaultMutableTreeNode("ROOT");
        this.treeModel = new DefaultTreeModel(root);
        //treeModel.addTreeModelListener(treeModelListener);   //listener
        
        this.tree = new JTree(this.treeModel);
        this.tree.setEditable(true);
        this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.tree.setShowsRootHandles(true);
        this.tree.setRootVisible(false);
        this.tree.addTreeSelectionListener(listener);
        //tree.addMouseListener(this);
        
        this.treePanel = new JScrollPane(this.tree);   //tree panel
        this.objectPanel = new JScrollPane();          //object panel
        
        this.splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, objectPanel);
        this.splitPanel.setAutoscrolls(true);
        Dimension minimumSize = new Dimension(225, 800);
        this.treePanel.setMinimumSize(minimumSize);
        this.setContentPane(this.splitPanel);
    }
    
    public void setAutoResizeJSplitPanel() {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Dimension resize;
                if (getWidth() > 1024 && getWidth() != 1032)
                    resize = new Dimension(300, 800);                    
                else
                    resize = new Dimension(225, 800);

                if (treePanel != null)   // FIXED 22/06/10
                    treePanel.setMinimumSize(resize);
                if (splitPanel != null)
                    splitPanel.resetToPreferredSizes();
            }
            });
    }

    public void valueChanged(TreeSelectionEvent e) {
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            BaseTreeNodeInfo nodeInfo = (BaseTreeNodeInfo) node.getUserObject();
            BaseJPane object = (BaseJPane) nodeInfo.getObject();
            object.refresh();   // Flexible for each BaseJPane
            
            this.objectPanel.setViewportView(object);
            
        } catch (NullPointerException np) {
            return;
        }
    }

    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) currentNode.getParent();
            
            if (parentNode.getIndex(currentNode) == 0) {   // is first node?
                if (parentNode.getChildCount() == 1)       // is only node?
                    this.tree.setSelectionPath(new TreePath(currentNode.getPreviousNode().getPath()));
                else
                    this.tree.setSelectionPath(new TreePath(currentNode.getNextSibling().getPath()));
            } else
                 this.tree.setSelectionPath(new TreePath(currentNode.getPreviousSibling().getPath()));
            
            treeModel.removeNodeFromParent(currentNode);
            return; 
        } 

        // Either there was no selection, or the root was selected.
        toolkit.beep();
    }

    public DefaultMutableTreeNode moveToParentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) currentNode.getParent();
        
        this.tree.setSelectionPath(new TreePath(parent.getPath()));
        
        return parent;
    }
    
    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addToCurrentNode(String noteName, Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = this.tree.getSelectionPath();

        if (parentPath == null)
            parentNode = this.root;
        else
            parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();

        return addToNode(parentNode, noteName, child, true);
    }

    public DefaultMutableTreeNode addToNode(DefaultMutableTreeNode parent, String noteName, Object child, boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new BaseTreeNodeInfo(noteName, child));

        if (parent == null)
            parent = this.root;
    
        //It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        this.treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible)
            this.tree.scrollPathToVisible(new TreePath(childNode.getPath()));

        return childNode;
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    public ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = UI.class.getResource(path);
        
        if (imgURL != null)
            return new ImageIcon(imgURL);
        else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}

/*
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) currentNode.getParent();
            
            if (this.isLastNode(parentNode, currentNode)) {
                if (isOnlyNode(parentNode, currentNode))
                    this.tree.setSelectionPath(new TreePath(parentNode.getPath()));                    
                else {
                    this.tree.setSelectionPath(new TreePath(currentNode.getPreviousLeaf().getPath())); 
                }
            } else
                this.tree.setSelectionPath(new TreePath(currentNode.getNextLeaf().getPath()));
            
            treeModel.removeNodeFromParent(currentNode);
            return; 
        } 
 */