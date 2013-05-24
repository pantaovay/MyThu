/*
 * Author: Tao Pan
 * Email: pantaovay@gmail.com
 * Function: main entrance
 */
package tk.godtao.mythu;

import javax.swing.UIManager;

public class Main {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		new Login();
	}

}