import javax.naming.ldap.Control;
import java.util.*;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import static java.lang.Math.*;
public class Main {
    static int count;
    public static void main(String[] args){
        count = 0;
        DemonstrationWindow balls = new DemonstrationWindow();
    }
}
class DemonstrationWindow extends Frame implements Observer, ActionListener, ItemListener {
    private LinkedList LL = new LinkedList();

    private Frame ControlPanel;
    private Button StartBtn;
    private Button ChangeBtn;
    private Button updateNumBtn;
    Label numberLab, objectLab, speedLabel,newspeedLabel;

    private Choice colorChoice; // список цветов
    private Choice speedChoice, textChoice,newspeedChoice;  //  список скоростей
    private Choice figureChoice; // выбор фигуры

    private int ballCount = 0;

    private TextField inputField;
    private TextField chooseNumberField,newNumberField;


    private void InitControlPanel()
    {
        ControlPanel = new Frame();
        ControlPanel.setSize(new Dimension(400,300));
        ControlPanel.setResizable(false);
        ControlPanel.setTitle("Управляющее окно");
        ControlPanel.setLayout(null);
        ControlPanel.addWindowListener(new WindowAdapter2());

        chooseNumberField = new TextField();
        chooseNumberField.setBounds(100,80, 50,20);
        ControlPanel.add(chooseNumberField, new Point(20,20));

        newNumberField = new TextField();
        newNumberField.setBounds(160,80, 50,20);
        ControlPanel.add(newNumberField, new Point(20,20));

        StartBtn = new Button("Полетели");
        StartBtn.setSize(new Dimension(20,40));
        StartBtn.setActionCommand("Start");
        StartBtn.addActionListener(this);
        ControlPanel.add(StartBtn, new Point(20,20));
        StartBtn.setBounds(320,250, 50,20);

        ChangeBtn = new Button("Изменить");
        ChangeBtn.setSize(new Dimension(45,40));
        ChangeBtn.setActionCommand("Изменить скорость");
        ChangeBtn.addActionListener(this);
        ChangeBtn.setBounds(20,250, 50,20);
        ControlPanel.add(ChangeBtn, new Point(20,20));

        inputField = new TextField();
        inputField.setBounds(100,250, 150,20);
        ControlPanel.add(inputField);

        // список номера фигуры
//        figureChoice = new Choice();
//        figureChoice.addItemListener(this);
//        figureChoice.setBounds(100,135, 150,20);
//        ControlPanel.add(figureChoice);
        // список цветов
        colorChoice = new Choice(); // цвета фигуры
        colorChoice.addItem("Синий");
        colorChoice.addItem("Зелёный");
        colorChoice.addItem("Красный");
        colorChoice.addItem("Чёрный");
        colorChoice.addItem("Жёлтый");
        colorChoice.addItemListener(this);
        colorChoice.setBounds(100,160, 150,20);
        ControlPanel.add(colorChoice);
        // список скоростей
        speedChoice = new Choice();

        speedChoice.addItem("10");

        speedChoice.addItem("5");

        speedChoice.addItem("1");
        speedChoice.addItemListener(this);
        speedChoice.setBounds(100,185, 150,20);
        ControlPanel.add(speedChoice);

        speedLabel = new Label("U0: ");
        speedLabel.setBounds(30,185, 50,20);
        ControlPanel.add(speedLabel);
        newspeedLabel = new Label("U1: ");
        newspeedLabel.setBounds(30,230, 50,20);
        ControlPanel.add(newspeedLabel);

        newspeedChoice = new Choice();
        newspeedChoice.addItem("10");
        newspeedChoice.addItem("5");
        newspeedChoice.addItem("1");
        newspeedChoice.setBounds(100,230, 150,20);
        ControlPanel.add(newspeedChoice);

        updateNumBtn = new Button("Обновить номер");
        updateNumBtn.setActionCommand("Обновить номер");
        updateNumBtn.setBounds(220,80, 100,20);
        updateNumBtn.addActionListener(this);
        ControlPanel.add(updateNumBtn);


        textChoice = new Choice();
        textChoice.addItem("T1");
        textChoice.addItem("T2");
        textChoice.addItem("T3");
        textChoice.addItem("T4");
        textChoice.addItem("T5");
        textChoice.setBounds(100,100, 150,20);
        ControlPanel.add(textChoice);

        ControlPanel.setVisible(true);
    }

    DemonstrationWindow(){
        this.addWindowListener(new WindowAdapter2());

        InitControlPanel(); // показываю контрольную панель

        this.setSize(500,200);
        this.setVisible(true);
        this.setLocation(100, 150);
        this.setTitle("Демонстрационное окно");
    }
    public void update(Observable o, Object arg) {
        MyFigure figure = (MyFigure)arg;

//        System.out.println ("x= " + figure.thr.getName() + figure.x);
        repaint();
    }
    public void paint (Graphics g) {
        if (!LL.isEmpty()){
            for (Object item : LL) {
                MyFigure figure = (MyFigure) item;
                g.setColor(figure.col);
                switch (figure.type)
                {
                    case 1:
                        g.fillOval(figure.x, figure.y, 20, 20);
                        g.drawString(Integer.toString(figure.Id) + this.textChoice.getSelectedItem(), figure.x, figure.y);break;
                    case 2:
                        g.drawRect(figure.x, figure.y, 20, 20);
                        g.drawString(Integer.toString(figure.Id) + this.textChoice.getSelectedItem(), figure.x, figure.y);break;
                }

            }
        }
    }
    public void itemStateChanged (ItemEvent iE) {}

