import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class DrawPanelControl extends JPanel implements
MouseListener,MouseMotionListener,FocusListener {
  private DrawPanel displayPanel;
  private JMenuItem openCommand,saveCommand,saveasCommand,closeCommand,exitCommand;
  private JMenuItem drawCommand,stopCommand,clearCommand;
  private JMenuItem redCommand,greenCommand,blueCommand,customcolorCommand;
  private JMenuItem toggleMessage,toggleButton;
  private JButton openButton,saveButton,saveasButton,exitButton,drawButton,stopButton,clearButton;
  private JButton redButton,greenButton,blueButton,customcolorButton;
  private JComboBox shapeSelectBox;
  private MenuHandler listener_m;
  private ButtonHandler listener_b;
  private ComboBoxHandler listener_c;
  private JLabel xLabel,yLabel;
  private int xCoord,yCoord;
  private int displayWidth,displayHeight;
  private Color currentColor;
  private final Color redColor = Color.RED;
  private final Color greenColor = Color.GREEN;
  private final Color blueColor = Color.BLUE;
  private boolean inDrawingMode = false;
  private Color currentButtonBackground;
  private JButton currentButton = null;
  private Color displayBackgroundColor;
  private Color frameColor;
  private int x1_coord,y1_coord;
  private int x2_coord,y2_coord;
  private static final int RECT = 0;
  private static final int CIRCLE = 1;
  private static final int LINE = 2;
  private static final int STRING = 3;
  private static final int FILLRECT = 4;
  private static final int FILLCIRCLE = 5;
  private int currentShape = RECT;
  private int upperLeft_x,upperLeft_y;
  private int rect_width,rect_height;
  private int old_upperLeft_x,old_upperLeft_y;
  private int old_rect_width,old_rect_height;
  private boolean inCreatingShape = false;


  private class DrawPanel extends JPanel {
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
//      draw_frame(g);
//    setBackground(Color.BLUE);
    }
  }
  private void draw_frame(Graphics g) {
    Color oldColor = g.getColor();
    g.setColor(frameColor);
    g.drawRect(0,0,displayWidth-1,displayHeight-1);
    g.setColor(oldColor);
  }

  private void addButton(JPanel panel,JButton button,ButtonHandler listener) {
    panel.add(button);
    button.addActionListener(listener);
  }
  public DrawPanelControl() {
    listener_m = new MenuHandler();
    listener_b = new ButtonHandler();
    listener_c = new ComboBoxHandler();

    JPanel filePanel = new JPanel();
    filePanel.setLayout(new GridLayout(1,4));
    openButton = new JButton("Open");
    saveButton = new JButton("Save");
    saveasButton = new JButton("Save as");
    exitButton = new JButton("Exit");
    drawButton = new JButton("Draw");
    stopButton = new JButton("Stop");
    clearButton = new JButton("Clear");
    openButton.setToolTipText("open a file");
    saveButton.setToolTipText("save to file");
    saveasButton.setToolTipText("save to a specified file");
    exitButton.setToolTipText("exit the program");
    drawButton.setToolTipText("set drawing mode");
    stopButton.setToolTipText("stop drawing mode");
    clearButton.setToolTipText("erase the drawing");
    addButton(filePanel,openButton,listener_b);
    addButton(filePanel,saveButton,listener_b);
    addButton(filePanel,saveasButton,listener_b);
    addButton(filePanel,exitButton,listener_b);
    addButton(filePanel,drawButton,listener_b);
    addButton(filePanel,stopButton,listener_b);
    addButton(filePanel,clearButton,listener_b);

    JPanel colorPanel = new JPanel();
    colorPanel.setLayout(new GridLayout(1,4));
    redButton = new JButton("Red");
    greenButton = new JButton("Green");
    blueButton = new JButton("Blue");
    customcolorButton = new JButton("Custom Color");
    redButton.setToolTipText("set red color for drawing");
    greenButton.setToolTipText("set green color for drawing");
    blueButton.setToolTipText("set blue color for drawing");
    customcolorButton.setToolTipText("set custom color for drawing");
    addButton(colorPanel,redButton,listener_b);
    addButton(colorPanel,greenButton,listener_b);
    addButton(colorPanel,blueButton,listener_b);
    addButton(colorPanel,customcolorButton,listener_b);
    shapeSelectBox = new JComboBox();
    shapeSelectBox.addItem("RECT");
    shapeSelectBox.addItem("CIRCLE");
    shapeSelectBox.addItem("LINE");
    shapeSelectBox.addItem("STRING");
    shapeSelectBox.addItem("FILLRECT");
    shapeSelectBox.addItem("FILLCIRCLE");
    shapeSelectBox.addActionListener(listener_c);
    shapeSelectBox.setBackground(Color.YELLOW);
    shapeSelectBox.setForeground(Color.BLUE);
    shapeSelectBox.setToolTipText("select drawing shape");
    colorPanel.add(shapeSelectBox);
    xLabel = new JLabel("  X: value");
    yLabel = new JLabel("Y: value");
    xLabel.setToolTipText("x coordinate value");
    yLabel.setToolTipText("y coordinate value");
    colorPanel.add(xLabel);
    colorPanel.add(yLabel);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BorderLayout());
    buttonPanel.add(filePanel,BorderLayout.NORTH);
    buttonPanel.add(colorPanel,BorderLayout.SOUTH);

    setLayout(new BorderLayout());
    add(buttonPanel,BorderLayout.NORTH);

    displayPanel = new DrawPanel();
    displayPanel.addMouseListener(this);
    displayPanel.addMouseMotionListener(this);
    displayPanel.addFocusListener(this);
    displayBackgroundColor = displayPanel.getBackground();
    add(displayPanel,BorderLayout.CENTER);

    xLabel.setOpaque(true);
    yLabel.setOpaque(true);
    set_current_color(redColor);
  }

  private class MenuHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      JMenuItem source = (JMenuItem) e.getSource();
      if (source == exitCommand) {
        exit_cmd();
      }
      else
      if (source == customcolorCommand) {
        customcolor_cmd();
      }
      else
      if (source == redCommand) {
        red_cmd();
      }
      else
      if (source == greenCommand) {
        green_cmd();
      }
      else
      if (source == blueCommand) {
        blue_cmd();
      }
      else
      if (source == drawCommand) {
        draw_cmd();
      }
      else
      if (source == stopCommand) {
        stop_cmd();
      }
      else
      if (source == clearCommand) {
        clear_cmd();
      }
      else
      if (source == saveCommand) {
        save_cmd();
      }
      else
      if (source == saveasCommand) {
        saveas_cmd();
      }
      else
      if (source == openCommand) {
        open_cmd();
      }
    }
  }
  private class ButtonHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      JButton source = (JButton) e.getSource();
      if (source == exitButton) {
        exit_cmd();
      }
      else
      if (source == customcolorButton) {
        customcolor_cmd();
      }
      else
      if (source == redButton) {
        red_cmd();
      }
      else
      if (source == greenButton) {
        green_cmd();
      }
      else
      if (source == blueButton) {
        blue_cmd();
      }
      else
      if (source == drawButton) {
        draw_cmd();
      }
      else
      if (source == stopButton) {
        stop_cmd();
      }
      else
      if (source == clearButton) {
        clear_cmd();
      }
      else
      if (source == saveButton) {
        save_cmd();
      }
      else
      if (source == saveasButton) {
        saveas_cmd();
      }
      else
      if (source == openButton) {
        open_cmd();
      }
    }
  }
  private class ComboBoxHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JComboBox source = (JComboBox) e.getSource();
      currentShape = source.getSelectedIndex();
      System.out.println("shape = " + currentShape);
    }
  }
  private void exit_cmd() {
    System.exit(0);
  }
  private void customcolor_cmd() {
    Color  c;
    resetButtonColor();
    c = JColorChooser.showDialog(null,"Select Drawing Color",new Color(255,0,0));
    if (c != null)
      set_current_color(c);
  }
  private void red_cmd() {
    resetButtonColor();
    set_current_color(redColor);
  }
  private void green_cmd() {
    resetButtonColor();
    set_current_color(greenColor);
  }
  private void blue_cmd() {
    resetButtonColor();
    set_current_color(blueColor);
  }
  private void resetButtonColor() {
    if (inDrawingMode != true && currentButton != null) {
      currentButton.setBackground(currentButtonBackground);
      currentButton = null;
    }
  }
  private void set_current_color(Color color) {
    currentColor = color;
    xLabel.setBackground(currentColor);
    yLabel.setBackground(currentColor);
  }
  private void draw_cmd() {
    highlight_button(drawButton);
    inDrawingMode = true;
  }
  private void stop_cmd() {
    highlight_button(stopButton);
  }
  private void clear_cmd() {
    highlight_button(clearButton);
  }
  private void save_cmd() {
    highlight_button(saveButton);
  }
  private void saveas_cmd() {
    highlight_button(saveasButton);
  }
  private void open_cmd() {
    highlight_button(openButton);
  }
  private void highlight_button(JButton button) {
    inDrawingMode = false;
    if (currentButton != button) {
      if (currentButton != null) {
        currentButton.setBackground(currentButtonBackground);
      }
      currentButtonBackground = button.getBackground();
      currentButton = button;
      button.setBackground(Color.RED);
    }
  }


  private void addMenuItem(JMenu menu,JMenuItem menuItem,MenuHandler listener) {
    menuItem.addActionListener(listener);
    menu.add(menuItem);
  }

  public JMenuBar getMenuBar() {
    JMenu fileMenu = new JMenu("File");
    fileMenu.setToolTipText("file open,save,close,exit commands");
    openCommand = new JMenuItem("Open");
    saveCommand = new JMenuItem("Save");
    saveasCommand = new JMenuItem("Save As ...");
    closeCommand = new JMenuItem("Close");
    exitCommand = new JMenuItem("Exit");

    addMenuItem(fileMenu,openCommand,listener_m);
    fileMenu.addSeparator();
    addMenuItem(fileMenu,saveCommand,listener_m);
    addMenuItem(fileMenu,saveasCommand,listener_m);
    fileMenu.addSeparator();
    addMenuItem(fileMenu,closeCommand,listener_m);
    fileMenu.addSeparator();
    addMenuItem(fileMenu,exitCommand,listener_m);

    JMenu commandMenu = new JMenu("Command");
    commandMenu.setToolTipText("draw,stop,clear commands");
    drawCommand = new JMenuItem("Draw");
    stopCommand = new JMenuItem("Stop");
    clearCommand = new JMenuItem("Clear");

    addMenuItem(commandMenu,drawCommand,listener_m);
    addMenuItem(commandMenu,stopCommand,listener_m);
    addMenuItem(commandMenu,clearCommand,listener_m);

    JMenu colorMenu = new JMenu("Color");
    colorMenu.setToolTipText("color selection commands");
    redCommand = new JMenuItem("Red");
    greenCommand = new JMenuItem("Green");
    blueCommand = new JMenuItem("Blue");
    customcolorCommand = new JMenuItem("Custom Color ...");

    addMenuItem(colorMenu,redCommand,listener_m);
    addMenuItem(colorMenu,greenCommand,listener_m);
    addMenuItem(colorMenu,blueCommand,listener_m);
    addMenuItem(colorMenu,customcolorCommand,listener_m);

    JMenu controlMenu = new JMenu("Control");
    controlMenu.setToolTipText("message,button controls");
    toggleMessage = new JCheckBoxMenuItem("Message");
    toggleButton = new JCheckBoxMenuItem("Button");

    addMenuItem(controlMenu,toggleMessage,listener_m);
    addMenuItem(controlMenu,toggleButton,listener_m);

    JMenuBar menuBar = new JMenuBar();

    menuBar.add(fileMenu);
    menuBar.add(commandMenu);
    menuBar.add(colorMenu);
    menuBar.add(controlMenu);

    return(menuBar);
  }
  public void mousePressed(MouseEvent e) {
    if (inDrawingMode) {
      switch (currentShape) {
        case RECT:
          inCreatingShape = false;
          x1_coord = e.getX();
          y1_coord = e.getY();
          System.out.println("1st point: " + x1_coord +"," + y1_coord); 
          break;
        case CIRCLE:
          inCreatingShape = false;
          x1_coord = e.getX();
          y1_coord = e.getY();
          System.out.println("center point: " + x1_coord + "," + y1_coord); 
          break;
        default:
          break;
      }
    }
  }
  public void mouseClicked(MouseEvent e) {
  }
  public void mouseReleased(MouseEvent e) {
  }
  public void mouseEntered(MouseEvent e) {
    Component comp = (Component) e.getSource();
    displayWidth = comp.getWidth();
    displayHeight = comp.getHeight();
    comp.requestFocus();
    frameColor = Color.RED;
    draw_frame(comp.getGraphics());
//    comp.repaint();
  }
  public void mouseExited(MouseEvent e) {
    Component comp = (Component) e.getSource();
    frameColor = displayBackgroundColor;
    draw_frame(comp.getGraphics());
//    comp.repaint();
  }
  public void mouseDragged(MouseEvent e) {
    int lowerRight_x,lowerRight_y;
    Component comp = (Component) e.getSource();
    Graphics g = comp.getGraphics();
    g.setColor(currentColor);

    if (inDrawingMode) {
      switch (currentShape) {
        case RECT:
          x2_coord = e.getX();
          y2_coord = e.getY();
          if (x1_coord < x2_coord) {
            upperLeft_x  = x1_coord;
            lowerRight_x = x2_coord;
          }
          else {
            upperLeft_x  = x2_coord;
            lowerRight_x = x1_coord;
          }
          if (y1_coord < y2_coord) {
            upperLeft_y  = y1_coord;
            lowerRight_y = y2_coord;
          }
          else {
            upperLeft_y  = y2_coord;
            lowerRight_y = y1_coord;
          }
          rect_width  = lowerRight_x - upperLeft_x;
          rect_height = lowerRight_y - upperLeft_y;
          g.setXORMode(displayBackgroundColor);
          if (inCreatingShape == false)
            inCreatingShape = true;
          else {
            g.drawRect(old_upperLeft_x,old_upperLeft_y,old_rect_width,old_rect_height);
          }
          g.drawRect(upperLeft_x,upperLeft_y,rect_width,rect_height);
          old_upperLeft_x = upperLeft_x;
          old_upperLeft_y = upperLeft_y;
          old_rect_width = rect_width;
          old_rect_height = rect_height;
          break;
        case CIRCLE:
          x2_coord = e.getX();
          y2_coord = e.getY();
          int xd = x1_coord - x2_coord;
          int yd = y1_coord - y2_coord;
          int r = (int) Math.sqrt(xd*xd+yd*yd);
          rect_width = 2*r;
          rect_height = rect_width;
          upperLeft_x = x1_coord - r;
          upperLeft_y = y1_coord - r;
          g.setXORMode(displayBackgroundColor);
          if (inCreatingShape == false)
            inCreatingShape = true;
          else {
            g.drawOval(old_upperLeft_x,old_upperLeft_y,old_rect_width,old_rect_height);
          }
          g.drawOval(upperLeft_x,upperLeft_y,rect_width,rect_height);
          old_upperLeft_x = upperLeft_x;
          old_upperLeft_y = upperLeft_y;
          old_rect_width = rect_width;
          old_rect_height = rect_height;
          break;
        default:
          break;
      }
    }
  }
  public void mouseMoved(MouseEvent e) {
    xCoord = e.getX();
    yCoord = e.getY();
    String x = "  X: " + xCoord;
    String y = "Y: " + xCoord;
    xLabel.setText(x);
    yLabel.setText(y);
  }
  public void focusGained(FocusEvent e) {
  }
  public void focusLost(FocusEvent e) {
  }
}

public class Draw {
  public static void main(String[] args) {
    JFrame w;
    DrawPanelControl content = new DrawPanelControl();
    JMenuBar menuBar = content.getMenuBar();
    w = new JFrame("MenuTest");
    w.setContentPane(content);
    w.setJMenuBar(menuBar);
    w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    w.setSize(600,600);
    w.setLocation(100,100);
    w.setVisible(true);
  }
}

