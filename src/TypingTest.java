package typingtest;

import edu.macalester.graphics.*;
import edu.macalester.graphics.events.Key;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Runs the main window and UI component of the typingTest
 * @Author Kien Nguyen, Daniel Seo
 */
public class TypingTest {
    private String text;
    private boolean newGame = true;

    private List<char[]> texts = new ArrayList<>();
    private Deque<LetterBlock> textDeque = new ArrayDeque<>();
    private Deque<LetterBlock> textStack = new ArrayDeque<>();

    private CanvasWindow canvas;
    private GraphicsGroup textOnScreen;

    private LetterBlock current;
    private Cursor cursor;
    private Timer timer;
    private RandomWordsGenerator rw;

    public static final double CANVAS_WIDTH = 1240;
    public static final double CANVAS_HEIGHT = 600;
    public static final int NUMWORDS = 80;    
    public static final int MAX_TIME = 10;

    private double wordCount = 0;
    private double characterCount = 0;
    private double accuracy = 0;
    private double error = 0;

    /**
     * Constructor for a new TypingTest game that can be replayed 
     */
    public TypingTest() {
        canvas = new CanvasWindow("TypingTest", (int) CANVAS_WIDTH, (int) CANVAS_HEIGHT);
        try {
            this.rw = new RandomWordsGenerator();
        } catch (Exception e) {
            System.out.println("File not found");
        }
        textOnScreen = new GraphicsGroup();
        timer = new Timer(canvas, CANVAS_WIDTH/2, 90, MAX_TIME);
        cursor = new Cursor();
        typing();
        canvas.animate(() -> {
            timer.update();
            cursor.animate();
            checkLose();
        });
    }

    /**
     * Use a randomWordsGenerator object to delete the old text on screen and 
     * generate a new text for typing with the same number of words
     */
    private void resetText() {
        text = rw.getRandomWords(NUMWORDS);
        List<String> words = Utils.splitWords(text);
        texts.clear();
        for (String word : words) {
            texts.add(word.toCharArray());
        }
    }

    /**
     * Run a new game of TypingTest
     */
    public void run() {
        newGame = false;
        reset();
        oneTurn();
    }

    /**
     * Clear the screen of all texts and reset the timer and cursor. 
     * All internal data for calculating final results are reset to 0.
     */
    private void reset() {
        canvas.removeAll();
        textDeque.clear();
        textStack.clear();
        timer.reset();
        wordCount = 0;
        characterCount = 0;
        error = 0;
        accuracy = 0;
        current  = null;
        resetText();
    }

    /**
     * Set up the text for one game of TypingTest and begin the timer
     */
    private void oneTurn() {
        printText();
        timer.run();
        update();
    }

    /**
     * Reset the previous letterBlock object to its original state, adjusting the internal data and cursor accordingly,
     * Can be called continously until the user reach the beginning of the text
     */
    private void deleteLetter() {
        if (!textStack.isEmpty()) {
            if (current.toString().equals("space")) {
                wordCount--;
            }
            textDeque.addFirst(current);
            current = textStack.pop();
            if (current.getScore() == -1) {
                error--;
            } else if (current.getScore() == 1) {
                accuracy--;
            }
            current.reset();
            cursor.moveCursor(current);
            characterCount--;
        } 
    }

    /**
     * Display the text for typing on the screen, setting the first letterBlock of the text as the current letter
     */
    private void printText() {
        double x = 4;
        double y = 4;
        double padding = 35;
        for (char[] word : texts) {
            for (char letter: word) {
                LetterBlock letterOnScreen = new LetterBlock(letter);
                textOnScreen.add(letterOnScreen, x, y);
                x += 17;
                textDeque.add(letterOnScreen);
            }
            LetterBlock spacing = new LetterBlock("space");
            textOnScreen.add(spacing, x, y);
            textDeque.add(spacing);
            x += 10;
            if (x > CANVAS_WIDTH*0.7) {
                x = 4;
                y = y + padding;
            }
        }
        current = textDeque.remove();
    }

    /**
     * Check if the timer is out and display the final stats of the player's turn
     */
    private void checkLose() {
        if (timer.isOut()) {
            GraphicsGroup loseVisual = new GraphicsGroup();

            GraphicsText wordCountText = new GraphicsText("Word Typed: " + (int) wordCount);
            wordCountText.setFontSize(50);
            wordCountText.setFillColor(Color.RED);
            wordCountText.setCenter(CANVAS_WIDTH/4, CANVAS_HEIGHT*0.4);

            GraphicsText wpmText = new GraphicsText("WPM: " + ((characterCount/5.0)/(MAX_TIME/60.0)));
            wpmText.setFontSize(50);
            wpmText.setFillColor(Color.RED);
            wpmText.setCenter(CANVAS_WIDTH*0.75, CANVAS_HEIGHT*0.75);

            GraphicsText accuracyText = new GraphicsText("Accuracy: " + (int) ((accuracy/characterCount)*100) + "%");
            accuracyText.setFontSize(50);
            accuracyText.setFillColor(Color.RED);
            accuracyText.setCenter(CANVAS_WIDTH/4, CANVAS_HEIGHT*0.75);

            GraphicsText errorText = new GraphicsText("Characters: " + (int) (accuracy) + "/" + (int) (error));
            errorText.setFontSize(50);
            errorText.setFillColor(Color.RED);
            errorText.setCenter(CANVAS_WIDTH*0.75, CANVAS_HEIGHT*0.4);

            GraphicsText continueText = new GraphicsText("Press SPACE to restart");
            continueText.setFontSize(40);
            continueText.setFillColor(Color.GRAY);
            continueText.setCenter(CANVAS_WIDTH*0.5, CANVAS_HEIGHT*0.85);

            loseVisual.add(wordCountText);
            loseVisual.add(wpmText);
            loseVisual.add(accuracyText);
            loseVisual.add(errorText);
            loseVisual.add(continueText);

            textOnScreen.removeAll();
            canvas.add(loseVisual);
            newGame = true;
        }
    }

    /**
     * Adjust the layout onscreen of the different UI elements
     */
    private void update() {
        textOnScreen.setCenter(CANVAS_WIDTH*0.5, CANVAS_HEIGHT*0.6);
        canvas.add(textOnScreen);
        textOnScreen.add(cursor);
        cursor.moveCursor(current);
    }

    /**
     * Create the internal logic of how the game reacts to user's interaction.
     * Highlighting letterBlock and changing the cursor and internal data accordingly
     * Stop receiving user's interaction once the timer is out and allowing the user to replay the game
     */
    private void typing() {
        canvas.onKeyDown((event) -> {
            timer.startTimer();
            if (newGame && event.getKey().equals(Key.SPACE)) {
                run();
            } else if (textDeque.size() != 0 && !timer.isOut()) {
                if (event.getKey().equals(Key.DELETE_OR_BACKSPACE)) {
                    deleteLetter();
                    return;
                }
                else if (event.getKey().toString().toLowerCase().equals(current.toString())) {
                    current.correctAnimation();
                    textStack.push(current);
                    current = textDeque.remove();
                    accuracy++;
                } else {
                    current.wrongAnimation();
                    textStack.push(current);
                    current = textDeque.remove();
                    error++;
                }
                if (current.toString().equals("space")) {
                    wordCount++;
                }
                cursor.moveCursor(current);
                characterCount++;
            } 
        });
    }

    public static void main(String[] args) {
        TypingTest test = new TypingTest();
        test.run();
    }
}
