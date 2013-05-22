package tk.godtao.mythu;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Login implements ActionListener {
	JTextField userid;
	JPasswordField userpass;
	JButton confirm, concel;
	Container dialogPane;
	JDialog d;
	JFrame f;
	
	public Login(JFrame f){
		this.d = new JDialog();
		d.setTitle("请输入用户名和密码：");
		this.dialogPane = d.getContentPane();
		this.dialogPane.setLayout(new GridLayout(3,4));
		this.dialogPane.add(new JLabel("用户名", SwingConstants.LEFT));
		this.userid = new JTextField();
		this.dialogPane.add(this.userid);
		this.dialogPane.add(new JLabel("密码", SwingConstants.LEFT));
		this.userpass = new JPasswordField();
		this.dialogPane.add(this.userpass);
		this.confirm = new JButton("确定");
		this.concel = new JButton("退出");
		this.dialogPane.add(this.confirm);
		this.dialogPane.add(this.concel);
		this.confirm.addActionListener(this);
		this.concel.addActionListener(this);
		this.d.setBounds(200, 150, 400, 130);
		this.d.getRootPane().setDefaultButton(confirm);
		this.d.setVisible(true);
		this.f = f;
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("确定")) {
			String name = this.userid.getText();
			char[] pass = this.userpass.getPassword();
			try {
				new MyThu(name, new String(pass));
				if(MyThu.login) {
					d.dispose();
					MyThu.f.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(this.d, "用户名或密码错误", "Error", JOptionPane.WARNING_MESSAGE);
					//userid.setText("");
					userpass.setText("");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		}
		if(cmd.equals("退出")){
			System.exit(0);
		}
	}

}
