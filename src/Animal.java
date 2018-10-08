import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import processing.core.PVector;

public class Animal {
	
	//other classes
	Random randnum = new Random(); // for calling random numbers within ranges
	
	//fields
	private int size; 
	public PVector pos, speed, acc, path;
	private double scale;
	private float angle; //calculate rotation
	
	public boolean hit; 
	
	//fields for drawing animal parts
	private Color animalColor; //animal color
	private Color cheekColor; //cheek color
	private int cheekSize; //size of cheek
	private int tentacleHeight; //tentacle height
	
	//constructor
    public Animal(int x, int y, int size, int speedx, int speedy, Color animalColor, Color cheekColor, int cheekSize, int tentacleHeight) {
    	this.pos = new PVector(x, y);
        this.speed = new PVector(speedx, speedy);
        this.size = size;
        
        this.animalColor = animalColor;
        this.cheekColor = cheekColor;
        this.cheekSize = cheekSize;
        this.tentacleHeight = tentacleHeight;
        this.scale = .7;
        
        angle = 0; //initial angle
        hit = false;
    }
    
	//draws a jellyfish
    public void draw(Graphics2D g) {
		
    	AffineTransform af = g.getTransform();
    	
		int d = size; //diameter (jellyfish fits into square)
        int ed = d/20; //calculate eye diameter as 1/20 of the face diameter
        
		g.translate((int)pos.x-pos.x/2, (int)pos.y-pos.y/2);
		g.rotate(Math.toRadians(75)); //jellyfish faces direction it's moving in
		g.rotate(angle); //turns jellyfish according to pathing
		g.scale(scale, scale); //scales jellyfish
        	
		//test collision point		
		g.setColor(Color.white);
        g.fillOval(0, 0, d/20, d/20);
		
        g.setColor(animalColor); //sets jellyfish color as randomized 
        
		//tentacles    
		g.setStroke(new BasicStroke(ed));
		g.drawLine(d/3, d/3, d/3, d - d/(tentacleHeight));
		g.drawLine(d/2, d/3, d/2, d - d/(tentacleHeight));
		g.drawLine(d/2 + d/6, d/3, d/2 + d/6, d - d/(tentacleHeight));
		g.drawLine(d/2 - d/3, d/3, d/2 - d/3, d - d/(tentacleHeight));
		g.drawLine(d/2 + d/3, d/3, d/2 + d/3, d - d/(tentacleHeight));
		
		//jellyfish body
		g.setStroke(new BasicStroke(3));
		g.fillOval(d/2 - d/3 - d/4 + d/10, d/3 - d/20, d/2, d/4);
		g.fillOval(d/2 - d/3, d/3 - d/20, d/2, d/4);
		g.fillOval(d/3, d/3 - d/20, d/2, d/4);
		g.fillOval(d - d/3 - d/6, d/3 - d/20, d/2, d/4);
		
		//draw a face
        g.fillOval(0, 0, d, d/2);

		//jellyfish cheeks
		g.setColor(cheekColor);
        g.fillOval(d/2 - d/3, d/3,  ed*cheekSize,  ed*cheekSize);
        g.fillOval(d/2 + d/3 -ed - ed/2, d/3,  ed*cheekSize,  ed*cheekSize);		
		        
        //draw 2 eyes
        g.setColor(Color.BLACK);
        g.fillOval(d/3, d/5, ed, ed);
        g.fillOval(2*d/3, d/5, ed, ed);
        
        //draw mouth
        g.drawArc(d/2-ed-ed/2, //x-coordinate of top left pivot
        		d/3, //y-coordinate of top left pivot
        		d/5, //horizontal size (width)
        		ed, //vertical size (height)
        		0, //start angle
        		-180); //end angle    
        
        g.setTransform(af);
	} 

    //moves thing when there is no food on screen
	public void move() {
		pos.add(speed);  //changed from adding speed to work with PVector
	}
    
    //moves thing towards food location
	public void moveToFood(Animal jellyfish, ArrayList<Food> food) {
		//gets information for all food in global array
		for (int i=0; i<food.size(); i++) {
			food.get(i);
		}
		
		path = PVector.sub(food.get(0).pos, jellyfish.pos); //points jellyfish towards food location
		
		acc = path.normalize(); //normalizes vector, reducing length to 1
		acc.mult((float)1); //keeps acceleration constant

		speed.add(acc); //adds acceleration to speed
		speed.limit(7); //maximum speed
		pos.add(speed); //adds speed to position
		
		angle = path.heading(); //changes direction of the jellyfish
		
		tentacleHeight = (int)(randnum.nextInt(10) + 5); //animates tentacles by making the height equal a random integer
		
    }	
	
	//checks whether or not the jellyfish has hit the screen boundary
    public void checkBoundaries(Animal jellyfish, Dimension panelSize) {
		if ((jellyfish.pos.x + jellyfish.size/2 * jellyfish.scale < 0) || (jellyfish.pos.x - (jellyfish.size * jellyfish.scale)  > panelSize.width*2)) {
			speed.x *= -1;
			angle = (float) (angle + Math.PI);
			System.out.println("hit wall at" + jellyfish.pos + "and width " + panelSize.width);
		}
		if ((jellyfish.pos.y + jellyfish.size/2 * jellyfish.scale) < 0 || (jellyfish.pos.y - (jellyfish.size * jellyfish.scale)  > panelSize.height*2)) { 
			speed.y *= -1;
			angle = (float) (angle + Math.PI);
			System.out.println("hit wall at" + jellyfish.pos + "and height " + panelSize.height);
		}
		
    }      
    
	public void hit(ArrayList<Food> food, Dimension panelSize) {
		//gets information for all food in global array
	    for (int i=0; i<food.size(); i++) {
			food.get(i);
		}
	    
	    //checks collision, makes hitCharacter true if hit
		if(Math.sqrt((pos.y - food.get(0).pos.y) * (pos.y - food.get(0).pos.y) + (pos.x - food.get(0).pos.x) * (pos.x - food.get(0).pos.x)) < size*scale/2 + food.get(0).size*food.get(0).scale/4) {
			hit = true;
			}
		}    
    
    //returns boolean == true if the jellyfish has gone over the boundaries
    boolean hitCharacter() {
        if (hit == true){
        	return true;
        }
        return false;
    }
}
