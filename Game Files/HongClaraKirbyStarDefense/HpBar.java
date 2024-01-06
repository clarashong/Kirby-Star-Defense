import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The HpBar is what displays the health of the rocks or the boss. It consists of 5 boxes, one box at a time will disappear when a fifth 
 * of the item's hp is lost. 
 * The boxes are also coloured to display the state of the corresponding object's health: 
 * 4-5 boxes left - green
 * 2-3 boxes left - yellow
 * 1 box left - red
 */
public class HpBar extends Actor
{
    private int maxHp, hp; 
    private GreenfootImage state; //how the hp bar looks according to health 

    public HpBar(int maxHp) {
        this.maxHp = maxHp; 
        hp = maxHp;
        state = new GreenfootImage(87, 19); //creates greenfoot image with those dimensions 
        setUpImage();
    }
    
    public void act() {
        setUpImage(); //updates the HpBar's image every act 
    }
    
    //Method that allows other objects to update their own corresponding hp bar with their current hp. 
    public void updateHp(int newHp) {
        hp = newHp;
    }
    
    //This method creates the look for an hp bar based on their health, and then sets the objects image to that look. 
    public void setUpImage() {
        //array of what x-coordinate the health boxes would be
        int[] boxLocations = {2,19,36,53,70};
        //how many full non-black boxes there are
        int numColouredBoxes = hp/ (maxHp/5);   
        if (numColouredBoxes < 5) {
            numColouredBoxes++; //This is to take care of the downwards rounding
        } 
        
        //gives all the boxes a white outline
        state.setColor(Color.WHITE); 
        state.fill(); 
        
        if (numColouredBoxes == 5 || numColouredBoxes == 4) {
            state.setColor(Color.GREEN); //using colours from the Color class 
        } else if (numColouredBoxes == 3 || numColouredBoxes == 2) {
            state.setColor(Color.YELLOW); //changes colours as less boxes are available
        } else if (numColouredBoxes == 1){
            state.setColor(Color.RED); 
        }
        for (int i = 0; i < boxLocations.length; i++) {
            state.fillRect(boxLocations[i], 2, 15, 15); //Fills all the non-black boxes 
        }
        state.setColor(Color.BLACK); 
        if (numColouredBoxes < 5) {
            for (int i = 0; i < 5 - numColouredBoxes; i ++) {
                state.fillRect(boxLocations[4-i], 2, 15, 15); 
            }
        }
        setImage(state); 
    }
    
    //Fills all the boxes with black. 
    public void setAllBlack() {
        int[] boxLocations = {2,19,36,53,70};
        state.setColor(Color.BLACK); 
        for (int i = 0; i < boxLocations.length; i++) {
            state.fillRect(boxLocations[i], 2, 15, 15); //Fills all the non-black boxes 
        }
        setImage(state); 
    }
}
