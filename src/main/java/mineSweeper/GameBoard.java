package mineSweeper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static mineSweeper.GameConfig.*;

/**
 * Created by yicliu on 9/22/20.
 */
public class GameBoard {

    private JButton[][] mineButtonBoard = new JButton[BOARD_X_SIZE][BOARD_Y_SIZE];
    private String[][] realMineBoard = new String[BOARD_X_SIZE][BOARD_Y_SIZE];
    private JButton gameIcon;
    private JButton gameDescriber;
    private JFrame frame = new JFrame("Mine Sweeper");
    private JPanel panel;
    private int availableClick;

    public GameBoard() {
        initializeGame();
    }

    public static void main(String[] args)
    {
         GameBoard gameBoard = new GameBoard();
    }

    private void initializeRealMineBoard() {
        generateMineOnBoard();

        for (int i = 0; i < BOARD_X_SIZE; ++i) {
            for (int j = 0; j < BOARD_Y_SIZE; ++j) {
                if(!MINE.equals(realMineBoard[i][j])) {
                    int mineCount = countAdjacentMine(i, j);
                    if(mineCount != 0) {
                        realMineBoard[i][j] = Integer.toString(mineCount);
                    }
                    else {
                        realMineBoard[i][j] = EMPTY;
                    }
                }
            }
        }
    }

    private int countAdjacentMine(int x, int y) {
        int count = 0;
        if( x-1 >= 0) {
            if( y-1 >= 0) {
                if(MINE.equals(realMineBoard[x-1][y-1])) {
                    count++;
                }
            }
            if(MINE.equals(realMineBoard[x-1][y])) {
                count++;
            }
            if( y+1 < BOARD_Y_SIZE) {
                if (MINE.equals(realMineBoard[x-1][y+1])) {
                    count++;
                }
            }
        }

        if( y-1 >= 0) {
            if(MINE.equals(realMineBoard[x][y-1])) {
                count++;
            }
        }

        if( y+1 < BOARD_Y_SIZE) {
            if (MINE.equals(realMineBoard[x][y+1])) {
                count++;
            }
        }

        if( x+1 < BOARD_X_SIZE) {
            if( y-1 >= 0) {
                if(MINE.equals(realMineBoard[x+1][y-1])) {
                    count++;
                }
            }
            if(MINE.equals(realMineBoard[x+1][y])) {
                count++;
            }
            if( y+1 < BOARD_Y_SIZE) {
                if(MINE.equals(realMineBoard[x+1][y+1])) {
                    count++;
                }
            }
        }

        return count;
    }

    private void generateMineOnBoard() {
        Random random = new Random();
        Set<Location> mines = new HashSet<>();
        while(mines.size() < MINE_NUMBER) {
            int xLocation = random.nextInt(BOARD_X_SIZE);
            int yLocation = random.nextInt(BOARD_Y_SIZE);
            mines.add(new Location(xLocation, yLocation));
        }

        mines.forEach(mine -> {
            realMineBoard[mine.getRow()][mine.getCol()] = MINE;
        });
    }

    private void initializeGameBoard(){
        frame.getContentPane();
        panel = new JPanel();
        panel.setLayout(null);

        gameDescriber = new JButton();
        gameIcon = new JButton();
        availableClick = BOARD_X_SIZE*BOARD_Y_SIZE - MINE_NUMBER;
        initializeRealMineBoard();
        setupGridOnBoard();
        setUtilButtonInfo(gameDescriber,  String.format(CLICK_INFO, availableClick), 500, 60);
        setupResetButton(RESET, 600, 140);
        setupIconButton(gameIcon, "Happy.png",600, 0);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        refreshJframe();
    }

