package process_deadlock;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class MyFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	final String Title = "Processes Deadlock Judge";
	MyPanel panel;
	final int width = 1000; 
	final int height = 600;
	
	int n_p;
	int n_r;
	ArrayList<Integer> available=new ArrayList<Integer>();//initial number of resources
	ArrayList<ArrayList<Integer>> allocation=new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> request=new ArrayList<ArrayList<Integer>>();
	ArrayList<Boolean> state=new ArrayList<Boolean>();//true: working; false: released
	ArrayList<Integer> work=new ArrayList<Integer>();//current number of resources
	
	ArrayList<Integer> n_lin_pro=new ArrayList<Integer>();
	ArrayList<Integer> n_lin_res=new ArrayList<Integer>();
	
	
	
	MyFrame(String fil_pat){	
		ArrayList<String> data=new ArrayList<String>();

		FileProcess fp=new FileProcess();
		fp.readTextLineByLine(data, fil_pat);
		if(dataProcess(data)) {
			if(checkValidity()) {
	
				setTitle(Title);
				setSize(width,height);		
				setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				setLocationRelativeTo(null);
				setLayout(new BorderLayout());
				
				Button but1=new Button("Next Step");
				but1.setFont(new Font("",0,15));
				but1.setBounds(800, 450, 100, 40);
				add(but1);
				
				ButtonListener1  bl = new ButtonListener1();
				but1.addActionListener(bl);
				
				panel  = new MyPanel();
				add(panel,"Center");
				
				setVisible(true);
			}
			else {
				JFrame fra=new JFrame("ERROR!");
				fra.setSize(600,200);
				fra.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				fra.setLocationRelativeTo(null);
				fra.setLayout(new BorderLayout());
				JLabel lab=new JLabel("Allocated resources are more than available ones!",JLabel.CENTER);
				lab.setFont(new Font("",0,20));
				fra.add(lab);
				fra.setVisible(true);
			}
		}
		else {
			
			JFrame fra=new JFrame("ERROR!");
			fra.setSize(600,200);
			fra.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			fra.setLocationRelativeTo(null);
			fra.setLayout(new BorderLayout());
			JLabel lab=new JLabel("Input has a wrong format!",JLabel.CENTER);
			lab.setFont(new Font("",0,20));
			fra.add(lab);
			fra.setVisible(true);
		}
	}
	
	boolean dataProcess(List<String> data){
		n_p=Integer.parseInt(data.get(0));
		n_r=Integer.parseInt(data.get(1));
		String[] t_available=data.get(2).split("\\s+");
	
		for(String e:t_available) {
			available.add(Integer.parseInt(e));
		}
		if(available.size()!=n_r) {
			System.out.println("Size is wrong!");
		
			
			return false;
		}

		for(int i=0;i<n_p;i++) {
			String[] t_allocation=data.get(3+i).split("\\s+");
			ArrayList<Integer> row_allocation =new ArrayList<Integer>();
			for(String e:t_allocation) {
				row_allocation.add(Integer.parseInt(e));
			}
			if(row_allocation.size()!=n_r) {
				System.out.println("Size is wrong!");
				
			
				
				return false;
			}
			allocation.add(row_allocation);
		}
		for(int i=0;i<n_p;i++) {
			ArrayList<Integer> row_request=new ArrayList<Integer>();
			String[] t_request=data.get(3+n_p+i).split("\\s+");
			for(String e:t_request) {
				row_request.add(Integer.parseInt(e));
			}
			if(row_request.size()!=n_r) {
				System.out.println("Size is wrong!");
				
				return false;
			}
			request.add(row_request);
		}
		
		for(int i=0;i<n_p;i++) {
			
			state.add(true);
		}
		
		if(data.size()!=3+2*n_p) {
			System.out.println("Size is wrong!");
			
			return false;
		}
		
		for(int i=0;i<n_p;i++){
			int sum1=0;
			for(int j=0;j<n_r;j++){
				sum1+=allocation.get(i).get(j);
			}
			int sum2=0;
			for(int j=0;j<n_r;j++){
				sum2+=request.get(i).get(j);
			}
			n_lin_pro.add(sum1+sum2);
		}
		
		for(int i=0;i<n_r;i++){
			int sum1=0;
			for(int j=0;j<n_p;j++){
				sum1+=allocation.get(j).get(i);
			}
			int sum2=0;
			for(int j=0;j<n_p;j++){
				sum2+=request.get(j).get(i);
			}
			n_lin_res.add(sum1+sum2);
		}
		return true;
		
	}
	
	boolean checkValidity() {
		
		for(int i=0;i<n_r;i++) {
			int sum=0;
			for(int j=0;j<n_p;j++) {
				sum+=allocation.get(j).get(i);
			}
			if(sum>available.get(i)) {
				System.out.println("Allocated resources are more than available resources!");

				return false;

			}
			else {
				work.add(available.get(i)-sum);
			}
		}
		return true;
	}
	
	class MyPanel extends JPanel{
		private static final long serialVersionUID = 1L;	
		
		
		protected void paintComponent(Graphics g){
			int len_cir=120;
			int len_rec=150;
			super.paintComponent(g);
			 Graphics2D g2d = (Graphics2D) g.create();

	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            //g2d.setColor(Color.RED);
	            int R_x1 = width/(n_r + 1);
	            System.out.println(width/(n_r + 1) + " " + width);
	            int R_x = width/(n_r + 1) - 75;
	            int R_y = height/2;
	            for(int i=0;i<n_r;i++){
	            	g2d.setColor(Color.BLACK);
	            	g2d.drawRect(R_x, 300, len_rec, 80);
	            	R_x =R_x + R_x1;

	            }
	            int P_x1 = width/(n_p + 1);
	            int P_x = width/(n_p + 1) - 40;
	            for(int j=0;j<n_p;j++){
	            	g2d.setColor(Color.BLACK);
	            	g2d.drawArc(P_x, 60, len_cir, len_cir, 0, 360);
	            	P_x =P_x + P_x1;
	            }
	            
	            R_x1 = width/(n_r + 1)-90;
	            System.out.println(R_x1 + " " + width);
	            R_x = width/(n_r + 1) - 50;
	            R_y = height/2;
	          
	           
	            for(int i = 0;i<n_r;i++){  
	            	int a = 90/available.get(i);
	            	
	            	 for(int j = 0;j < available.get(i);j++ ){

	            	
	            	g2d.drawArc(R_x+10+i*+R_x1, 340, 10, 10, 0, 360);
	            	R_x =R_x + a;
	            	
	            }
	    
	            	 System.out.println(R_x);
	            }
	            
	            ArrayList<Integer> pos_pro=new ArrayList<Integer>();
	            ArrayList<Integer> pos_res=new ArrayList<Integer>();
	            
	            for(int i=0;i<n_p;i++){
	            	pos_pro.add(0);
	            }
	            for(int i=0;i<n_r;i++){
	            	pos_res.add(0);
	            }
	            

	            
	            P_x1 = width/(n_p + 1);
	            P_x = width/(n_p + 1) - 40;
	            R_x1 = width/(n_r + 1);
	            System.out.println(R_x1 + " " + width);
	            R_x = width/(n_r + 1) - 75;
	            R_y = height/2;
	            
	             
	             for(int i=0;i<n_p;i++){
	            	 for(int j=0;j<n_r;j++){
	            		 int int_cir=len_cir/(n_lin_pro.get(i)+1);
	            		 int int_rec=len_rec/(n_lin_res.get(j)+1);
	    				for(int n = 0;n<allocation.get(i).get(j);n++){
	    						
	    						drawAL(R_x + j*R_x1  + int_rec*pos_res.get(j)+int_rec, 300,P_x+ i*P_x1 +int_cir*pos_pro.get(i)+int_cir, 180,  g, Color.RED);	
	    						pos_pro.set(i, pos_pro.get(i)+1);
	    						pos_res.set(j, pos_res.get(j)+1);

	    					}
	    				
	    							    				
	    					}
	    				}
	    				
	            for(int i=0;i<n_r;i++){
	            	 for(int j=0;j<n_p;j++){
	            		 int int_cir=len_cir/(n_lin_pro.get(j)+1);
	            		 int int_rec=len_rec/(n_lin_res.get(i)+1);
	    				for(int n = 0;n<request.get(j).get(i);n++){
	    					
	    						drawAL(P_x+ j*P_x1 +int_cir*pos_pro.get(j)+int_cir, 180,R_x + i*R_x1  + int_rec*pos_res.get(i)+int_rec, 300,  g, Color.blue);	
	    						pos_pro.set(j, pos_pro.get(j)+1);
	    						pos_res.set(i, pos_res.get(i)+1);
	    						
	    					}
	    				
	    							    				
	    					}
	    				}
	    			
	       
	            g2d.dispose();


		}
		
		void drawAL(int sx, int sy, int ex, int ey, Graphics g, Color c) {
			double H = 6;
			double L = 6;
			int x3 = 0;
			int y3 = 0;
			int x4 = 0;
			int y4 = 0;
			double awrad = Math.atan(L / H);
			double arraow_len = Math.sqrt(L * L + H * H);
			double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
			double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
			double x_3 = ex - arrXY_1[0];
			double y_3 = ey - arrXY_1[1];
			double x_4 = ex - arrXY_2[0];
			double y_4 = ey - arrXY_2[1];
			Double X3 = new Double(x_3);
			x3 = X3.intValue();
			Double Y3 = new Double(y_3);
			y3 = Y3.intValue();
			Double X4 = new Double(x_4);
			x4 = X4.intValue();
			Double Y4 = new Double(y_4);
			y4 = Y4.intValue();
			// g.setColor(Color.BLACK);
			// g.setColor(COLOR_WHITE);

			BasicStroke bs1 = new BasicStroke(1);
			((Graphics2D) g).setStroke(bs1);

			g.setColor(c);
			g.drawLine(sx, sy, ex, ey);

			g.drawLine(ex, ey, x3, y3);

			g.drawLine(ex, ey, x4, y4);

		}
		
		double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {
			double mathstr[] = new double[2];

			double vx = px * Math.cos(ang) - py * Math.sin(ang);
			double vy = px * Math.sin(ang) + py * Math.cos(ang);
			if (isChLen) {
				double d = Math.sqrt(vx * vx + vy * vy);
				vx = vx / d * newLen;
				vy = vy / d * newLen;
				mathstr[0] = vx;
				mathstr[1] = vy;
			}
			return mathstr;
		}
	}
	
	class ButtonListener1 implements ActionListener{
		public void actionPerformed ( ActionEvent e ) {
			boolean flag1=false;
			for(int i=0;i<n_p;i++) {
				if(state.get(i)==true) {
					boolean flag2=true;
					for(int j=0;j<n_r;j++) {
						if(request.get(i).get(j)>work.get(j)) {
							flag2=false;
							break;
						}
					}
					if(flag2){
						state.set(i, false);
						for(int j=0;j<n_r;j++) {
							work.set(j, work.get(j)+allocation.get(i).get(j));
							allocation.get(i).set(j, 0);
							request.get(i).set(j, 0);
						}
						System.out.println("Process "+i+" has been released!");
						flag1=true;
						break;
					}
				}
				
			}
			if(!flag1) {
				boolean flag3=true;
				for(int i=0;i<state.size();i++) {
					if(state.get(i)==true) {
						flag3=false;
						break;
					}
				}
				if(!flag3) {
					JFrame fra=new JFrame("Result");
					fra.setSize(300,150);
					fra.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
					fra.setLocationRelativeTo(null);
					fra.setLayout(new BorderLayout());
					JLabel lab=new JLabel("It is a deadlock!",JLabel.CENTER);
					lab.setFont(new Font("",0,30));
					fra.add(lab);
					fra.setVisible(true);

					
				}
				else {
					JFrame fra=new JFrame("Result");
					fra.setSize(300,150);
					fra.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
					fra.setLocationRelativeTo(null);
					fra.setLayout(new BorderLayout());
					JLabel lab=new JLabel("It is a safe state!",JLabel.CENTER);
					lab.setFont(new Font("",0,30));
					fra.add(lab);
					
					fra.setVisible(true);
					System.out.println("It is a safe state!");
				}
			}
			//System.out.println("»­Í¼");
			panel.repaint();
			//System.out.println("»­Í¼Íê±Ï" );
			
		}

	}
	
	
}



