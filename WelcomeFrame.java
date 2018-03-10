package process_deadlock;

import java.awt.Button;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class WelcomeFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	JTextField tf;

	WelcomeFrame(){
		super("Welcome");
		setSize(600,200);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(4,1));
		
		JLabel lab1=new JLabel("Process Deadlock Detector",JLabel.CENTER);
		lab1.setFont(new Font("",0,20));
		add(lab1);
		
		JLabel lab2=new JLabel("Designer: µÔÐÂÁú£¬ÁõÃ÷",JLabel.CENTER);
		lab2.setFont(new Font("",0,20));
		add(lab2);
		
		tf = new JTextField("Please enter input file path here");
		tf.setFont(new Font("",0,20));
		add(tf);
		
		Button but2=new Button("Confirm");
		but2.setFont(new Font("",0,20));
		but2.addActionListener(new ButtonListener2());
		add(but2);
		
		setVisible(true);
	}

	class ButtonListener2 implements ActionListener{
		public void actionPerformed ( ActionEvent e ) {
			String fil_pat=tf.getText();
			setVisible(false);
			new MyFrame(fil_pat);
		}
	}
}