    private void setupGridOnBoard() {
        for (int i = 0; i < BOARD_X_SIZE; ++i) {
            for (int j = 0; j < BOARD_Y_SIZE; ++j) {
                mineButtonBoard[i][j] = new MineButton(realMineBoard[i][j], i,j, this);
                mineButtonBoard[i][j].setText(EMPTY);
                mineButtonBoard[i][j].setPreferredSize(new Dimension(60, 60));
                mineButtonBoard[i][j].setBounds(j * 60, i * 60, 60, 60);
                mineButtonBoard[i][j].setFocusPainted( true );
                mineButtonBoard[i][j].setOpaque(true);
                mineButtonBoard[i][j].setBackground(Color.DARK_GRAY);
                mineButtonBoard[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                panel.add(mineButtonBoard[i][j]);
            }
        }
    }

    private void cleanupBoard() {
        mineButtonBoard = new JButton[BOARD_X_SIZE][BOARD_Y_SIZE];
        realMineBoard = new String[BOARD_X_SIZE][BOARD_Y_SIZE];
    }

    private void initializeGame() {
        initializeGameBoard();
    }

    private void setUtilButtonInfo(JButton button, String text, int xLocation, int yLocation) {
        button.setText(text);
        button.setPreferredSize(new Dimension(300, 60));
        button.setBounds(xLocation, yLocation, 300, 60);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        panel.add(button);
    }

    private void refreshJframe() {
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void setupResetButton(String text, int xLocation, int yLocation) {
        JButton jbutton  = new JButton(text);
        jbutton.setPreferredSize(new Dimension(60, 60));
        jbutton.setBounds(xLocation, yLocation,60,60);
        jbutton.setFont(new Font("Arial", Font.PLAIN, 12));
        jbutton.addActionListener(e -> handleResetButtonClick(e, this));
        panel.add(jbutton);
    }

    private void setupIconButton(JButton button, String path, int xLocation, int yLocation) {
        setIcon(button, path);
        button.setPreferredSize(new Dimension(60, 60));
        button.setBounds(xLocation, yLocation,60,60);
        panel.add(button);
    }

    private void setIcon(JButton button, String path) {
        try {
            Image img = ImageIO.read(getClass().getClassLoader().getResource(path));
            button.setIcon(resizeIcon(img, 60 ,60));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }

    private Icon resizeIcon(Image img, int resizedWidth, int resizedHeight) {
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private void handleResetButtonClick(ActionEvent e, GameBoard gb) {
        frame.remove(panel);
        cleanupBoard();
        initializeGame();
    }

    public void flip(Location location) {
        if(realMineBoard[location.getRow()][location.getCol()].equals(EMPTY)) {
            Set<Location> locations = new HashSet<>();
            locations = expandMineButton(location.getRow(), location.getCol(), locations);
            locations.forEach(expandedLocation -> {
                mineButtonBoard[expandedLocation.getRow()][expandedLocation.getCol()]
                        .setText(realMineBoard[expandedLocation.getRow()][expandedLocation.getCol()]);
                mineButtonBoard[expandedLocation.getRow()][expandedLocation.getCol()].setEnabled(false);
                availableClick--;
            });
        }
        else if(realMineBoard[location.getRow()][location.getCol()].equals(MINE)){
            mineButtonBoard[location.getRow()][location.getCol()].setText(MINE);
            gameOver(false);
            return;
        }
        else{
            mineButtonBoard[location.getRow()][location.getCol()].setText(realMineBoard[location.getRow()][location.getCol()]);
            mineButtonBoard[location.getRow()][location.getCol()].setEnabled(false);
            availableClick--;
        }

        gameDescriber.setText(String.format(CLICK_INFO, availableClick));
        if(availableClick == 0) {
            gameOver(true);
            return;
        }
        refreshJframe();
    }

    private Set<Location> expandMineButton(int x, int y, Set<Location> locations) {
        if(mineButtonBoard[x][y].isEnabled()) {
            locations.add(new Location(x, y));
        }
        if(!realMineBoard[x][y].equals(EMPTY)) {
            return locations;
        }
        if(x-1 >= 0 && !locations.contains(new Location(x-1, y))){
            locations = expandMineButton(x -1, y, locations);
        }
        if(x+1 < BOARD_X_SIZE && !locations.contains(new Location(x+1, y))){
            locations = expandMineButton(x +1, y, locations);
        }
        if(y-1 >= 0 && !locations.contains(new Location(x, y-1))){
            locations = expandMineButton(x, y-1, locations);
        }
        if(y+1 < BOARD_Y_SIZE && !locations.contains(new Location(x, y+1))){
            locations = expandMineButton(x, y+1, locations);
        }
        return locations;
    }

    private void gameOver(boolean isWin) {
        if(isWin) {
            gameDescriber.setText(PLAYER_WIN);
            setIcon(gameIcon, "Cool.png");
        }
        else {
            gameDescriber.setText(PLAYER_BOMBED);
            setIcon(gameIcon, "Dead.png");
        }

        disableBoard();
        refreshJframe();
    }

    private void disableBoard() {
        for (int i = 0; i < BOARD_X_SIZE; ++i) {
            for (int j = 0; j < BOARD_Y_SIZE; ++j) {
                mineButtonBoard[i][j].setEnabled(false);
            }
        }
    }
}
