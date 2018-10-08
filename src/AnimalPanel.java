import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.JPanel;
import javax.swing.Timer;

import processing.core.PVector;

public class AnimalPanel extends JPanel implements ActionListener {
	
	private Animal animal;
	private Timer t;
	public boolean hit;
	
	private ArrayList<Food> food = new ArrayList<Food>();
	
	Random randnum = new Random(); // for calling random numbers within ranges
	
	public AnimalPanel(Dimension initialSize) {
        super();
        
        //int x, int y, int size, int speedx, int speedy, Color animalColor, Color cheekColor, int cheekSize, int tentacleHeight
        animal = new Animal(randnum.nextInt(initialSize.width) - 0, 
        				randnum.nextInt(initialSize.height) - 0,
                        Math.min(initialSize.width,
                        		initialSize.height)/5, 
                        (randnum.nextInt(20) - 10), (randnum.nextInt(20) - 10),
                        new Color((int) (Math.random() * 200), (int) (Math.random() * 200), (int) (Math.random() * 200)),
                        new Color((int) (Math.random() * 200), (int) (Math.random() * 200), (int) (Math.random() * 200)),
                        (randnum.nextInt(2) + 2),
                        (randnum.nextInt(5) + 2)
                        );
          
        //timer for actionPerformed
        t = new Timer(33, this);
        t.start();
        
        //listens for mouse clicks
		addMouseListener(new MyMouseAdapter());
    }

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g); //refreshes system
		setBackground(Color.black); // draws a black background
		
		animal.draw(g2); //calls animal (jellyfish)
		for (int i = 0; i < food.size(); i++) { //calls food in array
			food.get(i).draw(g2);	
		} 
	}

	@Override
    public void actionPerformed(ActionEvent e) {	
			Food f = null;
			
			//if food is on the screen, move towards food
			for (int i = 0; i < food.size(); i++) {
				if (food.size() > 0) {
					animal.moveToFood(animal, food); //moves animal based on where the food is
					animal.checkBoundaries(animal, this.getSize()); //moves animal away from screen boundaries
					animal.hit(food, this.getSize()); //checks if animal has hit the food
			        
			        //creates a new food with new random variables if the old one has been eaten
			        if (animal.hitCharacter() == true) {
			        	animal.hit = false; //resets hit tracker
			        	f = food.get(i);
			        	food.remove(f);
			        	System.out.println(i);
			        }
				        
				}
			}
			//if there is no food on the screen, move in straight lines
			animal.move();
			animal.checkBoundaries(animal, this.getSize()); //moves animal away from screen boundaries
			repaint();
			//System.out.println(food.size());
	}
	
	//Used to trigger mouse events
	public class MyMouseAdapter extends MouseAdapter{
		public void mouseClicked (MouseEvent e){
			//switch to check for objects being present 
			boolean objectHere = false;
			
			Food f = null;
			for (int i = 0; i < food.size(); i++) {
				//if food exists at a given mouse click area
				if (food.get(i).checkMouseHit(e)) {
					objectHere = true;
					System.out.println(objectHere);
					if (e.isControlDown()) { //remove food when clicked on with Ctrl held down using f!=null statement
						f = food.get(i);
					}
					else {
						food.get(i).enlarge(); //enlarges food if clicked normally
					}
				}
				
				if (f != null) {
					food.remove(f);	
					System.out.println(f);
				}
			}
			
			//adds new food where clicked 
			if  (objectHere == false){
				int xClick = e.getX(); 
				int yClick = e.getY();
				food.add(new Food(xClick, yClick, 50));
				System.out.println(objectHere);
			}
		}
	}
		
}

