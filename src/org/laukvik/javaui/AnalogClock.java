/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.laukvik.javaui;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author morten
 */
public class AnalogClock extends JComponent{

    int timeInMillisecondsDifference;
    int radius = 100;
    Timer timer;
    Stroke hourStroke, minuteStroke, secondStroke, minuteMarker, minute5marker;
    Color hourColor, minuteColor, secondColor, minuteSingleColor, minuteFifthColor, dialColor;
    
    public AnalogClock( long timeInMilliseconds ){
        super();
        setOpaque( false );
        GregorianCalendar cal = new GregorianCalendar();
        this.timeInMillisecondsDifference = (int) (cal.getTimeInMillis()-timeInMilliseconds);
        
        hourStroke = new BasicStroke( 4 );
        minuteStroke = new BasicStroke( 3 );
        secondStroke = new BasicStroke( 1 );
        
        minuteMarker = new BasicStroke( 1 );
        minute5marker = new BasicStroke( 3 );
        
        hourColor = Color.BLACK;
        minuteColor = Color.BLACK;
        secondColor = Color.RED;
        minuteSingleColor = Color.BLACK;
        minuteFifthColor = Color.BLACK;
        
        dialColor = Color.WHITE;
        
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate( new ScheduledTask(), 0, 1000 );
        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        radius = (getWidth()/2)-10;
        
        radius = (getWidth() < getHeight()) ? ((getWidth()/2)-10) : ((getHeight()/2)-10);
    
        int width = getWidth();
        int height = getHeight();
        
        int ox = getWidth() / 2;
        int oy = getHeight() / 2;
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g2d.setBackground( getBackground() );
        g2d.clearRect(0,0,width,height);
        
        g2d.setColor( dialColor );
        g2d.fillOval( ox-radius,oy-radius, radius*2, radius*2 );

        
        GregorianCalendar cal = new GregorianCalendar();
        
        cal.add( GregorianCalendar.MILLISECOND, timeInMillisecondsDifference );
        int hour = cal.get( GregorianCalendar.HOUR );
        int minute = cal.get( GregorianCalendar.MINUTE );
        int seconds = cal.get( GregorianCalendar.SECOND );
        




        
        /* Draw minutes */
        for (int n=0; n<360; n+=6){
            boolean isFifth = (n % 5 == 0);
            if (isFifth){
                g2d.setColor( minuteFifthColor );
                g2d.setStroke( minute5marker );
                draw( n, radius-5, radius, g2d );
            } else {
                g2d.setColor( minuteSingleColor );
                g2d.setStroke( minuteMarker );
                draw( n, radius-3, radius, g2d );
            }
            
        }

        /* Draw hour */
        g2d.setColor( hourColor );
        g2d.setStroke( hourStroke );
        draw( (hour)*30, 0, radius-20, g2d );
        
        /* Draw minute */
        g2d.setColor( minuteColor );
        g2d.setStroke( minuteStroke );
        draw( (minute)*6, 0, radius-10, g2d );
        
        /* Draw seconds */
        g2d.setColor( secondColor );
        g2d.setStroke( secondStroke );
        draw( (seconds)*6, 0, radius-10, g2d );
        
    }
    
    public void draw( int degree, int pixelsFromOrigo, int size, Graphics2D g2d ){
        
        int ox = getWidth() / 2;
        int oy = getHeight() / 2;
        
        double fx = Math.cos( Math.toRadians( 90-degree )  )*size + ox;
        double fy = Math.cos( Math.toRadians( 90-degree+90 )  )*size + oy;

        double fx2 = Math.cos( Math.toRadians( 90-degree )  )*(size-(radius-pixelsFromOrigo)) + ox;
        double fy2 = Math.cos( Math.toRadians( 90-degree+90 )  )*(size-(radius-pixelsFromOrigo)) + oy;
        
        int x = (int) fx;
        int y = (int) fy;
        
        g2d.drawLine( (int)fx2, (int)fy2, x,y  );
            
    }
    
    private class ScheduledTask extends TimerTask {
        
            @Override
            public void run() {
                repaint();
            }
    }
 
    public static void main( String [] args ){
        JFrame frame = new JFrame("Analog Clock");
        frame.add( new AnalogClock( System.currentTimeMillis() ) );
        frame.setSize( 200, 200 );
        frame.setVisible( true );
    }
    
}