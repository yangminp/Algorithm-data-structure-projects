package gui;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] s){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new View();
				
			}
		});
	}
}