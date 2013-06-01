import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import java.io.FileFilter;


public class Room{
	
	Image bg;
	Image icon;
	JPanel jPanel;
	ArrayList<Thing> things;
	
	
	public <E extends JPanel> Room(E j,String bgImgPath,String iconImgPath,File thingImgDir){
		
		this.jPanel= j;
		try {
		    bg = ImageIO.read(new File(bgImgPath));
		    System.out.println("Background image loaded successfully");
		} catch (IOException e) {
			System.out.println("Failed to load background image");
		}
		
		try{
			icon = ImageIO.read(new File(iconImgPath));
			System.out.println("Icon image loaded successfully");
		}catch (IOException e) {
			System.out.println("Failed to load icon image");
		}
		
		things = new ArrayList<Thing>();
		//Import images of things
		if (thingImgDir.isDirectory()) { // make sure it's a directory
			FileFilter filter = new ThingLoadFilter("normal");
			File[] filesToCheck = thingImgDir.listFiles(filter);
            for (final File f : filesToCheck) {
                BufferedImage img = null;
                try {
                	String name = f.getName().substring(0, f.getName().length()-4); //Remove the file type (".png")
                    img = ImageIO.read(f);
                    things.add(new Thing(name,jPanel,img));
                } catch (final IOException e) {
                    System.out.println("Failed to load images of things in a room");
                }
            }
        }
		//Add animations and sounds
		for(Thing thing:things){
			try{
				String path = thingImgDir.getPath()+"/"+thing.getName()+"2.png";
				BufferedImage animation = ImageIO.read(new File(path));
				thing.addAnimation(animation);
			}catch(Exception e){
				
			}
			
			try{
				String path = thingImgDir.getPath()+"/"+thing.getName()+"2.gif";
				Image animation = Toolkit.getDefaultToolkit().createImage(path);
				thing.addAnimation(animation);
			}catch(Exception e){
				
			}
			
			String[] musicExtensions = {".wav",".mp3"};
			for(String me:musicExtensions){
				String path = thingImgDir.getPath()+"/"+thing.getName()+me;
				try{
					File audioFile = new File(path);
					if(audioFile.exists()){
						System.out.println("Added audiofile "+path);
						thing.addAudio(audioFile);
					}

				}catch(Exception e){
					System.out.println("Couldn't load sound"+path);
				}
			}
			
		}
		
			
	}
	
	public void updateThings(){
		for(Thing t: things){
			t.update();
		}
	}
	
	public Point getIconSize(){
		return new Point(icon.getWidth(jPanel),icon.getHeight(jPanel));
	}
	
	public void thingClicked(Point mousePos){
		for(Thing t: things){
			t.markAsClicked(mousePos);
		}
	}
	
	public void draw(Graphics g){
		g.drawImage(bg,0,0,jPanel);
		drawThings(g);
	}
	
	private void drawThings(Graphics g){
		for(Thing t: things){
			t.draw(g);
		}
	}
	
	public void drawIcon(Graphics g,Point coord){
		g.drawImage(icon, coord.x, coord.y,jPanel);
	}

}