    private int GetFigureType(String textFromControlPanel){
        switch (textFromControlPanel)
        {
            case "круг": return 1;
            case "квадрат": return 2;
            default: return -1;
        }
    }

    public void actionPerformed (ActionEvent aE) {
        String str = aE.getActionCommand();
        if (str.equals ("Start")){
            if (Main.count >= 3) {
                StartBtn.setEnabled(false); // Делаем кнопку неактивной
                System.out.println("Кол-во объектов превышено");
                return; // Прекращаем выполнение метода
            }
            Color col = null;
            switch (colorChoice.getSelectedIndex()) { // цвет фигуры
                case 0: col= Color.blue; break;
                case 1: col= Color.green; break;
                case 2: col= Color.red; break;
                case 3: col= Color.black; break;
                case 4: col= Color.yellow; break;
            }
            int figureType = GetFigureType(this.inputField.getText()); // тип фигуры (овал, прямоугольник)

            if (figureType != -1) {
                MyFigure figure = new MyFigure(col, figureType, this,Integer.parseInt(this.speedChoice.getSelectedItem()));

                for (Object item : LL) {
                    MyFigure fig = (MyFigure) item;

                    if ((figure.Id) == fig.Id) {
                        figure.Id +=1;
                    }
                }
                figure.thr = new Thread(figure,Main.count+":"+ figure.Id +":");
                LL.add(figure);
                figure.addObserver(this);
                ballCount++;
//                figureChoice.add(Integer.valueOf(figure.Id).toString());


            }
            else{System.out.println("Такой фигуры нет");}

        } else if (str.equals("Изменить скорость")) {

            for (Object item : LL) {
                MyFigure figure = (MyFigure) item;
                if ((figure.Id) == (Integer.parseInt(chooseNumberField.getText()))) {
                    figure.speed = Integer.parseInt(newspeedChoice.getSelectedItem());
                }
            }
        } else if (str.equals("Обновить номер")) {
            int newNum;
            try {
                newNum = Integer.parseInt(newNumberField.getText());
            } catch(Exception e) {
                System.out.println(e);
                return;
            }
            for (Object item: LL) {
                MyFigure figure = (MyFigure)item ;
                if(figure.Id == newNum) {
                    System.out.println("Такой номер уже существует");
                    return;
                }
            }
            for (Object item: LL) {
                MyFigure figure = (MyFigure) item;
                if((figure.Id) == (Integer.parseInt(chooseNumberField.getText()))) {
                    figure.Id = newNum;
                    figure.thr = new Thread(figure,Main.count+":"+ figure.Id +":");
                }
            }
        }
        repaint();
    }
}
class MyFigure extends Observable implements Runnable {

    private static Random random = new Random();

    Thread thr;

    private boolean xplus = false;
    private boolean yplus = false;

    public int speed;
    int incrementX;
    double incrementY;
    public int Id;
    public int type;
    int x; int y;
    Color col;

    DemonstrationWindow mainFrame = null;

    public MyFigure (Color col, int figureType, DemonstrationWindow f, int speed){
        mainFrame = f;

        xplus = true; yplus = true;
        this.speed = speed/2;
        while (incrementX==0){
        incrementX = random.nextInt(10);}

        incrementY = Math.sqrt(speed * speed - incrementX * incrementX);
//        incrementY = random.nextInt(3);


        if (incrementY == 0) incrementX++;
        x = 20; y = 30; // начальные координаты


        this.type = figureType;
        this.col = col;
        Id = ++Main.count;
        thr = new Thread(this,Main.count+":"+ Id +":");
        thr.start();

    }
    public void run(){
        while (true){

            if(x>= mainFrame.getSize().width - 20) xplus = false;
            if(x<=-1) xplus = true;

            if(y>=mainFrame.getSize().height - 20) yplus = false;
            if(y<=29) yplus = true;
//
//            if(xplus) x+=incrementX; else x-=incrementX;
//            if(yplus) y+=incrementY; else y-=incrementY;
            if (xplus) x += incrementX;else x -= incrementX;
            if (yplus) y += incrementY;else y -= incrementY;

            setChanged();notifyObservers (this);
            try{Thread.sleep (10);}
            catch (InterruptedException e){}
        }
    }
}
class WindowAdapter2 extends WindowAdapter {
    public void windowClosing (WindowEvent wE) {System.exit (0);} /*включающий
    переопределённую предопределённую функцию windowClosing(), в которой вызывается
    функция exit() класса.*/
}
