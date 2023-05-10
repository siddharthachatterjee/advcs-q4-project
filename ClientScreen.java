import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.RenderingHints.Key;
import java.awt.Color;
import java.awt.Font;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ClientScreen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {

    private JTextField textInput;
    private String chatMessage;
    private String hostName = "localhost";
    private String name = "";
    private String chat = "";

    // private ArrayList<Image> images = new ArrayList<>();
    // private JButton sendButton;
    private boolean drawing = false;

    private JTextArea textArea = new JTextArea();

    // private PrintWriter out;
    private ObjectOutputStream outObj;

    private int count = 0;

    private Square[][] grid;

    private int sizeY, sizeX, length;
    private int dimension;
    private int gridX, gridY;

    private HashMap<String, Pair<Integer, Integer>> locations = new HashMap();

    private int cornerX, cornerY;

    private Color color;
    private Color[] colors;
    private String word;

    public ClientScreen() {
        color = Color.RED;
        colors = new Color[] { Color.BLACK, Color.WHITE, Color.GRAY, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.BLUE, Color.PINK };
        sizeY = 600;
        sizeX = 800;
        length = 40;
        dimension = sizeX/ length;
        grid = new Square[3000][3000];
        gridX = 0;
        gridY = 0;

        textInput = new JTextField();
        textInput.setBounds(600, 300, 200, 30);
        this.add(textInput);
        textInput.addActionListener(this);

        textArea = new JTextArea();
        textArea.setBounds(600, 400, 200, 200);
        add(textArea);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Square(dimension);
                grid[i][j].fill(((int)(Math.random() * 2) == 1? Color.BLACK : Color.WHITE));
            }
        }
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setLayout(null);
    }

    private void fill(int x, int y) {
        if (x < gridX || x > gridX + dimension * (length) || y < gridY || y > gridY + dimension * (length))
            return;
        int i = (x - gridX) / dimension;
        int j = (y - gridY) / dimension;
        if (i < length && j < length) {
            // System.out.println(i + " " + j);
          //  for (int i1 = i - 3; i1 < i + 3; i1++) {
            //    for (int j1 = j - 3; j1 < j + 3; j1++) {
              //      if (i1 < length && j1 < length)
                        grid[i][j].fill(color);
                    repaint();
          //      }
          //  }
        }
    }

    private void clear() {
        for (Square[] row : grid) {
            for (Square s : row) {
                s.clear();
            }
        }
    }

    // private void changeColor(int x, int y) {
    //     if (x > gridX && x < gridX + (colors.length) * 60 && y > gridY + size + 50 && y < gridY + size + 100) {
    //         int i = (x - gridX) / 60;

    //         // int j = (y - gridY)/60;
    //         color = colors[i];
    //         repaint();
    //     }
    // }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (name.length() > 0 && locations.size() > 0) {
            
            cornerX = locations.get(name).getFirst();
            cornerY = locations.get(name).getSecond();
            g.drawString("Signed in as " + name, 700, 50);
            if (grid != null) {
                for (int r = cornerX; r < cornerX+ length; ++r) {
                    for (int c = cornerY; c < cornerY + sizeY/dimension; ++c) {
                        if (grid[r][c] != null) {
                        grid[r][c].drawMe(g, gridX + (r - cornerX) * dimension, gridY + (c - cornerY) * dimension);
                        }
                    }
                }
            }
            
            g.setColor(Color.BLUE);
            g.fillOval(375, 275, 50, 50);

            g.drawString("(" + cornerX  + ", " + cornerY + ")", 50, 50);

            for (String k : locations.keySet()) {
                if (!k.equals(name)) {
                    int x = locations.get(k).getFirst();
                    int y = locations.get(k).getSecond();
                    g.fillOval(gridX + (x - cornerX + length/2) * dimension, gridY + (y - cornerY + sizeY/dimension/2) * dimension, dimension, dimension);
                }
            }

             if (drawing) {
                
            // g.setColor(Color.BLACK);
            // g.drawString("word: " + word, 600, 200);
            // g.drawString("Select a color:", gridX, gridY + size + 20);
            // for (int i = 0; i < colors.length; i++) {
            //     if (colors[i] == color) {
            //         g.setColor(Color.DARK_GRAY);

            //         g.fillRect(gridX + i * (60) - 5, gridY + size + 45, 60, 60);
            //     }
            //     g.setColor(colors[i]);
            //     g.fillRect(gridX + i * (60), gridY + size + 50, 50, 50);
            // }
        //     g.setFont(new Font("Arial", Font.BOLD, 18));
        //     g.setColor(Color.BLACK);
        //  /   g.drawString("Drag or click to draw", gridX + size + 50, gridY + 100);
            }
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        // System.out.println("f");
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (drawing) {
        fill(e.getX(), e.getY());
    //    changeColor(e.getX(), e.getY());
        // System.out.println(grid.toString());
        try {

            outObj.reset();
            outObj.writeObject(grid);
            // System.out.println(grid.toString());
        } catch (Exception ex) {

        }
    }

    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (drawing) {
        fill(e.getX(), e.getY());

        // System.out.println(grid.toString());
        try {

            outObj.reset();
            outObj.writeObject(grid);
        } catch (Exception ex) {

        }
    }
    }

    public void poll() throws IOException {

        int portNumber = 1000;
        try {
            // ServerSocket clientServer = new ServerSocket(portNumber);
            Socket serverSocket = new Socket(hostName, portNumber);
            outObj = new ObjectOutputStream(serverSocket.getOutputStream());

            // out = new PrintWriter(serverSocket.getOutputStream(), true);

            ObjectInputStream inObj = new ObjectInputStream(serverSocket.getInputStream());

           //  outObj.writeObject("Connection Successful!");

            // BufferedReader in = new BufferedReader(new
            // InputStreamReader(serverSocket.getInputStream()));

            // Receive connection message
            // Waits for and receives an object
            // readObject() requires a ClassNOtFoundException
            // String serverMessage = (String) in.readObject();
            // System.out.println(serverMessage);

            // Receive server location
            while (true) {
                try {
                    Object o = inObj.readObject();
                 //   System.out.println(o);
                    if (o instanceof String) {

                        String s = (String) o;
                        //System.out.println(s);
                        if (s.equals("drawing")) {
                            drawing = true;
                            word = (String)inObj.readObject();
                        }
                        else {
                            textArea.setText(textArea.getText() + "\n" + s);
                            if (s.contains(word)) {
                                outObj.reset();
                                outObj.writeObject(s);
                            }
                            if (s.charAt(0) != '_') {
                                serverSocket.close();
                            }
                        }
                    } else if (o instanceof Square[][] && !drawing) {

                        grid = (Square[][]) o;
                    }
                    else if (o instanceof Pair) {
                        Pair<String, Pair<Integer,Integer>> p = (Pair)o;
                        locations.put(p.getFirst(), p.getSecond());
                    }
                    requestFocus();
                    repaint();
                } catch (Exception e) {

                }
            }
            // game.getTable();

            // in.close();

            // outObj.close();

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection");
            // System.exit(1);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == textInput) {
            String sendText = textInput.getText();
            if (name.length() == 0) {
                name = sendText;
            }
            try {
                outObj.reset();
                outObj.writeObject(textInput.getText());
            }
            catch (Exception err) {

            }
            requestFocus();
            textInput.setText(null);
            ;
            // if (out != null) {
            // // out.println(sendText);
            // System.out.println(sendText);
            // // chatMessage += sendText + "\n";
            // }
        }
        repaint();

    }

    public void keyPressed(KeyEvent e) {
    //    System.out.println("hi");
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
          cornerX++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            cornerX--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            cornerY--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            cornerY++;
        }
        try {
            outObj.reset();
            outObj.writeObject(new Pair(cornerX, cornerY));
        }
        catch (Exception err) {

        }
        repaint();
    }
    
    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }


}