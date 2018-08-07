package GetPointData;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class SelectPanel extends JPanel{

    
    //Draw the background again,继承自Jpanle,是Swing控件需要继承实现的方法,而不是AWT中的Paint()
    public void paintComponent(Graphics graphics)       //绘图类,详情可见博主的Java 下 java-Graphics 
    {  
        super.paintComponents(graphics);  
        
		int x = this.getWidth()/2;
		int y = this.getHeight()/2;
		int dis = this.getWidth()/3;
		graphics.setColor(Color.BLUE);
		paintNine( x,y, dis, graphics);
	
		graphics.setColor(Color.BLACK);
		paintNine( x - dis, y - dis, dis/3, graphics);
		paintNine( x , y - dis, dis/3, graphics);
		paintNine( x + dis , y - dis, dis/3, graphics);
		paintNine( x - dis, y, dis/3, graphics);
		paintNine( x , y, dis/3, graphics);
		paintNine( x + dis, y, dis/3, graphics);
		paintNine( x - dis, y + dis, dis/3, graphics);
		paintNine( x , y + dis, dis/3, graphics);
		paintNine( x + dis, y + dis, dis/3, graphics);
		
    } 
    
    public void paintNine( int mX,int mY,int distanceOfEachLayer,Graphics graphics) {		
		//内 竖线
		graphics.drawLine(mX - distanceOfEachLayer*3/2, mY - distanceOfEachLayer/2, mX + distanceOfEachLayer*3/2, mY - distanceOfEachLayer/2);
		graphics.drawLine(mX - distanceOfEachLayer*3/2, mY + distanceOfEachLayer/2, mX + distanceOfEachLayer*3/2, mY + distanceOfEachLayer/2);
		graphics.drawLine(mX - distanceOfEachLayer/2, mY - distanceOfEachLayer*3/2, mX - distanceOfEachLayer/2, mY + distanceOfEachLayer*3/2);
		graphics.drawLine(mX + distanceOfEachLayer/2, mY - distanceOfEachLayer*3/2, mX + distanceOfEachLayer/2, mY + distanceOfEachLayer*3/2);
	}
    
}
