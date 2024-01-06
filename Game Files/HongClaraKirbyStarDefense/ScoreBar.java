import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
 // Need Color and Font for drawing ScoreBar

/**
 * Code is borrowed/copied from Mr. Cohen's ScoreBar class in the Bug Simulator - OOPLessonInBugs. 
 * Some things have been modified to fit the aesthetic of my game. 
 */
public class ScoreBar extends Actor
{
    // Declare Objects
    private GreenfootImage scoreBoard;
    private Color background;
    private Color foreground;
    private Font textFont;
    private String text;

    // Declare Variables:
    private int width;
    /**
     * Construct a ScoreBar of the appropriate size.
     * 
     * @param width     width of the World where the
     *                  ScoreBar will be placed
     */
    public ScoreBar (int width)
    {
        scoreBoard = new GreenfootImage (width, 40);
        background = new Color (255, 255, 255); //white 
        foreground = new Color (230, 151, 211); //the pinkish colour
        textFont = new Font ("Helvetica", 30);
        scoreBoard.setColor(background);
        scoreBoard.fill(); //fills the scoreboard with white 
        this.setImage (scoreBoard);
        scoreBoard.setFont(textFont);

        this.width = width;
    } 

    /**
     * Takes a String and displays it centered to the screen.
     * 
     * @param output    Text for displaying. 
     */
    public void update (String output)
    {
        // Refill the background with background color
        scoreBoard.setColor(background);
        scoreBoard.fill();

        // Write text over the solid background
        scoreBoard.setColor(foreground);  
        // Smart piece of code that centers text
        int centeredY = (width/2) - ((output.length() * 12)/2);
        // Draw the text onto the image
        scoreBoard.drawString(output, centeredY, 30);
    }
}
