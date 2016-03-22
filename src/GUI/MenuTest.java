package GUI;

import javax.swing.JFrame;

/**
 * An application for clustering and visualizing microarray gene expression data
 * @author Ivo Rakar
 * Date: July 2, 2015
 * */

public class MenuTest
{
   public static void main( String[] args )
   { 
      ClusteringFrame menuFrame = new ClusteringFrame(); // create MenuFrame 
      menuFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      menuFrame.setSize( 1000, 650 ); // set frame size
      menuFrame.setVisible( true ); // display frame
   } // end main
} // end class MenuTest


